package com.example.java_demo1;

import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;

import java.util.List;

public class PageProviderOfBooks extends PageSliderProvider {
    //数据实体类
    public static class DataItem{
        String des;
        int imageID;
        int index;

        public DataItem(int index, int id, String txt) {
            this.index = index;
            imageID = id;
            des = txt;
        }
    }
    // 数据源，每个页面对应list中的一项
    private List<DataItem> list;
    private Context mContext;
    public PageProviderOfBooks( Context context,List<DataItem> list) {
        this.list = list;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int i) {
        final DataItem data = list.get(i);
        Image image = new Image(this.mContext);
        image.setPixelMap(data.imageID);
        image.setLayoutConfig( new StackLayout.LayoutConfig(
                        ComponentContainer.LayoutConfig.MATCH_PARENT,
                        ComponentContainer.LayoutConfig.MATCH_PARENT

                ));
        image.setMarginsLeftAndRight(15, 15);
        image.setMarginsTopAndBottom(15, 15);
        image.setClipAlignment(Image.GRAVITY_CENTER);
        image.setScaleMode(Image.ScaleMode.CLIP_CENTER);
        ShapeElement element = new ShapeElement(mContext,ResourceTable.Graphic_background_slidepage_books);
        image.setBackground(element);
        componentContainer.addComponent(image);

        return image;

    }
    @Override
    public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
        componentContainer.removeComponent((Component) o);
    }
    @Override
    public boolean isPageMatchToObject(Component component, Object o) {
        //可添加具体处理逻辑
        return true;
    }
}