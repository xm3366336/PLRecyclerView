package com.pengl.demo.singleitem;

import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pengl.PLRecyclerView.AbstractViewHolder;
import com.pengl.demo.R;

/**
 *
 */
class NormalViewHolder extends AbstractViewHolder<NormalBean> {

    private ImageView mHead;
    private TextView mTitle;
    private TextView mContent;

    private Context mContext;

    NormalViewHolder(ViewGroup parent) {
        super(parent, R.layout.normal_item);
        mContext = parent.getContext();
        mHead = itemView.findViewById(R.id.head);
        mTitle = itemView.findViewById(R.id.title);
        mContent = itemView.findViewById(R.id.content);
    }

    @Override
    public void setData(NormalBean data) {
        Glide.with(mContext).load(Uri.parse(data.mImg)).into(mHead);
        mTitle.setText(data.mTitle);
        mContent.setText(data.mContent);
    }
}