package com.meetfine.pingyugov.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Andriod05 on 2017/1/6.
 */
public class WeModel {


    private String link_url;//头像图片地址
    private String type;
    private String title;
    private String avater;//微博跳转地址

    public WeModel() {
    }

    @JSONField(name = "link_url")
    public String getLink_url() {
        return link_url;
    }

    @JSONField(name = "link_url")
    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    @JSONField(name = "type")
    public String getType() {
        return type;
    }

    @JSONField(name = "type")
    public void setType(String type) {
        this.type = type;
    }

    @JSONField(name = "title")
    public String getTitle() {
        return title;
    }

    @JSONField(name = "title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JSONField(name = "avater")
    public String getAvater() {
        return avater;
    }

    @JSONField(name = "avater")
    public void setAvater(String avater) {
        this.avater = avater;
    }
}
