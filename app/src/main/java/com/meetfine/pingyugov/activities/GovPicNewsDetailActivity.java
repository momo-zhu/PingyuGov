package com.meetfine.pingyugov.activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.adapter.GovPicNewsDetailAdapter;
import com.meetfine.pingyugov.model.MyFavor;
import com.meetfine.pingyugov.utils.Config;
import com.meetfine.pingyugov.utils.Utils;
import com.zanlabs.widget.infiniteviewpager.InfiniteViewPager;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.KJDB;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andriod05 on 2016/12/26.
 */
public class GovPicNewsDetailActivity extends KJActivity {
    @BindView(id = R.id.title_back, click = true)
    protected TextView title_back;
    @BindView(id = R.id.title_operation1, click = true)
    protected TextView title_operation1;
    @BindView(id = R.id.title_tv)
    protected TextView title_tv;
    @BindView(id = R.id.viewpager)
    private InfiniteViewPager viewpager;//图片
    @BindView(id = R.id.waiting)
    private ImageView waiting;//loading动画
    @BindView(id = R.id.web_content)
    private WebView web_content;//正文
    @BindView(id = R.id.subject)
    private TextView subject;//标题
    @BindView(id = R.id.copyFrom)
    private TextView copyFrom;//信息来源
    @BindView(id = R.id.createDate)
    private TextView createDate;//发布日期
    @BindView(id = R.id.description)
    private TextView description;//说明
    private AnimationDrawable drawable;
    private String id;//新闻ID
    private List<JSONObject> pictures = new ArrayList<>();
    private JSONObject contentObj;
    private GovPicNewsDetailAdapter photoAdapter;
    private StringBuilder sbCopy = new StringBuilder();
    private StringBuilder sbDate = new StringBuilder();
    private KJDB favordb;
    private boolean oldHasSaved;//初始收藏状态
    private boolean currentHasSaved;//当前收藏状态
    private StringBuilder sb = new StringBuilder();
    private String currentSubject;
    private String currentDate;
    private int myFavorPosition;
    private int jump;
    @Override
    public void setRootView() {
        setContentView(R.layout.activity_gov_pic_news_detail);
    }
    @Override
    public void initData() {
        super.initData();
        title_tv.setText("新闻详情");
        id = getIntent().getStringExtra("id");
        jump = getIntent().getIntExtra("jump",-1);
        myFavorPosition = getIntent().getIntExtra("MyFavorPosition", -1);
        favordb = KJDB.create(this, Config.favorDBName,true);
        loadData();
    }
    /*加载数据*/
    public void loadData(){
        if(id ==null){
            return;
        }
        waiting.setVisibility(View.VISIBLE);
        drawable = (AnimationDrawable) waiting.getDrawable();
        drawable.start();
        String url=null;
        if (jump==1){
            url="Content?id="+id;
        }
        if (jump==4){
            url="ContentPic?id="+id;
        }
        new KJHttp.Builder().url(Config.HOST +url )
                .callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        pictures.clear();
                        waiting.setVisibility(View.GONE);
                        drawable.stop();
                        JSONObject result = JSON.parseObject(t);
                        if (jump==4) {
                            JSONArray content=result.getJSONArray("content");
                            if (content!=null){
                                contentObj=content.getJSONObject(0);
                            }
                            JSONArray array = result.getJSONArray("contentPicture");
                            if (array == null || array.size() == 0) {
                                JSONObject object = new JSONObject();
                                object.put("title", "没有图片哦！");
                                object.put("photo", null);
                                pictures.add(object);
                            } else {
                                for (int i = 0; i < array.size(); i++) {
                                    pictures.add(array.getJSONObject(i));
                                }
                            }
                        }
                        if (jump==1){
                            contentObj = result.getJSONObject("content");
                        }

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
    /*判断是否已经收藏*/
    public boolean hasSaved(){
        sb.delete(0, sb.length());
        sb.append(String.format("type = %d",Config.favorTypes[1]));
        sb.append(String.format(" and id = \"%s\"", id));
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
                    myFavor.setType(Config.favorTypes[1]);
                    myFavor.setId(id);
                    myFavor.setJump(jump);
                    myFavor.setAvator(contentObj.getString("thumb_name"));
                    favordb.save(myFavor);
                    title_operation1.setText("取消收藏");
                    currentHasSaved = true;
                    ViewInject.toast("已收藏");
                }
                break;
            case R.id.title_back://返回
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

    @Override
    public void onBackPressed() {
        goBack();
        super.onBackPressed();
    }

    /*设置数据*/
    public void setWidget() {
        if (contentObj == null) {
            return;
        }
        title_operation1.setVisibility(View.VISIBLE);
        oldHasSaved = hasSaved();
        currentHasSaved = oldHasSaved;
        if(currentHasSaved){
            title_operation1.setText("取消收藏");
        }else{
            title_operation1.setText("添加收藏");
        }
        if(pictures != null && pictures.size() != 0){
            viewpager.setVisibility(View.VISIBLE);
            iniScrollPage();
        }else{
            viewpager.setVisibility(View.GONE);
        }
        iniWebView();
        iniTxtView();
    }

    /*设置文本信息*/
    public void iniTxtView(){
        sbCopy.delete(0,sbCopy.length());
        sbDate.delete(0,sbDate.length());
        if(contentObj == null){
            return;
        }
        if(contentObj.getString("title") == null || contentObj.getString("title").length() == 0){
            subject.setText("正文内容");
            currentSubject = "正文内容";
        }else{
            subject.setText(contentObj.getString("title"));
            currentSubject = contentObj.getString("title");
        }
        sbCopy.append("信息来源:").append(contentObj.getString("copy_from") == null ? "" : contentObj.getString("copy_from"));
        sbDate.append("发布日期:").append(Config.YEAR.format(Utils.utcToLocal(contentObj.getString("release_date"))));
        currentDate = Config.YEAR.format(Utils.utcToLocal(contentObj.getString("release_date")));
        copyFrom.setText(sbCopy.toString());
        createDate.setText(sbDate.toString());
        description.setText(contentObj.getString("description"));
    }

    /*设置webView*/
    public void iniWebView(){
        WebSettings settings = web_content.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setDefaultFontSize(18);
        settings.setJavaScriptEnabled(true);//启用JS
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 排版适应屏幕
        web_content.setWebViewClient(new WebViewClient());
        web_content.loadData(Utils.getWebViewData(contentObj.getString("body")), "text/html; charset=UTF-8", null);

    }
    /*设置滚动的ViewPager*/
    private void iniScrollPage() {
        photoAdapter = new GovPicNewsDetailAdapter(this, pictures);
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
