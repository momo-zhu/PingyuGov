package com.meetfine.pingyugov.application;

import android.app.Application;
import android.util.Base64;

import com.meetfine.pingyugov.model.User;

import java.io.UnsupportedEncodingException;

/**
 * Created by Tech07 on 2016/6/30.
 */
public class CustomApplication extends Application {
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAuth() {
        if(user == null)
            return "";
        try {
            return "Basic " + Base64.encodeToString(("session_key:" + user.getSession_key()).getBytes("utf-8"), Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

}
