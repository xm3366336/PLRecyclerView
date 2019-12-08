package com.pengl.demo.grid;

import java.util.List;

/**
 *
 */
public interface GridView {
    void onDataLoadSuccess(List<GridBean> list, boolean isRefresh);

    void onDataLoadFailed(boolean isRefresh);
}
