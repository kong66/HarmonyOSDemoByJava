package com.example.java_demo1.slice;

import com.example.java_demo1.PageProviderOfBooks;
import com.example.java_demo1.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.PageSlider;
import ohos.agp.components.Text;

import java.util.ArrayList;

public class BookSlice extends AbilitySlice {

    Text des;
    Text page;
    PageSlider pageSlider;

    public  void onActive(){
        onPageChosen(0);
        pageSlider.setCurrentPage(0);
    }
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_slide_page);

        Button btnBack = findComponentById(ResourceTable.Id_btnBack);
        pageSlider =  findComponentById(ResourceTable.Id_page_slider);
        des = findComponentById(ResourceTable.Id_text_des);
        page = findComponentById(ResourceTable.Id_text_page);

        btnBack.setClickedListener(this::onBtnBack);
        initPageSlider();
    }
    void onBtnBack(Component listener){
        present(new MainAbilitySlice(), new Intent());
    }


    private void initPageSlider() {
        BookSlice slice = this;
        pageSlider.setProvider(new PageProviderOfBooks(this,getData()));
        pageSlider.addPageChangedListener(new PageSlider.PageChangedListener() {
            @Override
            public void onPageSliding(int i, float v, int i1) {
            }
            @Override
            public void onPageSlideStateChanged(int i) {
            }
            @Override
            public void onPageChosen(int i) {
                slice.onPageChosen(i);
            }
        });
    }
    private  void onPageChosen(int i){
        des.setText(story[i]);
        page.setText((i+1)+"/"+story.length);
    }
    private ArrayList<PageProviderOfBooks.DataItem> getData() {
        ArrayList<PageProviderOfBooks.DataItem> dataItems = new ArrayList<>();
        dataItems.add(new PageProviderOfBooks.DataItem(0,ResourceTable.Media_book_1,"1"));
        dataItems.add(new PageProviderOfBooks.DataItem(1,ResourceTable.Media_book_2,"2"));
        dataItems.add(new PageProviderOfBooks.DataItem(2,ResourceTable.Media_book_3,"3"));
        dataItems.add(new PageProviderOfBooks.DataItem(3,ResourceTable.Media_book_4,"4"));
        dataItems.add(new PageProviderOfBooks.DataItem(4,ResourceTable.Media_book_5,"5"));
        dataItems.add(new PageProviderOfBooks.DataItem(5,ResourceTable.Media_book_6,"6"));
        dataItems.add(new PageProviderOfBooks.DataItem(6,ResourceTable.Media_book_7,"7"));
        dataItems.add(new PageProviderOfBooks.DataItem(7,ResourceTable.Media_book_8,"8"));
        dataItems.add(new PageProviderOfBooks.DataItem(8,ResourceTable.Media_book_9,"9"));
        dataItems.add(new PageProviderOfBooks.DataItem(9,ResourceTable.Media_book_10,"10"));
        dataItems.add(new PageProviderOfBooks.DataItem(10,ResourceTable.Media_book_11,"11"));
        return dataItems;
    }
    private String story[]=new String[]{
            "The Surprise",
            "A Letter from Bear",
            "Fox went to see Bear.On the way he met Rabbit",
            "Fox and Rabbit went to see Bear. On the way, they met Deer.",
            "Fox and Rabbit and Deer went to see Bear.On the way,they met Squirrel",
            "Fox and Rabbit and Deer and Squirrel went to see Bear.On the way, they met Bird",
            "Fox and Rabbit and Deer and Squirrel and Bird went to see Bear.On the way, they met Raccoon",
            "So Fox and Rabbit and Deer and Squirrel and Bird and Raccoon all went to see Bear.",
            "'Hello, Bear', said Fox.\n'Come in!' said Bear",
            "Hi",
            "What a surprise!"
    };
}
