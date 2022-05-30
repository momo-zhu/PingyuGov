package com.meetfine.pingyugov.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.activities.LoginActivity;
import com.meetfine.pingyugov.application.CustomApplication;
import com.meetfine.pingyugov.model.FeedBackInfo;
import com.meetfine.pingyugov.model.User;
import com.meetfine.pingyugov.utils.Config;
import com.meetfine.pingyugov.utils.Utils;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.http.Request;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.KJFragment;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Andriod05 on 2017/1/12.
 */
public class MessageCreatFragment extends KJFragment {
    @BindView(id = R.id.contact)
    private EditText contact;//姓名
    @BindView(id = R.id.cellphone)
    private EditText cellphone;//联系电话
    @BindView(id = R.id.email)
    private EditText email;//电子邮箱
    @BindView(id = R.id.address)
    private EditText address;//联系地址
    @BindView(id = R.id.department_spinner)
    private MaterialSpinner department_spinner;//受理部门
    @BindView(id = R.id.question_type_txt)
    private TextView question_type_txt;//问题类别
    @BindView(id = R.id.public_spinner)
    private MaterialSpinner public_spinner;//是否公开
    @BindView(id = R.id.subject)
    private EditText subject;//标题
    @BindView(id = R.id.content)
    private EditText content;//正文
    @BindView(id = R.id.submit,click = true)
    private TextView submit;//提交按钮
    @BindView(id = R.id.reset, click = true)
    private TextView reset;//重置按钮
    @BindView(id = R.id.retry)
    private TextView retry;//重新加载部门按钮
    @BindView(id = R.id.verifyCodePic)
    private ImageView verifyCodePic;//验证码
    @BindView(id = R.id.verifyCode)
    private EditText verifyCode;//s输入验证码
    @BindView(id = R.id.waiting)
    private ImageView waiting;//s输入验证码
    @BindView(id = R.id.change_code,click = true)
    private RelativeLayout change_code;//更换验证码
    private String[] typeStr=new String[]{"咨询","投诉","举报","建议"};
    private List<String> publicStrs = new ArrayList<>();//是否公开
    private List<String> departmentNames = new ArrayList<>();//部门名称
    private List<String> departmentId = new ArrayList<>();//部门ID
    private KJActivity aty;
    private CustomApplication app;
    private User user;

    private String contacttemp;
    private String cellphonetemp;
    private String emailtemp;
    private String addresstemp;
    private String subjecttemp;
    private String contenttemp;
    private int type;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_message_create, container, false);
    }

    @Override
    protected void initData() {
        aty = (KJActivity) getActivity();
        app = (CustomApplication) aty.getApplication();
        super.initData();
        type=getArguments().getInt("type",-1);
        loadDepartmentInfo();
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
        publicStrs.clear();
        publicStrs.add("公开");
        publicStrs.add("不公开");
        public_spinner.setItems(publicStrs);
        question_type_txt.setText(typeStr[type]);
    }

    @Override
    public void onStart() {
        super.onStart();
        user = app.getUser();
        if(user != null){
            contact.setEnabled(false);
            contact.setText(user.getName());
        }
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()){
            case R.id.submit://提交信件内容
                user = app.getUser();
                if(user == null){
                    new SweetAlertDialog(aty, SweetAlertDialog.WARNING_TYPE)
                            .showTitle(false)
                            .showContentText(true)
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .setContentText("登录后才能进行写信操作，是否确认登录？")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    aty.showActivity(LoginActivity.class);
                                }
                            }).show();
                }else{
                    contacttemp = contact.getText().toString().trim();//姓名
                    cellphonetemp = cellphone.getText().toString().trim();//联系电话
                    emailtemp = email.getText().toString().trim();//邮箱
                    addresstemp = address.getText().toString().trim();//地址
                    subjecttemp = subject.getText().toString().trim();//留言标题
                    contenttemp = content.getText().toString().trim();//留言正文

                    if(hasEmptyInput()){
                        ViewInject.toast("请将信息填写完整!");
                        return;
                    }
                    doSubmit();
                }
                break;
            case R.id.reset://重置
                doReset();
                break;
            case R.id.change_code://更换验证码
