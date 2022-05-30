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
import com.meetfine.pingyugov.activities.ContentDetailActivity;
import com.meetfine.pingyugov.adapter.PublicAnnualReportListAdapter;
import com.meetfine.pingyugov.bases.BaseContentListFragment;
import com.meetfine.pingyugov.utils.Config;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import java.util.ArrayList;

/**
 * Created by Andriod05 on 2017/1/16.
 */
public class     PublicAnnualReportFragment extends BaseContentListFragment {
    @BindView(id = R.id.list_view)
    private PullToRefreshListView list_view;

    private String branchId;//部门及区县的BranchID
    private KJActivity aty;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_public_directory,container,false);
    }

    @Override
    protected void initData() {
        branchId = getArguments().getString("branchId");
        aty = (KJActivity) getActivity();
        super.initData();

    }

    @Override
    protected String getUrl() {
        return "OpennessAnnualReports?branchid="+branchId;
    }

    @Override
    protected void doSucess(String t, ArrayList arrayList) {
        JSONObject result = JSON.parseObject(t);
        JSONArray array = result.getJSONArray("content");
        if (array.size()!=0) {
            for (int i = 0; i < array.size(); i++) {
                arrayList.add(array.getJSONObject(i));
            }
        }
    }

    @Override
    protected PullToRefreshListView iniListView() {
        return list_view;
    }

    @Override
    protected ArrayAdapter iniAdapter() {
        return new PublicAnnualReportListAdapter(aty, mList);
    }

    @Override
    protected AdapterView.OnItemClickListener iniOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject item = (JSONObject) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("ContentId", item.getString("_id"));
                bundle.putInt("ContentType", Config.OpennessAnnualReportController);
                aty.showActivity(ContentDetailActivity.class, bundle);
            }
        };
    }
}
