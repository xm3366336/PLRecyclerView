package com.pengl.demo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pengl.recyclerview.ConfigureAdapter;
import com.pengl.demo.R;
import com.pengl.demo.adapter.AdapterNormal;
import com.pengl.demo.model.BeanNormal;
import com.pengl.demo.utils.BaseActivity;
import com.pengl.demo.utils.Common;
import com.pengl.demo.viewHolder.ViewHeader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

public class SingleItemActivity extends BaseActivity {

    private AdapterNormal mAdapter;
    private final MyHandler mHandler = new MyHandler(this);
    private int pageNum;    // 当前页码
    private boolean isShowLoadMoreFailed; // 是否显示了加载失败，用于模拟加载失败的情况

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new AdapterNormal();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapterWithLoading(mAdapter);
        mRecycler.setRefreshListener(() -> {
            pageNum = 1;
            getData();
        });
        mRecycler.setLoadMoreListener(this::getData);
        mRecycler.configureView(new ConfigureAdapter() {
            @Override
            public void configureEmptyView(View emptyView) {

            }

            @Override
            public void configureErrorView(View errorView) {

            }

            @Override
            public void configureLoadingView(View loadingView) {

            }

            @Override
            public void configureLoadMoreView(View loadMoreView) {

            }

            @Override
            public void configureNoMoreView(View noMoreView) {
                noMoreView.setOnClickListener(view1 -> Toast.makeText(SingleItemActivity.this, "就这么多数据了", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void configureLoadMoreFailedView(View loadMoreFailedView) {
                loadMoreFailedView.setOnClickListener(view1 -> mAdapter.resumeLoadMore());
            }
        });

        pageNum = 1;
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_opt_insert) {
            mAdapter.insert(5, new BeanNormal(getString(R.string.seq_data_5), getString(R.string.seq_data_6, Common.getRandom(4))));
        } else if (id == R.id.action_opt_insert_back) {
            mAdapter.insertBack(5, new BeanNormal(getString(R.string.seq_data_5), getString(R.string.seq_data_6, Common.getRandom(4))));
        } else if (id == R.id.action_opt_remove) {
            mAdapter.remove(5);
        } else if (id == R.id.action_opt_remove_multi) {
            mAdapter.removeBack(5, 2);
        } else if (id == R.id.action_opt_empty) {
            mAdapter.showEmpty();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        mHandler.postDelayed(() -> mHandler.sendEmptyMessage(0), 1000);
    }

    private static class MyHandler extends Handler {
        private final WeakReference<SingleItemActivity> mActivity;

        MyHandler(SingleItemActivity activity) {
            super(Looper.myLooper());
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            SingleItemActivity activity = mActivity.get();
            if (null == activity) {
                return;
            }

            if (activity.pageNum == 1) {
                activity.mAdapter.clear();
                activity.mAdapter.addHeader(new ViewHeader());
            }

            int currCount = activity.mAdapter.getDataSize();

            // 超过50条，再次获取，则显示没有更多数据了。
            if (currCount >= 50) {
                activity.mAdapter.showNoMore();
                return;
            }

            // 取到30条时，取次获取，则模拟提示一次加载失败
            if (currCount == 30 && !activity.isShowLoadMoreFailed) {
                activity.mAdapter.loadMoreFailed();
                activity.isShowLoadMoreFailed = true;
                return;
            }

            List<BeanNormal> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                int position = i + 1 + currCount;
                String str = activity.getString(R.string.seq_data_2, position);
                list.add(new BeanNormal(str, str));
            }

            activity.mAdapter.addAll(list);
            activity.pageNum++;
        }
    }
}
