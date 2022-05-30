package com.meetfine.pingyugov.activities;

import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.http.Request;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Andriod05 on 2016/12/8.
 */
public class BBSDetailActivity extends KJActivity {
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
    @BindView(id = R.id.name)
    private TextView name;//姓名
    @BindView(id = R.id.date)
    private TextView date;//日期
    @BindView(id = R.id.reply_tv)
    private TextView reply_tv;//“网友留言”标签
    @BindView(id = R.id.content)
    private WebView content;//正文
    @BindView(id = R.id.waiting)
    private ImageView waiting;//动画
    @BindView(id = R.id.listView)
    private MyListView listView;//网友留言
    private AnimationDrawable drawable;
    private JSONObject contentObj = new JSONObject();
    private String threadId;//新闻ID
    private StringBuilder sbName = new StringBuilder();
    private StringBuilder sbDate = new StringBuilder();

    private JSONArray arrayForumThreadPosts;
    private List<JSONObject> list = new ArrayList<>();
    private ReplyListAdapter adapter;
    private CustomApplication app;
    private User user;
    private LayoutInflater inflater;
    @Override
    public void setRootView() {
        setContentView(R.layout.activity_bbs_detail);
    }

    @Override
    public void initData() {
        super.initData();
        inflater = getLayoutInflater();
        title_tv.setText("内容详情");
        app = (CustomApplication) getApplication();
        threadId = getIntent().getStringExtra("ThreadId");
        loadData();
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.title_operation1://留言
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
        View layout = inflater.inflate(R.layout.dialog_bbs_reply_layout, null);
        final EditText content = ViewFindUtils.find(layout,R.id.content);
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_TYPE)
                .setCustomView(layout)
                .showTitle(false)
                .showCancelButton(true)
                .setConfirmText("留言")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if(StringUtils.isEmpty(content.getText().toString())){
                            Toast.makeText(aty,"请输入评论内容", Toast.LENGTH_SHORT);
                            return;
                        }
                        String reply = Utils.string2UTF8(content.getText().toString().trim());
                        submitReply(reply, sweetAlertDialog);
                    }
                }).show();

    }
    /*提交发言内容*/
    public void submitReply(String reply, final SweetAlertDialog sweetAlertDialog){
        sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE)
                .showTitle(true)
                .setTitleText(R.string.requesting)
                .showConfirmButton(false)
                .showCancelButton(false);
        HttpParams params = new HttpParams();
        params.putHeaders("Authorization", app.getAuth());
        params.put("id", threadId);
        params.put("msg", reply);
        new KJHttp.Builder().url(Config.HOST + "ForumPost")
                .httpMethod(Request.HttpMethod.POST)
                .contentType(KJHttp.ContentType.FORM)
                .params(params)
                .callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        sweetAlertDialog.dismiss();
                        Toast.makeText(aty, "留言成功", Toast.LENGTH_SHORT);
                        loadData();
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
        if (threadId == null) {
            return;
        }
        String url = "ForumThread/" + threadId;
        waiting.setVisibility(View.VISIBLE);
        drawable = (AnimationDrawable) waiting.getDrawable();
        drawable.start();
        new KJHttp.Builder().url(Config.HOST + url)
                .callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        waiting.setVisibility(View.GONE);
                        drawable.stop();
                        try{
                            JSONObject result = JSON.parseObject(t);
                            arrayForumThreadPosts = result.getJSONArray("forumposts");
                            contentObj = result.getJSONObject("forumthread");
                            setWidget();

                        }catch (Exception e){
                             String msg = e.getMessage();
                            if(StringUtils.isEmpty(msg)) msg = "发生未知错误";
                             new SweetAlertDialog(aty,SweetAlertDialog.ERROR_TYPE)
                                     .setTitleText(R.string.fail_operation)
                                    .setContentText(msg)
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
        sbName.delete(0, sbName.length());
        sbDate.delete(0, sbDate.length());
        scrollView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings settings = content.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setJavaScriptEnabled(true);//启用JS
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 排版适应屏幕
        content.setWebViewClient(new WebViewClient());

        String title = contentObj.getString("title");
        String nameStr = contentObj.getString("name");
        String datetemp = contentObj.getString("create_date");
        String dateStr = Config.YEAR.format(new Date(Long.parseLong(datetemp) * 1000));
        reply_tv.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);

        subject.setText(title);
        date.setText("发表于：" + dateStr);
        name.setText("作者：" + nameStr);

        title_operation1.setVisibility(View.VISIBLE);
        title_operation1.setText("留言");

        for (int i = 0; i < arrayForumThreadPosts.size(); i++) {
            list.add(arrayForumThreadPosts.getJSONObject(i));
        }
        adapter = new ReplyListAdapter(BBSDetailActivity.this, list);
        listView.setAdapter(adapter);
        content.loadData(Utils.getWebViewData(contentObj.getString("body")), "text/html; charset=UTF-8", null);
    }


}
