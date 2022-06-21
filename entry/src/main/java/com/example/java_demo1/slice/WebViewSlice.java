package com.example.java_demo1.slice;

import com.example.java_demo1.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.webengine.WebView;
import ohos.agp.utils.Rect;

public class WebViewSlice extends AbilitySlice {

    @Override
    public void onActive() {
    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_web_view);

        Button btnBack = (Button) findComponentById(ResourceTable.Id_btnBack);
        btnBack.setClickedListener(listener -> present(new MainAbilitySlice(), new Intent()));

        WebView webView = (WebView) findComponentById(ResourceTable.Id_webview);
        webView.getWebConfig().setJavaScriptPermit(true);  // 如果网页需要使用JavaScript，增加此行；如何使用JavaScript下文有详细介绍
        final String url = "https://cn.bing.com/";
        //final String url = "https://www.baidu.com/";
        webView.load(url);
    }
}
