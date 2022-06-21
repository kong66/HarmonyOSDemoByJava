package com.example.java_demo1.slice;

import com.example.java_demo1.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Button;
import ohos.agp.components.Checkbox;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.window.dialog.CommonDialog;

public class ChoiceQuestionSlice extends AbilitySlice {
    MyCheckbox[] checkboxes;
    MyCheckbox curBox;
    int[] checkboxIDs = new int[]{
            ResourceTable.Id_check_box_1,
            ResourceTable.Id_check_box_2,
            ResourceTable.Id_check_box_3,
            ResourceTable.Id_check_box_4
    };
    String question = "bananas";


    public  void onActive(){
        for(int i=0;i<checkboxes.length;++i){
            checkboxes[i].box.setChecked(false);
        }
        curBox = null;
    }
    public  void onInactive(){
        for(int i=0;i<checkboxes.length;++i){
            checkboxes[i].box.setChecked(false);
            checkboxes[i].onCheckBoxChanged();
        }
    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_choice_question);

        checkboxes = new MyCheckbox[4];
        Button btnBack = findComponentById(ResourceTable.Id_btnBack);
        Button btnOK = findComponentById(ResourceTable.Id_button_ok);

        btnBack.setClickedListener(this::onBtnBack);
        btnOK.setClickedListener(this::onClickOK);
        for(int i=0;i<checkboxIDs.length;++i){
            checkboxes[i] = new MyCheckbox(this,checkboxIDs[i]);
            checkboxes[i].box.setCheckedStateChangedListener(this::onCheckboxStateChanged);
        }
    }
    void onClickOK(Component component){
        if(curBox!=null){
            if(curBox.value.equals(question)){
                popDialog();
            }
        }
    }
    void popDialog(){
        CommonDialog dialog = new  CommonDialog(this);
        Component component =   LayoutScatter.getInstance(getContext())
                .parse(ResourceTable.Layout_dialog_awesome,   null, true);
        dialog.setSize(800, 600);
        dialog.setContentCustomComponent(component);
        dialog.setTransparent(true);
        component.findComponentById(ResourceTable.Id_dialogBG).setClickedListener(listener ->{
            onBtnBack(null);
            dialog.destroy();
        });
        dialog.show();
    }
    void onBtnBack(Component listener){
        present(new MainAbilitySlice(), new Intent());
    }
    void onCheckboxStateChanged(Component component,boolean b){
        for (int i = 0; i < checkboxes.length; ++i) {
            MyCheckbox box = checkboxes[i];
            if (box.box.getId() != component.getId() ) {
                if(b){
                    box.box.setChecked(false);
                }
            }else{
                if(b){
                    curBox = box;
                }
            }
            box.onCheckBoxChanged();
        }
    }

}
class MyCheckbox {
    AbilitySlice slice;
    float[] pos;
    ShapeElement checkedElement;
    ShapeElement normalElement;
    public Checkbox box;
    public String value;
    public MyCheckbox(AbilitySlice s, int id) {
        this.slice = s;
        box = slice.findComponentById(id);
        value = box.getText().toLowerCase();
        box.setLayoutRefreshedListener(this::onLayoutRefresh);
        checkedElement = new ShapeElement();
        normalElement = new ShapeElement();
        normalElement.setRgbColor(new RgbColor(206,238,150));
        checkedElement.setRgbColor(new RgbColor(96 ,146,9));
    }
    public void onCheckBoxChanged(){
        if(pos==null)
            return;
        if(box.isChecked()){
            box.setContentPosition(pos[0]-30,pos[1]-20);
            box.setBackground(checkedElement);
        }else{
            box.setContentPosition(pos[0],pos[1]);
            box.setBackground(normalElement);
        }
    }
    void onLayoutRefresh(Component component){
        pos = box.getContentPosition();
    }
}
