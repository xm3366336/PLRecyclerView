package com.pengl.demo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pengl.demo.R;
import com.pengl.demo.adapter.AdapterNormal;
import com.pengl.demo.model.BeanNormal;
import com.pengl.demo.utils.BaseActivity;
import com.pengl.demo.utils.Common;
import com.pengl.demo.viewHolder.ViewHeader;
import com.pengl.recyclerview.ConfigureAdapter;
import com.pengl.recyclerview.PLLinearLayoutManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends BaseActivity {

    private AdapterNormal mAdapter;
    private final MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new AdapterNormal();
        mRecycler.setLayoutManager(new PLLinearLayoutManager(this));
        mRecycler.setAdapterWithLoading(mAdapter);
        mRecycler.setRefreshListener(this::getData);
        mRecycler.configureView(new ConfigureAdapter() {
            @Override
            public void configureNoMoreView(View noMoreView) {

            }
        });
        getData();
    }

    private void getData() {
        mHandler.postDelayed(() -> mHandler.sendEmptyMessage(0), 1000);
    }

    private static class MyHandler extends Handler {
        private final WeakReference<TestActivity> mActivity;

        MyHandler(TestActivity activity) {
            super(Looper.myLooper());
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            TestActivity activity = mActivity.get();
            if (null == activity) {
                return;
            }

            activity.mAdapter.clear();

            List<BeanNormal> list = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                int position = i + 1;
                String str = activity.getString(R.string.seq_data_2, position);
                list.add(new BeanNormal(str, str));
            }

            activity.mAdapter.addAll(list);
            activity.mAdapter.addFooterSpace(160);
            activity.mAdapter.showNoMore();
        }
    }
}
