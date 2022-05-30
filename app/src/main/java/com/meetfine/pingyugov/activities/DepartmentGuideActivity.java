package com.meetfine.pingyugov.activities;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.bases.BaseActivity;
import com.meetfine.pingyugov.utils.Config;
import com.meetfine.pingyugov.utils.Utils;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;

import mehdi.sakout.dynamicbox.DynamicBox;

/**
 * Created by MF201701 on 2017/8/16.
 */

public class DepartmentGuideActivity extends BaseActivity{
    @BindView(id = R.id.content)
    private WebView content;
    @BindView(id = R.id.Exception)
    private RelativeLayout Exception;
    @BindView(id = R.id.scrollView)
    private ScrollView scrollView;
    @BindView(id = R.id.waiting)
    private ImageView waiting;
    private String branchId;
    private DynamicBox box;
    private AnimationDrawable drawable;
    @Override
    public void setRootView() {
    setContentView(R.layout.activity_guide_detail);
    }

    @Override
    public void initData() {
        super.initData();
        title_tv.setText("指南");
        branchId=getIntent().getStringExtra("id");
    }

    @Override
    public void initWidget() {
        super.initWidget();
        iniWebView();
        loadData();
    }
    /*加载数据据*/
    private void loadData() {
        waiting.setVisibility(View.VISIBLE);
        drawable = (AnimationDrawable) waiting.getDrawable();
        drawable.start();
        HttpParams params = new HttpParams();
        new KJHttp.Builder().url(Config.HOST + "OpennessGuides?branch_id="+branchId).params(params)
                .callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        waiting.setVisibility(View.GONE);
                        drawable = (AnimationDrawable) waiting.getDrawable();
                        drawable.stop();
                        JSONObject result = JSON.parseObject(t);
                        JSONArray array=result.getJSONArray("contents");
                        JSONObject contentObj = new JSONObject();
                        if (array.size()!=0) {
                            for (int i = 0; i < array.size(); i++) {
                                contentObj = array.getJSONObject(0);
                            }
                            content.loadData(Utils.getWebViewData(contentObj.getString("body")), "text/html; charset=UTF-8", null);
                        }else {
                            Exception.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        waiting.setVisibility(View.GONE);
                        drawable = (AnimationDrawable) waiting.getDrawable();
                        drawable.stop();
                    }
                }).request();

    }
    /*设置webview*/
    public void iniWebView(){
        WebSettings settings = content.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setBuiltInZoomControls(false);// 隐藏缩放按钮
        settings.setJavaScriptEnabled(true);//启用JS
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 排版适应屏幕
//        settings.setUseWideViewPort(true);// 可任意比例缩放
        settings.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        content.setWebViewClient(new WebViewClient());
    }
}
