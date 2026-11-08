package com.pengl.recyclerview;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;

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

    /**
     * 清空 Header、Data、Footer，并刷新列表。
     * <p>
     * Extra（LoadMoreView 等）不受影响。
     */
    public void clear() {
        clearDataSet();
    }

    /**
     * 清空数据（Header、Footer 不受影响），并刷新列表。
     */
    public void clearData() {
        int removed = dataSet.data.size();
        if (removed <= 0) {
            return;
        }
        int start = dataSet.header.size();
        dataSet.data.clear();
        notifyItemRangeRemoved(start, removed);
    }

    /**
     * 清空 Header，并刷新列表。
     */
    public void clearHeader() {
        int removed = dataSet.header.size();
        if (removed <= 0) {
            return;
        }
        dataSet.header.clear();
        notifyItemRangeRemoved(0, removed);
    }

    /**
     * 清空 Footer，并刷新列表。
     */
    public void clearFooter() {
        int removed = dataSet.footer.size();
        if (removed <= 0) {
            return;
        }
        int start = dataSet.header.size() + dataSet.data.size();
        dataSet.footer.clear();
        notifyItemRangeRemoved(start, removed);
    }

    /**
     * 添加单个数据,并触发刷新
     *
     * @param item item
     */
    public void add(T item) {
        if (null == item) {
            return;
        }
        dataSet.data.add(item);
        notifyItemInserted(dataSet.header.size() + dataSet.data.size() - 1);
        dataSet.notifyContent();
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
        int headerSize = dataSet.header.size();

        if (!data.isEmpty()) {
            dataSet.data.addAll(data);
            // 数据位于 Header 之后，插入位置需要加上 Header 的偏移量
            notifyItemRangeInserted(headerSize + oldSize, data.size());
        }

        if (dataSet.totalSize() == 0) {
            dataSet.notifyEmpty(0, null);
        } else {
            dataSet.notifyContent();
            if (data.isEmpty()) {
                dataSet.notifyNoMore();
            }
        }
    }

    /**
     * 添加 Header，并刷新列表。
     *
     * @param header header
     */
    public void addHeader(SectionItem header) {
        if (null == header) {
            return;
        }
        dataSet.header.add(header);
        notifyItemInserted(dataSet.header.size() - 1);
    }

    /**
     * 添加 Footer，并刷新列表。
     *
     * @param footer footer
     */
    public void addFooter(SectionItem footer) {
        if (null == footer) {
            return;
        }
        dataSet.footer.add(footer);
        notifyItemInserted(dataSet.header.size() + dataSet.data.size() + dataSet.footer.size() - 1);
    }

    /**
     * 底部加个space空间
     *
     * @param height 高度
     */
    public void addFooterSpace(int height) {
        addFooter(new SectionItem() {
            @Override
            public View createView(ViewGroup parent) {
                Space space = new Space(mRecyclerView.getContext());
                space.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                return space;
            }

            @Override
            public void onBind() {
            }
        });
    }

    /**
     * 正常插入数据
     *
     * @param adapterPosition 插入的位置
     * @param item            插入的数据
     */
    public void insert(int adapterPosition, T item) {
        if (null == item) {
            return;
        }
        if (dataSet.data.size() <= adapterPosition) {
            // 位置越界，退化为追加，保持原有行为
            add(item);
            return;
        }
        int index = dataSet.data.insert(adapterPosition, item);
        if (index < 0) {
            // 数据未发生变化，不通知 RecyclerView，避免状态不一致
            return;
        }
        notifyItemInserted(index);
    }

    /**
     * 正常插入多项数据
     *
     * @param adapterPosition 插入的位置
     * @param items           插入的数据
     */
    public void insertAll(int adapterPosition, List<? extends T> items) {
        if (null == items || items.isEmpty()) {
            return;
        }
        if (dataSet.data.size() <= adapterPosition) {
            // 位置越界，退化为追加，保持原有行为
            addAll(items);
            return;
        }
        int index = dataSet.data.insertAll(adapterPosition, items);
        if (index < 0) {
            // 数据未发生变化，不通知 RecyclerView，避免状态不一致
            return;
        }
        notifyItemRangeInserted(index, items.size());
    }

    /**
     * 插入数据到position之后
     *
     * @param adapterPosition position 从0开始，含header的条数
     * @param item            插入的数据
     */
    public void insertBack(int adapterPosition, T item) {
        if (null == item) {
            return;
        }
        if (dataSet.data.size() <= adapterPosition) {
            // 位置越界，退化为追加，保持原有行为
            add(item);
            return;
        }
        int index = dataSet.data.insertBack(adapterPosition, item);
        if (index < 0) {
            // 数据未发生变化，不通知 RecyclerView，避免状态不一致
            return;
        }
        notifyItemInserted(index);
    }

    /**
     * 插入多项数据到position之后
     *
     * @param adapterPosition position 从0开始，含header的条数
     * @param items           插入的数据
     */
    public void insertAllBack(int adapterPosition, List<? extends T> items) {
        if (null == items || items.isEmpty()) {
            return;
        }
        int index = dataSet.data.insertAllBack(adapterPosition, items);
        if (index < 0) {
            return;
        }
        notifyItemRangeInserted(index, items.size());
    }

    public void swap(int fromAdapterPosition, int toAdapterPosition) {
        if (!dataSet.data.swap(fromAdapterPosition, toAdapterPosition)) {
            return;
        }
        notifyItemMoved(fromAdapterPosition, toAdapterPosition);
    }

    /**
     * 正常的删除
     *
     * @param adapterPosition 待删除的位置 从0开始，含header的条数
     */
    public void remove(int adapterPosition) {
        int index = dataSet.data.remove(adapterPosition);
        if (index < 0) {
            return;
        }
        notifyItemRemoved(index);
    }

    /**
     * 从指定的position之后删除size个数据
     * 不含position
     *
     * @param adapterPosition position 从0开始，含header的条数
     * @param removeSize      删除的数据大小
     */
    public void removeBack(int adapterPosition, int removeSize) {
        if (removeSize <= 0) {
            return;
        }
        if (dataSet.data.size() <= adapterPosition) {
            return;
        }
        if (dataSet.data.size() <= adapterPosition + removeSize) {
            return;
        }
        int index = dataSet.data.removeAllBack(adapterPosition, removeSize);
        if (index < 0) {
            return;
        }
        notifyItemRangeRemoved(index, removeSize);
    }

    /**
     * 通知数据内容发生了变化（重新绑定数据区域的 item）。
     * <p>
     * 当只是数据内容改变（例如修改了某个字段），而 item 的数量与位置没有变化时，
     * 使用该方法代替 {@link #notifyDataSetChanged()}，避免整屏刷新带来的卡顿。
     * <p>
     * 仅数据区（Data）会被重新绑定，Header / Footer / Extra 保持不变。
     */
    public void notifyItemsChanged() {
        int dataSize = dataSet.data.size();
        if (dataSize <= 0) {
            return;
        }
        int start = dataSet.header.size();
        notifyItemRangeChanged(start, dataSize);
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

    /**
     * 获取Footer的个数
     *
     * @return footer size
     */
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
        clearDataSet();
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
        clearDataSet();
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
        if (lp instanceof StaggeredGridLayoutManager.LayoutParams p) {
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
        if (manager instanceof GridLayoutManager gridManager) {
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

    /**
     * 注册数据集观察者
     *
     * @param observer observer
     */
    public void registerObserver(DataSetObserver observer) {
        dataSet.addObserver(observer);
    }

    /**
     * 注销数据集观察者
     *
     * @param observer observer
     */
    public void unregisterObserver(DataSetObserver observer) {
        dataSet.removeObserver(observer);
    }

    /**
     * 注册旧版观察者（{@link java.util.Observer}）
     * <p>
     * 为兼容旧版本而保留，{@link java.util.Observer} 已被废弃。
     *
     * @param observer observer
     * @deprecated 请使用 {@link #registerObserver(DataSetObserver)}
     */
    @Deprecated
    @SuppressWarnings("deprecation")
    public void registerObserver(Observer observer) {
        if (null == observer) {
            return;
        }
        dataSet.addObserver(new LegacyObserver(observer));
    }

    /**
     * 注销旧版观察者（{@link java.util.Observer}）
     * <p>
     * 为兼容旧版本而保留，{@link java.util.Observer} 已被废弃。
     * <p>
     * 注意：旧版观察者被 {@link LegacyObserver} 包装后无法再被精确移除，
     * 该方法仅用于兼容旧代码，实际不会移除已注册的旧版观察者。
     *
     * @param observer observer
     * @deprecated 请使用 {@link #unregisterObserver(DataSetObserver)}
     */
    @Deprecated
    @SuppressWarnings("deprecation")
    public void unregisterObserver(Observer observer) {
        // 旧版观察者无法精确移除，此处保留空实现以兼容旧代码
    }

    protected abstract VH onNewCreateViewHolder(ViewGroup parent, int viewType);

    protected abstract void onNewBindViewHolder(VH holder, int position);

    /**
     * 清空 Header、Data、Footer，并通知 RecyclerView 移除对应的 item。
     * <p>
     * 这里不触发 Empty 状态，仅做数据清理。
     */
    private void clearDataSet() {
        int oldSize = dataSet.totalSize();
        dataSet.clear();
        int removed = oldSize - dataSet.totalSize();
        if (removed > 0) {
            // Header / Data / Footer 均位于 Extra 之前，因此被移除的一定是前 removed 个
            notifyItemRangeRemoved(0, removed);
        }
    }

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
