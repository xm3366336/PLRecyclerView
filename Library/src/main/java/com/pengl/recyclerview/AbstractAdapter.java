package com.pengl.recyclerview;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 *
 */
public abstract class AbstractAdapter<T extends ItemType, VH extends AbstractViewHolder<T>> extends RecyclerView.Adapter<VH> {

    private final DataSetObservable<T> dataSet;
    private RecyclerView mRecyclerView;

    public AbstractAdapter() {
        dataSet = new DataSetObservable<>();
    }

    public void clear() {
        dataSet.clear();
        notifyDataSetChanged();
    }

    public void clearData() {
        dataSet.data.clear();
        notifyDataSetChanged();
    }

    public void clearHeader() {
        dataSet.header.clear();
        notifyDataSetChanged();
    }

    public void clearFooter() {
        dataSet.footer.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加单个数据,并触发刷新
     *
     * @param item item
     */
    public void add(T item) {
        dataSet.data.add(item);
        dataSet.notifyContent();
        notifyDataSetChanged();
    }

    /**
     * 添加数据, 并触发刷新.
     * 添加之后数据总数为0, 显示EmptyView;
     * 添加之后数据总数大于0, 当添加0个数据时,自动停止LoadMore;
     *
     * @param data list of data
     */
    public void addAll(List<? extends T> data) {
        if (null == data) {
            return;
        }

        int oldSize = dataSet.data.size();
        if (oldSize > 0) {
            dataSet.data.addAll(data);
            notifyItemRangeInserted(oldSize, data.size());
        } else {
            dataSet.data.clear();
            dataSet.data.addAll(data);
            notifyDataSetChanged();
        }

        if (dataSet.totalSize() == 0) {
            dataSet.notifyEmpty(0, null);
        } else {
            dataSet.notifyContent();
            if (data.size() == 0) {
                dataSet.notifyNoMore();
            }
        }
    }

    public void addHeader(SectionItem header) {
        dataSet.header.add(header);
    }

    public void addFooter(SectionItem footer) {
        dataSet.footer.add(footer);
    }

    /**
     * 正常插入数据
     *
     * @param adapterPosition 插入的位置
     * @param item            插入的数据
     */
    public void insert(int adapterPosition, T item) {
        if (dataSet.data.size() <= adapterPosition) {
            add(item);
        } else {
            dataSet.data.insert(adapterPosition, item);
            notifyItemInserted(adapterPosition);
        }
    }

    /**
     * 正常插入多项数据
     *
     * @param adapterPosition 插入的位置
     * @param items           插入的数据
     */
    public void insertAll(int adapterPosition, List<? extends T> items) {
        if (dataSet.data.size() <= adapterPosition) {
            addAll(items);
        } else {
            dataSet.data.insertAll(adapterPosition, items);
            notifyItemRangeInserted(adapterPosition, items.size());
        }
    }

    /**
     * 插入数据到position之后
     *
     * @param adapterPosition position 从0开始，含header的条数
     * @param item            插入的数据
     */
    public void insertBack(int adapterPosition, T item) {
        if (dataSet.data.size() <= adapterPosition) {
            add(item);
        } else {
            dataSet.data.insertBack(adapterPosition, item);
            notifyItemInserted(adapterPosition + 1);
        }
    }

    /**
     * 插入多项数据到position之后
     *
     * @param adapterPosition 插入的位置
     * @param items           插入的数据
     */
    public void insertAllBack(int adapterPosition, List<? extends T> items) {
        dataSet.data.insertAllBack(adapterPosition, items);
        notifyItemRangeInserted(adapterPosition + 1, items.size());
    }

    public void swap(int fromAdapterPosition, int toAdapterPosition) {
        dataSet.data.swap(fromAdapterPosition, toAdapterPosition);
        notifyItemMoved(fromAdapterPosition, toAdapterPosition);
    }

    /**
     * 正常的删除
     *
     * @param adapterPosition 待删除的位置 从0开始，含header的条数
     */
    public void remove(int adapterPosition) {
        if (dataSet.data.size() < adapterPosition) {
            return;
        }
        dataSet.data.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    /**
     * 从指定的position之后删除size个数据
     * 不含position
     *
     * @param adapterPosition position 从0开始，含header的条数
     * @param removeSize      删除的数据大小
     */
    public void removeBack(int adapterPosition, int removeSize) {
        if (dataSet.data.size() <= adapterPosition) {
            return;
        }
        if (dataSet.data.size() <= adapterPosition + removeSize) {
            return;
        }
        dataSet.data.removeAllBack(adapterPosition, removeSize);
        notifyItemRangeRemoved(adapterPosition + 1, removeSize);
    }

    public T get(int position) {
        return dataSet.data.get(position);
    }

    public SectionItem getHeader(int position) {
        return dataSet.header.get(position);
    }

    public SectionItem getFooter(int position) {
        return dataSet.footer.get(position);
    }

    public List<T> getData() {
        return dataSet.data.getAll();
    }

    public List<SectionItem> getHeader() {
        return dataSet.header.getAll();
    }

    public List<SectionItem> getFooter() {
        return dataSet.footer.getAll();
    }

    /**
     * 该position是否为Header
     *
     * @return true是
     */
    public boolean isHeader(int position) {
        return dataSet.header.is(position);
    }

    public boolean isFooter(int position) {
        return dataSet.footer.is(position);
    }

    public boolean isData(int position) {
        return dataSet.data.is(position);
    }

    public boolean isExtra(int position) {
        return dataSet.extra.is(position);
    }

    /**
     * 获取Header的个数
     *
     * @return header size
     */
    public int getHeaderSize() {
        return dataSet.header.size();
    }

    public int getFooterSize() {
        return dataSet.footer.size();
    }

    /**
     * 获取数据量，不包含Header和Footer
     *
     * @return data size
     */
    public int getDataSize() {
        return dataSet.data.size();
    }

    public int getExtraSize() {
        return dataSet.extra.size();
    }

    /**
     * 显示Loading View
     */
    public void showLoading() {
        dataSet.notifyLoading();
    }

    /**
     * 清除当前所有数据，并显示Empty
     */
    public void showEmpty() {
        showEmpty(0, null);
    }

    /**
     * 清除当前所有数据，并显示Empty
     */
    public void showEmpty(String content) {
        showEmpty(0, content);
    }

    /**
     * 清除当前所有数据，并显示Empty
     */
    public void showEmpty(int resId, String content) {
        dataSet.clear();
        dataSet.notifyEmpty(resId, content);
    }

    /**
     * 清除当前所有数据,并显示ErrorView
     */
    public void showError() {
        showError(0, null);
    }

    /**
     * 清除当前所有数据,并显示ErrorView
     *
     * @param error 显示具体的错误原因
     */
    public void showError(String error) {
        showError(0, error);
    }

    /**
     * 清除当前所有数据,并显示ErrorView
     *
     * @param resId 错误原因，显示一个图片id
     * @param error 显示具体的错误原因
     */
    public void showError(int resId, String error) {
        dataSet.clear();
        dataSet.notifyError(resId, error);
    }

    /**
     * 显示底部LoadMoreErrorView
     */
    public void loadMoreFailed() {
        dataSet.notifyLoadMoreFailed();
    }

    /**
     * 恢复LoadMore，加载更多
     */
    public void resumeLoadMore() {
        dataSet.notifyResumeLoadMore();
    }

    /**
     * 手动触发加载更多, 显示LoadMoreView ,并执行loadMore逻辑
     */
    public void manualLoadMore() {
        dataSet.notifyManualLoadMore();
    }

    /**
     * 没有更多的数据了
     */
    public void showNoMore() {
        dataSet.notifyNoMore();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VH viewHolder = createHeaderFooterViewHolder(parent, viewType);
        if (viewHolder != null)
            return viewHolder;
        return onNewCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (dataSet.header.is(position)) {
            dataSet.header.get(position).onBind();
        } else if (dataSet.data.is(position)) {
            onNewBindViewHolder(holder, position);
        } else if (dataSet.footer.is(position)) {
            dataSet.footer.get(position).onBind();
        } else {
            dataSet.extra.get(position).onBind();
        }
    }

    @Override
    public final int getItemViewType(int position) {
        // 用header和footer的HashCode表示它们的ItemType,
        // 普通类型的数据由该数据类型的ItemType决定
        if (dataSet.header.is(position)) {
            return dataSet.header.get(position).hashCode();
        } else if (dataSet.data.is(position)) {
            return dataSet.data.get(position).itemType();
        } else if (dataSet.footer.is(position)) {
            return dataSet.footer.get(position).hashCode();
        } else {
            return dataSet.extra.get(position).hashCode();
        }
    }

    @Override
    public final int getItemCount() {
        return dataSet.totalSize();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VH holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getBindingAdapterPosition();

        // 瀑布流的 Header Footer 宽度处理
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            if (!dataSet.data.is(position)) {
                p.setFullSpan(true);
            }
        }

        // 判断是否需要自动加载
        if (mRecyclerView.getScrollState() != SCROLL_STATE_IDLE)
            return;

        if (dataSet.extra.size() == 0) {
            if (position == dataSet.totalSize() - 1) {
                loadMore();
            }
        } else {
            if (position == dataSet.totalSize() - 1 - dataSet.extra.size()) {
                loadMore();
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;

        // Grid的 Header Footer 宽度处理
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isData = dataSet.data.is(position);
                    if (isData) {
                        return 1;
                    } else {
                        return gridManager.getSpanCount();
                    }
                }
            });
        }
    }

    public void resolveSwipeConflicts(boolean enabled) {
        dataSet.notifyResolveSwipeConflicts(enabled);
    }

    public boolean canDrag(int adapterPosition) {
        return dataSet.data.is(adapterPosition);
    }

    public void show(View view, boolean enabled) {
        if (dataSet.extra.size() == 0) {
            if (enabled) {
                dataSet.extra.add(new SectionItemImpl(view));
                notifyItemInserted(dataSet.extra.position());
            }
        } else {
            if (!dataSet.extra.get(dataSet.extra.position()).createView(null).equals(view)) {
                if (enabled) {
                    dataSet.extra.set(dataSet.extra.position(), new SectionItemImpl(view));
                    notifyItemChanged(dataSet.extra.position());
                }
            } else {
                if (!enabled) {
                    int position = dataSet.extra.position();
                    dataSet.extra.remove(position);
                    notifyItemRemoved(position);
                }
            }
        }
    }

    public void registerObserver(Observer observer) {
        dataSet.addObserver(observer);
    }

    protected abstract VH onNewCreateViewHolder(ViewGroup parent, int viewType);

    protected abstract void onNewBindViewHolder(VH holder, int position);

    private void loadMore() {
        new Handler(Looper.getMainLooper()).post(dataSet::notifyAutoLoadMore);
    }

    private VH createHeaderFooterViewHolder(ViewGroup parent, int viewType) {
        List<SectionItem> tempContainer = new ArrayList<>();
        tempContainer.addAll(dataSet.header.getAll());
        tempContainer.addAll(dataSet.footer.getAll());
        tempContainer.addAll(dataSet.extra.getAll());

        for (SectionItem each : tempContainer) {
            if (each.hashCode() == viewType) {
                View view = each.createView(parent);
                return (VH) new SectionItemViewHolder(view);
            }
        }
        return null;
    }

    private class SectionItemViewHolder extends AbstractViewHolder<T> {

        SectionItemViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(ItemType data) {

        }
    }
}
