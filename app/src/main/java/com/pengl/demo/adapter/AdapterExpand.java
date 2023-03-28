package com.pengl.demo.adapter;

import android.view.ViewGroup;

import com.pengl.recyclerview.AbstractAdapter;
import com.pengl.recyclerview.AbstractViewHolder;
import com.pengl.recyclerview.ItemType;
import com.pengl.demo.model.RecyclerItemType;
import com.pengl.demo.viewHolder.ViewHolderChild;
import com.pengl.demo.viewHolder.ViewHolderParent;

/**
 *
 */
public class AdapterExpand extends AbstractAdapter<ItemType, AbstractViewHolder<ItemType>> {

    @Override
    protected AbstractViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RecyclerItemType.PARENT.getValue()) {
            return new ViewHolderParent(this, parent);
        } else if (viewType == RecyclerItemType.CHILD.getValue()) {
            return new ViewHolderChild(parent);
        }
        return null;
    }

    @Override
    protected void onNewBindViewHolder(AbstractViewHolder holder, int position) {
        if (holder instanceof ViewHolderParent) {
            holder.setData(get(position));
        } else if (holder instanceof ViewHolderChild) {
            holder.setData(get(position));
        }
    }
}
