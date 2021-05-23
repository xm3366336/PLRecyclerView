package com.pengl.demo.model;

import com.pengl.recyclerview.ItemType;

/**
 *
 */
public class BeanTypeOne implements ItemType {

    private String text;

    public BeanTypeOne() {
    }

    public BeanTypeOne(String text) {
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
        return RecyclerItemType.TYPE1.getValue();
    }
}
