package com.meetfine.pingyugov.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Andriod05 on 2016/12/23.
 */
public class User {
    private String username ;
    private String avatar;
    private String name;
    private String nickname;
    private String phone;


    private String session_key;
    private String create_date;
    private String update_date;
    private String email;
    private int site_id;

    public User() {
    }
    @JSONField(name = "phone")
    public String getPhone() {
        return phone;
    }
    @JSONField(name = "phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }
    @JSONField(name = "username")
    public String getUsername() {
        return username;
    }

    @JSONField(name = "username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JSONField(name = "avatar")
    public String getAvatar() {
        return avatar;
    }

    @JSONField(name = "avatar")
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JSONField(name = "name")
    public String getName() {
        return name;
    }

    @JSONField(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @JSONField(name = "nickname")
    public String getNickname() {
        return nickname;
    }

    @JSONField(name = "nickname")
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @JSONField(name = "session_key")
    public String getSession_key() {
        return session_key;
    }

    @JSONField(name = "session_key")
    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    @JSONField(name = "create_date")
    public String getCreate_date() {
        return create_date;
    }

    @JSONField(name = "create_date")
    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    @JSONField(name = "update_date")
    public String getUpdate_date() {
        return update_date;
    }

    @JSONField(name = "update_date")
    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    @JSONField(name = "email")
    public String getEmail() {
        return email;
    }

    @JSONField(name = "email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JSONField(name = "site_id")
    public int getSite_id() {
        return site_id;
    }

    @JSONField(name = "site_id")
    public void setSite_id(int site_id) {
        this.site_id = site_id;
    }
}
