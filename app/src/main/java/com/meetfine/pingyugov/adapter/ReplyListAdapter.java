package com.meetfine.pingyugov.adapter;

import android.content.Context;
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
 * Created by Andriod05 on 2016/12/8.
 */
public class ReplyListAdapter extends ArrayAdapter<JSONObject> {
    private Context context;
    private LayoutInflater inflater;

    public ReplyListAdapter(Context context, List<JSONObject> news) {
        super(context,0,news);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject item = getItem(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_reply_list,parent,false);
        }
        TextView name = ViewFindUtils.hold(convertView,R.id.name);
        TextView date = ViewFindUtils.hold(convertView,R.id.date);
        TextView content = ViewFindUtils.hold(convertView,R.id.content);

        StringBuilder sbName=new StringBuilder();
        StringBuilder sbDate=new StringBuilder();
        sbName.append("网友：").append(item.getString("name"));
        String temp = item.getString("create_date");
        sbDate.append("留言时间：").append(Config.YEAR.format(new Date(Long.parseLong(temp)*1000)));
        date.setText(sbDate);
        name.setText(sbName);
        String contentStr = Utils.delHTMLTag(item.getString("body"));
        content.setText(contentStr);
        return convertView;
    }

}
