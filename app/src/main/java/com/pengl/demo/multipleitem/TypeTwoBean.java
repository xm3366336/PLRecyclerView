package com.pengl.demo.multipleitem;

import com.pengl.demo.RecyclerItemType;
import com.pengl.PLRecyclerView.ItemType;

/**
 *
 */
public class TypeTwoBean implements ItemType {
    String text;

    public TypeTwoBean(String text) {
        this.text = text;
    }

    @Override
    public int itemType() {
        return RecyclerItemType.TYPE2.getValue();
    }
}
