package com.meetfine.pingyugov.activities;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.adapter.InterPicDetailAdapter;
import com.meetfine.pingyugov.bases.BaseActivity;
import com.meetfine.pingyugov.utils.Config;
import com.zanlabs.widget.infiniteviewpager.InfiniteViewPager;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andriod05 on 2016/12/22.
 */
public class InterOnlinePicDetailActivity extends BaseActivity {
    @BindView(id = R.id.subject)
    private TextView subject;//标题
    @BindView(id = R.id.viewpager)
    private InfiniteViewPager viewpager;//图片
    @BindView(id = R.id.waiting)
    private ImageView waiting;//loading动画

    private AnimationDrawable drawable;
    private String id;
    private List<JSONObject> pictures = new ArrayList<>();
    private JSONObject contentObj;
    private InterPicDetailAdapter photoAdapter;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_interonline_pic_detail);
    }

    @Override
    public void initData() {
        super.initData();
        title_tv.setText("精彩图片");
        id = getIntent().getStringExtra("id");

        loadData();
    }
    /*加载数据*/
    public void loadData(){
        if(id == null){
            return;
        }
        waiting.setVisibility(View.VISIBLE);
        drawable = (AnimationDrawable) waiting.getDrawable();
        drawable.start();
        new KJHttp.Builder().url(Config.HOST + "InteractionLive?id="+id)
                .callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        pictures.clear();
                        waiting.setVisibility(View.GONE);
                        drawable.stop();
                        JSONObject result = JSON.parseObject(t);
                        JSONArray array = result.getJSONArray("photos");

                        for(int i = 0; i<array.size(); i++){
                            pictures.add(array.getJSONObject(i));
                        }
                        contentObj = result.getJSONObject("content");
                        setWidget();
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        waiting.setVisibility(View.GONE);
                        drawable.stop();
                        ViewInject.toast("数据加载失败，请重试...");
                        super.onFailure(errorNo, strMsg);
                        finish();
                    }
                }).request();

    }

    /*设置数据*/
    public void setWidget() {
        if (pictures == null || contentObj == null) {
            return;
        }
        iniScrollPage();
        subject.setText(contentObj.getString("title"));

    }
    /*设置滚动的ViewPager*/
    private void iniScrollPage() {
        photoAdapter = new InterPicDetailAdapter(this, pictures);
        viewpager.setAdapter(photoAdapter);
        viewpager.setAutoScrollTime(5000);
        viewpager.startAutoScroll();
    }
    @Override
    public void onPause() {
        super.onPause();
        viewpager.stopAutoScroll();
    }

    @Override
    public void onStart() {
        super.onStart();
        viewpager.startAutoScroll();
    }
}
