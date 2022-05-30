package com.meetfine.pingyugov.activities;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.adapter.ReplyListAdapter;
import com.meetfine.pingyugov.application.CustomApplication;
import com.meetfine.pingyugov.model.MyListView;
import com.meetfine.pingyugov.model.User;
import com.meetfine.pingyugov.utils.Config;
import com.meetfine.pingyugov.utils.Utils;
import com.meetfine.pingyugov.utils.ViewFindUtils;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.KJDB;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.http.Request;
import org.kymjs.kjframe.ui.BindView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Andriod05 on 2016/12/8.
 */
public class SolicitationDetailActivity extends KJActivity {
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
    @BindView(id = R.id.overTime)
    private TextView overTime;//结束时间
    @BindView(id = R.id.reply_tv)
    private TextView reply_tv;//
    @BindView(id = R.id.overTxt)
    private TextView overTxt;//结束文字
    @BindView(id = R.id.content)
    private WebView content;//正文
    @BindView(id = R.id.waiting)
    private ImageView waiting;//动画
    @BindView(id = R.id.listView)
    private MyListView listView;//征集留言
    private AnimationDrawable drawable;
    private JSONObject contentObj = new JSONObject();
    private String contentId;//新闻ID
    private String temp = null;
    private int siteId;
    private int jump;
    private StringBuilder sbCopy = new StringBuilder();
    private StringBuilder sbDate = new StringBuilder();
    private KJDB favordb;
    private StringBuilder sb = new StringBuilder();
    private String currentSubject;
    private String currentDate;
    private boolean oldHasSaved;//初始收藏状态
    private boolean currentHasSaved;//当前收藏状态
    private int myFavorPosition;
    private JSONArray arrayFeedBack;
    private List<JSONObject> list = new ArrayList<>();
    private ReplyListAdapter adapter;
    private CustomApplication app;
    private User user;
    private LayoutInflater inflater;
    @Override
    public void setRootView() {
        setContentView(R.layout.activity_content_detail);
    }

