package com.pengl.demo.drag;

import android.support.v7.widget.helper.ItemTouchHelper;

/**
 *
 */
class ItemTouchHelperProvider {

    private static ItemTouchHelper ourInstance;

    private ItemTouchHelperProvider() {
    }

    static ItemTouchHelper getInstance() {
        if (ourInstance == null) {
            throw new IllegalStateException("you should call init first");
        }
        return ourInstance;
    }

    static void init(ItemTouchHelper.Callback callback) {
        ourInstance = new ItemTouchHelper(callback);
    }

}
