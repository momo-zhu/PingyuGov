package com.meetfine.pingyugov.activities;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.application.CustomApplication;
import com.meetfine.pingyugov.bases.BaseActivity;
import com.meetfine.pingyugov.model.User;
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

public class LoginActivity extends BaseActivity {
    @BindView(id = R.id.forget_pwd, click = true)
    private TextView forget_pwd;
    @BindView(id = R.id.userName_login)
    private EditText Username;//用户名
    @BindView(id = R.id.password_login)
    private EditText Password;//密码
    @BindView(id = R.id.loginButton, click = true)
    private TextView loginButton;//登录按钮
    @BindView(id = R.id.registerButton, click = true)
    private TextView registerButton;//注册按钮
    @BindView(id = R.id.login_layout)
    private View editView;//登录输入栏布局

    private InputMethodManager manager;
    private CustomApplication app;
    private User user=new User();

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initData() {
        super.initData();
        app = (CustomApplication) getApplication();
        title_tv.setText("登录");
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                if (hasEmptyLogInInfo()) {
                    ViewInject.toast("请将信息填写完整。。。");
                    return;
                }
                doLogin(Username.getText().toString().trim(),Password.getText().toString().trim());
                break;
            case R.id.registerButton:
                showActivity(RegisterActivity1.class);
                break;
            case R.id.forget_pwd:
//                showActivity(ForgetPwdActivity.class);
                break;
        }
    }

    /*登录信息是否完整*/
    public boolean hasEmptyLogInInfo() {
        boolean hasEmpty = false;
        String userName_login_str = Username.getText().toString().trim();
        String password_login_str = Password.getText().toString().trim();
        if (StringUtils.isEmpty(userName_login_str) || StringUtils.isEmpty(password_login_str)) {
            hasEmpty = true;
        }
        return hasEmpty;
    }

    /*执行登录操作*/
    public void doLogin(final String userName, String password) {
        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在登录。。。")
                .showCancelButton(false)
                .showConfirmButton(false);
        dialog.show();
        HttpParams params = new HttpParams();
        params.put("UserName", userName);
        params.put("PassWord", password);
        new KJHttp.Builder().url(Config.HOST + "Login/PostLogin")
                .httpMethod(Request.HttpMethod.POST).params(params).callback(new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                ViewInject.toast("登录成功");
                JSONObject result = JSON.parseObject(t);
                JSONObject session=result.getJSONObject("session");
                user.setName(session.getString("name"));
                user.setNickname(session.getString("nickname"));
                user.setSession_key(session.getString("session_key"));
                dialog.dismissWithAnimation();
                app.setUser(user);
                finish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                dialog.setTitleText(R.string.fail_operation)
                        .setContentText(Utils.failMessage(strMsg))
                        .showContentText(true)
                        .showConfirmButton(true)
                        .setConfirmText("确定")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);

            }
        }).request();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (!Utils.isEventInView(ev, editView)) {
                manager.hideSoftInputFromWindow(editView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            return super.dispatchTouchEvent(ev);
        }
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }


}
