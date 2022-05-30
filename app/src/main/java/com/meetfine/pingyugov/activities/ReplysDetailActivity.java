package com.meetfine.pingyugov.activities;

import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.application.CustomApplication;
import com.meetfine.pingyugov.bases.BaseActivity;
import com.meetfine.pingyugov.model.User;
import com.meetfine.pingyugov.utils.Config;
import com.meetfine.pingyugov.utils.Utils;
import com.meetfine.pingyugov.utils.ViewFindUtils;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.http.Request;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Andriod05 on 2016/12/30.
 */
public class ReplysDetailActivity extends BaseActivity {
    @BindView(id = R.id.subject)
    private TextView subject;
    @BindView(id = R.id.createDate)
    private TextView createDate;
    @BindView(id = R.id.type)
    private TextView type;
    @BindView(id = R.id.createUser)
    private TextView createUser;
    @BindView(id = R.id.content)
    private TextView content;
    @BindView(id = R.id.waiting)
    private ImageView waiting;
    @BindView(id = R.id.replyName)
    private TextView replyName;//回复人
    @BindView(id = R.id.reply_content)
    private TextView reply_content;//回复内容
    private String id,subjectName;
    private AnimationDrawable drawable;
    private int statusId;
    private CustomApplication app;
    private User user;
    private LayoutInflater inflater;
    @Override
    public void setRootView() {
        setContentView(R.layout.item_reply_detail_content);
    }

    @Override
    public void initData() {
        super.initData();
        title_tv.setText("反馈详情");
        inflater = getLayoutInflater();
        id=getIntent().getStringExtra("id");
        app = (CustomApplication) getApplication();
        statusId=getIntent().getIntExtra("status",-1);
        title_operation1.setText("留言");
        title_operation1.setVisibility(View.VISIBLE);
        subjectName=getIntent().getStringExtra("subject");
        load();
    }
    private void load() {
        waiting.setVisibility(View.VISIBLE);
        drawable = (AnimationDrawable) waiting.getDrawable();
        drawable.start();
        final String url = Config.HOST + getUrl();
        new KJHttp.Builder().url(url).useCache(false).
                callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        waiting.setVisibility(View.GONE);
                        drawable.stop();
                        setDate(t);
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        waiting.setVisibility(View.GONE);
                        drawable.stop();
                        ViewInject.toast("数据加载失败，请重试...");
                    }
                }).request();
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
        View layout = inflater.inflate(R.layout.dialog_feedback_layout, null);
        final EditText contact = ViewFindUtils.find(layout, R.id.contact);
        final EditText cellphone = ViewFindUtils.find(layout,R.id.cellphone);
        final EditText email = ViewFindUtils.find(layout,R.id.email);
        final EditText content = ViewFindUtils.find(layout,R.id.content);
        final EditText title = ViewFindUtils.find(layout,R.id.title);
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
                        String titleTemp = Utils.string2UTF8(title.getText().toString().trim());
                        submitReply(reply,contactTemp,cellphoneTemp,emailTemp,titleTemp, sweetAlertDialog);
                    }
                }).show();

    }
    /*提交发言内容*/
    public void submitReply(String reply,String contactTemp,String cellphoneTemp,String emailTemp,String titleTemp, final SweetAlertDialog sweetAlertDialog){
        sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE)
                .showTitle(true)
                .setTitleText(R.string.requesting)
                .showConfirmButton(false)
                .showCancelButton(false);
        JSONObject replyObj = new JSONObject();
        replyObj.put("id", id);
        replyObj.put("name", contactTemp);
        replyObj.put("phone", cellphoneTemp);
        replyObj.put("email", emailTemp);
        replyObj.put("body", reply);
        replyObj.put("title", titleTemp);
        replyObj.put("typeId", "541a74d19a05c20d3b70cd10");
        HttpParams params = new HttpParams();
        params.putHeaders("Authorization", app.getAuth());
        params.putJsonParams(replyObj.toJSONString());
        new KJHttp.Builder().url(Config.HOST + "SiteFeedback/PostSiteFeedBack")
                .httpMethod(Request.HttpMethod.POST)
                .contentType(KJHttp.ContentType.JSON)
                .params(params)
                .callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        JSONObject result = JSON.parseObject(t);
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                                .showTitle(false)
                                .setContentText("反馈成功")
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

    private void setDate(String t) {
        JSONObject result= JSON.parseObject(t);
        JSONObject detail=result.getJSONObject("siteFeedbackDetail");
        subject.setText(subjectName);
        createUser.setText(detail.getString("name"));
        String createDateStr = Utils.TimeStamp2Date(detail.getString("create_date"), "yyyy-MM-dd HH:mm");
        createDate.setText("来信时间："+createDateStr);
        StringBuilder status=new StringBuilder();
        status.append(detail.getDouble("reply_status"));
        type.setText("处理情况："+status);
        content.setText(detail.getString("body"));
        if (detail.getString("creatorName")!=null) {
            replyName.setText("回复人："+detail.getString("creatorName"));
        }else {
            replyName.setText("回复人：");
        }
        reply_content.setText(detail.getString("reply_content"));
    }

    protected String getUrl() {
        return "SiteFeedback?id="+id;
    }

    @Override
    public void initWidget() {
        super.initWidget();

    }
}
