package com.pengl.demo.model;

/**
 *
 */
public enum RecyclerItemType {

    NORMAL(0), //
    TYPE1(1),  //
    TYPE2(2),  //
    TYPE3(3),  //
    PARENT(4), //
    CHILD(5);  //

    private int nCode;

    RecyclerItemType(int _nCode) {
        this.nCode = _nCode;
    }

    public int getValue() {
        return this.nCode;
    }
}

