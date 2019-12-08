package com.pengl.demo.main;

import java.util.List;

/**
 *
 */
interface MainView {
    void onLoadSuccess(List<MenuBean> menu);

    void onLoadFailed();
}
