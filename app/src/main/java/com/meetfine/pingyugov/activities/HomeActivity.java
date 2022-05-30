package com.meetfine.pingyugov.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.huangzj.slidingmenu.SlidingMenu;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.application.CustomApplication;
import com.meetfine.pingyugov.bases.BaseActivity;
import com.meetfine.pingyugov.fragments.GotoPYFragment;
import com.meetfine.pingyugov.fragments.HomeFragment;
import com.meetfine.pingyugov.fragments.InteractionFragment;
import com.meetfine.pingyugov.fragments.PublicFragment;
import com.meetfine.pingyugov.fragments.ServiceFragment;
import com.meetfine.pingyugov.model.TabEntity;
import com.meetfine.pingyugov.model.User;
import com.meetfine.pingyugov.utils.Config;
import com.meetfine.pingyugov.utils.Utils;
import com.meetfine.pingyugov.utils.ViewFindUtils;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.KJFragment;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.SystemTool;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Andriod05 on 2016/12/14.
 */
public class HomeActivity extends BaseActivity {
    @BindView(id = R.id.container)
    private FrameLayout container;
    @BindView(id = R.id.tabs)
    private CommonTabLayout tabs;
    @BindView(id = R.id.anchor)
    private View anchor;//点
    //侧滑菜单
    @BindView(id = R.id.icon_header)
    private ImageView icon_header;//头像
    @BindView(id = R.id.login, click = true)
    private TextView login;//登录
    @BindView(id = R.id.cancel, click = true)
    private TextView cancel;//注销
    @BindView(id = R.id.slide_operation)
    private LinearLayout slide_operation;//登录和注册布局
    @BindView(id = R.id.register, click = true)
    private TextView register;//注册
    @BindView(id = R.id.collection, click = true)
    private LinearLayout collection;//收藏
    @BindView(id = R.id.setting, click = true)
    private LinearLayout setting;//设置
    @BindView(id = R.id.shang, click = true)
    private LinearLayout shang;//招商引资
//    @BindView(id = R.id.pic_new, click = true)
//    private LinearLayout pic_new;//图片平舆
    @BindView(id = R.id.username)
    private TextView username;//登录的用户名
    @BindView(id = R.id.point)
    private ImageView point;//点

    private SlidingMenu slidingMenu;

    private int[] iconUnselected = {R.drawable.icon_home_def, R.drawable.icon_goto_def, R.drawable.icon_public_def, R.drawable.icon_work_def, R.drawable.icon_chat_def};
    private int[] iconSelected = {R.drawable.icon_home_sel, R.drawable.icon_goto_sel, R.drawable.icon_public_sel, R.drawable.icon_work_sel, R.drawable.icon_chat_sel};
    private String[] bottomTitles = {"首页", "走进平舆", "信息公开", "办事服务", "政民互动"};
    private String[] topTitles = {"首页", "走进平舆", "信息公开", "办事服务", "政民互动"};
    private List<KJFragment> fragments = new ArrayList<>(bottomTitles.length);
    private SweetAlertDialog searchDialog;
    private LayoutInflater inflater;
    private CustomApplication app;
    private User user;
    private boolean isExit;
    public static boolean hasNewVersion = false;
    private PopupWindow window;

    @Override
    public void setRootView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_home);
        initSlidingMenu();
    }

    @Override
    public void initData() {
        super.initData();
        inflater = this.getLayoutInflater();
        app= (CustomApplication) getApplication();
        rl_back.setVisibility(View.GONE);
        title_user.setVisibility(View.VISIBLE);
//        title_search.setVisibility(View.VISIBLE);
        initTabs();
        menuInit();
        if(Utils.checkPermission(aty, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            checkVersion();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = app.getUser();
        if(user == null){
            username.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        }else{
            username.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            slide_operation.setVisibility(View.GONE);
            StringBuilder sbname = new StringBuilder();
            sbname.append("您好! ").append(user.getNickname());
            username.setText(sbname.toString());
            Glide.with(this).load(user.getAvatar()).error(R.drawable.default_head).into(icon_header);
        }
        updatePoint();
    }

    private void menuInit() {
        View menuView = getLayoutInflater().inflate(R.layout.ticket_detail_menu, null);
        final Button consult = ViewFindUtils.find(menuView, R.id.consult);
        Button complain = ViewFindUtils.find(menuView, R.id.complain);
        Button report = ViewFindUtils.find(menuView, R.id.report);
        Button propose = ViewFindUtils.find(menuView, R.id.propose);
        consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this, MessageActivity.class);
                intent.putExtra("type",0);
                showActivity(intent);
                window.dismiss();
            }
        });
        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this, MessageActivity.class);
                intent.putExtra("type",1);
                showActivity(intent);
                window.dismiss();
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this, MessageActivity.class);
                intent.putExtra("type",2);
                showActivity(intent);
                window.dismiss();
            }
        });
        propose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this, MessageActivity.class);
                intent.putExtra("type",3);
                showActivity(intent);
                window.dismiss();
            }
        });
        window = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(getResources().getDrawable(R.color.tabLayout_color));
    }
    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.title_user://侧边栏
                slidingMenu.toggle();
                break;
            case R.id.title_operation2://操作
                window.showAsDropDown(anchor);
                break;
            case R.id.collection://我的收藏
                showActivity(MyFavorListActivity.class);
                break;
            case R.id.shang://招商引资
                showActivity(AttractInvestmentActivity.class);
                break;
            case R.id.setting://设置
                showActivity(SettingActivity.class);
                break;
