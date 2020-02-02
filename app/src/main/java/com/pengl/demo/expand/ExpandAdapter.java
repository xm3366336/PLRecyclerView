package com.pengl.demo.expand;

import android.view.ViewGroup;

import com.pengl.PLRecyclerView.AbstractAdapter;
import com.pengl.PLRecyclerView.AbstractViewHolder;
import com.pengl.PLRecyclerView.ItemType;
import com.pengl.demo.RecyclerItemType;

/**
 *
 */
class ExpandAdapter extends AbstractAdapter<ItemType, AbstractViewHolder> {

    @Override
    protected AbstractViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RecyclerItemType.PARENT.getValue()) {
            return new ParentViewHolder(this, parent);
        } else if (viewType == RecyclerItemType.CHILD.getValue()) {
            return new ChildViewHolder(parent);
        }
        return null;
    }

    @Override
    protected void onNewBindViewHolder(AbstractViewHolder holder, int position) {
        if (holder instanceof ParentViewHolder) {
            holder.setData(get(position));
        } else if (holder instanceof ChildViewHolder) {
            holder.setData(get(position));
        }
    }
}
