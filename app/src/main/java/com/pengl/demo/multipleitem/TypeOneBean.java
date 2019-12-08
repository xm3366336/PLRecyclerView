package com.pengl.demo.multipleitem;

import com.pengl.demo.RecyclerItemType;
import com.pengl.PLRecyclerView.ItemType;

/**
 *
 */
class TypeOneBean implements ItemType {

    public String text;

    public TypeOneBean(String text) {
        this.text = text;
    }

    @Override
    public int itemType() {
        return RecyclerItemType.TYPE1.getValue();
    }
}
