package com.meetfine.pingyugov.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.application.CustomApplication;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Andriod05 on 2016/12/15.
 */
public class Utils {

    public static String TimeStamp2Date(String timestampString, String formats) {
        if (StringUtils.isEmpty(formats))
            formats = "yyyy-MM-dd";
        String date;
        if(timestampString == null){
            date = new SimpleDateFormat(formats, Locale.getDefault()).format(new Date());
        }else{
            Long timestamp = Long.parseLong(timestampString) * 1000;
            date = new SimpleDateFormat(formats, Locale.getDefault()).format(new Date(timestamp));
        }
        return date;
    }

    public static long StrTime2Long(String mill, long currentTime){
        long timeMill = 0;
        if(StringUtils.isEmpty(mill)){
            timeMill = currentTime;
        }else{
            Long timestamp = Long.parseLong(mill) * 1000;
            timeMill = timestamp.longValue();
        }
        return timeMill;
    }

    public static String getWebViewData(String content) {
        return "<html><head lang='zh-CN'><meta charset='UTF-8'><title></title><style>" +
                "body{padding:0px;font-size:16px;font-style:normal;font-weight:normal;" +
                "font-variant:normal;color:#666666;text-decoration:none;line-height:1.5;" +
                "margin:6px;}.fullimg{width:100%;-moz-border-radius:5px;border-radius:5px;}" +
                "body p{text-indent: 2em;}"+"body p > img {margin-left: -2em;}"+
                "</style></head><body>"+content + "</body></html>";
    }

    /*
   设置Listview高度
    */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();

        int totalHeight = 0;
        if (listAdapter != null) {
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            totalHeight += listView.getDividerHeight() * (listAdapter.getCount() - 1);
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }

    public static String failMessage(String msg) {
        if (StringUtils.isEmpty(msg)) return "";
        try {
            JSONObject object = JSON.parseObject(msg);
            String mes = object.getString("Message");
            if(mes == null) mes = object.getString("message");
            return mes == null ? msg : mes;
        } catch (JSONException e) {
            return msg;
        }
    }


