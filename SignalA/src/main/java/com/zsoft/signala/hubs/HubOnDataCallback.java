package com.zsoft.signala.hubs;


import com.alibaba.fastjson.JSONArray;

public abstract class HubOnDataCallback {
	public abstract void OnReceived(JSONArray args);
}
