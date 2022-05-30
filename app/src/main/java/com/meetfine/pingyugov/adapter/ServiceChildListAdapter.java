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

import java.util.List;

/**
 * Created by Andriod05 on 2016/12/8.
 */
public class ServiceChildListAdapter extends ArrayAdapter<JSONObject> {
    private Context context;
    private LayoutInflater inflater;
    private boolean isTable = false;

    public ServiceChildListAdapter(Context context, List<JSONObject> news) {
        super(context,0,news);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ServiceChildListAdapter isServiceTable(boolean isTable){
        this.isTable = isTable;
        return this;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject item = getItem(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_service_child_list,parent,false);
        }
        TextView subject = ViewFindUtils.hold(convertView,R.id.subject);//标题
        TextView description = ViewFindUtils.hold(convertView, R.id.description);//描述
        TextView createDate = ViewFindUtils.hold(convertView,R.id.date);

        if(isTable){
            description.setVisibility(View.GONE);
            createDate.setVisibility(View.GONE);
        }else{
            description.setVisibility(View.VISIBLE);
            createDate.setVisibility(View.VISIBLE);
            String temp;
            if(item.getString("release_date") == null || item.getString("release_date").length() == 0){
                temp = item.getString("create_date");
            }else{
                temp = item.getString("release_date");
            }
            createDate.setText(Config.YEAR.format(Utils.utcToLocal(temp)));
            description.setText(item.getString(""));
        }
        subject.setText(item.getString("title"));
        return convertView;
    }

}
