package com.pengl.demo.grid;

import com.pengl.PLRecyclerView.ItemType;

/**
 *
 */
public class GridBean implements ItemType {
    String text;

    public GridBean(String text) {
        this.text = text;
    }

    @Override
    public int itemType() {
        return 0;
    }
}
