package com.pengl.demo.expand;

import com.google.gson.annotations.SerializedName;
import com.pengl.PLRecyclerView.ItemType;
import com.pengl.demo.RecyclerItemType;

/**
 *
 */
class ChildBean implements ItemType {

    @SerializedName("text")
    int text;

    @Override
    public int itemType() {
        return RecyclerItemType.CHILD.getValue();
    }
}
