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
 * Date: 2016/10/10
 * Time: 12:01
 * FIXME
 */
public class TypeTwoViewHolder extends AbstractViewHolder<TypeTwoBean> {

    @BindView(R2.id.text)
    TextView mText;
    private Context mContext;

    public TypeTwoViewHolder(ViewGroup parent) {
        super(parent, R.layout.type_two_item);
        ButterKnife.bind(this, itemView);
        mContext = parent.getContext();
    }

    @Override
    public void setData(TypeTwoBean data) {
        mText.setText(data.text);
    }

    @OnClick(R.id.text)
    public void onClick() {
        Toast.makeText(mContext, mText.getText(), Toast.LENGTH_SHORT).show();
    }
}
