package com.meetfine.pingyugov.activities;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
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

public class RegisterActivity extends BaseActivity {

    @BindView(id = R.id.userName)//用户名
    protected EditText userName;
    @BindView(id = R.id.userPwd)
    protected EditText userPwd;//密码
    @BindView(id = R.id.confirmPwd)
    protected EditText confirmPwd;//确认密码
    @BindView(id = R.id.phone)
    protected EditText phone;//手机号码
    @BindView(id = R.id.register_now, click = true)
    private TextView register_now;//注册按钮
    @BindView(id = R.id.agree)
    private CheckBox agree;//同意
    @BindView(id = R.id.protocol, click = true)
    private TextView protocol;//协议
    @BindView(id = R.id.editview)
    private View editView;//注册输入栏布局
    @BindView(id = R.id.sendMsg, click = true)
    private TextView sendMsg;//发送验证码
    @BindView(id = R.id.verificationCode)
    private EditText verificationCode;//验证码

    private InputMethodManager manager;
    private String usernameStr;
    private String passwordStr;
    private String confirmPwdStr;
    private String celephoneStr;
    private TimeCount timer;
    private double lastClickeTime = 0;
    private String verificationCodeStr;


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_register);
    }

    @Override
    public void initData() {
        super.initData();
        title_tv.setText("注册");
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        timer = new TimeCount(60000, 1000);//构造CountDownTimer对象
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.protocol:
//                showActivity(ProtocolActivity.class);
                break;
            case R.id.register_now:
                usernameStr = userName.getText().toString().trim();
                passwordStr = userPwd.getText().toString().trim();
                confirmPwdStr = confirmPwd.getText().toString().trim();
                celephoneStr = phone.getText().toString().trim();
                verificationCodeStr = verificationCode.getText().toString().trim();
                if (hasEmptyRegInfo()) {
                    ViewInject.toast("请将信息填写完整!");
                    return;
                }
                if(!isPwdSame()){
                    ViewInject.toast("密码前后输入不一致！");
                    return;
                }
                if(!agree.isChecked()){
                    ViewInject.toast("请阅读并接受协议后再进行注册！");
                    return;
                }
                submitRegInfo();
                break;
            case R.id.sendMsg://发送验证码
                celephoneStr = phone.getText().toString();
                if(celephoneStr == null || celephoneStr.trim().length() == 0)//输入为空不允许发送短信
                    return;
                if((System.currentTimeMillis() - lastClickeTime) < 3000)//三秒内不允许发送短信
                    return;
                lastClickeTime = System.currentTimeMillis();
                sendMsg();
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
        JSONObject regObject = new JSONObject();
        regObject.put("nickname", usernameStr);
        regObject.put("password", passwordStr);
        regObject.put("ConfirmPassword", confirmPwdStr);
        regObject.put("Phone", celephoneStr);
        regObject.put("Agreement", true);
        regObject.put("ValidateCode", verificationCodeStr);
        HttpParams params = new HttpParams();
        params.putJsonParams(regObject.toJSONString());
        new KJHttp.Builder().url(Config.HOST + "Register")
                .contentType(KJHttp.ContentType.JSON)
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
                || StringUtils.isEmpty(confirmPwdStr) || StringUtils.isEmpty(celephoneStr) || StringUtils.isEmpty(verificationCodeStr)) {
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

    /*获取验证码*/
    public void sendMsg(){
        JSONObject phoneObj = new JSONObject();
        phoneObj.put("Cellphone",phone.getText().toString().trim());
        HttpParams params = new HttpParams();
        params.putJsonParams(phoneObj.toJSONString());
        new KJHttp.Builder().url(Config.HOST + "VerificationCode")
                .contentType(KJHttp.ContentType.JSON)
                .params(params)
                .httpMethod(Request.HttpMethod.POST)
                .callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        timer.start();
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        ViewInject.toast(Utils.failMessage(strMsg));
                    }
                }).request();

    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }
        @Override
        public void onFinish() {//计时完毕时触发
            sendMsg.setText(getString(R.string.send_msg));
            sendMsg.setClickable(true);
        }
        @Override
        public void onTick(long millisUntilFinished){//计时过程显示
            sendMsg.setVisibility(View.VISIBLE);
            sendMsg.setClickable(false);
            sendMsg.setText(millisUntilFinished /1000+"s");
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.onFinish();
    }

}
