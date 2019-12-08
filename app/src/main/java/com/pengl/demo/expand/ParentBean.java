package com.pengl.demo.expand;

import com.google.gson.annotations.SerializedName;
import com.pengl.PLRecyclerView.ItemType;
import com.pengl.demo.RecyclerItemType;

import java.util.List;

/**
 *
 */
class ParentBean implements ItemType {

    @SerializedName("text")
    int text;

    @SerializedName("child")
    List<ChildBean> mChild;

    /**
     * 是否展开
     */
    boolean isExpand;

    @Override
    public int itemType() {
        return RecyclerItemType.PARENT.getValue();
    }
}
