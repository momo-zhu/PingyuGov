package com.meetfine.pingyugov.activities;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.adapter.InterOnlineDetailQueAdapter;
import com.meetfine.pingyugov.application.CustomApplication;
import com.meetfine.pingyugov.bases.BaseActivity;
import com.meetfine.pingyugov.model.MyListView;
import com.meetfine.pingyugov.model.User;
import com.meetfine.pingyugov.utils.Config;
import com.meetfine.pingyugov.utils.Utils;
import com.meetfine.pingyugov.utils.ViewFindUtils;

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
 * Created by Andriod05 on 2016/12/21.
 */
public class InterOnlineDetailActivity extends BaseActivity {

    @BindView(id = R.id.waiting)
    private ImageView waiting;//loading动画
    @BindView(id = R.id.subject)
    private TextView subject;//标题
    @BindView(id = R.id.guests)
    private TextView guests;//访谈嘉宾
    @BindView(id = R.id.manage)
    private TextView manage;//主持人
    @BindView(id = R.id.intro)
    private TextView intro;//引言
    @BindView(id = R.id.content)
    private TextView content;//正文
    @BindView(id = R.id.listView)
    private MyListView listView;//提问内容
    @BindView(id = R.id.overTxt)
    private TextView overTxt;//结束语


    private String id;
    private AnimationDrawable drawable;
    private List<JSONObject> questions = new ArrayList<>();
    private JSONObject contentObj;
    private InterOnlineDetailQueAdapter adapter;
    private StringBuilder sbGuest = new StringBuilder();
    private StringBuilder sbManage = new StringBuilder();
    private LayoutInflater inflater;
    private CustomApplication app;
    private User user;
    private int canFeedBack;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_inter_online_detail);
    }

    @Override
    public void initData() {
        super.initData();
        inflater = getLayoutInflater();
        app = (CustomApplication) getApplication();
        title_tv.setText("访谈详情");

        id = getIntent().getStringExtra("id");
        adapter = new InterOnlineDetailQueAdapter(this,questions);
        listView.setAdapter(adapter);
        loadData();
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.title_operation1:
                user = app.getUser();
                if(user != null){
//                    if(canFeedBack==1){
                        showMsgDialog();
//                    }else{
//                        ViewInject.toast("当前不允许进行发言！");
//                    }

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
        }
    }
    /*显示发言对话框*/
    public void showMsgDialog(){
        View layout = inflater.inflate(R.layout.dialog_inter_online_layout, null);
        final  EditText content = ViewFindUtils.find(layout,R.id.content);
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_TYPE)
                .setCustomView(layout)
                .showTitle(false)
                .showCancelButton(true)
                .setConfirmText("发言")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
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
        JSONObject replyObj = new JSONObject();
        replyObj.put("id", id);
        replyObj.put("message", reply);
        HttpParams params = new HttpParams();
        params.putHeaders("Authorization", app.getAuth());
        params.putJsonParams(replyObj.toJSONString());
        new KJHttp.Builder().url(Config.HOST + "InteractionLivePost")
                .httpMethod(Request.HttpMethod.POST)
                .contentType(KJHttp.ContentType.JSON)
                .params(params)
                .callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        JSONObject result = JSON.parseObject(t);
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                                .showTitle(false)
                                .setContentText(result.getString("Result"))
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
                        questions.clear();
                        waiting.setVisibility(View.GONE);
                        drawable.stop();
                        JSONObject result = JSON.parseObject(t);
                        JSONArray array = result.getJSONArray("questions");
                        for(int i = 0; i<array.size(); i++){
                            questions.add(array.getJSONObject(i));
                        }
                        contentObj = result.getJSONObject("content");
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
    public void setWidget(){
        if(questions == null || contentObj == null){
            return;
        }
        long currentMill = new Date().getTime();
        long startMill = Utils.StrTime2Long(contentObj.getString("startdate"), currentMill);
        long overMill = Utils.StrTime2Long(contentObj.getString("overdate"), currentMill);
        canFeedBack = Integer.parseInt(contentObj.getString("isask"));
        if (canFeedBack==0){
            title_operation1.setVisibility(View.VISIBLE);
            title_operation1.setTextColor(Color.GRAY);
            title_operation1.setText("发言");
            title_operation1.setEnabled(false);
        }else if (canFeedBack==1){
            title_operation1.setVisibility(View.VISIBLE);
            title_operation1.setText("发言");
        }
        sbGuest.delete(0,sbGuest.length());
        sbManage.delete(0,sbManage.length());
        subject.setText(contentObj.getString("title"));
        sbGuest.append("访谈嘉宾：").append(contentObj.getString("guests"));
        guests.setText(sbGuest.toString());
        sbManage.append("主持人：").append(contentObj.getString("manage"));
        manage.setText(sbManage.toString());
        if(contentObj.getString("intro") == null || contentObj.getString("intro").length() == 0){
            intro.setVisibility(View.GONE);
        }else{
            intro.setText(contentObj.getString("intro"));
        }
        content.setText(contentObj.getString("starttxt"));
        adapter.notifyDataSetChanged();
        Utils.setListViewHeightBasedOnChildren(listView);
        overTxt.setText(contentObj.getString("overtxt"));
    }
}
