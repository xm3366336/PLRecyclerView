package com.pengl.demo.model;

import com.pengl.recyclerview.ItemType;

/**
 *
 */
public class BeanMainFun implements ItemType {

    private String menuName;
    private Class<?> jumpToClass;

    public BeanMainFun() {
    }

    public BeanMainFun(String menuName, Class<?> jumpToClass) {
        this.menuName = menuName;
        this.jumpToClass = jumpToClass;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Class<?> getJumpToClass() {
        return jumpToClass;
    }

    public void setJumpToClass(Class<?> jumpToClass) {
        this.jumpToClass = jumpToClass;
    }

    @Override
    public int itemType() {
        return 0;
    }
}
