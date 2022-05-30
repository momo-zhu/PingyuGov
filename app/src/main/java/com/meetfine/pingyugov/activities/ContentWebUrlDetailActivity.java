package com.meetfine.pingyugov.activities;

import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.bases.BaseActivity;

import org.kymjs.kjframe.ui.BindView;

/**
 * Created by Andriod05 on 2016/12/22.
 */
public class ContentWebUrlDetailActivity extends BaseActivity {
    @BindView(id = R.id.content)
    private WebView content;
    @BindView(id = R.id.waiting)
    private ImageView waiting;//动画

    private String linkUrl;
    private AnimationDrawable drawable;
    private String title;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_weburl_detail);
    }

    @Override
    public void initData() {
        super.initData();
        title = getIntent().getStringExtra("title");
        if(title == null){
            title = "详情";
        }
        title_tv.setText(title);
        linkUrl = getIntent().getStringExtra("linkUrl");
        iniWebView();
    }

    /*设置webview*/
    public void iniWebView(){
        content = (WebView) findViewById(R.id.content);
        WebSettings settings = content.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setBuiltInZoomControls(true);// 隐藏缩放按钮
        settings.setJavaScriptEnabled(true);//启用JS
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 排版适应屏幕
//        settings.setUseWideViewPort(true);// 可任意比例缩放
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

    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                if(content.canGoBack()){
                    if(linkUrl.equals(content.getUrl())){
                        finish();
                    }else{
                        content.goBack();// 返回前一个页面
                    }
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && content.canGoBack()) {
            if(linkUrl.equals(content.getUrl())){
                finish();
            }else{
                content.goBack();// 返回前一个页面
                return true;
            }
        }
        return super.onKeyDown(keyCode,event);
    }

}
