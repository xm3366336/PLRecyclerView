package com.pengl.demo.main;

import com.pengl.PLRecyclerView.ItemType;

/**
 *
 */
class MenuBean implements ItemType {

    private String menu;
    private int type;

    public MenuBean() {
    }

    public MenuBean(String menu, int type) {
        this.menu = menu;
        this.type = type;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int itemType() {
        return 0;
    }
}
