package com.example.java_demo1.slice;

import com.example.java_demo1.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;

public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        findComponentById(ResourceTable.Id_btnDrag).setClickedListener(
                listener -> present(new DragAbilitySlice(), new Intent()));
        findComponentById(ResourceTable.Id_btnWebView).setClickedListener(
                listener -> present(new WebViewSlice(), new Intent()));
        findComponentById(ResourceTable.Id_btnFillInBlank).setClickedListener(
                listener -> present(new FillInBlankSlice(), new Intent()));
        findComponentById(ResourceTable.Id_btnChoiceQuestion).setClickedListener(
                listener -> present(new ChoiceQuestionSlice(), new Intent()));
        findComponentById(ResourceTable.Id_btnSlidePage).setClickedListener(
                listener -> present(new BookSlice(), new Intent()));
        findComponentById(ResourceTable.Id_btnSetting).setClickedListener(
                listener -> present(new SettingSlice(), new Intent()));

    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
