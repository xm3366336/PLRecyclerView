package com.pengl.demo.expand;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pengl.PLRecyclerView.AbstractAdapter;
import com.pengl.PLRecyclerView.AbstractViewHolder;
import com.pengl.demo.R;

import java.util.List;

/**
 *
 */
class ParentViewHolder extends AbstractViewHolder<ParentBean> implements View.OnClickListener {

    TextView mText;
    ImageView mImageView;

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
            Glide.with(mImageView.getContext()).load(R.drawable.ic_keyboard_arrow_down).into(mImageView);
        } else {
            mAdapter.insertAllBack(getAdapterPosition(), child);
            parent.isExpand = true;
            Glide.with(mImageView.getContext()).load(R.drawable.ic_keyboard_arrow_up).into(mImageView);
        }
    }
}
