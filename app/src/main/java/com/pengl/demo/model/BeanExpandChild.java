package com.pengl.demo.model;

import com.pengl.recyclerview.ItemType;

/**
 *
 */
public class BeanExpandChild implements ItemType {

    private String text;

    public BeanExpandChild(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int itemType() {
        return RecyclerItemType.CHILD.getValue();
    }
}
