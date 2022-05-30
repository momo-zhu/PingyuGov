package com.meetfine.pingyugov.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.activities.ServiceItemDetailActivity;
import com.meetfine.pingyugov.adapter.ServiceParentListAdapter;
import com.meetfine.pingyugov.bases.BaseListFragment;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Admin on 2017/4/1.
 */

public class TxtInfoListFragment extends BaseListFragment<JSONObject> {
    private String parentId;
    private String title;

    @Override
    protected void initData() {
        super.initData();
        parentId = getArguments().getString("parentId");
        title = getArguments().getString("title");
    }

    @Override
    protected String getUrl() {
        return "Services?type=" + parentId;
    }

    @Override
    protected List<JSONObject> doSuccess(String t) {
        JSONObject result = JSON.parseObject(t);
        List<JSONObject> items = Arrays.asList(result.getObject("services", JSONObject[].class));
        return items;
    }

    @Override
    protected ArrayAdapter<JSONObject> iniAdapter() {
        return new ServiceParentListAdapter(aty,mList);
    }

    @Override
    protected AdapterView.OnItemClickListener iniOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject item = (JSONObject) parent.getItemAtPosition(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("parentId", item.getString("_id"));
                    bundle.putString("title", title);
                    aty.showActivity(ServiceItemDetailActivity.class, bundle);
            }
        };
    }
}
