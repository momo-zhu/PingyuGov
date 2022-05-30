package com.zsoft.signala.hubs;


import com.alibaba.fastjson.JSONObject;

public class HubResult {

    private String mId;
    private String mResult;

    public HubResult(JSONObject message) {
        mId = message.getString("I");
        if (mId == null) mId = "";
        mResult = message.getString("R");
        if (mResult == null) mResult = "";
    }

    public String getId() {
        return mId;
    }

    public String getResult() {
        return mResult;
    }
}
