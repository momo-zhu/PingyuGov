package com.meetfine.pingyugov.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.utils.Config;
import com.meetfine.pingyugov.utils.Utils;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.KJFragment;

import mehdi.sakout.dynamicbox.DynamicBox;

/**
 * Created by Andriod05 on 2017/1/13.
 */
public class ContentDetailFragment extends KJFragment {
    @BindView(id = R.id.content)
    private WebView content;
    @BindView(id = R.id.scrollView)
    private ScrollView scrollView;
    private String branchId;
    private int siteId;//仅对市直部门有效
    private Activity aty;
    private DynamicBox box;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_content_detail, container, false);
    }

    @Override
    protected void initData() {
        super.initData();
        aty = getActivity();
        branchId = getArguments().getString("branchId");

    }

    @Override
    protected void initWidget(View parentView) {
        box = new DynamicBox(aty, scrollView);
        View emptyCollectionView = aty.getLayoutInflater().inflate(R.layout.empty_data, null, false);
        box.addCustomView(emptyCollectionView, "empty");
        box.showLoadingLayout();
        box.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                box.showLoadingLayout();
                loadData();
            }
        });
        iniWebView();
        loadData();

    }

    /*加载数据据*/
    private void loadData() {
        HttpParams params = new HttpParams();
        new KJHttp.Builder().url(Config.HOST + "OpennessGuides?branch_id="+branchId).params(params)
                .callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        JSONObject result = JSON.parseObject(t);
                        JSONArray array=result.getJSONArray("contents");
                        JSONObject contentObj=new JSONObject();
                        if (array.size()!=0){
                        for (int i = 0; i <array.size() ; i++) {
                            contentObj = array.getJSONObject(0);
                        }
                        }

                        if(contentObj == null){
                            box.showExceptionLayout();
                        }else{
                            box.hideAll();
                            content.loadData(Utils.getWebViewData(contentObj.getString("body")), "text/html; charset=UTF-8", null);
                        }
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        box.showExceptionLayout();
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
