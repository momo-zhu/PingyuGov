package com.meetfine.pingyugov.fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.activities.ServiceTempListActivity;
import com.meetfine.pingyugov.adapter.GridTxtAdapter;
import com.meetfine.pingyugov.utils.Config;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.KJFragment;
import org.kymjs.kjframe.ui.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andriod05 on 2017/1/16.
 */
public class ServiceTxtGridFragment1 extends KJFragment{
    @BindView(id = R.id.gridView,click = true)
    private GridView gridView;
    @BindView(id = R.id.waiting)
    private ImageView waiting;//loading动画
    private int type;//0 重点领域 ，1民生领域 ，3特殊人群
    private List<JSONObject> list=new ArrayList<>();
    private GridTxtAdapter gridTxtAdapter;
    private KJActivity aty;
    private AnimationDrawable drawable;
    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_enjoyhs_grid, container, false);
    }
    @Override
    protected void initData() {
        super.initData();
        aty = (KJActivity) getActivity();
        type = getArguments().getInt("type", -1);
        load();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject item = (JSONObject) parent.getItemAtPosition(position);
                doItemClick(item,position);
            }
        });
    }

    private void load() {
        waiting.setVisibility(View.VISIBLE);
        drawable = (AnimationDrawable) waiting.getDrawable();
        drawable.start();
        new KJHttp.Builder().url(Config.HOST+"Service/GetTypes?type="+Config.ServiceInfo[type]).callback(new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                waiting.setVisibility(View.GONE);
                drawable.stop();
                JSONObject result= JSON.parseObject(t);
                JSONArray array=result.getJSONArray("serviceTypes");
                for (int i = 0; i <array.size() ; i++) {
                    list.add(array.getJSONObject(i));
                }
                gridTxtAdapter = new GridTxtAdapter(aty, list);
                if(gridTxtAdapter != null){
                    gridView.setAdapter(gridTxtAdapter);
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                waiting.setVisibility(View.GONE);
                drawable.stop();
                ViewInject.toast("数据加载失败，请重试...");
            }
        }).request();
    }
    /*处理点击操作*/
    public void doItemClick(JSONObject item, int position){
        Bundle bundle = new Bundle();
        bundle.putString("parentId", item.getString("_id"));
        bundle.putString("title", item.getString("name"));
        aty.showActivity(ServiceTempListActivity.class, bundle);
    }
}
