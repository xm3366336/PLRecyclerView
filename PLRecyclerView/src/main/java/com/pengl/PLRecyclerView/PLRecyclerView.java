package com.pengl.PLRecyclerView;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 *
 */
public class PLRecyclerView extends FrameLayout {

    private boolean mAutoLoadMoreEnabled = true;
    private boolean mNoMoreViewEnabled = true;
    private boolean mLoadMoreViewEnabled = true;
    private boolean mLoadMoreFailedViewEnabled = true;

    private OnRefreshListener mRefreshListener;
    private OnLoadMoreListener mLoadMoreListener;

    private FrameLayout mMainContainer;
    private FrameLayout mLoadingContainer;
    private FrameLayout mErrorContainer;
    private FrameLayout mEmptyContainer;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private View mEmptyView;
    private View mLoadingView;
    private View mErrorView;

    private View mLoadMoreView;
    private View mNoMoreView;
    private View mLoadMoreFailedView;

    private boolean isRefreshing = false;
    private boolean nowLoading = false;
    private boolean noMore = false;
    private boolean loadMoreFailed = false;

    private OnScrollListener mScrollListener = new OnScrollListener();
    private DataSetObserver mObserver = new DataSetObserver();

    public PLRecyclerView(Context context) {
        this(context, null);
    }

