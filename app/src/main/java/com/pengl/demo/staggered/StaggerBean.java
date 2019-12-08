package com.pengl.demo.staggered;

import com.pengl.PLRecyclerView.ItemType;

/**
 *
 */
public class StaggerBean implements ItemType {
    String text;

    public StaggerBean(String text) {
        this.text = text;
    }

    @Override
    public int itemType() {
        return 0;
    }
}
