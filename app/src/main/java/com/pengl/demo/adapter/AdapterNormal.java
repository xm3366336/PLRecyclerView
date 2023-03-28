package com.pengl.demo.adapter;

import android.view.ViewGroup;

import com.pengl.recyclerview.AbstractAdapter;
import com.pengl.demo.model.BeanNormal;
import com.pengl.demo.viewHolder.ViewHolderNormal;

public class AdapterNormal extends AbstractAdapter<BeanNormal, ViewHolderNormal> {

    @Override
    protected ViewHolderNormal onNewCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderNormal(parent);
    }

    @Override
    protected void onNewBindViewHolder(ViewHolderNormal holder, int position) {
        holder.setData(get(position));
    }
}
