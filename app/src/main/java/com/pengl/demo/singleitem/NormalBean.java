package com.pengl.demo.singleitem;

import com.pengl.PLRecyclerView.ItemType;

/**
 *
 */
class NormalBean implements ItemType {
    String mImg;
    String mTitle;
    String mContent;

    NormalBean(String img, String title, String content) {
        mImg = img;
        mContent = content;
        mTitle = title;
    }

    @Override
    public int itemType() {
        return 0;
    }
}
