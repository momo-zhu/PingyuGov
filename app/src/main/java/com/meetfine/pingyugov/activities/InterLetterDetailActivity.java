package com.meetfine.pingyugov.activities;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.bases.BaseActivity;
import com.meetfine.pingyugov.utils.Config;
import com.meetfine.pingyugov.utils.Utils;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;

/**
 * Created by Andriod05 on 2016/12/30.
 */
public class InterLetterDetailActivity extends BaseActivity {
    @BindView(id = R.id.subject)
    private TextView subject;
    @BindView(id = R.id.createDate)
    private TextView createDate;
    @BindView(id = R.id.type)
    private TextView type;
    @BindView(id = R.id.createUser)
    private TextView createUser;
    @BindView(id = R.id.department)
    private TextView department;
    @BindView(id = R.id.status)
    private TextView status;
    @BindView(id = R.id.content)
    private TextView content;
    @BindView(id = R.id.waiting)
    private ImageView waiting;
    @BindView(id = R.id.reply_Company)
    private TextView reply_Company;//回复单位
    @BindView(id = R.id.replyDate)
    private TextView replyDate;//回复时间
    @BindView(id = R.id.reply_content)
    private TextView reply_content;//回复内容
    @BindView(id = R.id.rl_reply)
    private RelativeLayout rl_reply;//回复内容布局
    private String id,subjectName;
    private AnimationDrawable drawable;
    private int statusId;
    @Override
    public void setRootView() {
        setContentView(R.layout.item_inter_letter_detail_content);
    }

    @Override
    public void initData() {
        super.initData();
        title_tv.setText("信件详情");
        id=getIntent().getStringExtra("ContentId");
        statusId=getIntent().getIntExtra("status",-1);
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

    private void setDate(String t) {
        JSONObject result= JSON.parseObject(t);
        JSONObject supervision=result.getJSONObject("supervision");
        JSONArray array=result.getJSONArray("replys");
        JSONObject Replys=new JSONObject();
        if (array.size()!=0){
            for (int i = 0; i <array.size() ; i++) {
                Replys=array.getJSONObject(0);
            }
            rl_reply.setVisibility(View.VISIBLE);
            reply_Company.setText(result.getString("replyUser"));
            String replyDateStr = Utils.TimeStamp2Date(Replys.getString("create_date"), "yyyy-MM-dd HH:mm:ss");
            replyDate.setText("回复时间："+replyDateStr);
            reply_content.setText("回复内容："+Replys.getString("message"));
        }

        subject.setText(subjectName);
        String createDateStr = Utils.TimeStamp2Date(supervision.getString("create_date"), "yyyy-MM-dd HH:mm");
        String typeStr = "信件类别:未知";
        StringBuilder sbType = new StringBuilder();
        if (result.getString("Question")!=null){
            typeStr = sbType.append("信件类别:").append(result.getString("Question")).toString();
        }
        String tempName = supervision.getString("name");
        String departmentStr = "督办部门:未知";
        StringBuilder sbStatus = new StringBuilder();
        sbStatus.append("[").append(Config.ThreadStatus[statusId]).append("]");
        String contentStr = supervision.getString("message");
        createDate.setText(createDateStr);
        type.setText(typeStr);
        createUser.setText(tempName.substring(0,1)+"**");
        department.setText(departmentStr);
        status.setText(sbStatus.toString());
        status.setTextColor(Color.parseColor(Config.StatusColor[statusId]));
        content.setText("\u3000\u3000"+contentStr);
    }

    protected String getUrl() {
        return "Supervision?id="+id;
    }

    @Override
    public void initWidget() {
        super.initWidget();

    }
}
