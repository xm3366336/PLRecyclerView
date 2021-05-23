package com.pengl.demo.model;

import com.pengl.recyclerview.ItemType;

import java.util.List;

/**
 *
 */
public class BeanExpandParent implements ItemType {

    private String text;
    private List<BeanExpandChild> ChildList;
    private boolean isExpand;

    public BeanExpandParent() {
    }

    public BeanExpandParent(String text, List<BeanExpandChild> childList, boolean isExpand) {
        this.text = text;
        ChildList = childList;
        this.isExpand = isExpand;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<BeanExpandChild> getChildList() {
        return ChildList;
    }

    public void setChildList(List<BeanExpandChild> childList) {
        ChildList = childList;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    @Override
    public int itemType() {
        return RecyclerItemType.PARENT.getValue();
    }
}
