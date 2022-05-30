package com.meetfine.pingyugov.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.adapter.PublicDirAdapter;
import com.meetfine.pingyugov.bases.BaseContentListFragment;

import org.kymjs.kjframe.ui.BindView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andriod05 on 2016/12/20.
 */
public class PublicDirFragment extends BaseContentListFragment {

    @BindView(id = R.id.list_view)
    private PullToRefreshListView list_view;
    private int  type;
    private List<JSONObject> list=new ArrayList<>();
    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected String getUrl() {
        return "SiteBranchs?parentId=5417e7909a05c27b3bdefe36";
    }

    @Override
    protected void doSucess(String t, ArrayList arrayList) {
        JSONObject result = JSON.parseObject(t);
        JSONArray array = result.getJSONArray("siteBranch");
        for(int i =0; i<array.size(); i++){
            arrayList.add(array.getJSONObject(i));
        }
    }

    @Override
    protected PullToRefreshListView iniListView() {
        return list_view;
    }
    @Override
    protected ArrayAdapter iniAdapter() {
        ArrayAdapter adapter=new PublicDirAdapter(getActivity(),mList);
        return adapter;
    }
    @Override
    protected AdapterView.OnItemClickListener iniOnItemClickListener() {
        return null;
    }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_public_directory,container,false);
    }
}
