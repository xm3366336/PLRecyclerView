package com.pengl.demo.singleitem;

import java.util.List;

/**
 *
 */
interface SingleItemView {
    void onDataLoadSuccess(List<NormalBean> list, boolean isRefresh);

    void onDataLoadFailed(boolean isRefresh);
}