//                if(waiting.getVisibility() == View.VISIBLE){
//                    ViewInject.toast(getString(R.string.requesting_and_wait));
//                    return;
//                }
//                getVerifyCodePic();
                break;
        }
    }
    /*获取图片验证码*/
    public void getVerifyCodePic(){
        waiting.setVisibility(View.VISIBLE);
        new KJHttp.Builder().url(Config.HOST + "user/vcode")
                .httpMethod(Request.HttpMethod.GET)
                .callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(byte[] t) {
                        waiting.setVisibility(View.GONE);
                        if(t == null || t.length == 0){
                            ViewInject.toast(getString(R.string.something_error));
                        }else{
                            Glide.with(aty).load(t).asBitmap().error(R.drawable.icon_error_pic).into(verifyCodePic);
                        }
                    }
                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        waiting.setVisibility(View.GONE);
                        ViewInject.toast(getString(R.string.operation_failed));
                    }
                }).request();
    }


    /*是否有空白的输入栏*/
    public boolean hasEmptyInput(){
        boolean hasEmpty = false;
        if(StringUtils.isEmpty(subjecttemp) || StringUtils.isEmpty(contenttemp) || StringUtils.isEmpty(cellphonetemp)
                ){
            hasEmpty = true;
        }
        if(departmentId.size() == 0){
            hasEmpty = true;
        }
        return hasEmpty;
    }

    /*提交留言信息*/
    public void doSubmit(){
        final SweetAlertDialog dialog = new SweetAlertDialog(aty, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(R.string.requesting)
                .showTitle(true);
        dialog.show();
        String departmentIDtemp = departmentId.get(department_spinner.getSelectedIndex());//部门ID
        boolean ispublic = public_spinner.getSelectedIndex() == 0? true: false;//是否公开

        /*构造对象*/
        FeedBackInfo msgObject = new FeedBackInfo();
        msgObject.setName(contacttemp);
        msgObject.setAddress(addresstemp);
        msgObject.setBranch_id(departmentIDtemp);
        msgObject.setEmail(emailtemp);
        msgObject.setShare_on(ispublic);
        msgObject.setMessage(contenttemp);
        msgObject.setPhone(cellphonetemp);
        msgObject.setSubject(subjecttemp);
        msgObject.setValidateCode(null);
        HttpParams params = new HttpParams();
        params.putHeaders("Authorization", app.getAuth());
        params.putJsonParams(JSON.toJSONString(msgObject));
        Log.e("YBB","YBB=="+JSON.toJSONString(msgObject));
        new KJHttp.Builder().url(Config.HOST + "Supervision/PostWriteFeedback").params(params)
                .contentType(KJHttp.ContentType.JSON)
                .httpMethod(Request.HttpMethod.POST)
                .callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        dialog.setTitleText(R.string.success_operation)
                                .showContentText(true)
                                .showConfirmButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        doReset();
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        getVerifyCodePic();
                        dialog.setTitleText(R.string.fail_operation)
                                .setContentText(Utils.failMessage(strMsg))
                                .showContentText(true)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                }).request();
    }

    /*重置输入信息*/
    public void doReset(){
        subject.setText("");
        content.setText("");
        cellphone.setText("");
        email.setText("");
        content.setText("");
        address.setText("");
        iniSpinner();
    }

    /*设置Spinner默认显示*/
    public void iniSpinner(){
        public_spinner.setSelectedIndex(0);
        department_spinner.setSelectedIndex(0);
    }

    /*设置部门选择器数据*/
    public void iniDepartmentSpinner(){
        department_spinner.setItems(departmentNames);
        iniSpinner();
    }

    /*加载部门信息*/
    public void loadDepartmentInfo(){
        departmentNames.clear();
        departmentId.clear();
        HttpParams params = new HttpParams();
        params.putHeaders("Authorization", app.getAuth());
        new KJHttp.Builder().url(Config.HOST + "Supervision/GetFeedbackInfo?parent_id=5417e7c69a05c2be41ddd813&parent_id=5417e7909a05c27b3bdefe36").params(params)
                .httpMethod(Request.HttpMethod.GET)
                .callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        JSONObject result = JSON.parseObject(t);
                        JSONArray array = result.getJSONArray("sitebranch");
                        for (int i = 0; i <array.size() ; i++) {
                            departmentNames.add(array.getJSONObject(i).getString("name"));
                            departmentId.add(array.getJSONObject(i).getString("_id"));
                        }
                        iniDepartmentSpinner();
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        new SweetAlertDialog(aty, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(R.string.fail_operation)
                                .setContentText("部门信息获取失败，是否重试？")
                                .showContentText(true)
                                .showConfirmButton(true)
                                .showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        loadDepartmentInfo();
                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
//                                        iniDepartmentSpinner();
                                    }
                                }).show();
                    }
                }).request();
    }


    /*显示用户信息*/
    public void iniUser(JSONObject userObj){
        if(userObj == null)
            return;
        contact.setText(userObj.getString("name"));
        cellphone.setText(userObj.getString("phone"));
    }
}
