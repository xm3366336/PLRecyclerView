package com.pengl.demo.staggered;

import java.util.List;

/**
 *
 */
public interface StaggerView {
    void onDataLoadSuccess(List<StaggerBean> list, boolean isRefresh);

    void onDataLoadFailed(boolean isRefresh);
}
