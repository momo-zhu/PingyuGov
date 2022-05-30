package com.zsoft.signala.hubs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Locale;


public class HubInvocationMessage {

    private String mHubName;
    private String mMethod;
    private JSONArray mArgs;

    public HubInvocationMessage(JSONObject message) {

        mHubName = message.getString("H");
        if (mHubName != null)
            mHubName = mHubName.toLowerCase(Locale.US);
        mMethod = message.getString("M");
        if (mMethod != null)
            mMethod = mMethod.toLowerCase(Locale.US);
        mArgs = message.getJSONArray("A");
        //[{"H":"CalculatorHub","M":"newCalculation","A":["4/7/2013 12:42:23 PM : 10 + 5 = 15"]}]}" +
    }

    public String getHubName() {
        return mHubName;
    }

    public JSONArray getArgs() {
        return mArgs;
    }

    public String getMethod() {
        return mMethod;
    }

}
