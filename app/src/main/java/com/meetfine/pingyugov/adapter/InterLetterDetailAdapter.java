package com.meetfine.pingyugov.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.model.BranchModel;
import com.meetfine.pingyugov.utils.Config;
import com.meetfine.pingyugov.utils.Utils;
import com.meetfine.pingyugov.utils.ViewFindUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andriod05 on 2016/12/30.
 */
public class InterLetterDetailAdapter extends BaseAdapter {
    private Activity aty;
    private List<JSONObject> replies;
    private LayoutInflater inflater;
    private JSONObject contentObj;
    private ArrayList<BranchModel> departments;
    private String replyUser;

    public InterLetterDetailAdapter(Activity aty){
        this.aty = aty;
        inflater = aty.getLayoutInflater();
    }
    public void setContentData(JSONObject contentObj){
        this.contentObj = contentObj;
    }
    public void setReplies(List<JSONObject> replies){
        this.replies = replies;
    }
    public void setReplyUser(String replyUser){
        this.replyUser = replyUser;
    }

    @Override
    public int getCount() {
        if(contentObj == null) return 0;
        if(replies.isEmpty()) return 1;
        return replies.size() + 2;
    }

    @Override
    public Object getItem(int position) {
        if(position == 0) return contentObj;
        if(position == 1) return null;
        return replies.get(position - 2);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return 0;
        if(position == 1) return 1;
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject item = (JSONObject) getItem(position);
        if(getItemViewType(position) == 0){//????????????
            if(convertView == null){
                convertView = inflater.inflate(R.layout.item_inter_letter_detail_content, parent,false);
            }
            TextView subject = ViewFindUtils.hold(convertView,R.id.subject);//????????????
            TextView createDate = ViewFindUtils.hold(convertView, R.id.createDate);//????????????
            TextView type = ViewFindUtils.hold(convertView, R.id.type);//????????????
            TextView createUser = ViewFindUtils.hold(convertView, R.id.createUser);//???????????????
            TextView department = ViewFindUtils.hold(convertView, R.id.department);//????????????
            TextView status = ViewFindUtils.hold(convertView, R.id.status);//??????????????????
            TextView content = ViewFindUtils.hold(convertView, R.id.content);//????????????

            String subjectStr = item.getString("subject");
            String createDateStr = Utils.TimeStamp2Date(item.getString("create_date"), "yyyy-MM-dd HH:mm");
            String typeStr = "????????????:??????";
            StringBuilder sbType = new StringBuilder();
            if (item.getString("Question")!=null){
                typeStr = sbType.append("????????????:").append(item.getString("Question")).toString();
            }
            String tempName = item.getString("name");
            String departmentStr = "????????????:??????";
            int process_status = item.getIntValue("process_status");
            if(process_status > Config.ThreadStatus.length || process_status < 0){
                process_status = 0;
            }
            StringBuilder sbStatus = new StringBuilder();
            sbStatus.append("[").append(Config.ThreadStatus[process_status]).append("]");
            String contentStr = item.getString("message");

            subject.setText(subjectStr);
            createDate.setText(createDateStr);
            type.setText(typeStr);
            createUser.setText(tempName);
            department.setText(departmentStr);
            status.setText(sbStatus.toString());
            status.setTextColor(Color.parseColor(Config.StatusColor[process_status]));
            content.setText(contentStr);

        }else if(getItemViewType(position) == 1){
            if (convertView == null)
                convertView = inflater.inflate(R.layout.list_divider, parent, false);
            ((TextView) convertView).setText("??????");
        }else{//??????
            if(convertView == null){
                convertView = inflater.inflate(R.layout.item_inter_letter_detail_reply, parent,false);
            }
            TextView createDate = ViewFindUtils.hold(convertView, R.id.createDate);//????????????
            TextView department = ViewFindUtils.hold(convertView, R.id.department);//????????????
            TextView reply_content = ViewFindUtils.hold(convertView, R.id.reply_content);//????????????

            String createDateReply = Utils.TimeStamp2Date(String.valueOf(item.getLongValue("create_date")), "yyyy-MM-dd HH:mm");
            if(replyUser == null){
                replyUser = "??????";
            }
            String replyContentStr = item.getString("message");

            createDate.setText(createDateReply);
            department.setText(replyUser);
            reply_content.setText(replyContentStr);
        }

        return convertView;
    }
}
