package com.pengl.demo.utils;

import androidx.recyclerview.widget.ItemTouchHelper;

/**
 *
 */
public class ItemTouchHelperProvider {

    private static ItemTouchHelper ourInstance;

    private ItemTouchHelperProvider() {
    }

    public static ItemTouchHelper getInstance() {
        if (ourInstance == null) {
            throw new IllegalStateException("you should call init first");
        }
        return ourInstance;
    }

    public static void init(ItemTouchHelper.Callback callback) {
        ourInstance = new ItemTouchHelper(callback);
    }

}
