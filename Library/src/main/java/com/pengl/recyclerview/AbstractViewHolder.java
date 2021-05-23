package com.pengl.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 */
public abstract class AbstractViewHolder<T extends ItemType> extends RecyclerView.ViewHolder {

    public AbstractViewHolder(View itemView) {
        super(itemView);
    }

    public AbstractViewHolder(ViewGroup parent, @LayoutRes int res) {
        super(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));
    }

    public abstract void setData(T data);
}