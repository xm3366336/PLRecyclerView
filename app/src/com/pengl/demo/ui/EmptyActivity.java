package com.pengl.demo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.pengl.recyclerview.ConfigureAdapter;
import com.pengl.demo.R;
import com.pengl.demo.adapter.AdapterNormal;
import com.pengl.demo.model.BeanNormal;
import com.pengl.demo.utils.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

public class EmptyActivity extends BaseActivity {

    private AdapterNormal mAdapter;
    private final MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new AdapterNormal();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapterWithLoading(mAdapter);
        mRecycler.setRefreshListener(() -> getData(true));

        mRecycler.configureView(new ConfigureAdapter() {
            @Override
            public void configureEmptyView(View emptyView) {
                emptyView.setOnClickListener(view -> Toast.makeText(EmptyActivity.this, getString(R.string.tips_1), Toast.LENGTH_SHORT).show());
            }
        });

        getData(false);
    }

    private void getData(boolean isSucc) {
        mHandler.postDelayed(() -> mHandler.sendEmptyMessage(isSucc ? 0 : 1), 1000);
    }

    private static class MyHandler extends Handler {
        private final WeakReference<EmptyActivity> mActivity;

        MyHandler(EmptyActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            EmptyActivity activity = mActivity.get();
            if (null == activity) {
                return;
            }

            if (msg.what == 0) {
                activity.mAdapter.clear();
                List<BeanNormal> list = new ArrayList<>();
                for (int i = 1; i <= 10; i++) {
                    String str = activity.getString(R.string.seq_data_2, i);
                    list.add(new BeanNormal(str, str));
                }
                activity.mAdapter.addAll(list);
                activity.mAdapter.showNoMore();
            } else {
                activity.mAdapter.showEmpty();
            }
        }
    }
}
