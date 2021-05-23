package com.pengl.recyclerview;

import android.view.View;

/**
 *
 */
public interface Configure {
    void configureEmptyView(View emptyView);

    void configureErrorView(View errorView);

    void configureLoadingView(View loadingView);

    void configureLoadMoreView(View loadMoreView);

    void configureNoMoreView(View noMoreView);

    void configureLoadMoreFailedView(View loadMoreFailedView);
}