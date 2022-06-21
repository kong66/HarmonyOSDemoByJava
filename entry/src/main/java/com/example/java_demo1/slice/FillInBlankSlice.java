package com.example.java_demo1.slice;
import com.example.java_demo1.MyAnimator;
import com.example.java_demo1.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.accessibility.ability.SoftKeyBoardController;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.*;
import ohos.agp.text.CharInputFilter;
import ohos.agp.text.InputFilter;
import ohos.agp.window.dialog.CommonDialog;
import ohos.multimodalinput.event.KeyEvent;


public class FillInBlankSlice extends AbilitySlice {

    Image imageCheck;
    Image imageQuestion;
    TextField textField;
    ProgressBar progressBar;
    MyAnimator ani;

    float originalX=-9999;
    float speed = 100;
    int[] questionIDs;
    int curQuestionID = 0;
    String[] answers;

    public  void onActive(){
        curQuestionID = 0;
        textField.setText("");
        showCheckIcon(false);
        imageQuestion.setPixelMap(questionIDs[curQuestionID]);
        progressBar.setProgressValue(0);
    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_fill_in_blank);

        questionIDs = new int[]{ResourceTable.Media_banana,ResourceTable.Media_pear,ResourceTable.Media_apple};
        answers = new String[]{"banana","pear","apple"};

        Button btnBack = findComponentById(ResourceTable.Id_btnBack);
        Button btnSubmit = findComponentById(ResourceTable.Id_button_submit);
        Component rootContainer = findComponentById(ResourceTable.Id_root);
        textField = findComponentById(ResourceTable.Id_text_field);
        imageCheck = findComponentById(ResourceTable.Id_flag);
        imageQuestion = findComponentById(ResourceTable.Id_image_question);
        progressBar = findComponentById(ResourceTable.Id_progressbar);

        btnBack.setClickedListener(this::onBtnBack);
        btnSubmit.setClickedListener(this::onSubmit);
        rootContainer.setClickedListener(this::onClickBlankArea);
        imageQuestion.setLayoutRefreshedListener(this::onImageQuestionRefresh);
        textField.setFocusChangedListener(this::onTextFieldFocus);

        textField.addTextObserver(this::onTextUpdated);
        //断点无响应，文档说只有物理键盘才生效
        //textField.setKeyEventListener((component, keyEvent) -> false);
        //过滤器会崩溃
        //textField.setInputFilters(new InputFilter[]{CharInputFilter.ALPHABET});

        createAni();
    }

    void onBtnBack(Component listener){
        present(new MainAbilitySlice(), new Intent());
    }
    void onImageQuestionRefresh(Component component){
        if(originalX==-9999)
            originalX = imageQuestion.getContentPositionX();
    }
    void onClickBlankArea(Component com){
        if(com.getId() != ResourceTable.Id_text_field)
        {
            textField.clearFocus();
        }
    }
    void onTextFieldFocus(Component component,boolean isFocused){
        if (!isFocused) {
            setCheckIcon(checkAnswer());
            showCheckIcon(true);
        }
        else {
            textField.setText("");//这里不清空的话会有缓存，影响下次updateText的判断
        }
    }

    void onSubmit(Component btn){
        textField.clearFocus();
        boolean b = checkAnswer();
        setCheckIcon(b);
        showCheckIcon(true);
        if(b){
            ++curQuestionID;
            int curProgress =  (int)((float)(curQuestionID*100)/ questionIDs.length);
            progressBar.setProgressValue(curProgress);
            if(curQuestionID < questionIDs.length) {
                showCheckIcon(false);
                textField.setText("");
                startAni();
            }else{
                PopDialog();
            }
        }
    }
    void PopDialog(){

        //默认Dialog
//        CommonDialog dialog = new CommonDialog(getContext());
//        dialog.setTitleText(getString(ResourceTable.String_congratulation));
//        dialog.setContentText(getString((ResourceTable.String_finishingChallenge)));
//        dialog.setButton(IDialog.BUTTON3,
//                getString(ResourceTable.String_ok),
//                (iDialog, i) -> {
//                    iDialog.destroy();
//                    onBtnBack(null);
//                });
//        dialog.show();

        //自定义Dialog
        CommonDialog dialog = new  CommonDialog(this);
        Component component =   LayoutScatter.getInstance(getContext())
                .parse(ResourceTable.Layout_dialog_congratulation,   null, true);
        dialog.setSize(800, 500);
        dialog.setContentCustomComponent(component);
        dialog.setTransparent(true);
        component.findComponentById(ResourceTable.Id_ok).setClickedListener(listener ->{
            onBtnBack(null);
            dialog.destroy();
        });
        dialog.show();
    }
    //这是仅有的，可以实时检测输入的方式
    void onTextUpdated(String s, int start, int before, int count) {
        String s2 = s.replaceAll("[^a-zA-Z]","");
        textField.setText(s2);
        if(s.contains("\n")){
            textField.clearFocus();
        }
    }
    boolean checkAnswer(){
        String content = textField.getText().toLowerCase();
        return answers[curQuestionID].equals(content);
    }
    void showCheckIcon(boolean b){
        imageCheck.setVisibility(b?Component.VISIBLE: Component.INVISIBLE);
    }
    void setCheckIcon(boolean b){
        imageCheck.setPixelMap(b?ResourceTable.Media_right:ResourceTable.Media_wrong);
    }
    void startAni(){
        ani.ani().start();
    }
    void createAni(){
        ani = new MyAnimator(imageQuestion, 500, 0, AnimatorValue.INFINITE, Animator.CurveType.ACCELERATE,
                (animatorValue, value) ->{
                    float offX = imageQuestion.getContentPositionX()+ speed * value;
                    imageQuestion.setContentPositionX(offX);
                },
                (animator,state)->{
                    if(state == MyAnimator.AnimationState.end){
                        imageQuestion.setContentPositionX(originalX);
                    }
                });
        ani.ani().setLoopedListener(animator -> {
            if(speed<0)
            {
                imageQuestion.setContentPositionX(originalX);
                ani.ani().pause();
            }
            else
            {
                imageQuestion.setPixelMap(questionIDs[curQuestionID]);
            }
            speed = -speed;
        });
    }

}


