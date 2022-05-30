package com.meetfine.pingyugov.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.utils.ViewFindUtils;

import java.util.List;

/**
 * Created by Admin on 2017/4/1.
 */

public class ServiceParentListAdapter extends ArrayAdapter<JSONObject>{
    private LayoutInflater inflater;

    public ServiceParentListAdapter(Context context, List<JSONObject> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject item = getItem(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_service_parent_list, parent, false);
        }
        TextView subject = ViewFindUtils.hold(convertView, R.id.subject);//标题

        subject.setText(item.getString("title"));

        return convertView;
    }
}
