package com.meetfine.pingyugov.model;

/**
 * Created by Andriod05 on 2017/1/11.
 */
public class GridItem {

    private String name;
    private String Id;
    private int picResId;
    private String linkUrl;

    public GridItem(String name, String Id, int picResId) {
        this.name = name;
        this.Id = Id;
        this.picResId = picResId;
    }

    public GridItem(String name, String id, int picResId, String linkUrl) {
        this.name = name;
        Id = id;
        this.picResId = picResId;
        this.linkUrl = linkUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public int getPicResId() {
        return picResId;
    }

    public void setPicResId(int picResId) {
        this.picResId = picResId;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}