    @Override
    public void initData() {
        super.initData();
        inflater = getLayoutInflater();
        title_tv.setText("内容详情");
        app = (CustomApplication) getApplication();
        favordb = KJDB.create(this, Config.favorDBName, true);

        contentId = getIntent().getStringExtra("id");
        jump = getIntent().getIntExtra("jump", -1);
        siteId = getIntent().getIntExtra("siteId", 1);//该字段暂时未用
        myFavorPosition = getIntent().getIntExtra("MyFavorPosition", -1);
        overTime.setVisibility(View.VISIBLE);
        loadData();
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.title_operation1:
                user = app.getUser();
                if(user != null){
                    showMsgDialog();
                }else{
                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .showTitle(false)
                            .showContentText(true)
                            .setContentText("登录后才能进行发言操作，是否确认登录？")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    showActivity(LoginActivity.class);
                                }
                            }).show();
                }
                break;
            case R.id.title_back:
                finish();
                break;
        }
    }
    /*显示发言对话框*/
    public void showMsgDialog(){
        View layout = inflater.inflate(R.layout.dialog_reply_layout, null);
         final EditText contact = ViewFindUtils.find(layout, R.id.contact);
         final EditText cellphone = ViewFindUtils.find(layout,R.id.cellphone);
         final EditText email = ViewFindUtils.find(layout,R.id.email);
        final EditText content = ViewFindUtils.find(layout,R.id.content);
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_TYPE)
                .setCustomView(layout)
                .showTitle(false)
                .showCancelButton(true)
                .setConfirmText("发言")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        String reply = Utils.string2UTF8(content.getText().toString().trim());
                        String contactTemp = Utils.string2UTF8(contact.getText().toString().trim());
                        String cellphoneTemp = Utils.string2UTF8(cellphone.getText().toString().trim());
                        String emailTemp = Utils.string2UTF8(email.getText().toString().trim());
                        submitReply(reply,contactTemp,cellphoneTemp,emailTemp, sweetAlertDialog);
                    }
                }).show();

    }
    /*提交发言内容*/
    public void submitReply(String reply,String contactTemp,String cellphoneTemp,String emailTemp, final SweetAlertDialog sweetAlertDialog){
        sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE)
                .showTitle(true)
                .setTitleText(R.string.requesting)
                .showConfirmButton(false)
                .showCancelButton(false);
        JSONObject replyObj = new JSONObject();
        replyObj.put("id", contentId);
        replyObj.put("name", contactTemp);
        replyObj.put("phone", cellphoneTemp);
        replyObj.put("email", emailTemp);
        replyObj.put("body", reply);
        replyObj.put("sendTime", System.currentTimeMillis()/1000);
        HttpParams params = new HttpParams();
        params.putHeaders("Authorization", app.getAuth());
        params.putJsonParams(replyObj.toJSONString());
        new KJHttp.Builder().url(Config.HOST + "PostInteractionColl")
                .httpMethod(Request.HttpMethod.POST)
                .contentType(KJHttp.ContentType.JSON)
                .params(params)
                .callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        JSONObject result = JSON.parseObject(t);
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                                .showTitle(false)
                                .setContentText("留言成功")
                                .setConfirmText("确认")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        sweetAlertDialog.setTitleText(R.string.fail_operation)
                                .setContentText(Utils.failMessage(strMsg))
                                .showContentText(true)
                                .setConfirmText("确认")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                }).request();
    }


    /*加载详情*/
    public void loadData() {
        if (contentId == null) {
            return;
        }
        String url = null;
        if (jump == 3) {
            url = "ContentPic?id=" + contentId;
        }
        if (jump == 2) {
            url = "InteractionColl?id=" + contentId;
        }
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
                        JSONArray array;
                        if (jump == 3) {
                            array = result.getJSONArray("content");
                        } else {
                            array = result.getJSONArray("collDetail");
                            arrayFeedBack = result.getJSONArray("feedBack");
                        }
                        if (array.size() != 0) {
                            for (int i = 0; i < array.size(); i++) {
                                contentObj = array.getJSONObject(0);
                            }
                        }
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

    /*设置数据*/
    public void setWidget() {
        sbCopy.delete(0, sbCopy.length());
        sbDate.delete(0, sbDate.length());
        scrollView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings settings = content.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setJavaScriptEnabled(true);//启用JS
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 排版适应屏幕
        content.setWebViewClient(new WebViewClient());

        String title;
        title = contentObj.getString("title");
        subject.setText(title);
        currentSubject = title;
        String datetemp = contentObj.getString("create_date");
        String date = Config.YEAR.format(new Date(Long.parseLong(datetemp) * 1000));
        currentDate = date;
        if (jump == 3) {
            copyFrom.setText("信息来源：" + contentObj.getString("copy_from"));
            createDate.setText("发布时间：" + currentDate);
            overTime.setText("访问次数：：" + contentObj.getString("views"));
        }
        if (jump == 2) {
            reply_tv.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            String startDate = contentObj.getString("startdate");
            String overDate = contentObj.getString("overdate");
            String start = Config.YEAR.format(new Date(Long.parseLong(startDate) * 1000));
            String over = Config.YEAR.format(new Date(Long.parseLong(overDate) * 1000));
            copyFrom.setText("征集开始日期：" + start);
            overTime.setText("征集结束日期：" + over.toString());
            if (Long.parseLong(overDate) * 1000 < System.currentTimeMillis()) {
                title_operation1.setVisibility(View.VISIBLE);
                title_operation1.setEnabled(false);
                title_operation1.setTextColor(Color.GRAY);
                title_operation1.setText("留言");
                overTxt.setVisibility(View.VISIBLE);
            } else {
                title_operation1.setVisibility(View.VISIBLE);
                title_operation1.setText("留言");
            }
            for (int i = 0; i < arrayFeedBack.size(); i++) {
                list.add(arrayFeedBack.getJSONObject(i));
            }
            adapter = new ReplyListAdapter(SolicitationDetailActivity.this, list);
            listView.setAdapter(adapter);
        }
        content.loadData(Utils.getWebViewData(contentObj.getString("body")), "text/html; charset=UTF-8", null);
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
}
