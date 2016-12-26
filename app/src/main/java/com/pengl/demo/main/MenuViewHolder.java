package com.pengl.demo.main;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.pengl.demo.R;
import com.pengl.demo.grid.GridActivity;
import com.pengl.demo.multipleitem.MultiItemActivity;
import com.pengl.demo.singleitem.ManualLoadMoreActivity;
import com.pengl.demo.singleitem.SingleItemActivity;
import com.pengl.demo.staggered.StaggeredActivity;
import com.pengl.PLRecyclerView.AbstractViewHolder;

/**
 * Author: Season(ssseasonnn@gmail.com)
 * Date: 2016/10/8
 * Time: 14:22
 * FIXME
 */
class MenuViewHolder extends AbstractViewHolder<MenuBean> {
    @BindView(R.id.menu)
    TextView mMenu;
    @BindView(R.id.container)
    LinearLayout mLayout;

    private Context mContext;
    private MenuBean mBean;

    MenuViewHolder(ViewGroup parent) {
        super(parent, R.layout.menu_item);
        ButterKnife.bind(this, itemView);
        mContext = parent.getContext();
    }

    @OnClick(R.id.container)
    public void onClick(View view) {
        int type = mBean.type;
        switch (type) {
            case 0:
                mContext.startActivity(new Intent(mContext, SingleItemActivity.class));
                break;
            case 1:
                mContext.startActivity(new Intent(mContext, MultiItemActivity.class));
                break;
            case 2:
                mContext.startActivity(new Intent(mContext, GridActivity.class));
                break;
            case 3:
                mContext.startActivity(new Intent(mContext, StaggeredActivity.class));
                break;
            case 4:
                mContext.startActivity(new Intent(mContext, ManualLoadMoreActivity.class));
        }

    }

    @Override
    public void setData(MenuBean data) {
        mBean = data;
        mMenu.setText(data.menu);
    }
}
