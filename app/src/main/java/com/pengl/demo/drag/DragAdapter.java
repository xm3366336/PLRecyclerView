package com.pengl.demo.drag;

import android.view.ViewGroup;

import com.pengl.PLRecyclerView.AbstractAdapter;

/**
 *
 */
class DragAdapter extends AbstractAdapter<DragBean, DragViewHolder> {
    @Override
    protected DragViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
        return new DragViewHolder(parent);
    }

    @Override
    protected void onNewBindViewHolder(DragViewHolder holder, int position) {
        holder.setData(get(position));
    }

}
