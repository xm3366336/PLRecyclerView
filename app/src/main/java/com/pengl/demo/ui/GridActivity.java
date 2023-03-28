package com.pengl.demo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.pengl.demo.R;
import com.pengl.demo.adapter.AdapterGrid;
import com.pengl.demo.model.BeanGrid;
import com.pengl.demo.utils.BaseActivity;
import com.pengl.demo.viewHolder.ViewHeader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

public class GridActivity extends BaseActivity {

    private AdapterGrid mAdapter;
    private int pageNum;
    private final MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new AdapterGrid();
        mRecycler.setLayoutManager(new GridLayoutManager(this, 3));
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
        private final WeakReference<GridActivity> mActivity;

        MyHandler(GridActivity activity) {
            super(Looper.myLooper());
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            GridActivity activity = mActivity.get();
            if (null == activity) {
                return;
            }

            if (activity.pageNum == 1) {
                activity.mAdapter.clear();
                activity.mAdapter.addHeader(new ViewHeader());
            }

            int currCount = activity.mAdapter.getDataSize();
            List<BeanGrid> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                int position = i + 1 + currCount;
                list.add(new BeanGrid(activity.getString(R.string.seq_data_1, position)));
            }

            activity.mAdapter.addAll(list);
            activity.pageNum++;
        }
    }

}
