package com.pengl.demo.multipleitem;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pengl.PLRecyclerView.AbstractViewHolder;
import com.pengl.demo.R;
import com.pengl.demo.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author: Season(ssseasonnn@gmail.com)
 * Date: 2016/9/22
 * Time: 09:59
 * FIXME
 */
class TypeOneViewHolder extends AbstractViewHolder<TypeOneBean> {

    @BindView(R2.id.text)
    TextView mText;

    private Context mContext;

    TypeOneViewHolder(ViewGroup parent) {
        super(parent, R.layout.type_one_item);
        ButterKnife.bind(this, itemView);
        mContext = parent.getContext();
    }

    @Override
    public void setData(TypeOneBean data) {
        mText.setText(data.text);
    }

    @OnClick(R.id.text)
    public void onClick() {
        Toast.makeText(mContext, mText.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}