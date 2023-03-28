package com.pengl.demo.model;

import com.pengl.recyclerview.ItemType;

/**
 *
 */
public class BeanNormal implements ItemType {

    private String title;
    private String content;

    public BeanNormal() {
    }

    public BeanNormal(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content + " " + content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int itemType() {
        return 0;
    }
}
