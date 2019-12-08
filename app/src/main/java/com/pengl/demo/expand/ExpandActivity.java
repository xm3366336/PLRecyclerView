package com.pengl.demo.expand;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.pengl.PLRecyclerView.PLRecyclerView;
import com.pengl.demo.R;

import java.util.List;

public class ExpandActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private PLRecyclerView mRecycler;

    private ExpandAdapter mAdapter;
    private ExpandPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mRecycler = findViewById(R.id.recycler);
        mAdapter = new ExpandAdapter();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapterWithLoading(mAdapter);

        mRecycler.setRefreshListener(new PLRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadData();
            }
        });

        mPresenter = new ExpandPresenter(this);
        mPresenter.setDataLoadCallBack(new ExpandView() {
            @Override
            public void onDataLoadSuccess(List<ParentBean> list) {
                mAdapter.clear();
//                mAdapter.addHeader(new Header());
                //                    mAdapter.addFooter(new Header());
                mAdapter.addAll(list);
            }

            @Override
            public void onDataLoadFailed() {
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
        mPresenter.loadData();
    }
}
