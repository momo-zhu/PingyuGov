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
public class TxtInfoListAdapter extends ArrayAdapter<JSONObject> {
    private Context context;
    private LayoutInflater inflater;

    public TxtInfoListAdapter(Context context, List<JSONObject> news) {
        super(context,0,news);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject item = getItem(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_txt_info_list,parent,false);
        }
        TextView subject = ViewFindUtils.hold(convertView,R.id.subject);
        TextView createDate = ViewFindUtils.hold(convertView,R.id.createDate);
        String temp;
        if (item.containsKey("time")){
            createDate.setText(item.getString("time"));
        }else {
            if (item.getString("release_date") == null || item.getString("release_date").length() == 0) {
                temp = item.getString("create_date");
            } else {
                temp = item.getString("release_date");
            }
            createDate.setText(Config.YEAR.format(new Date(Long.parseLong(temp) * 1000)));
        }
        subject.setText(Utils.delHTMLTag(item.getString("title")));
        return convertView;
    }

}
