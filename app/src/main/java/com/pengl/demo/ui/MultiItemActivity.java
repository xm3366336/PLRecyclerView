package com.pengl.demo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.pengl.recyclerview.ItemType;
import com.pengl.demo.R;
import com.pengl.demo.adapter.AdapterMultiItem;
import com.pengl.demo.model.BeanTypeOne;
import com.pengl.demo.model.BeanTypeTwo;
import com.pengl.demo.utils.BaseActivity;
import com.pengl.demo.viewHolder.ViewHeader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MultiItemActivity extends BaseActivity {

    private AdapterMultiItem mAdapter;

    private final MyHandler mHandler = new MyHandler(this);
    private int pageNum;    // 当前页码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new AdapterMultiItem();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
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
        private final WeakReference<MultiItemActivity> mActivity;

        MyHandler(MultiItemActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            MultiItemActivity activity = mActivity.get();
            if (null == activity) {
                return;
            }

            if (activity.pageNum == 1) {
                activity.mAdapter.clear();
                activity.mAdapter.addHeader(new ViewHeader());
            }

            int currCount = activity.mAdapter.getDataSize();
            List<ItemType> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                int position = i + 1 + currCount;
                if (position % 3 == 0) {
                    list.add(new BeanTypeTwo(activity.getString(R.string.seq_data_2, position)));
                } else {
                    list.add(new BeanTypeOne(activity.getString(R.string.seq_data_2, position)));
                }
            }

            activity.mAdapter.addAll(list);
            activity.pageNum++;
        }
    }
}
