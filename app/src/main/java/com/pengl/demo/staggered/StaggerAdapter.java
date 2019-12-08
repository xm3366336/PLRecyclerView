package com.pengl.demo.staggered;

import android.view.ViewGroup;

import com.pengl.PLRecyclerView.AbstractAdapter;

/**
 *
 */
public class StaggerAdapter extends AbstractAdapter<StaggerBean, StaggerViewHolder> {
    @Override
    protected StaggerViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
        return new StaggerViewHolder(parent);
    }

    @Override
    protected void onNewBindViewHolder(StaggerViewHolder holder, int position) {
        holder.setData(get(position));
    }
}
