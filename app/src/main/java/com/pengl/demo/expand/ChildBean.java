package com.pengl.demo.expand;

import com.google.gson.annotations.SerializedName;
import com.pengl.PLRecyclerView.ItemType;
import com.pengl.demo.RecyclerItemType;

/**
 * Author: Season(ssseasonnn@gmail.com)
 * Date: 2016/10/17
 * Time: 15:29
 * FIXME
 */
class ChildBean implements ItemType {
    @SerializedName("text")
    int text;

    @Override
    public int itemType() {
        return RecyclerItemType.CHILD.getValue();
    }
}
