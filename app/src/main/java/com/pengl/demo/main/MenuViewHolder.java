package com.pengl.demo.main;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pengl.PLRecyclerView.AbstractViewHolder;
import com.pengl.demo.R;
import com.pengl.demo.drag.DragActivity;
import com.pengl.demo.expand.ExpandActivity;
import com.pengl.demo.grid.GridActivity;
import com.pengl.demo.multipleitem.MultiItemActivity;
import com.pengl.demo.singleitem.ManualLoadMoreActivity;
import com.pengl.demo.singleitem.SingleItemActivity;
import com.pengl.demo.staggered.StaggeredActivity;

/**
 *
 */
class MenuViewHolder extends AbstractViewHolder<MenuBean> {

    private View container;
    private TextView mMenu;
    private Context mContext;

    MenuViewHolder(ViewGroup parent) {
        super(parent, R.layout.menu_item);
        mContext = parent.getContext();
        container = itemView.findViewById(R.id.container);
        mMenu = itemView.findViewById(R.id.menu);
    }

    @Override
    public void setData(final MenuBean data) {
        mMenu.setText(data.getMenu());
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = data.getType();
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
                        break;
                    case 5:
                        mContext.startActivity(new Intent(mContext, DragActivity.class));
                        break;
                    case 6:
                        mContext.startActivity(new Intent(mContext, ExpandActivity.class));
                        break;
                }
            }
        });
    }
}
