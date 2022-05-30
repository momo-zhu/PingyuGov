package com.meetfine.pingyugov.fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.meetfine.pingyugov.R;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.KJFragment;

/**
 * Created by Andriod05 on 2017/1/13.
 */
public class ContentWebUrlDetailFragment extends KJFragment {
    @BindView(id = R.id.content)
    private WebView content;
    @BindView(id = R.id.waiting)
    private ImageView waiting;//动画

    private String linkUrl;
    private AnimationDrawable drawable;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_content_url_detail, container, false);
    }

    @Override
    protected void initData() {
        super.initData();
        linkUrl = getArguments().getString("LinkUrl");
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
        iniWebView();
    }

    /*设置webview*/
    public void iniWebView(){
        WebSettings settings = content.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setBuiltInZoomControls(false);// 隐藏缩放按钮
        settings.setJavaScriptEnabled(true);//启用JS
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 排版适应屏幕
        settings.setUseWideViewPort(true);// 可任意比例缩放
        settings.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        content.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(waiting.isShown()){
                    drawable.stop();
                }
            }
        });
        waiting.setVisibility(View.VISIBLE);
        drawable = (AnimationDrawable) waiting.getDrawable();
        drawable.start();

        loadWithUrl();
    }
    /*根据URL加载并显示内容*/
    private void loadWithUrl() {
        content.loadUrl(linkUrl);
    }

}
