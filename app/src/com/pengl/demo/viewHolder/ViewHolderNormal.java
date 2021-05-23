package com.pengl.demo.viewHolder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.pengl.recyclerview.AbstractViewHolder;
import com.pengl.demo.R;
import com.pengl.demo.model.BeanNormal;

public class ViewHolderNormal extends AbstractViewHolder<BeanNormal> {

    private TextView mTitle;
    private TextView mContent;

    public ViewHolderNormal(ViewGroup parent) {
        super(parent, R.layout.item_normal);
        mTitle = itemView.findViewById(R.id.title);
        mContent = itemView.findViewById(R.id.content);
    }

    @Override
    public void setData(BeanNormal data) {
        mTitle.setText(data.getTitle());
        mContent.setText(data.getContent());
    }
}
