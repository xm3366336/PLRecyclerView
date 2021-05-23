package com.pengl.demo.model;

import com.pengl.recyclerview.ItemType;

/**
 *
 */
public class BeanStagger implements ItemType {

    private String text;

    public BeanStagger(String text) {
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
        return 0;
    }
}
