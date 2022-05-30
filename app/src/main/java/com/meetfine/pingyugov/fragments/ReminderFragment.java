package com.meetfine.pingyugov.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.meetfine.pingyugov.R;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.KJFragment;

/**
 * Created by MF201701 on 2017/8/16.
 */

public class ReminderFragment extends KJFragment{
    @BindView(id = R.id.content)
    private WebView content;
    @BindView(id = R.id.waiting)
    private ImageView waiting;//动画
    private String htmlData;
    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_content_url_detail, container, false);
    }
    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);

        htmlData="<div class=\"is-wxtscon\">\n" +
                "                                尊敬的网民朋友：\n" +
                "                                            <p>&nbsp;&nbsp;&nbsp;&nbsp;欢迎您通过网络问政平台给部门和乡镇写信。您在撰写信件时，请遵守国务院《信访条例》、《平舆县人民政府信访条例》以及平舆县人民政府有关信访工作规定。您的来信将按照\"属地管理、分级负责，谁主管、谁负责，依法、及时、就地解决问题的原则\"受理，并按《平舆县政府门户网站网民来信办理工作制度》规定，及时呈报、交办、督办、回复！</p>\n" +
                "                                            <p>&nbsp;&nbsp;&nbsp;&nbsp;对所有来信以受理为原则，不受理为例外。对以下来信，将不予受理： <br>\n" +
                "                                                <strong>一、</strong>无实质性内容，或内容不具体，无法核实的；<br>\n" +
                "                                                <strong>二、</strong>具有商业广告性质或要求提供赞助的；<br>\n" +
                "                                                <strong>三、</strong>纯属个人问题，与政府职责无关的；<br>\n" +
                "                                                <strong>四、</strong>不属政府管理权限范围的；<br>\n" +
                "                                                <strong>五、</strong>已进入仲裁、司法程序的；<br>\n" +
                "                                                <strong>六、</strong>已定性且实行信访终结处理的；<br>\n" +
                "                                                <strong>七、</strong>匿名或其他可以不予受理的。<br>\n" +
                "                                                对来信人的个人信息我们将按有关规定加以保护，来信是否在网站发布，我们将根据您许可状态及来信内容进行判定。</p>\n" +
                "                                            <p><strong>[注意事项]</strong><br>\n" +
                "                                                1、没有按要求正确填写信息的将不予受理；带\"*\"为必填项！<br>\n" +
                "                                                2、来信回复后，请您对答复作出满意度评价。</p>\n" +
                "                            </div>";
        iniWebView();
    }
    /*设置webview*/
    public void iniWebView(){
        WebSettings settings = content.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setDefaultFontSize(36);
        settings.setBuiltInZoomControls(false);// 隐藏缩放按钮
        settings.setJavaScriptEnabled(true);//启用JS
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 排版适应屏幕
        settings.setUseWideViewPort(true);// 可任意比例缩放
        settings.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        content.loadData(htmlData, "text/html; charset=UTF-8", null);
    }
}
