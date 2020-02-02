package com.pengl.demo.expand;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pengl.PLRecyclerView.AbstractAdapter;
import com.pengl.PLRecyclerView.AbstractViewHolder;
import com.pengl.demo.R;

import java.util.List;

/**
 *
 */
class ParentViewHolder extends AbstractViewHolder<ParentBean> implements View.OnClickListener {

    private TextView mText;
    private ImageView mImageView;

    private ParentBean parent;
    private List<ChildBean> child;

    private ExpandAdapter mAdapter;

    ParentViewHolder(AbstractAdapter adapter, ViewGroup parent) {
        super(parent, R.layout.parent_item);
        mText = itemView.findViewById(R.id.text);
        mImageView = itemView.findViewById(R.id.image);
        mAdapter = (ExpandAdapter) adapter;

        mText.setOnClickListener(this);
    }

    @Override
    public void setData(ParentBean data) {
        mText.setText(String.valueOf(data.text));
        child = data.mChild;
        parent = data;
    }

    @Override
    public void onClick(View v) {
        if (parent.isExpand) {
            mAdapter.removeBack(getAdapterPosition(), child.size());
            parent.isExpand = false;
            mImageView.setImageResource(R.drawable.ic_keyboard_arrow_down);
        } else {
            mAdapter.insertAllBack(getAdapterPosition(), child);
            parent.isExpand = true;
            mImageView.setImageResource(R.drawable.ic_keyboard_arrow_up);
        }
    }
}
