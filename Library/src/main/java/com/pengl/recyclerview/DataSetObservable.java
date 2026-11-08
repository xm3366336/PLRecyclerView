package com.pengl.recyclerview;

import android.util.Log;

import com.pengl.PLRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 数据集容器，负责持有数据，并向外广播数据集状态的变化。
 * <p>
 * 该类早期继承自 {@link java.util.Observable}，由于 {@code java.util.Observable}
 * 在 Java 9 起已被标记为废弃，这里改为自行维护观察者列表，行为保持一致：
 * <ul>
 *     <li>通知按注册的先后顺序依次分发；</li>
 *     <li>通知过程中不会持有任何锁，允许观察者在回调中增删观察者；</li>
 *     <li>未注册观察者时，通知为无副作用的空操作。</li>
 * </ul>
 *
 * @author pengl
 */
class DataSetObservable<E> {

    Segment<SectionItem> header = new HeaderSegment();
    Segment<E> data = new DataSegment();
    Segment<SectionItem> footer = new FooterSegment();
    Segment<SectionItem> extra = new ExtraSegment();

    private final List<SectionItem> mHeader = new ArrayList<>();
    private final List<E> mData = new ArrayList<>();
    private final List<SectionItem> mFooter = new ArrayList<>();
    private final List<SectionItem> mExtra = new ArrayList<>();

    // 观察者列表，线程安全
    private final List<DataSetObserver> mObservers = new CopyOnWriteArrayList<>();

    //------------------------------
    //|position    size     item   |
    //|----------------------------|
    //|    0               header0 |
    //|    1        2      header1 |
    //|----------------------------|
    //|    2               data0   |
    //|    3               data1   |
    //|    4               data2   |
    //|    5        4      data3   |
    //|----------------------------|
    //|    6               footer0 |
    //|    7        2      footer1 |
    //|----------------------------|
    //|    8               extra0  |
    //|    9        2      extra1  |
    //|------------------------------

    int totalSize() {
        return mHeader.size() + mData.size() + mFooter.size() + mExtra.size();
    }

    void clear() {
        mHeader.clear();
        mData.clear();
        mFooter.clear();
    }

    /**
     * 注册观察者
     */
    void addObserver(DataSetObserver observer) {
        if (null == observer) {
            return;
        }
        mObservers.add(observer);
    }

    /**
     * 移除观察者
     */
    void removeObserver(DataSetObserver observer) {
        if (null == observer) {
            return;
        }
        mObservers.remove(observer);
    }

    /**
     * 通知所有观察者数据集状态发生了变化
     *
     * @param bridge 本次变化对应的桥接对象
     */
    private void notifyObservers(Bridge bridge) {
        for (DataSetObserver observer : mObservers) {
            observer.onDataSetChanged(bridge);
        }
    }

    void notifyLoading() {
        notifyObservers(new Bridge.Loading());
    }

    void notifyContent() {
        notifyObservers(new Bridge.Content());
    }

    void notifyError(int resId, String error) {
        notifyObservers(new Bridge.Error(resId, error));
    }

    void notifyEmpty(int resId, String content) {
        notifyObservers(new Bridge.Empty(resId, content));
    }

    void notifyLoadMoreFailed() {
        notifyObservers(new Bridge.LoadMoreFailed());
    }

    void notifyResumeLoadMore() {
        notifyObservers(new Bridge.ResumeLoadMore());
    }

    void notifyNoMore() {
        notifyObservers(new Bridge.NoMore());
    }

    void notifyAutoLoadMore() {
        notifyObservers(new Bridge.AutoLoadMore());
    }

    void notifyManualLoadMore() {
        notifyObservers(new Bridge.ManualLoadMore());
    }

    void notifyResolveSwipeConflicts(boolean enabled) {
        notifyObservers(new Bridge.SwipeConflicts(enabled));
    }

    abstract static class Segment<T> {

        /**
         * 正常的插入
         *
         * @param adapterPosition 待插入的位置
         * @param item            待插入的数据
         */
        final int insert(int adapterPosition, T item) {
            if (is(adapterPosition)) {
                insertImpl(adapterPosition - positionImpl(), item);
                return adapterPosition;
            } else {
                Log.e(PLRecyclerView.TAG, "insert error,  insert position");
                return -1;
            }
        }

        final int insertAll(int adapterPosition, List<? extends T> items) {
            if (is(adapterPosition)) {
                insertAllImpl(adapterPosition - positionImpl(), items);
                return adapterPosition;
            } else {
                Log.e(PLRecyclerView.TAG, "insert error, check your insert position");
                return -1;
            }
        }

