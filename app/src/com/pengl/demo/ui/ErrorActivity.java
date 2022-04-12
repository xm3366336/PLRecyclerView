package com.pengl.demo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

public class ErrorActivity extends BaseActivity {

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
            public void configureErrorView(View errorView) {
                super.configureErrorView(errorView);
                errorView.setOnClickListener(view -> {
                    Toast.makeText(ErrorActivity.this, getString(R.string.tips_1), Toast.LENGTH_SHORT).show();
                });
            }
        });

        getData(false);
    }

    private void getData(boolean isSucc) {
        mHandler.postDelayed(() -> mHandler.sendEmptyMessage(isSucc ? 0 : 1), 1000);
    }

    private static class MyHandler extends Handler {
        private final WeakReference<ErrorActivity> mActivity;

        MyHandler(ErrorActivity activity) {
            super(Looper.myLooper());
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            ErrorActivity activity = mActivity.get();
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
                activity.mAdapter.showError(R.mipmap.img_no_network, activity.getString(R.string.tips_2));
            }
        }
    }
}