    public PLRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PLRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMainView(context);
        obtainStyledAttributes(context, attrs);
        configDefaultBehavior();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PLRecyclerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initMainView(context);
        obtainStyledAttributes(context, attrs);
        configDefaultBehavior();
    }

    public void configureView(Configure configure) {
        if (configure == null) {
            return;
        }
        configure.configureEmptyView(mEmptyView);
        configure.configureErrorView(mErrorView);
        configure.configureLoadingView(mLoadingView);
        configure.configureLoadMoreView(mLoadMoreView);
        configure.configureNoMoreView(mNoMoreView);
        configure.configureLoadMoreFailedView(mLoadMoreFailedView);
    }

    public RecyclerView get() {
        return mRecyclerView;
    }

    public void setRefreshListener(OnRefreshListener refreshListener) {
        if (refreshListener == null)
            return;
        mSwipeRefreshLayout.setEnabled(true);
        mRefreshListener = refreshListener;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        if (loadMoreListener == null)
            return;
        mLoadMoreListener = loadMoreListener;
        /*
         * 避免第一次滑动到底部时需要再次滑动才会显示load more
         */
        displayLoadMoreViewOrDisappear();
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void attachItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setAdapterWithLoading(RecyclerView.Adapter adapter) {
        if (adapter instanceof AbstractAdapter) {
            AbstractAdapter abstractAdapter = (AbstractAdapter) adapter;
            subscribeWithAdapter(abstractAdapter);
        }
        displayLoadingAndResetStatus();
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 开启或关闭自动加载功能
     * 注意, 仍然会显示LoadMoreView, 如需关闭LoadMoreView, 请往下看
     *
     * @param autoLoadMoreEnable true 为到自动加载, false为手动触发加载
     */
    public void setAutoLoadEnable(boolean autoLoadMoreEnable) {
        mAutoLoadMoreEnabled = autoLoadMoreEnable;
    }

    /**
     * 显示或关闭LoadMoreView , 不建议使用
     * 注意, 仅仅只是不显示, 但仍会继续加载, 如需关闭自动加载功能, 请往上看
     * 将在2.0.0版本后移除
     *
     * @param enabled true 为显示, false为不显示
     * @deprecated 不建议使用，以后会去除
     */
    @Deprecated
    public void setLoadMoreViewEnabled(boolean enabled) {
        mLoadMoreViewEnabled = enabled;
        if (!enabled) {
            displayLoadMoreViewOrDisappear();
        } else {
            if (mScrollListener.isLastItem(mRecyclerView)) {
                autoLoadMoreIfEnabled();
                smoothScrollToPosition(mRecyclerView.getLayoutManager().getItemCount() - 1);
            }
        }
    }

    /**
     * 显示或关闭LoadMoreFailedView, 不建议使用
     * 将在2.0.0版本后移除
     *
     * @param enabled true 为显示, false为关闭
     * @deprecated 不用了，出错时，请手工调用
     */
    @Deprecated
    public void setLoadMoreFailedViewEnabled(boolean enabled) {
        mLoadMoreFailedViewEnabled = enabled;
        if (!enabled) {
            displayLoadMoreFailedViewOrDisappear();
        } else {
            if (loadMoreFailed) {
                displayLoadMoreFailedViewOrDisappear();
                smoothScrollToPosition(mRecyclerView.getLayoutManager().getItemCount() - 1);
            }
        }
    }

    /**
     * 显示或关闭NoMoreView, 按需使用
     * 将在2.0.0版本后移除
     *
     * @param enabled true为显示, false为关闭
     * @deprecated 不用了，没有数据时，请手工调用 mAdapter.showNoMore()显示；
     */
    @Deprecated
    public void setNoMoreViewEnabled(boolean enabled) {
        mNoMoreViewEnabled = enabled;
        if (!enabled) {
            displayNoMoreViewOrDisappear();
        } else {
            if (noMore) {
                displayNoMoreViewOrDisappear();
                smoothScrollToPosition(mRecyclerView.getLayoutManager().getItemCount() - 1);
            }
        }
    }

    /**
     * RecyclerView原生的方法，请使用get()获取到RecyclerView再操作
     * 将在2.0.0版本后移除
     *
     * @param hasFixedSize hasFixedSize
     * @deprecated get().setHasFixedSize(hasFixedSize);
     */
    @Deprecated
    public void setHasFixedSize(boolean hasFixedSize) {
        mRecyclerView.setHasFixedSize(hasFixedSize);
    }

    /**
     * RecyclerView原生的方法，请使用get()获取到RecyclerView再操作
     * 将在2.0.0版本后移除
     *
     * @param position position
     * @deprecated get().smoothScrollToPosition(position);
     */
    @Deprecated
    public void smoothScrollToPosition(int position) {
        mRecyclerView.smoothScrollToPosition(position);
    }

    /**
     * RecyclerView原生的方法，请使用get()获取到RecyclerView再操作
     * 将在2.0.0版本后移除
     *
     * @param animator animator
     * @deprecated get().setItemAnimator(animator);
     */
    @Deprecated
    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }

    /**
     * RecyclerView原生的方法，请使用get()获取到RecyclerView再操作
     * 将在2.0.0版本后移除
     *
     * @param itemDecoration itemDecoration
     * @deprecated get().addItemDecoration(itemDecoration);
     */
    @Deprecated
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    /**
     * RecyclerView原生的方法，请使用get()获取到RecyclerView再操作
     * 将在2.0.0版本后移除
     *
     * @param itemDecoration itemDecoration
     * @param index          index
     * @deprecated get().addItemDecoration(itemDecoration, index);
     */
    @Deprecated
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        mRecyclerView.addItemDecoration(itemDecoration, index);
    }

    /**
     * RecyclerView原生的方法，请使用get()获取到RecyclerView再操作
     * 将在2.0.0版本后移除
     *
     * @param decor decor
     * @deprecated get().removeItemDecoration(decor);
     */
    @Deprecated
    public void removeItemDecoration(RecyclerView.ItemDecoration decor) {
        mRecyclerView.removeItemDecoration(decor);
    }

    void resolveSwipeConflicts(boolean enabled) {
        if (mRefreshListener == null) {
            return;
        }
        mSwipeRefreshLayout.setEnabled(enabled);
    }

    void displayLoadingAndResetStatus() {
        mErrorContainer.setVisibility(GONE);
        mRecyclerView.setVisibility(GONE);
        mEmptyContainer.setVisibility(GONE);
        mLoadingContainer.setVisibility(VISIBLE);
        resetStatus();
    }

    void displayContentAndResetStatus() {
        mLoadingContainer.setVisibility(GONE);
        mErrorContainer.setVisibility(GONE);
        mEmptyContainer.setVisibility(GONE);
        mRecyclerView.setVisibility(VISIBLE);
        resetStatus();
    }

    void displayEmptyAndResetStatus(int resId, String content) {
        mLoadingContainer.setVisibility(GONE);
        mRecyclerView.setVisibility(GONE);
        mErrorContainer.setVisibility(GONE);
        mEmptyContainer.setVisibility(VISIBLE);

        TextView tv = mEmptyView.findViewById(R.id.tv_content);
        if (null != mEmptyView && null != tv) {
            if (!TextUtils.isEmpty(content)) {
                tv.setText(content);
            } else {
                tv.setText(getResources().getString(R.string.pl_rv_empty));
            }
            if (resId != 0) {
                tv.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
            }
        }

        resetStatus();
    }

    void displayErrorAndResetStatus(int resId, String error) {
        mLoadingContainer.setVisibility(GONE);
        mRecyclerView.setVisibility(GONE);
        mEmptyContainer.setVisibility(GONE);
        mErrorContainer.setVisibility(VISIBLE);

        TextView tv = mErrorView.findViewById(R.id.tv_content);
        if (null != mErrorView && null != tv) {
            if (!TextUtils.isEmpty(error)) {
                tv.setText(error);
            } else {
                tv.setText(getResources().getString(R.string.pl_rv_error));
            }
            if (resId != 0) {
                tv.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
            }
        }

        resetStatus();
    }

    void showNoMoreIfEnabled() {
        displayNoMoreViewOrDisappear();
        noMore = true;
    }

    void showLoadMoreFailedIfEnabled() {
        displayLoadMoreFailedViewOrDisappear();
        loadMoreFailed = true;
    }

    void resumeLoadMoreIfEnabled() {
        loadMoreFailed = false;
        autoLoadMoreIfEnabled();
    }

    void autoLoadMoreIfEnabled() {
        if (canNotLoadMore())
            return;
        displayLoadMoreViewOrDisappear();
        if (mAutoLoadMoreEnabled) {
            nowLoading = true;
            mLoadMoreListener.onLoadMore();
        }
    }

    void manualLoadMoreIfEnabled() {
        if (canNotLoadMore())
            return;
        displayLoadMoreViewOrDisappear();
        nowLoading = true;
        mLoadMoreListener.onLoadMore();
    }

    private void displayNoMoreViewOrDisappear() {
        displayOrDisappear(mNoMoreView, mNoMoreViewEnabled);
    }

    private void displayLoadMoreFailedViewOrDisappear() {
        displayOrDisappear(mLoadMoreFailedView, mLoadMoreFailedViewEnabled);
    }

    private void displayLoadMoreViewOrDisappear() {
        displayOrDisappear(mLoadMoreView, mLoadMoreViewEnabled);
    }

    private void displayOrDisappear(View view, boolean enabled) {
        if (!(mRecyclerView.getAdapter() instanceof AbstractAdapter))
            return;
        AbstractAdapter adapter = (AbstractAdapter) mRecyclerView.getAdapter();
        adapter.show(view, enabled);
    }

    private void resetStatus() {
        isRefreshing = false;
        nowLoading = false;
        loadMoreFailed = false;
        noMore = false;
    }

    private void initMainView(Context context) {
        mMainContainer = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.pl_rv_layout, this, true);
        mLoadingContainer = mMainContainer.findViewById(R.id.pl_rv_loading);
        mErrorContainer = mMainContainer.findViewById(R.id.pl_rv_error);
        mEmptyContainer = mMainContainer.findViewById(R.id.pl_rv_empty);

        mSwipeRefreshLayout = mMainContainer.findViewById(R.id.pl_rv_swipe_refresh);
        mRecyclerView = mMainContainer.findViewById(R.id.pl_rv_recycler);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.material_colors));
    }

    private void configDefaultBehavior() {
        // 默认为关闭,设置OnRefreshListener时打开
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mRecyclerView.addOnScrollListener(mScrollListener);

        // 设置error view 默认行为,点击刷新
        mErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLoadingAndResetStatus();
                refresh();
            }
        });

        // 设置load more failed view 默认行为, 点击恢复加载
        mLoadMoreFailedView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeLoadMoreIfEnabled();
                displayOrDisappear(mLoadMoreFailedView, false);
            }
        });
    }

    private void refresh() {
        if (canNotRefresh()) {
            closeRefreshing();
            return;
        }
        mRefreshListener.onRefresh();
        isRefreshing = true;
    }

    public void closeRefreshing() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
            isRefreshing = false;
        }
    }

    private void closeLoadingMore() {
        if (nowLoading) {
            nowLoading = false;
        }
    }

    private boolean canNotRefresh() {
        return mRefreshListener == null || isRefreshing || nowLoading;
    }

    private boolean canNotLoadMore() {
        return mLoadMoreListener == null || isRefreshing || nowLoading || loadMoreFailed || noMore;
    }

    private void obtainStyledAttributes(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PLRecyclerView);
        int loadingResId = attributes.getResourceId(R.styleable.PLRecyclerView_pl_rv_loading_layout, R.layout.pl_rv_loading_layout);
        int emptyResId = attributes.getResourceId(R.styleable.PLRecyclerView_pl_rv_empty_layout, R.layout.pl_rv_empty_layout);
        int errorResId = attributes.getResourceId(R.styleable.PLRecyclerView_pl_rv_error_layout, R.layout.pl_rv_error_layout);
        int loadMoreResId = attributes.getResourceId(R.styleable.PLRecyclerView_pl_rv_load_more_layout, R.layout.pl_rv_load_more_layout);
        int noMoreResId = attributes.getResourceId(R.styleable.PLRecyclerView_pl_rv_no_more_layout, R.layout.pl_rv_no_more_layout);
        int loadMoreErrorResId = attributes.getResourceId(R.styleable.PLRecyclerView_pl_rv_load_more_failed_layout, R.layout.pl_rv_load_more_failed_layout);

        mLoadingView = LayoutInflater.from(context).inflate(loadingResId, mLoadingContainer, true);
        mEmptyView = LayoutInflater.from(context).inflate(emptyResId, mEmptyContainer, true);
        mErrorView = LayoutInflater.from(context).inflate(errorResId, mErrorContainer, true);
        mLoadMoreView = LayoutInflater.from(context).inflate(loadMoreResId, mMainContainer, false);
        mNoMoreView = LayoutInflater.from(context).inflate(noMoreResId, mMainContainer, false);
        mLoadMoreFailedView = LayoutInflater.from(context).inflate(loadMoreErrorResId, mMainContainer, false);

        attributes.recycle();
    }

    private void subscribeWithAdapter(AbstractAdapter adapter) {
        adapter.registerObserver(mObserver);
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    private class DataSetObserver implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            closeRefreshing();
            closeLoadingMore();
            Bridge type = (Bridge) arg;
            type.doSomething(PLRecyclerView.this);
        }
    }

    private class OnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            // 当滚动到最后一个item时,自动加载更多
            if (isLastItem(recyclerView)) {
                autoLoadMoreIfEnabled();
            }
        }

        private boolean isLastItem(RecyclerView recyclerView) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int lastVisibleItemPosition = getLastVisibleItemPosition(layoutManager);

            return visibleItemCount > 0 && lastVisibleItemPosition >= totalItemCount - 1 && totalItemCount >= visibleItemCount;
        }

        private int getLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            return lastVisibleItemPosition;
        }

        private int findMax(int[] lastPositions) {
            int max = lastPositions[0];
            for (int value : lastPositions) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
        }
    }
}