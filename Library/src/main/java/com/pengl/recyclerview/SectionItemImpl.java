package com.pengl.recyclerview;

import android.view.View;
import android.view.ViewGroup;

/**
 *
 */
public class SectionItemImpl implements SectionItem {

    private View mView;

    public SectionItemImpl() {
    }

    public SectionItemImpl(View view) {
        mView = view;
    }

    @Override
    public View createView(ViewGroup parent) {
        return mView;
    }

    @Override
    public void onBind() {

    }
}

