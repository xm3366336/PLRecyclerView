package com.pengl.demo.grid;

import android.view.ViewGroup;

import com.pengl.PLRecyclerView.AbstractAdapter;

/**
 *
 */
public class GridAdapter extends AbstractAdapter<GridBean, GridViewHolder> {
    @Override
    protected GridViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
        return new GridViewHolder(parent);
    }

    @Override
    protected void onNewBindViewHolder(GridViewHolder holder, int position) {
        holder.setData(get(position));
    }
}
