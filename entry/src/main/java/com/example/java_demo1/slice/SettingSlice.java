package com.example.java_demo1.slice;

import com.example.java_demo1.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class SettingSlice extends AbilitySlice {

    public void onActive() {

    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_setting);

        Button btnBack = findComponentById(ResourceTable.Id_btnBack);
        btnBack.setClickedListener(this::onBtnBack);
    }
    void onBtnBack(Component listener){
        present(new MainAbilitySlice(), new Intent());
    }

}
