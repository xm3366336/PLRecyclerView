package com.pengl.demo.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.pengl.PLRecyclerView.SimpleItemTouchHelperCallback;
import com.pengl.demo.R;
import com.pengl.demo.adapter.AdapterDrag;
import com.pengl.demo.model.BeanDrag;
import com.pengl.demo.utils.BaseActivity;
import com.pengl.demo.utils.ItemTouchHelperProvider;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;


public class DragActivity extends BaseActivity {

    private boolean flag = false;
    private boolean gridChecked = false;

    private AdapterDrag mAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drag_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem grid = menu.findItem(R.id.action_grid);
        grid.setChecked(gridChecked);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_edit:
                flag = !flag;
                if (flag) {
                    item.setTitle("保存");
                } else {
                    item.setTitle("编辑");
                }

                // 点击编辑显示拖动图标
                List<BeanDrag> copy = mAdapter.getData();
                for (BeanDrag each : copy) {
                    each.setStatus(!each.isStatus());
                }
                mAdapter.notifyDataSetChanged();

                return true;

            case R.id.action_grid:
                gridChecked = !item.isChecked();
                item.setChecked(gridChecked);

                if (gridChecked) {
                    mRecycler.setLayoutManager(new GridLayoutManager(this, 3));
                    mRecycler.setAdapter(mAdapter);
                } else {
                    mRecycler.setLayoutManager(new LinearLayoutManager(this));
                    mRecycler.setAdapter(mAdapter);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new AdapterDrag();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapterWithLoading(mAdapter);

        getData();

        // 初始化拖拽, 设置不启用默认的长按拖拽, 设置不启用默认的滑动删除
        // 具体的拖拽实现在 ViewHolder 中
        ItemTouchHelperProvider.init(new SimpleItemTouchHelperCallback(false, false));
        mRecycler.attachItemTouchHelper(ItemTouchHelperProvider.getInstance());
    }

    private void getData() {
        List<BeanDrag> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new BeanDrag("第" + (i + 1) + "个"));
        }
        mAdapter.clear();
        mAdapter.addAll(list);
    }

}