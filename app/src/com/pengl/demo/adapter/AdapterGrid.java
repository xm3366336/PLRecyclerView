package com.pengl.demo.adapter;

import android.view.ViewGroup;

import com.pengl.recyclerview.AbstractAdapter;
import com.pengl.demo.viewHolder.ViewHolderGrid;
import com.pengl.demo.model.BeanGrid;

/**
 *
 */
public class AdapterGrid extends AbstractAdapter<BeanGrid, ViewHolderGrid> {
    @Override
    protected ViewHolderGrid onNewCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderGrid(parent);
    }

    @Override
    protected void onNewBindViewHolder(ViewHolderGrid holder, int position) {
        holder.setData(get(position));
    }
}
