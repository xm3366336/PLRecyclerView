package com.pengl.demo.utils;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.pengl.PLRecyclerView;
import com.pengl.demo.R;

import java.util.Objects;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BaseActivity extends AppCompatActivity {

    public PLRecyclerView mRecycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // targetSdk 35及以上系统强制edge-to-edge, 显式开启并交由页面处理系统栏inset
        EdgeToEdge.enable(this);
        setContentView(getContentLayout());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mRecycler = findViewById(R.id.recycler);
        applyEdgeToEdgeInsets(mRecycler);
    }

    /**
     * 页面根布局, 子类可重写以使用不同的布局
     */
    protected int getContentLayout() {
        return R.layout.activity_recyclerview;
    }

    /**
     * targetSdk 35及以上系统强制edge-to-edge, 需要手动处理系统栏遮挡问题
     *
     * @param view 需要避开系统栏(状态栏/导航栏)的根视图
     */
    protected void applyEdgeToEdgeInsets(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets bars = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        ViewCompat.requestApplyInsets(view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
