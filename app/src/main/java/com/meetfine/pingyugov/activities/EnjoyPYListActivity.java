package com.meetfine.pingyugov.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.adapter.GotoListAdapter;
import com.meetfine.pingyugov.adapter.TxtInfoListAdapter;
import com.meetfine.pingyugov.bases.BaseListActivity;
import com.meetfine.pingyugov.utils.Config;

import java.util.ArrayList;

/**
 * Created by Andriod05 on 2017/1/11.
 */
public class EnjoyPYListActivity extends BaseListActivity{

    private String channelId;
    private int type;
    @Override
    public void setRootView() {
        setContentView(R.layout.activity_enjoyhs_list);
    }

    @Override
    public void initData() {
        channelId = getIntent().getStringExtra("channelId");
        type = getIntent().getIntExtra("type",-1);
        super.initData();
        String titleStr = getIntent().getStringExtra("title");
        title_tv.setText(titleStr);
    }

    @Override
    protected String getUrl() {
        String url=null;
        if (type==0 || type==1 || type==2 || type==3){
            url="SiteChannels?parent_id="+channelId;
        }else {
            url="Contents?channelId=" + channelId;
        }

        return url;
    }

    @Override
    protected void doSuccess(String t, ArrayList arrayList) {
        JSONObject result = JSON.parseObject(t);
        if (type==0 || type==1 || type==2 || type==3){
            JSONArray array = result.getJSONArray("channeltree");
            for (int i = 0; i <array.size() ; i++) {
                arrayList.add(array.getJSONObject(i));
            }
        }else {
            JSONArray array = result.getJSONArray("contents");
            for(int i=0; i<array.size(); i++){
                arrayList.add(array.getJSONObject(i));
            }
        }

    }

    @Override
    protected void iniAdapter() {
        if (type==0 || type==1 || type==2 || type==3){
            adapter = new GotoListAdapter(this, mList);
        }else {
            adapter = new TxtInfoListAdapter(this, mList);
        }

    }

    @Override
    protected AdapterView.OnItemClickListener iniOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject item = (JSONObject) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("ContentId", item.getString("_id"));
                bundle.putInt("ContentType", Config.ContentController);//请求Content控制器
                if (type==0 || type==1 || type==2 || type==3){
                    bundle.putString("title", item.getString("name"));
                    showActivity(EnjoyPYListActivity1.class, bundle);
                }else {
                    showActivity(ContentDetailActivity.class, bundle);
                }
            }
        };
    }
}
