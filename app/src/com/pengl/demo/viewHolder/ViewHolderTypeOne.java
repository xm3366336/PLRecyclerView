package com.pengl.demo.viewHolder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pengl.recyclerview.AbstractViewHolder;
import com.pengl.demo.R;
import com.pengl.demo.model.BeanTypeOne;

/**
 *
 */
public class ViewHolderTypeOne extends AbstractViewHolder<BeanTypeOne> {

    private final Context mContext;
    private final TextView mText;

    public ViewHolderTypeOne(ViewGroup parent) {
        super(parent, R.layout.item_type_one);
        mContext = parent.getContext();
        mText = itemView.findViewById(R.id.text);
    }

    @Override
    public void setData(BeanTypeOne data) {
        mText.setText(data.getText());
        mText.setOnClickListener(v -> Toast.makeText(mContext, mText.getText().toString(), Toast.LENGTH_SHORT).show());
    }

}