package com.pengl.demo.drag;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.pengl.PLRecyclerView.PLRecyclerView;
import com.pengl.PLRecyclerView.SimpleItemTouchHelperCallback;
import com.pengl.demo.Header;
import com.pengl.demo.R;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;


public class DragActivity extends AppCompatActivity {

    PLRecyclerView mRecycler;
    Toolbar mToolbar;
    RelativeLayout mContentItemDrag;

    private boolean flag = false;
    private boolean gridChecked = false;

    private DragAdapter mAdapter;
    private DragPresenter mPresenter;

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

                //点击编辑显示拖动图标
                List<DragBean> copy = mAdapter.getData();
                for (DragBean each : copy) {
                    each.status = !each.status;
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
        setContentView(R.layout.activity_drag);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mContentItemDrag = findViewById(R.id.content_item_drag);
        mRecycler = findViewById(R.id.recycler);
        mAdapter = new DragAdapter();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapterWithLoading(mAdapter);

        mPresenter = new DragPresenter(this);
        mPresenter.setDataLoadCallBack(new DragView() {
            @Override
            public void onDataLoadSuccess(List<DragBean> list) {
                mAdapter.addHeader(new Header());
                mAdapter.addAll(list);
            }

            @Override
            public void onDataLoadFailed() {
                mAdapter.showError();
            }
        });

        //初始化拖拽, 设置不启用默认的长按拖拽, 设置不启用默认的滑动删除
        //具体的拖拽实现在 ViewHolder 中
        ItemTouchHelperProvider.init(new SimpleItemTouchHelperCallback(false, false));
        mRecycler.attachItemTouchHelper(ItemTouchHelperProvider.getInstance());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribeAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadData();
    }
}
