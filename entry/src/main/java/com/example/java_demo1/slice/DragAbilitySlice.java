package com.example.java_demo1.slice;
import com.example.java_demo1.MyAnimator;
import com.example.java_demo1.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.*;
import ohos.agp.utils.Point;
import ohos.agp.utils.Rect;

import java.util.function.Consumer;

public class DragAbilitySlice extends AbilitySlice {
    Rect originalRectOfDragItem;
    Component dragQuestion;
    Answer[] options;
    String questionStr;
    @Override
    public  void onActive(){
        dragQuestion.setLayoutRefreshedListener(this::onDragItemRefreshed);
    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_drag);

        dragQuestion =  findComponentById(ResourceTable.Id_text_drag);
        options = new Answer[]{
                new Answer(this,ResourceTable.Id_image1,ResourceTable.Id_flag1,ResourceTable.Id_answer1),
                new Answer(this,ResourceTable.Id_image2,ResourceTable.Id_flag2,ResourceTable.Id_answer2),
                new Answer(this,ResourceTable.Id_image3,ResourceTable.Id_flag3,ResourceTable.Id_answer3),
        };

        Button btnBack = (Button) findComponentById(ResourceTable.Id_btnBack);
        Button btnRest = (Button)findComponentById(ResourceTable.Id_btnReset);

        btnBack.setClickedListener(listener -> present(new MainAbilitySlice(), new Intent()));
        btnRest.setClickedListener(this::onClickResetBtn);
        dragQuestion.setDraggedListener(Component.DRAG_HORIZONTAL_VERTICAL,
                new DragListener(options,this::onGetAnswer));

        dragQuestion.setScale(1,1);
        questionStr = ((Text)dragQuestion).getText();
        showFlags(false);
    }
    Component[] getComponents( int [] ids){
        Component[] res = new Component[ids.length];
        for (int i=0;i<ids.length;++i){
            res[i] = findComponentById(ids[i]);
        }
        return res;
    }
    void onGetAnswer(Answer option){
        MyAnimator ani = new MyAnimator(dragQuestion, 200, 50, 0, Animator.CurveType.ACCELERATE,
                (animatorValue, v) ->{
                    dragQuestion.setScale(1 - v, 1 - v);
                },
                (animator,state)->{
                    if(state == MyAnimator.AnimationState.end){
                        option.check(questionStr);
                    }
                });
        ani.ani().start();
    }

    void onDragItemRefreshed(Component component) {
        if(originalRectOfDragItem==null){
            originalRectOfDragItem = component.getComponentPosition();
        }
    }
    void onClickResetBtn(Component component){
        dragQuestion.setScale(1,1);
        dragQuestion.setComponentPosition(originalRectOfDragItem);
        showFlags(false);
    }
    void showFlags(boolean b){
        for (int i=0;i<options.length;++i){
            options[i].showFlag(b);
        }
    }
}

class Answer{
    Component option;
    Image flag;
    AbilitySlice slice;
    String value;
    public Answer(AbilitySlice s,int optionID,int flagID,int valueID){
        slice = s;
        option = slice.findComponentById(optionID);
        flag = slice.findComponentById(flagID);
        Text valueTxt = slice.findComponentById(valueID);
        value =valueTxt.getText();
    }

    public String getAnswer(){
        return value;
    }
    public void check(String question){
        boolean res = question.equals(value);
        setFlag(res);
        showFlag(true);
    }
    public  void setFlag(boolean b){
        flag.setPixelMap(b? ResourceTable.Media_right :ResourceTable.Media_wrong );
    }
    public  void showFlag(boolean b){
        flag.setVisibility(b? Component.VISIBLE : Component.INVISIBLE);
    }
    public  Component getOption(){
        return  option;
    }
}

class DragListener implements  Component.DraggedListener {

    Point mousePosStart;
    Point mousePosEnd;
    Rect curRect;
    Rect originalRect;
    Answer[] options;
    Component dragCom;

    public DragListener(Answer[] optionArray, Consumer<Answer> giveAnswer) {
        options = optionArray;
        onGiveAnswer = giveAnswer;
    }
    Consumer<Answer> onGiveAnswer;

    @Override
    public void onDragDown(Component component, DragInfo dragInfo) {
    }

    @Override
    public void onDragStart(Component component, DragInfo dragInfo) {
        dragCom = component;
        mousePosStart = dragInfo.startPoint;
        curRect =  component.getComponentPosition();
        originalRect = new Rect(curRect);
    }

    @Override
    public void onDragUpdate(Component component, DragInfo dragInfo) {
        mousePosEnd = dragInfo.updatePoint;
        int offsetX = mousePosEnd.getPointXToInt() - mousePosStart.getPointXToInt();
        int offsetY = mousePosEnd.getPointYToInt() - mousePosStart.getPointYToInt();
        curRect.offset(offsetX,offsetY);
        component.setComponentPosition(curRect);
    }

    @Override
    public void onDragEnd(Component component, DragInfo dragInfo) {
        Answer option = null;
        for(int i=0;i<options.length;++i){
            if(isInOption(options[i].getOption()))
            {
                option = options[i];
                break;
            }
        }
        if(option == null){
            component.setComponentPosition(originalRect);
        }
        else
        {
            onGiveAnswer.accept(option);
        }
        dragCom = null;
    }


    @Override
    public void onDragCancel(Component component, DragInfo dragInfo) {
        component.setComponentPosition(originalRect);
        dragCom = null;
    }

    private boolean isInOption(Component option){
        //左上角坐标
        int[] posDrag = dragCom.getLocationOnScreen();
        int[] posOption = option.getLocationOnScreen();
        //中心坐标
        posDrag[0] += dragCom.getWidth()/2;
        posDrag[1] += dragCom.getHeight()/2;
        posOption[0] += option.getWidth()/2;
        posOption[1] += option.getHeight()/2;
        int r =  option.getWidth()/2;
        int offX = posDrag[0] - posOption[0];
        int offY = posDrag[1] - posOption[1];

        return r*r >= offX*offX + offY*offY;
    }
}
