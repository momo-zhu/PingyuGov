package com.meetfine.pingyugov.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.utils.Config;
import com.meetfine.pingyugov.utils.Utils;
import com.meetfine.pingyugov.utils.ViewFindUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Andriod05 on 2016/12/14.
 */
public class InteractionListAdapter extends ArrayAdapter<JSONObject> {
    private LayoutInflater inflater;
    private StringBuilder sb = new StringBuilder();

    public InteractionListAdapter(Context context, List<JSONObject> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject item = getItem(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_interaction_list,parent,false);
        }
        TextView description = ViewFindUtils.hold(convertView, R.id.description);//标题
        TextView createDate = ViewFindUtils.hold(convertView,R.id.createDate);//日期
        TextView state = ViewFindUtils.hold(convertView,R.id.state);//状态

        description.setText(item.getString("subject"));
        String temp = item.getString("no");
        createDate.setText(temp.substring(4,6)+"-"+temp.substring(6,8));
        int process_status = item.getIntValue("process_status");
        sb.delete(0,sb.length());
        int color;
//        if(process_status<Config.ThreadStatus.length){
//            sb.append("[").append(Config.ThreadStatus[process_status]).append("]");
//            color = Color.parseColor(Config.StatusColor[process_status]);
//        }else{
//            sb.append("[").append(Config.ThreadStatus[0]).append("]");
//            color = Color.parseColor(Config.StatusColor[0]);
//        }
        sb.append("[").append(Config.ThreadStatus[process_status]).append("]");
        color = Color.parseColor(Config.StatusColor[process_status]);
        state.setText(sb.toString());
        state.setTextColor(color);

        return convertView;
    }
}
