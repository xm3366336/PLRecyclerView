package com.pengl;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pengl.recyclerview.AbstractAdapter;
import com.pengl.recyclerview.Bridge;
import com.pengl.recyclerview.Configure;
import com.pengl.recyclerview.R;

import java.util.Observable;
import java.util.Observer;

public class PLRecyclerView extends FrameLayout {

    private boolean mAutoLoadMoreEnabled = true;

    private OnRefreshListener mRefreshListener;
    private OnLoadMoreListener mLoadMoreListener;

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

    private final OnScrollListener mScrollListener = new OnScrollListener();
    private final DataSetObserver mObserver = new DataSetObserver();

    public PLRecyclerView(Context context) {
        this(context, null);
    }

    public PLRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PLRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

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

    public void setAdapter(AbstractAdapter<?, ?> adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setAdapterWithLoading(AbstractAdapter<?, ?> adapter) {
        if (adapter != null) {
            subscribeWithAdapter(adapter);
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

    public void resolveSwipeConflicts(boolean enabled) {
        if (mRefreshListener == null) {
            return;
        }
        mSwipeRefreshLayout.setEnabled(enabled);
    }

    public void displayLoadingAndResetStatus() {
        mErrorContainer.setVisibility(GONE);
        mRecyclerView.setVisibility(GONE);
        mEmptyContainer.setVisibility(GONE);
        mLoadingContainer.setVisibility(VISIBLE);
        resetStatus();
    }

    public void displayContentAndResetStatus() {
        mLoadingContainer.setVisibility(GONE);
        mErrorContainer.setVisibility(GONE);
        mEmptyContainer.setVisibility(GONE);
        mRecyclerView.setVisibility(VISIBLE);
        resetStatus();
    }

    public void displayEmptyAndResetStatus(int resId, String content) {
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

    public void displayErrorAndResetStatus(int resId, String error) {
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

    public void showNoMoreIfEnabled() {
        displayNoMoreViewOrDisappear();
        noMore = true;
    }

    public void showLoadMoreFailedIfEnabled() {
        displayLoadMoreFailedViewOrDisappear();
        loadMoreFailed = true;
    }

    public void resumeLoadMoreIfEnabled() {
        loadMoreFailed = false;
        autoLoadMoreIfEnabled();
    }

    public void autoLoadMoreIfEnabled() {
        if (canNotLoadMore())
            return;
        displayLoadMoreViewOrDisappear();
        if (mAutoLoadMoreEnabled) {
            nowLoading = true;
            mLoadMoreListener.onLoadMore();
        }
    }

    public void manualLoadMoreIfEnabled() {
        if (canNotLoadMore())
            return;
        displayLoadMoreViewOrDisappear();
        nowLoading = true;
        mLoadMoreListener.onLoadMore();
    }

    private void displayNoMoreViewOrDisappear() {
        displayOrDisappear(mNoMoreView, true);
    }

    private void displayLoadMoreFailedViewOrDisappear() {
        displayOrDisappear(mLoadMoreFailedView, true);
    }

    private void displayLoadMoreViewOrDisappear() {
        displayOrDisappear(mLoadMoreView, true);
    }

    private void displayOrDisappear(View view, boolean enabled) {
        if (!(mRecyclerView.getAdapter() instanceof AbstractAdapter))
            return;
        ((AbstractAdapter<?, ?>) mRecyclerView.getAdapter()).show(view, enabled);
    }

    private void resetStatus() {
        isRefreshing = false;
        nowLoading = false;
        loadMoreFailed = false;
        noMore = false;
    }

    private void initMainView(Context context) {
        View mMainContainer = LayoutInflater.from(context).inflate(R.layout.pl_rv_layout, this, true);
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
        mSwipeRefreshLayout.setOnRefreshListener(this::refresh);
        mRecyclerView.addOnScrollListener(mScrollListener);

        // 设置error view 默认行为,点击刷新
        mErrorView.setOnClickListener(v -> {
            displayLoadingAndResetStatus();
            refresh();
        });

        // 设置load more failed view 默认行为, 点击恢复加载
        mLoadMoreFailedView.setOnClickListener(v -> {
            resumeLoadMoreIfEnabled();
            displayOrDisappear(mLoadMoreFailedView, false);
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
        if (null == attrs)
            return;

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
        mLoadMoreView = LayoutInflater.from(context).inflate(loadMoreResId, this, false);
        mNoMoreView = LayoutInflater.from(context).inflate(noMoreResId, this, false);
        mLoadMoreFailedView = LayoutInflater.from(context).inflate(loadMoreErrorResId, this, false);

        attributes.recycle();
    }

    private void subscribeWithAdapter(AbstractAdapter<?, ?> adapter) {
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
            if (null == layoutManager) {
                return true;
            }
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
