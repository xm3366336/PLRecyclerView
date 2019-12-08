package com.pengl.demo.drag;

import java.util.List;

/**
 *
 */
interface DragView {
    void onDataLoadSuccess(List<DragBean> list);

    void onDataLoadFailed();
}
