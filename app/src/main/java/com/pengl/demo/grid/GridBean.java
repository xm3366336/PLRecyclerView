package com.pengl.demo.grid;

import com.pengl.PLRecyclerView.ItemType;

/**
 * Author: Season(ssseasonnn@gmail.com)
 * Date: 2016/10/10
 * Time: 10:15
 * FIXME
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
