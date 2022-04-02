package com.pengl.demo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.pengl.PLRecyclerView;
import com.pengl.recyclerview.ConfigureAdapter;
import com.pengl.demo.R;
import com.pengl.demo.adapter.AdapterNormal;
import com.pengl.demo.model.BeanNormal;
import com.pengl.demo.viewHolder.ViewHeader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 *
 */
public class ManualLoadMoreActivity extends AppCompatActivity {

    private AdapterNormal mAdapter;
    private final MyHandler mHandler = new MyHandler(this);

    private Button mNextPage;
    private LinearLayout mProgress;

    private int pageNum;    // 当前页码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_load);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        PLRecyclerView mRecycler = findViewById(R.id.recycler);
        mRecycler.setAutoLoadEnable(false);// 关闭自动加载更多, 改为手动触发
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AdapterNormal();
        mRecycler.setAdapterWithLoading(mAdapter);
        mRecycler.setRefreshListener(() -> {
            pageNum = 1;
            getData();
        });
        mRecycler.setLoadMoreListener(this::getData);

        mRecycler.configureView(new ConfigureAdapter() {
            @Override
            public void configureLoadMoreView(View loadMoreView) {
                super.configureLoadMoreView(loadMoreView);
                mNextPage = loadMoreView.findViewById(R.id.next_page);
                mProgress = loadMoreView.findViewById(R.id.loading);
                mNextPage.setOnClickListener(v -> {
                    setVisibility(View.GONE, View.VISIBLE);
                    mAdapter.manualLoadMore(); // 手动触发加载更多
                });
            }
        });

        pageNum = 1;
        getData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        mHandler.postDelayed(() -> mHandler.sendEmptyMessage(0), 1000);
    }

    private void setVisibility(int visible1, int visible2) {
        mNextPage.setVisibility(visible1);
        mProgress.setVisibility(visible2);
    }

    private static class MyHandler extends Handler {
        private final WeakReference<ManualLoadMoreActivity> mActivity;

        MyHandler(ManualLoadMoreActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            ManualLoadMoreActivity activity = mActivity.get();
            if (null == activity) {
                return;
            }

            if (activity.pageNum == 1) {
                activity.mAdapter.clear();
                activity.mAdapter.addHeader(new ViewHeader());
            }

            int currCount = activity.mAdapter.getDataSize();
            List<BeanNormal> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                int position = i + 1 + currCount;
                String str = activity.getString(R.string.seq_data_2, position);
                list.add(new BeanNormal(str, str));
            }

            activity.mAdapter.addAll(list);
            activity.pageNum++;
            activity.setVisibility(View.VISIBLE, View.GONE);
        }
    }
}