package com.pengl.demo.singleitem;

import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pengl.PLRecyclerView.AbstractViewHolder;
import com.pengl.demo.R;
import com.pengl.demo.R2;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: Season(ssseasonnn@gmail.com)
 * Date: 2016/9/22
 * Time: 09:58
 * FIXME
 */

class NormalViewHolder extends AbstractViewHolder<NormalBean> {
    @BindView(R2.id.head)
    ImageView mHead;
    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.content)
    TextView mContent;

    private Context mContext;

    NormalViewHolder(ViewGroup parent) {
        super(parent, R.layout.normal_item);
        mContext = parent.getContext();
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setData(NormalBean data) {
        Picasso.with(mContext).load(Uri.parse(data.mImg)).into(mHead);
        mTitle.setText(data.mTitle);
        mContent.setText(data.mContent);
    }
}