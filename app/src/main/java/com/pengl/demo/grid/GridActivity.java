package com.pengl.demo.grid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.pengl.PLRecyclerView.PLRecyclerView;
import com.pengl.demo.Header;
import com.pengl.demo.R;

import java.util.List;

public class GridActivity extends AppCompatActivity {

    Toolbar mToolbar;
    PLRecyclerView mRecycler;
    RelativeLayout mContentGrid;

    private GridAdapter mAdapter;
    private GridPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mContentGrid = findViewById(R.id.content_grid);
        mRecycler = findViewById(R.id.recycler);
        mAdapter = new GridAdapter();
        mRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        mRecycler.setAdapterWithLoading(mAdapter);

        mRecycler.setRefreshListener(new PLRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadData(true);
            }
        });

        mRecycler.setLoadMoreListener(new PLRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPresenter.loadData(false);
            }
        });

        mPresenter = new GridPresenter(this);
        mPresenter.setDataLoadCallBack(new GridView() {
            @Override
            public void onDataLoadSuccess(List<GridBean> list, boolean isRefresh) {
                if (isRefresh) {
                    mAdapter.clear();
                    mAdapter.addHeader(new Header());
                    //                    mAdapter.addFooter(new Header());
                    mAdapter.addAll(list);
                } else {
                    mAdapter.addAll(list);
                }
            }

            @Override
            public void onDataLoadFailed(boolean isRefresh) {
                if (isRefresh) {
                    mAdapter.showError();
                } else {
                    mAdapter.loadMoreFailed();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribeAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadData(true);
    }

}
