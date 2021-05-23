package com.pengl.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 林北！！！先简单处理一下，解决几个越界的问题！！！
 * <p>
 * java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter positionViewHolder{57819f7 position=7 id=-1, oldPos=-1, pLpos:-1 no parent}
 * java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid item position 7(offset:7).state:17
 * java.lang.IllegalStateException: Added View has RecyclerView as parent but view is not a real child. Unfiltered index:0
 * <p>
 * Created by pengl on 2018/1/11.
 */

public class PLLinearLayoutManager extends LinearLayoutManager {
    public PLLinearLayoutManager(Context context) {
        super(context);
    }

    public PLLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public PLLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

}
