package com.pengl.demo.main;

import android.os.Bundle;

import com.pengl.PLRecyclerView.PLLinearLayoutManager;
import com.pengl.PLRecyclerView.PLRecyclerView;
import com.pengl.demo.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private MainAdapter mAdapter;
//    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mAdapter = new MainAdapter();
//        mPresenter = new MainPresenter(this);

        PLRecyclerView mRecycler = findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new PLLinearLayoutManager(this));
        mRecycler.setAdapterWithLoading(mAdapter);

//        mPresenter.setDataLoadCallBack(new MainView() {
//            @Override
//            public void onLoadSuccess(List<MenuBean> menu) {
//                mAdapter.clear();
//                mAdapter.addAll(menu);
//            }
//
//            @Override
//            public void onLoadFailed() {
//                mAdapter.showError();
//            }
//        });

//        mPresenter.loadData();

        final String[] resource = getResources().getStringArray(R.array.main_fun);
        List<MenuBean> mData = new ArrayList<>();
        for (int i = 0; i < resource.length; i++) {
            mData.add(new MenuBean(resource[i], i));
        }
        mAdapter.clear();
        mAdapter.addAll(mData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mPresenter.unsubscribeAll();
    }

}
