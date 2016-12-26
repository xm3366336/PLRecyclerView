package com.pengl.demo.grid;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.pengl.demo.R;
import com.pengl.PLRecyclerView.AbstractViewHolder;

/**
 * Author: Season(ssseasonnn@gmail.com)
 * Date: 2016/10/10
 * Time: 10:15
 * FIXME
 */
public class GridViewHolder extends AbstractViewHolder<GridBean> {
    @BindView(R.id.text)
    TextView mText;
    private Context mContext;

    public GridViewHolder(ViewGroup parent) {
        super(parent, R.layout.grid_item);
        ButterKnife.bind(this, itemView);
        mContext = parent.getContext();
    }

    @Override
    public void setData(GridBean data) {
        mText.setText(data.text);
    }

    @OnClick(R.id.text)
    public void onClick() {
        Toast.makeText(mContext, mText.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}
