package com.pengl.demo.multipleitem;

import com.pengl.PLRecyclerView.ItemType;

import java.util.List;

/**
 *
 */
public interface MultiItemView {
    void onDataLoadSuccess(List<ItemType> list, boolean isRefresh);

    void onDataLoadFailed(boolean isRefresh);
}
