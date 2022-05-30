package com.zsoft.signala.hubs;


import com.alibaba.fastjson.JSONObject;

public abstract class OnDataCallback {
	public abstract JSONObject Callback(JSONObject result);

}
