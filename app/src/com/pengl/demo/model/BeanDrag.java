package com.pengl.demo.model;


import com.pengl.recyclerview.ItemType;

/**
 *
 */
public class BeanDrag implements ItemType {

    private String text;
    private boolean status;

    public BeanDrag(String text) {
        this.text = text;
        this.status = false;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public int itemType() {
        return 0;
    }
}
