package com.meetfine.pingyugov.activities;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.bases.BaseActivity;
import com.meetfine.pingyugov.utils.Config;
import com.meetfine.pingyugov.utils.Utils;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.http.Request;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Andriod05 on 2016/12/23.
 */

public class RegisterActivity1 extends BaseActivity {
    @BindView(id = R.id.name)
    private EditText name;//姓名
    @BindView(id = R.id.userName)
    private EditText userName;//用户名
    @BindView(id = R.id.password)
    private EditText password;//密码
    @BindView(id = R.id.confirm_pwd)
    private EditText confirm_pwd;//确认密码
    @BindView(id = R.id.phone)
    private EditText phone;//电话号码
    @BindView(id = R.id.email)
    private EditText email;//邮箱
    @BindView(id = R.id.register_now, click = true)
    private TextView register_now;//注册信息提交按钮
    private String nameStr;
    private String usernameStr;
    private String passwordStr;
    private String confirmPwdStr;
    private String celephoneStr;
    private String emailStr;


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_register1);
    }

    @Override
    public void initData() {
        super.initData();
        title_tv.setText("注册");
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.register_now:
                nameStr = name.getText().toString().trim();
                usernameStr = userName.getText().toString().trim();
                passwordStr = password.getText().toString().trim();
                confirmPwdStr = confirm_pwd.getText().toString().trim();
                celephoneStr = phone.getText().toString().trim();
                emailStr = email.getText().toString().trim();
                if (hasEmptyRegInfo()) {
                    ViewInject.toast("请将信息填写完整!");
                    return;
                }
                if(!isPwdSame()){
                    ViewInject.toast("密码前后输入不一致！");
                    return;
                }
                submitRegInfo();
                break;
        }
    }

    /*提交注册信息*/
    public void submitRegInfo() {
        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("提交数据。。。")
                .showCancelButton(false)
                .showConfirmButton(false);
        dialog.show();
        HttpParams params = new HttpParams();
        params.put("nickname", usernameStr);
        params.put("password", passwordStr);
        params.put("name", nameStr);
        params.put("phone", celephoneStr);
        params.put("email", emailStr);
        new KJHttp.Builder().url(Config.HOST + "Register/PostReg")
                .httpMethod(Request.HttpMethod.POST).params(params).callback(new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                        .showContentText(true)
                        .setContentText("信息提交成功，请等待相关人员审核...")
                        .showConfirmButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                finish();
                            }
                        });
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                dialog.setTitleText(R.string.fail_operation)
                        .setContentText(Utils.failMessage(strMsg))
                        .showContentText(true)
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);

            }
        }).request();
    }

    /*注册信息是否完整*/
    public boolean hasEmptyRegInfo() {
        boolean hasEmpty = false;
        if (StringUtils.isEmpty(usernameStr) || StringUtils.isEmpty(passwordStr)
                || StringUtils.isEmpty(confirmPwdStr) || StringUtils.isEmpty(nameStr) || StringUtils.isEmpty(celephoneStr) || StringUtils.isEmpty(emailStr)) {
            hasEmpty = true;
        }
        return hasEmpty;
    }

    /*密码与确认密码是否一致*/
    public boolean isPwdSame(){
        boolean isSame = true;
        if(!passwordStr.equals(confirmPwdStr)){
            isSame = false;
        }
        return isSame;
    }
}
