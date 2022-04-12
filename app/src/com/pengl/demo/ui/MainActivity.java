package com.pengl.demo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pengl.PLRecyclerView;
import com.pengl.recyclerview.AbstractAdapter;
import com.pengl.recyclerview.AbstractViewHolder;
import com.pengl.recyclerview.PLLinearLayoutManager;
import com.pengl.demo.R;
import com.pengl.demo.model.BeanMainFun;
import com.pengl.demo.viewHolder.ViewFooter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final Class<?>[] ARRAY_CLASS = new Class<?>[]{//
            EmptyActivity.class,//
            ErrorActivity.class,//
            SingleItemActivity.class, //
            MultiItemActivity.class, //
            GridActivity.class, //
            StaggeredActivity.class, //
            ManualLoadMoreActivity.class, //
            DragActivity.class, //
            ExpandActivity.class //
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        MainAdapter mAdapter = new MainAdapter();

        PLRecyclerView mRecycler = findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new PLLinearLayoutManager(this));
        mRecycler.setAdapterWithLoading(mAdapter);

        String[] ARRAY_TITLE = getResources().getStringArray(R.array.main_fun_name);
        List<BeanMainFun> mData = new ArrayList<>();
        for (int i = 0, size = ARRAY_TITLE.length; i < size; i++) {
            mData.add(new BeanMainFun(ARRAY_TITLE[i], ARRAY_CLASS[i]));
        }
        mAdapter.clear();
        mAdapter.addFooter(new ViewFooter());
        mAdapter.addAll(mData);
    }

    private static class MainAdapter extends AbstractAdapter<BeanMainFun, MenuViewHolder> {

        @Override
        protected MenuViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
            return new MenuViewHolder(parent);
        }

        @Override
        protected void onNewBindViewHolder(MenuViewHolder holder, int position) {
            holder.setData(get(position));
        }
    }

    private static class MenuViewHolder extends AbstractViewHolder<BeanMainFun> {

        private final View container;
        private final TextView mMenu;
        private final Context mContext;

        MenuViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_menu);
            mContext = parent.getContext();
            container = itemView.findViewById(R.id.container);
            mMenu = itemView.findViewById(R.id.menu);
        }

        @Override
        public void setData(final BeanMainFun data) {
            mMenu.setText(data.getMenuName());
            container.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, data.getJumpToClass())));
        }
    }
}
