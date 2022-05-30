package com.meetfine.pingyugov.activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.model.MyFavor;
import com.meetfine.pingyugov.utils.Config;
import com.meetfine.pingyugov.utils.Utils;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.KJDB;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;

import java.util.Date;
import java.util.List;

/**
 * Created by Andriod05 on 2016/12/8.
 */
public class ContentDetailActivity extends KJActivity {
    @BindView(id = R.id.title_back, click = true)
    protected TextView title_back;
    @BindView(id = R.id.title_operation1, click = true)
    protected TextView title_operation1;
    @BindView(id = R.id.title_tv)
    protected TextView title_tv;
    @BindView(id = R.id.scrollView)
    private ScrollView scrollView;
    @BindView(id = R.id.subject)
    private TextView subject;//标题
    @BindView(id = R.id.createDate)
    private TextView createDate;//创建日期
    @BindView(id = R.id.copyFrom)
    private TextView copyFrom;//信息来源
    @BindView(id = R.id.content)
    private WebView content;//正文
    @BindView(id = R.id.waiting)
    private ImageView waiting;//动画

    private AnimationDrawable drawable;
    private JSONObject contentObj=new JSONObject();
    private String contentId ;//新闻ID
    private int contentType;//控制器
    private String temp = null;
    private int siteId;
    private StringBuilder sbCopy = new StringBuilder();
    private StringBuilder sbDate = new StringBuilder();
    private KJDB favordb;
    private StringBuilder sb = new StringBuilder();
    private String currentSubject;
    private String currentDate;
    private boolean oldHasSaved;//初始收藏状态
    private boolean currentHasSaved;//当前收藏状态
    private int myFavorPosition;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_content_detail);
    }

    @Override
    public void initData() {
        super.initData();
        title_tv.setText("内容详情");

        favordb = KJDB.create(this, Config.favorDBName,true);
        contentId = getIntent().getStringExtra("ContentId");
        contentType = getIntent().getIntExtra("ContentType",-1);
        siteId = getIntent().getIntExtra("siteId", 1);//该字段暂时未用
        myFavorPosition = getIntent().getIntExtra("MyFavorPosition", -1);
        loadData();
    }

    /*判断是否已经收藏*/
    public boolean hasSaved(){
        sb.delete(0, sb.length());
        sb.append(String.format("type = %d",Config.favorTypes[0]));
        sb.append(String.format(" and ContentId = \"%s\"", contentId));
        sb.append(String.format(" and ContentType = %d", contentType));

        sb.append(String.format(" and SiteId = \"%s\"", siteId));

        List<MyFavor> easyArticles = favordb.findAllByWhere(MyFavor.class, sb.toString());
        if(easyArticles != null){
            if(easyArticles.size() != 0){
                return true;
            }
        }
        return false;
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.title_operation1:
                if(hasSaved()){
                    favordb.deleteByWhere(MyFavor.class, sb.toString());
                    title_operation1.setText("添加收藏");
                    currentHasSaved = false;
                    ViewInject.toast("已取消收藏");
                }else{
                    MyFavor myFavor = new MyFavor();
                    myFavor.setSubject(currentSubject);
                    myFavor.setMill(currentDate);
                    myFavor.setType(Config.favorTypes[0]);
                    myFavor.setContentId(contentId);
                    myFavor.setContentType(contentType);
                    myFavor.setSiteId(String.valueOf(siteId));
                    favordb.save(myFavor);
                    title_operation1.setText("取消收藏");
                    currentHasSaved = true;
                    ViewInject.toast("已收藏");
                }
                break;
            case R.id.title_back:
                goBack();
                finish();
                break;
        }
    }


    /*对返回键的操作*/
    public void goBack(){
        if(oldHasSaved != currentHasSaved && myFavorPosition != -1){
            Intent intent = new Intent();
            intent.putExtra("MyFavorPosition", myFavorPosition);
            setResult(MyFavorListActivity.NEED_REFRESH, intent);
        }
    }

    /*加载详情*/
    public void loadData(){
        if(contentId == null){
            return;
        }
        String url = getUrl();
        waiting.setVisibility(View.VISIBLE);
        drawable = (AnimationDrawable) waiting.getDrawable();
        drawable.start();
        new KJHttp.Builder().url(Config.HOST + url)
                .callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        waiting.setVisibility(View.GONE);
                        drawable.stop();
                        JSONObject result = JSON.parseObject(t);
                        contentObj = result.getJSONObject(Config.ContentFlag[contentType]);
                        setWidget();
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        waiting.setVisibility(View.GONE);
                        drawable.stop();
                        super.onFailure(errorNo, strMsg);
                    }
                }).request();

    }

    /*确认url接口*/
    public String getUrl(){
        switch (contentType){
            case Config.ContentController://Content控制器
                temp = "Content?id="+contentId;
                break;
            case Config.opennesscontentController://opennesscontent控制器
                temp = "OpennessContent?id="+contentId;
                break;
            case Config.OpennessAnnualReportController://OpennessAnnualReport控制器
                temp = "OpennessAnnualReport?id="+contentId;
                break;
            case Config.OpennessRulesController://OpennessRules控制器
                temp = "OpennessRule?id="+contentId;
                break;
            case Config.OpennessRequestController://OpennessRequest控制器
                temp = "OpennessRequest?id="+contentId;
                break;
            case Config.OpennessGuideController://OpennessGuide控制器
                temp = "OpennessGuide";
                break;
            case Config.ServiceContentController://ServiceContent控制器
                temp = "ServiceContent";
                break;
        }

        return temp;
    }

    /*设置数据*/
    public void setWidget(){
        sbCopy.delete(0,sbCopy.length());
        sbDate.delete(0,sbDate.length());
        scrollView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings settings = content.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setJavaScriptEnabled(true);//启用JS
//        settings.setBuiltInZoomControls(true);// 隐藏缩放按钮
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 排版适应屏幕
//        settings.setUseWideViewPort(true);// 可任意比例缩放
//        settings.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        content.setWebViewClient(new WebViewClient());

        String title;
        if(contentType == Config.OpennessGuideController){
            title = contentObj.getString("name");
        }else{
            title = contentObj.getString("title");
        }
        subject.setText(title);
        currentSubject = title;
        String datetemp = contentObj.getString("release_date");
//        if(datetemp == null || datetemp.length() == 0){
//            datetemp = contentObj.getString("openness_date");
//        }
//        if(datetemp == null || datetemp.length() ==0){
//            datetemp = contentObj.getString("update_date");
//        }
        if(datetemp == null || datetemp.length() == 0){
            datetemp = contentObj.getString("create_date");
        }
        String date = Config.YEAR.format(new Date(Long.parseLong(datetemp)*1000));
        sbDate.append("发布日期:").append(date);
        currentDate = date;
        createDate.setText(sbDate.toString());
        if(contentType == Config.OpennessRulesController){//公开制度
            sbCopy.append("责任编辑:").append(contentObj.getString("confirmer_name") == null? "未知" : contentObj.getString("confirmer_name"));
        }else if(contentType == Config.opennesscontentController){//公开目录
            sbCopy.append("发布机构:").append(contentObj.getString("copy_from") == null? "未知" : contentObj.getString("copy_from"));
        }else{
            sbCopy.append("信息来源:").append(contentObj.getString("copy_from") == null? "未知" : contentObj.getString("copy_from"));
        }
        copyFrom.setText(sbCopy.toString());
        if(contentType == Config.ServiceContentController){
            content.loadData(Utils.getWebViewData(contentObj.getString("content")), "text/html; charset=UTF-8", null);
        }else{
            content.loadData(Utils.getWebViewData(contentObj.getString("body")), "text/html; charset=UTF-8", null);
        }

        title_operation1.setVisibility(View.VISIBLE);
        oldHasSaved = hasSaved();
        currentHasSaved = oldHasSaved;
        if(currentHasSaved){
            title_operation1.setText("取消收藏");
        }else{
            title_operation1.setText("添加收藏");
        }
    }

    @Override
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && content.canGoBack()) {
            content.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onBackPressed() {
        goBack();
        super.onBackPressed();
    }
}
