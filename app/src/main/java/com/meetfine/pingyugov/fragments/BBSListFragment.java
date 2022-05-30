package com.meetfine.pingyugov.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.activities.BBSDetailActivity;
import com.meetfine.pingyugov.activities.InterLetterDetailActivity;
import com.meetfine.pingyugov.adapter.BBSListAdapter;
import com.meetfine.pingyugov.adapter.InteractionListAdapter;
import com.meetfine.pingyugov.application.CustomApplication;
import com.meetfine.pingyugov.bases.BaseContentListFragment;
import com.meetfine.pingyugov.model.User;

import org.kymjs.kjframe.ui.BindView;

import java.util.ArrayList;

/**
 * Created by Andriod05 on 2016/12/15.
 */
public class BBSListFragment extends BaseContentListFragment<JSONObject>{
    @BindView(id = R.id.type_spinner)
    private MaterialSpinner type_spinner;//信件类型
    @BindView(id = R.id.state_spinner)
    private MaterialSpinner state_spinner;//信件处理状态
    @BindView(id = R.id.department_spinner)
    private MaterialSpinner department_spinner;//信件所属部门
    @BindView(id = R.id.search, click = true)
    private TextView search;//查询
    @BindView(id = R.id.create, click = true)
    private TextView create;//写信
    @BindView(id = R.id.list_view)
    private PullToRefreshListView listView;

    private JSONObject groupDir;//分组字典

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_inter_default, container, false);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected String getUrl() {
        //如果需要根据Group筛选，则以Query方式加上groupId参数即可，这里默认展示全部论坛信息
        return "ForumThreads";
    }

    @Override
    protected void doSucess(String t, ArrayList arrayList) {
        JSONObject result = JSON.parseObject(t);
        JSONArray array = result.getJSONArray("threads");
        groupDir = result.getJSONObject("groups");
        for (int i = 0; i <array.size() ; i++) {
            JSONObject object = array.getJSONObject(i);
            arrayList.add(object);
        }
        ((BBSListAdapter)adapter).setGroupDir(groupDir);
    }

    @Override
    protected PullToRefreshListView iniListView() {
        return listView;
    }

    @Override
    protected ArrayAdapter iniAdapter() {
        ArrayAdapter adapter = new BBSListAdapter(acy, mList);//平舆论坛
        return adapter;
    }

    @Override
    protected AdapterView.OnItemClickListener iniOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    JSONObject item = (JSONObject) (parent.getItemAtPosition(position));
                    Bundle bundle = new Bundle();
                    bundle.putString("ThreadId", item.getString("_id"));
                    acy.showActivity(BBSDetailActivity.class, bundle);
            }
        };
    }

}
