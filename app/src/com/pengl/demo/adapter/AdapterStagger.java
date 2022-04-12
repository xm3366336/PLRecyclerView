package com.pengl.demo.adapter;

import android.view.ViewGroup;

import com.pengl.recyclerview.AbstractAdapter;
import com.pengl.demo.model.BeanStagger;
import com.pengl.demo.viewHolder.ViewHolderStagger;

/**
 *
 */
public class AdapterStagger extends AbstractAdapter<BeanStagger, ViewHolderStagger> {
    @Override
    protected ViewHolderStagger onNewCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderStagger(parent);
    }

    @Override
    protected void onNewBindViewHolder(ViewHolderStagger holder, int position) {
        holder.setData(get(position));
    }
}
