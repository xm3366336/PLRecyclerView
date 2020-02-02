package com.pengl.demo.drag;

import androidx.recyclerview.widget.ItemTouchHelper;

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
