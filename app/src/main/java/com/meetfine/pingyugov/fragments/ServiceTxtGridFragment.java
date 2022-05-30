package com.meetfine.pingyugov.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.activities.ServiceTempListActivity;
import com.meetfine.pingyugov.adapter.GridTxtAdapter;
import com.meetfine.pingyugov.bases.BaseGridFragment;
import com.meetfine.pingyugov.utils.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andriod05 on 2017/1/16.
 */
public class ServiceTxtGridFragment extends BaseGridFragment<JSONObject> {
    private int type;//0 重点领域 ，1民生领域 ，3特殊人群

    @Override
    protected void initData() {
        type = getArguments().getInt("type", -1);
        super.initData();
    }

    @Override
    protected String getUrl() {
        return "ServiceTypes?type="+ Config.ServiceInfo[type];
    }

    @Override
    protected List<JSONObject> doSuccess(String t) {
        List<JSONObject> lists = new ArrayList<>();
        JSONObject result = JSON.parseObject(t);
        JSONObject[] items = result.getObject("serviceTypes", JSONObject[].class);
        if (items==null){
        }else {
            Collections.addAll(lists, items);
        }

        return lists;
    }

    @Override
    protected ArrayAdapter<JSONObject> iniAdapter() {
        return new GridTxtAdapter(aty, mList);
    }

    @Override
    protected AdapterView.OnItemClickListener iniOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject item = (JSONObject) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("parentId", item.getString("_id"));
                bundle.putString("title", item.getString("name"));
                aty.showActivity(ServiceTempListActivity.class, bundle);
            }
        };
    }

}