        /**
         * 插入到position之后
         *
         * @param adapterPosition 待插入的位置的前一个
         * @param item            待插入的数据
         * @return 实际插入的 adapterPosition，失败返回 -1
         */
        final int insertBack(int adapterPosition, T item) {
            if (is(adapterPosition)) {
                int insertPosition = adapterPosition - positionImpl() + 1;
                if (insertPosition == size()) {
                    add(item);
                } else {
                    insertImpl(insertPosition, item);
                }
                return adapterPosition + 1;
            } else {
                Log.e(PLRecyclerView.TAG, "insert error, check your insert position");
                return -1;
            }
        }

        final int insertAllBack(int adapterPosition, List<? extends T> items) {
            if (is(adapterPosition)) {
                int insertPosition = adapterPosition - positionImpl() + 1;
                if (insertPosition == size()) {
                    addAll(items);
                } else {
                    insertAllImpl(insertPosition, items);
                }
                return adapterPosition + 1;
            } else {
                Log.e(PLRecyclerView.TAG, "insert error, check your insert position");
                return -1;
            }
        }

        /**
         * 从指定位置后删除size个数据
         *
         * @param adapterPosition 指定的位置
         * @param removeSize      删除的大小
         * @return 被删除的第一个 item 的 adapterPosition，失败返回 -1
         */
        final int removeAllBack(int adapterPosition, int removeSize) {
            if (is(adapterPosition)) {
                int removePosition = adapterPosition - positionImpl() + 1;
                // 安全兜底：最多删除到末尾，避免越界
                int maxRemove = size() - (removePosition - positionImpl());
                int realRemove = Math.min(removeSize, Math.max(maxRemove, 0));
                if (realRemove <= 0) {
                    Log.e(PLRecyclerView.TAG, "remove error, check your remove position");
                    return -1;
                }
                for (int i = 0; i < realRemove; i++) {
                    removeImpl(removePosition);
                }
                return adapterPosition + 1;
            } else {
                Log.e(PLRecyclerView.TAG, "remove error, check your remove position");
                return -1;
            }
        }

        final T get(int adapterPosition) {
            if (is(adapterPosition)) {
                return getImpl(adapterPosition - positionImpl());
            } else {
                throw new NullPointerException();
            }
        }

        final int position() {
            if (size() == 0)
                return -1;
            return positionImpl();
        }

        final boolean is(int adapterPosition) {
            return adapterPosition >= 0 && size() > 0
                    && adapterPosition - positionImpl() < size()
                    && adapterPosition - positionImpl() >= 0;
        }

        final void set(int adapterPosition, T newItem) {
            if (is(adapterPosition)) {
                setImpl(adapterPosition - positionImpl(), newItem);
            } else {
                Log.e(PLRecyclerView.TAG, "set error, check your set position");
            }
        }

        final int remove(int adapterPosition) {
            if (is(adapterPosition)) {
                removeImpl(adapterPosition - positionImpl());
                return adapterPosition;
            } else {
                Log.e(PLRecyclerView.TAG, "remove error, check your remove position");
                return -1;
            }
        }

        final void remove(T needRemove) {
            if (size() != 0) {
                removeImpl(needRemove);
            } else {
                Log.e(PLRecyclerView.TAG, "remove error, check your remove position");
            }
        }

        /**
         * 交换两个位置的数据
         *
         * @return 交换是否成功
         */
        boolean swap(int fromAdapterPosition, int toAdapterPosition) {
            final List<T> l = getAll();
            int from = fromAdapterPosition - positionImpl();
            int to = toAdapterPosition - positionImpl();
            if (from < 0 || to < 0 || from >= l.size() || to >= l.size()) {
                Log.e(PLRecyclerView.TAG, "swap error, check your swap position");
                return false;
            }
            l.set(from, l.set(to, l.get(from)));
            return true;
        }

        abstract int size();

        abstract void clear();

        abstract void add(T item);

        abstract void addAll(List<? extends T> items);

        abstract List<T> getAll();

        abstract int positionImpl();

        abstract void insertImpl(int position, T item);

        abstract void insertAllImpl(int position, List<? extends T> items);

        abstract T getImpl(int position);

        abstract void setImpl(int position, T newItem);

        abstract void removeImpl(int position);

        abstract void removeImpl(T needRemove);

    }

