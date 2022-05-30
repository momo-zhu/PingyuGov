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
import com.meetfine.pingyugov.utils.ViewFindUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Andriod05 on 2017/1/13.
 */
public class PublicAnnualReportListAdapter extends ArrayAdapter<JSONObject> {
    private LayoutInflater inflater;
    private Context context;

    public PublicAnnualReportListAdapter(Context context, List<JSONObject> objects) {
        super(context, 0, objects);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject item = getItem(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_public_directory_list,parent,false);
        }
        TextView subject = ViewFindUtils.hold(convertView,R.id.subject);
        TextView createDate = ViewFindUtils.hold(convertView,R.id.createDate);
        String temp = item.getString("openness_date");
        createDate.setText(temp);
        subject.setText(item.getString("title"));
        return convertView;

    }

}
