package com.pengl.demo.expand;

import java.util.List;

/**
 *
 */
interface ExpandView {
    void onDataLoadSuccess(List<ParentBean> list);

    void onDataLoadFailed();
}
