package com.pengl.demo.model;

import com.pengl.recyclerview.ItemType;

/**
 *
 */
public class BeanTypeTwo implements ItemType {
    private String text;

    public BeanTypeTwo() {
    }

    public BeanTypeTwo(String text) {
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
        return RecyclerItemType.TYPE2.getValue();
    }
}