//            case R.id.pic_new://图片平舆
//                showActivity(PicPingYuActivity.class);
//                break;
            case R.id.login://登录
                showActivity(LoginActivity.class);
                break;
            case R.id.register://注册
                showActivity(RegisterActivity1.class);
                break;
            case R.id.cancel://注销
                app.setUser(null);
                cancel.setVisibility(View.GONE);
                username.setVisibility(View.GONE);
                slide_operation.setVisibility(View.VISIBLE);
                break;
            case R.id.title_search://搜索
                Bundle bundle = new Bundle();
                bundle.putInt("SearchType", tabs.getCurrentTab());
                showActivity(EasySearchActivity.class, bundle);
                break;
        }
    }

    private void initSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setMenu(R.layout.left_menu);
        // 设置触摸屏幕的模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
//        slidingMenu.setShadowDrawable(R.drawable.shadow);
        // 设置滑动菜单视图的宽度
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        slidingMenu.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content9
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */

        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
    }


    private void initTabs() {
        fragments.add(new HomeFragment());//首页
        fragments.add(new GotoPYFragment());//走进平舆
        fragments.add(new PublicFragment());//信息公开
        fragments.add(new ServiceFragment());//办事服务
        fragments.add(new InteractionFragment());//政民互动
        ArrayList<CustomTabEntity> entities = new ArrayList<>(bottomTitles.length);
        for (int i = 0; i < bottomTitles.length; i++)
            entities.add(new TabEntity(bottomTitles[i], iconSelected[i], iconUnselected[i]));
        tabs.setTabData(entities);
        tabs.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                title_tv.setText(topTitles[position]);
                if (position==4) {
                    title_search.setVisibility(View.GONE);
                    title_operation2.setText("操作");
                    title_operation2.setVisibility(View.VISIBLE);
                }else {
                    rl_back.setVisibility(View.GONE);
                    title_operation2.setVisibility(View.GONE);
                    title_user.setVisibility(View.VISIBLE);
//                    title_search.setVisibility(View.VISIBLE);
                }
                changeFragment(R.id.container, fragments.get(position));
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        title_tv.setText(topTitles[0]);
        changeFragment(R.id.container, fragments.get(0));
    }
    /*检查版本*/
    private void checkVersion() {
        new KJHttp.Builder().url(Config.VERSION_CHECK_URL).callback(new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                JSONArray versionArray = JSON.parseArray(t);
                JSONObject version = versionArray.getJSONObject(0);
                int versionCode = version.getIntValue("versionCode");
                String versionName = version.getString("versionName");
                final String appName = version.getString("appName") + ".apk";
                String releaseDate = version.getString("releaseDate");
                if (versionCode > SystemTool.getAppVersionCode(aty)) {
                    new SweetAlertDialog(aty, SweetAlertDialog.WARNING_TYPE).setTitleText("发现新版本"+ versionName)
                            .setContentText("发布日期:"+ releaseDate)
                            .setConfirmText("下载")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                    hasNewVersion = false;
                                    updatePoint();
                                    Utils.downloadFile(aty, Config.DOWNLOAD_URL, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + appName, -1, appName);
                                }
                            }).showCancelButton(false).show();
                } else{
                    hasNewVersion = false;
                    updatePoint();
                }
            }
        }).request();
    }
    public void updatePoint(){
        if(hasNewVersion) point.setVisibility(View.VISIBLE);
        else point.setVisibility(View.GONE);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (slidingMenu.isMenuShowing()) {
                slidingMenu.toggle();
            }else{
                exit();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    public void exit() {
        if (!isExit) {
            isExit = true;
            ViewInject.toast("再按一次退出");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (window.isShowing()) {
            window.dismiss();
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
}
