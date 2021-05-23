package com.pengl.recyclerview;

import android.view.View;
import android.view.ViewGroup;

/**
 *
 */
public interface SectionItem {

    View createView(ViewGroup parent);

    void onBind();
}
