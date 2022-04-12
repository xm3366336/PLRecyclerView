package com.pengl.demo.adapter;

import android.view.ViewGroup;

import com.pengl.recyclerview.AbstractAdapter;
import com.pengl.demo.model.BeanDrag;
import com.pengl.demo.viewHolder.ViewHolderDrag;

/**
 *
 */
public class AdapterDrag extends AbstractAdapter<BeanDrag, ViewHolderDrag> {
    @Override
    protected ViewHolderDrag onNewCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderDrag(parent);
    }

    @Override
    protected void onNewBindViewHolder(ViewHolderDrag holder, int position) {
        holder.setData(get(position));
    }

}