    /*保存图片至相册*/
    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "pingyugovImg");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
            Toast.makeText(context,"二维码保存成功！",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
    }
    public static void downloadFile(final Activity acy, final String url, final String storeFilePath, final long fileSize, final String fileName) {
        CustomApplication app = (CustomApplication) acy.getApplication();
        final File file = new File(storeFilePath);
        if (file.exists() && file.length() == fileSize) {
            new SweetAlertDialog(acy, SweetAlertDialog.SUCCESS_TYPE).
                    setTitleText("已下载").
                    setContentText("路径:"+ storeFilePath).
                    showCancelButton(true).
                    setCancelText("取消").
                    setConfirmText("打开").
                    setCancelClickListener(null).
                    setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            DisplayUtils.openWithOtherApp(acy, new File(storeFilePath));
                        }
                    }).show();
            return;
        }
        final KJHttp kjHttp = new KJHttp();
        View view = View.inflate(acy, R.layout.download, null);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        final SweetAlertDialog dialog = new SweetAlertDialog(acy, SweetAlertDialog.CUSTOM_TYPE).setCustomView(view)
                .setTitleText(fileName).setContentText("0B").setCancelClickListener(
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                kjHttp.getDownloadController(storeFilePath, url).removeTask();
                            }
                        }).showCancelButton(true).showConfirmButton(false);
        /*kjHttp.download(storeFilePath, url, headers, new HttpCallBack() {*/
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "");
        kjHttp.download(storeFilePath, url, headers, new HttpCallBack() {
            @Override
            public void onPreStart() {
                progressBar.setMax(100);
                progressBar.setProgress(0);
                dialog.show();
            }

            @Override
            public void onSuccess(Map<String, String> headers, byte[] t) {
                dialog.setTitleText("下载完成").
                        setContentText("下载路径:" + storeFilePath).
                        setConfirmText("打开").
                        setCancelClickListener(null).
                        setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                dialog.dismissWithAnimation();
                                DisplayUtils.openWithOtherApp(acy, new File(storeFilePath));
                            }
                        }).changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                        .showCancelButton(true);

            }


            @Override
            public void onFailure(int errorNo, final String strMsg) {
                acy.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.setTitleText("下载失败")
                                .setContentText(strMsg)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                });
            }


            @Override
            public void onLoading(final long count, final long current) {
                acy.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.setContentText(Utils.getFileLength(current));
                        progressBar.setProgress((int) (current * 100 / count));
                    }
                });
            }
        });
    }

    /*
  clickspan
   */
    public static ClickableSpan getDownLoadClickSpan(final Activity acy, final String storeFilePath, final String fileName, final long fileSize, final String url) {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                downloadFile(acy, url, storeFilePath, fileSize, fileName);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.rgb(13, 140, 209));
                ds.setUnderlineText(false);
            }
        };
    }

    public static String getFileLength(long length) {
        if (length < 1024) return length + "B";
        if (length < 1024 * 1024) return String.format(Locale.CHINA, "%.1fKB", length / 1024f);
        if (length < 1024 * 1024 * 1024)
            return String.format(Locale.CHINA, "%.1fMB", length / (1024 * 1024f));
        return String.format(Locale.CHINA, "%.1fGB", length / (1024 * 1024 * 1024f));
    }

    /*以UTF-8获取字符串*/
    public static String string2UTF8(String str){
        String temp = "";
        try {
            temp = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return temp;

    }
    /*UTC格式日期转为本地日期*/
    public static Date utcToLocal(String utc) {
        if (utc == null || utc.length() < 19) return new Date();
        try {
            return Config.UTC_FORMAT.parse(utc.substring(0, 19));
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static boolean isEventInView(MotionEvent event, View view) {
        if (view != null) {
            int[] leftTop = {0, 0};
            view.getLocationOnScreen(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            return event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom;
        }
        return false;
    }
    public static void setListViewHeightBasedOnChildren(GridView listView) {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = listView.getNumColumns();// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
//        // 设置margin
//        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        listView.setLayoutParams(params);
    }

    /*检查是否获得权限*/
    public static boolean checkPermission(Activity aty, String permission){
        if (ContextCompat.checkSelfPermission(aty, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(aty, new String[]{permission},
                    Config.REQUEST_PERMISSION);
            return false;
        }
        return true;
    }
    /*处理权限检查结果*/
    public static void dealPermissionRequest(Activity activity, AlertDialog requestDialog, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        if (requestCode == Config.REQUEST_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = activity.shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        switch (permissions[0]) {
                            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                                showDialogTipUserGoToAppSettting(activity, requestDialog, activity.getString(R.string.no_storage_permission),
                                        activity.getString(R.string.please_open_storage_permission), Config.REQUEST_PERMISSION_STORAGE);
                                break;
                            case Manifest.permission.CAMERA:
                                showDialogTipUserGoToAppSettting(activity, requestDialog, activity.getString(R.string.no_camera_permission),
                                        activity.getString(R.string.please_open_camera_permission), Config.REQUEST_PERMISSION_CAMERA);
                                break;
                            case Manifest.permission.RECORD_AUDIO:
                                showDialogTipUserGoToAppSettting(activity, requestDialog, activity.getString(R.string.no_audio_permission),
                                        activity.getString(R.string.please_open_audio_permission), Config.REQUEST_PERMISSION_AUDIO);
                                break;

                        }
                    } else
                        ViewInject.toast(activity.getString(R.string.can_not_use_this_operation));
                } else {
                    Toast.makeText(activity, activity.getString(R.string.permission_get_successful), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    // 提示用户去应用设置界面手动开启权限

    private static void showDialogTipUserGoToAppSettting(final Activity activity, final AlertDialog requestDialog, String title, String message, final int requestCode) {

        requestDialog.setTitle(title);
        requestDialog.setMessage(message);
        requestDialog.getButton(AlertDialog.BUTTON_POSITIVE).setText(activity.getString(R.string.open_now));
        requestDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到应用设置界面
                goToAppSetting(activity,requestCode);
            }
        });
        requestDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setText(activity.getString(R.string.dialog_cancel));
        requestDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog.dismiss();
                return;
            }
        });
        requestDialog.setCancelable(false);
        requestDialog.show();
    }

    // 跳转到当前应用的设置界面
    private static void goToAppSetting(Activity activity, int requsetCode) {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);

        activity.startActivityForResult(intent, requsetCode);
    }

    //处理权限获取结果
    public static void dealPermissionOpen(Activity activity, AlertDialog requestDialog, String subject, String message, int requestCode){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int i = -1;
            switch (requestCode){
                case Config.REQUEST_PERMISSION_STORAGE:
                    i = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    break;
                case Config.REQUEST_PERMISSION_CAMERA:
                    i = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
                    break;
                case Config.REQUEST_PERMISSION_AUDIO:
                    i = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
                    break;
            }
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 提示用户应该去应用设置界面手动开启权限
                showDialogTipUserGoToAppSettting(activity, requestDialog, subject, message, requestCode);
            } else {
                if (requestDialog != null && requestDialog.isShowing()) {
                    requestDialog.dismiss();
                }
                Toast.makeText(activity, activity.getString(R.string.permission_get_successful), Toast.LENGTH_SHORT).show();
            }
        }

    }

    /*清除HTML标签*/
    public static String delHTMLTag(String htmlStr){
        if(StringUtils.isEmpty(htmlStr))
            return "";
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式
        String regEx_special = "\\&[a-zA-Z]{1,10};"; // 定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(htmlStr);
        htmlStr=m_script.replaceAll(""); //过滤script标签

        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(htmlStr);
        htmlStr=m_style.replaceAll(""); //过滤style标签

        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(htmlStr);
        htmlStr=m_html.replaceAll(""); //过滤html标签

        Pattern p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
        Matcher m_special = p_special.matcher(htmlStr);
        htmlStr = m_special.replaceAll(""); // 过滤特殊标签
        return htmlStr.trim(); //返回文本字符串
    }

}
