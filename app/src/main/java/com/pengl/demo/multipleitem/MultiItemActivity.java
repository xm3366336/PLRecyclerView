package com.pengl.demo.multipleitem;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pengl.PLRecyclerView.ItemType;
import com.pengl.PLRecyclerView.PLRecyclerView;
import com.pengl.demo.Header;
import com.pengl.demo.R;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MultiItemActivity extends AppCompatActivity {

    Toolbar mToolbar;
    PLRecyclerView mRecycler;
    RelativeLayout mContentMultiItem;

    private MultiItemAdapter mAdapter;
    private MultiItemPresenter mPresenter;

    private boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_item);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mContentMultiItem = findViewById(R.id.content_multi_item);
        mRecycler = findViewById(R.id.recycler);
        mAdapter = new MultiItemAdapter();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
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

        mPresenter = new MultiItemPresenter(this);
        mPresenter.setDataLoadCallBack(new MultiItemView() {
            @Override
            public void onDataLoadSuccess(List<ItemType> list, boolean isRefresh) {
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

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = !flag;
                Toast.makeText(MultiItemActivity.this, "flag:" + flag, Toast.LENGTH_SHORT).show();
//        mRecycler.setNoMoreViewEnabled(flag);
                mRecycler.setLoadMoreViewEnabled(flag);
//        mRecycler.setLoadMoreFailedViewEnabled(flag);
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
