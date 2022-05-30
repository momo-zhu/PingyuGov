package com.meetfine.pingyugov.model;

/**
 * Created by Admin on 2017/5/11.
 */

public class MyMap {
    private int key;
    private String value;

    public MyMap(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
