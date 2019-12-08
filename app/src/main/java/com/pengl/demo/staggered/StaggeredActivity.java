package com.pengl.demo.staggered;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.pengl.PLRecyclerView.PLRecyclerView;
import com.pengl.demo.Header;
import com.pengl.demo.R;

import java.util.List;

/**
 * 瀑布流
 */
public class StaggeredActivity extends AppCompatActivity {

    Toolbar mToolbar;
    PLRecyclerView mRecycler;
    RelativeLayout mContentStaggered;

    private StaggerAdapter mAdapter;
    private StaggerPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staggered);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mContentStaggered = findViewById(R.id.content_staggered);
        mRecycler = findViewById(R.id.recycler);
        mAdapter = new StaggerAdapter();
        mRecycler.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
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

        mPresenter = new StaggerPresenter(this);
        mPresenter.setDataLoadCallBack(new StaggerView() {
            @Override
            public void onDataLoadSuccess(List<StaggerBean> list, boolean isRefresh) {
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
