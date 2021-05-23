package com.pengl;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 */
public class TouchHelperDragCallback extends ItemTouchHelper.Callback {

    private final boolean isLongPressDragEnabled;
    private final boolean isSwipeEnabled;
    private AbstractAdapter mAdapter;

    /**
     * SimpleItemTouchHelperCallback
     *
     * @param isLongPressDragEnabled 是否启用默认的长按拖动
     * @param isSwipeEnabled         是否启用默认的滑动删除
     */
    public TouchHelperDragCallback(boolean isLongPressDragEnabled, boolean isSwipeEnabled) {
        this.isLongPressDragEnabled = isLongPressDragEnabled;
        this.isSwipeEnabled = isSwipeEnabled;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (!(recyclerView.getAdapter() instanceof AbstractAdapter)) {
            return 0;
        }

        mAdapter = (AbstractAdapter) recyclerView.getAdapter();
        int currentPosition = viewHolder.getAdapterPosition();
        if (!mAdapter.canDrag(currentPosition)) {
            return 0;
        }

        int dragFlags;
        int swipeFlags;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            swipeFlags = 0;
        } else if (layoutManager instanceof LinearLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        } else {
            dragFlags = 0;
            swipeFlags = 0;
        }

        return makeMovementFlags(dragFlags, swipeFlags);
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        if (!mAdapter.canDrag(viewHolder.getAdapterPosition()) || !mAdapter.canDrag(target.getAdapterPosition())) {
            return true;
        }
        mAdapter.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isLongPressDragEnabled;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return isSwipeEnabled;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (!mAdapter.canDrag(viewHolder.getAdapterPosition())) {
            return;
        }
        mAdapter.remove(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        final boolean enabled = !(actionState == ItemTouchHelper.ACTION_STATE_DRAG || actionState == ItemTouchHelper.ACTION_STATE_SWIPE);
        mAdapter.resolveSwipeConflicts(enabled);
    }
}
