package com.pengl.demo.adapter;

import android.view.ViewGroup;

import com.pengl.recyclerview.AbstractAdapter;
import com.pengl.recyclerview.AbstractViewHolder;
import com.pengl.recyclerview.ItemType;
import com.pengl.demo.model.RecyclerItemType;
import com.pengl.demo.model.BeanTypeOne;
import com.pengl.demo.model.BeanTypeTwo;
import com.pengl.demo.viewHolder.ViewHolderTypeOne;
import com.pengl.demo.viewHolder.ViewHolderTypeTwo;

/**
 *
 */
public class AdapterMultiItem extends AbstractAdapter<ItemType, AbstractViewHolder<ItemType>> {

    @Override
    protected AbstractViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RecyclerItemType.TYPE1.getValue()) {
            return new ViewHolderTypeOne(parent);
        } else if (viewType == RecyclerItemType.TYPE2.getValue()) {
            return new ViewHolderTypeTwo(parent);
        }
        return null;
    }

    @Override
    protected void onNewBindViewHolder(AbstractViewHolder holder, int position) {
        if (holder instanceof ViewHolderTypeOne) {
            holder.setData((BeanTypeOne) get(position));
        } else if (holder instanceof ViewHolderTypeTwo) {
            holder.setData((BeanTypeTwo) get(position));
        }
    }
}
