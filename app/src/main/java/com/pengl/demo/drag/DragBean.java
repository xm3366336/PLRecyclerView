package com.pengl.demo.drag;


import com.pengl.PLRecyclerView.ItemType;

/**
 * Author: Season(ssseasonnn@gmail.com)
 * Date: 2016/10/12
 * Time: 11:42
 * FIXME
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
