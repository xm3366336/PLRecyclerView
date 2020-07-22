package com.pengl.demo.ui;

import android.os.Bundle;

import com.pengl.demo.adapter.AdapterExpand;
import com.pengl.demo.model.BeanExpandChild;
import com.pengl.demo.model.BeanExpandParent;
import com.pengl.demo.utils.BaseActivity;
import com.pengl.demo.utils.Common;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

public class ExpandActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdapterExpand mAdapter = new AdapterExpand();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapterWithLoading(mAdapter);

        List<BeanExpandParent> listParent = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            List<BeanExpandChild> listChild = new ArrayList<>();
            for (int x = 0, childSize = Common.getRandom(1, 5); x < childSize; x++) {
                listChild.add(new BeanExpandChild("我爹是" + (i + 1) + "，而我是" + (x + 1)));
            }

            listParent.add(new BeanExpandParent("我是爹" + (i + 1), listChild, false));
        }

        mAdapter.clear();
        mAdapter.addAll(listParent);
    }

}
