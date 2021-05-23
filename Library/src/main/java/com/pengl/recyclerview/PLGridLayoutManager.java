package com.pengl.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * 林北！！！先简单处理一下，解决几个越界的问题！！！
 * <p>
 * java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter positionViewHolder{57819f7 position=7 id=-1, oldPos=-1, pLpos:-1 no parent}
 * java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid item position 7(offset:7).state:17
 * java.lang.IllegalStateException: Added View has RecyclerView as parent but view is not a real child. Unfiltered index:0
 * <p>
 * Created by pengl on 2018/1/11.
 */

public class PLGridLayoutManager extends GridLayoutManager {

    public PLGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PLGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public PLGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}
