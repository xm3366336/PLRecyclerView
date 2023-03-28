package com.pengl.demo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.pengl.demo.adapter.AdapterStagger;
import com.pengl.demo.model.BeanStagger;
import com.pengl.demo.utils.BaseActivity;
import com.pengl.demo.viewHolder.ViewHeader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * 瀑布流
 */
public class StaggeredActivity extends BaseActivity {

    private AdapterStagger mAdapter;

    private final MyHandler mHandler = new MyHandler(this);
    private int pageNum;    // 当前页码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new AdapterStagger();
        mRecycler.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mRecycler.setAdapterWithLoading(mAdapter);
        mRecycler.setRefreshListener(() -> {
            pageNum = 1;
            getData();
        });
        mRecycler.setLoadMoreListener(this::getData);

        pageNum = 1;
        getData();
    }

    private void getData() {
        mHandler.postDelayed(() -> mHandler.sendEmptyMessage(0), 1000);
    }

    private static class MyHandler extends Handler {
        private final WeakReference<StaggeredActivity> mActivity;

        MyHandler(StaggeredActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            StaggeredActivity activity = mActivity.get();
            if (null == activity) {
                return;
            }

            if (activity.pageNum == 1) {
                activity.mAdapter.clear();
                activity.mAdapter.addHeader(new ViewHeader());
            }

            int currCount = activity.mAdapter.getDataSize();
            List<BeanStagger> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                int position = i + 1 + currCount;
                list.add(new BeanStagger(String.valueOf(position)));
            }

            activity.mAdapter.addAll(list);
            activity.pageNum++;
        }
    }
}
