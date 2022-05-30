package com.meetfine.pingyugov.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.adapter.DepartmentListAdapter;
import com.meetfine.pingyugov.bases.BaseListActivity;

import java.util.ArrayList;

/**
 * Created by MF201701 on 2017/8/16.
 */

public class DepartmentListActivity extends BaseListActivity{
    private String branchId;
    private int type;
    private String[] arrayStr=new String[]{"","opennessContents","content"};
    @Override
    public void initData() {
        branchId=getIntent().getStringExtra("id");
        type=getIntent().getIntExtra("type",-1);
        if (type==1){
            title_tv.setText("目录");
        }else if (type==2){
            title_tv.setText("年报");
        }
        super.initData();
    }

    @Override
    protected String getUrl() {
        String url=null;
        if (type==1){
            url="OpennessContents?branchId="+branchId;
        }else if (type==2){
            url="OpennessAnnualReports?branchid="+branchId;
        }
        return url;
    }

    @Override
    protected void doSuccess(String t, ArrayList arrayList) {
        JSONObject result = JSON.parseObject(t);
        JSONArray array = result.getJSONArray(arrayStr[type]);
        if (array.size()!=0) {
            for (int i = 0; i < array.size(); i++) {
                arrayList.add(array.getJSONObject(i));
            }
        }
    }

    @Override
    protected void iniAdapter() {
         adapter=new DepartmentListAdapter(this, mList);
    }

    @Override
    protected AdapterView.OnItemClickListener iniOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject item = (JSONObject) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("ContentId", item.getString("_id"));
                bundle.putInt("ContentType", type);
                showActivity(ContentDetailActivity.class, bundle);
            }
        };
    }


}
