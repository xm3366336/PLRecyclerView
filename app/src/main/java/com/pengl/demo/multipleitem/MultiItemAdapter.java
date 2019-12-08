package com.pengl.demo.multipleitem;

import android.view.ViewGroup;

import com.pengl.demo.RecyclerItemType;
import com.pengl.PLRecyclerView.AbstractAdapter;
import com.pengl.PLRecyclerView.AbstractViewHolder;
import com.pengl.PLRecyclerView.ItemType;

/**
 *
 */
public class MultiItemAdapter extends AbstractAdapter<ItemType, AbstractViewHolder> {

    @Override
    protected AbstractViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RecyclerItemType.TYPE1.getValue()) {
            return new TypeOneViewHolder(parent);
        } else if (viewType == RecyclerItemType.TYPE2.getValue()) {
            return new TypeTwoViewHolder(parent);
        }
        return null;
    }

    @Override
    protected void onNewBindViewHolder(AbstractViewHolder holder, int position) {
        if (holder instanceof TypeOneViewHolder) {
            holder.setData((TypeOneBean) get(position));
        } else if (holder instanceof TypeTwoViewHolder) {
            holder.setData((TypeTwoBean) get(position));
        }
    }
}
