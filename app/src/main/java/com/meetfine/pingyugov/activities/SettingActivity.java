package com.meetfine.pingyugov.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.bases.BaseActivity;
import com.meetfine.pingyugov.model.DataCleanManager;
import com.meetfine.pingyugov.utils.Config;
import com.meetfine.pingyugov.utils.Utils;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.SystemTool;

import cn.pedant.SweetAlert.ProgressHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Andriod05 on 2017/1/16.
 */
public class SettingActivity extends BaseActivity {
    @BindView(id = R.id.cleanUp, click = true)
    private RelativeLayout cleanUp;//清空缓存
    @BindView(id = R.id.update, click = true)
    private RelativeLayout update;//检查更新
    @BindView(id = R.id.currentVersion)
    private TextView currentVersion;//当前版本
    @BindView(id = R.id.clean_text)
    private TextView clean_text;//缓存量
    @BindView(id = R.id.point)
    private ImageView point;//点

    private KJActivity aty;
    private long totalCacheSize = 0;
    private Thread cleanThread;
    private AlertDialog requestDialog;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void initData() {
        super.initData();
        aty = this;
        title_tv.setText("设置");
        try {
            clean_text.setText(DataCleanManager.getTotalCacheSize(aty));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initWidget() {
        super.initWidget();
        currentVersion.setText(getVersion());
        if (HomeActivity.hasNewVersion) point.setVisibility(View.VISIBLE);
        else point.setVisibility(View.GONE);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.cleanUp://清空缓存
                doClean();
                break;
            case R.id.update://检查更新
                doUpdate();
                break;
        }
    }

    /*清空缓存*/
    public void doClean() {
        String totalSizeStr;
        try {
            totalCacheSize = DataCleanManager.getTotalSizeLong(this);
            totalSizeStr = DataCleanManager.getTotalCacheSize(this);

        } catch (Exception e) {
            e.printStackTrace();
            ViewInject.toast("数据获取失败");
            return;
        }
        if (0 == totalCacheSize || "0K".equals(totalSizeStr)) {
            ViewInject.toast("没有多余的缓存哦!");
            return;
        }

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .showContentText(true)
                .showTitle(false)
                .setContentText("当前缓存大小为" + totalSizeStr + ",是否确认清空?")
                .showCancelButton(true)
                .showConfirmButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE)
                                .setCancel(false)
                                .setContentText("正在清除缓存，请稍后...");
                        cleanThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DataCleanManager.clearAllCache(aty);
                            }
                        });
                        cleanThread.start();
                        setProgress(sweetAlertDialog);
                    }
                }).show();
    }

    /*设置进度条*/
    public void setProgress(final SweetAlertDialog sweetAlertDialog) {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long cacheSize = 0;
                try {
                    cacheSize = DataCleanManager.getTotalSizeLong(aty);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (cacheSize == 0) {
                    cleanThread.interrupt();
                    clean_text.setText("0k");
                    sweetAlertDialog.dismissWithAnimation();
                    return;
                }
                setCurrentProgress(cacheSize, sweetAlertDialog.getProgressHelper());
                handler.postDelayed(this, 100);
            }
        };
        handler.postDelayed(runnable, 100);
    }

    /*设置当前进度*/
    public void setCurrentProgress(long cacheSize, ProgressHelper helper) {
        float currentProgress = cacheSize % totalCacheSize + cacheSize / totalCacheSize;
        helper.setProgress(currentProgress);
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            StringBuilder sb = new StringBuilder();
            sb.append("当前版本为：v").append(version);
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "未获取到版本信息";
        }
    }

    /*执行更新操作*/
    public void doUpdate() {
        if (!Utils.checkPermission(aty, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(aty, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Config.REQUEST_PERMISSION);
        } else
            checkVersion();
    }

    /*检查更新*/
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
                    new SweetAlertDialog(aty, SweetAlertDialog.WARNING_TYPE).setTitleText("发现新版本" + versionName)
                            .setContentText("发布日期:" + releaseDate)
                            .setConfirmText("下载")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                    Utils.downloadFile(aty, Config.DOWNLOAD_URL, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + appName, -1, appName);
                                }
                            }).showCancelButton(true).
                            setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                }
                            }).show();
                } else {
                    if (HomeActivity.hasNewVersion) {
                        HomeActivity.hasNewVersion = false;
                        point.setVisibility(View.GONE);
                    }
                    new SweetAlertDialog(aty).showContentText(false).setTitleText("已是最新版本").show();
                }
            }
        }).request();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Config.REQUEST_PERMISSION_STORAGE:
                Utils.dealPermissionOpen(aty, requestDialog, getString(R.string.no_storage_permission), getString(R.string.please_open_storage_permission), requestCode);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        requestDialog = new AlertDialog.Builder(aty).create();
        Utils.dealPermissionRequest(aty, requestDialog, requestCode, permissions, grantResults);
    }
}
