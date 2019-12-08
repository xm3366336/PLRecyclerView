package com.pengl.demo.main;

import android.view.ViewGroup;

import com.pengl.PLRecyclerView.AbstractAdapter;

/**
 *
 */
class MainAdapter extends AbstractAdapter<MenuBean, MenuViewHolder> {

    @Override
    protected MenuViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
        return new MenuViewHolder(parent);
    }

    @Override
    protected void onNewBindViewHolder(MenuViewHolder holder, int position) {
        holder.setData(get(position));
    }

}
