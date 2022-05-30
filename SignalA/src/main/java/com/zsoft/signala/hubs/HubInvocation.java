package com.zsoft.signala.hubs;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class HubInvocation {

    private String mHubName;
    private String mMethod;
    private String mCallbackId;
    private JSONArray mArgs;

    public HubInvocation(String hubName, String method, JSONArray args, String callbackId) {
        mHubName = hubName;
        mMethod = method;
        mArgs = args;
        mCallbackId = callbackId;

    }

    public String getHubName() {
        return mHubName;
    }

    public String getMethod() {
        // TODO Auto-generated method stub
        return null;
    }

    public JSONObject getArgs() {
        // TODO Auto-generated method stub
        return null;
    }

    public String Serialize() {
        JSONObject json = new JSONObject();
        json.put("I", mCallbackId);
        json.put("M", mMethod);
        json.put("A", mArgs);
        json.put("H", mHubName);
        return json.toJSONString();
    }

}
