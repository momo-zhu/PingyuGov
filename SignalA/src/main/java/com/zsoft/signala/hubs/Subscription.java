package com.zsoft.signala.hubs;


import com.alibaba.fastjson.JSONObject;

public abstract class Subscription {

	public abstract void OnReceived(JSONObject args);

}
