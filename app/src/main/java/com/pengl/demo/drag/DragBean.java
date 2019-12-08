package com.pengl.demo.drag;


import com.pengl.PLRecyclerView.ItemType;

/**
 *
 */
class DragBean implements ItemType {
    String text;
    boolean status = false;

    DragBean(String text) {
        this.text = text;
    }

    @Override
    public int itemType() {
        return 0;
    }
}