    private class HeaderSegment extends Segment<SectionItem> {

        @Override
        int size() {
            return mHeader.size();
        }

        @Override
        void clear() {
            mHeader.clear();
        }

        @Override
        List<SectionItem> getAll() {
            return mHeader;
        }

        @Override
        void add(SectionItem item) {
            mHeader.add(item);
        }

        @Override
        void addAll(List<? extends SectionItem> items) {
            mHeader.addAll(items);
        }

        @Override
        int positionImpl() {
            return 0;
        }

        @Override
        void insertImpl(int position, SectionItem item) {
            mHeader.add(position, item);
        }

        @Override
        void insertAllImpl(int position, List<? extends SectionItem> items) {
            mHeader.addAll(position, items);
        }

        @Override
        SectionItem getImpl(int position) {
            return mHeader.get(position);
        }

        @Override
        void setImpl(int position, SectionItem newItem) {
            mHeader.set(position, newItem);
        }

        @Override
        void removeImpl(int position) {
            mHeader.remove(position);
        }

        @Override
        void removeImpl(SectionItem needRemove) {
            mHeader.remove(needRemove);
        }
    }

    private class DataSegment extends Segment<E> {

        @Override
        int size() {
            return mData.size();
        }

        @Override
        void clear() {
            mData.clear();
        }

        @Override
        void add(E item) {
            mData.add(item);
        }

        @Override
        void addAll(List<? extends E> items) {
            mData.addAll(items);
        }

        @Override
        List<E> getAll() {
            return mData;
        }

        @Override
        int positionImpl() {
            return header.size();
        }

        @Override
        void insertImpl(int position, E item) {
            mData.add(position, item);
        }

        @Override
        void insertAllImpl(int position, List<? extends E> items) {
            mData.addAll(position, items);
        }

        @Override
        E getImpl(int position) {
            return mData.get(position);
        }

        @Override
        void setImpl(int position, E newItem) {
            mData.set(position, newItem);
        }

        @Override
        void removeImpl(int position) {
            mData.remove(position);
        }

        @Override
        void removeImpl(E needRemove) {
            mData.remove(needRemove);
        }
    }

    private class FooterSegment extends Segment<SectionItem> {

        @Override
        int size() {
            return mFooter.size();
        }

        @Override
        void clear() {
            mFooter.clear();
        }

        @Override
        List<SectionItem> getAll() {
            return mFooter;
        }

        @Override
        void add(SectionItem item) {
            mFooter.add(item);
        }

        @Override
        void addAll(List<? extends SectionItem> items) {
            mFooter.addAll(items);
        }

        @Override
        int positionImpl() {
            return header.size() + data.size();
        }

        @Override
        void insertImpl(int position, SectionItem item) {
            mFooter.add(position, item);
        }

        @Override
        void insertAllImpl(int position, List<? extends SectionItem> items) {
            mFooter.addAll(position, items);
        }

        @Override
        SectionItem getImpl(int position) {
            return mFooter.get(position);
        }

        @Override
        void setImpl(int position, SectionItem newItem) {
            mFooter.set(position, newItem);
        }

        @Override
        void removeImpl(int position) {
            mFooter.remove(position);
        }

        @Override
        void removeImpl(SectionItem needRemove) {
            mFooter.remove(needRemove);
        }
    }

    private class ExtraSegment extends Segment<SectionItem> {

        @Override
        int size() {
            return mExtra.size();
        }

        @Override
        void clear() {
            mExtra.clear();
        }

        @Override
        List<SectionItem> getAll() {
            return mExtra;
        }

        @Override
        void add(SectionItem item) {
            mExtra.add(item);
        }

        @Override
        void addAll(List<? extends SectionItem> items) {
            mExtra.addAll(items);
        }

        @Override
        int positionImpl() {
            return header.size() + data.size() + footer.size();
        }

        @Override
        void insertImpl(int position, SectionItem item) {
            mExtra.add(position, item);
        }

        @Override
        void insertAllImpl(int position, List<? extends SectionItem> items) {
            mExtra.addAll(position, items);
        }

        @Override
        SectionItem getImpl(int position) {
            return mExtra.get(position);
        }

        @Override
        void setImpl(int position, SectionItem newItem) {
            mExtra.set(position, newItem);
        }

        @Override
        void removeImpl(int position) {
            mExtra.remove(position);
        }

        @Override
        void removeImpl(SectionItem needRemove) {
            mExtra.remove(needRemove);
        }
    }

}
