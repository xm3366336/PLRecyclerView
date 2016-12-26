package com.pengl.demo.main;

import com.pengl.PLRecyclerView.ItemType;

/**
 * Author: Season(ssseasonnn@gmail.com)
 * Date: 2016/10/8
 * Time: 14:21
 * FIXME
 */
class MenuBean implements ItemType {
    public String menu;
    public int type;

    @Override
    public int itemType() {
        return 0;
    }
}
