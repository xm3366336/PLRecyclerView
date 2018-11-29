package com.pengl.demo.expand;

import com.google.gson.annotations.SerializedName;
import com.pengl.PLRecyclerView.ItemType;
import com.pengl.demo.RecyclerItemType;

import java.util.List;

/**
 * Author: Season(ssseasonnn@gmail.com)
 * Date: 2016/10/17
 * Time: 15:28
 * FIXME
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
