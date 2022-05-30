package com.meetfine.pingyugov.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.activities.DepartmentGuideActivity;
import com.meetfine.pingyugov.activities.DepartmentListActivity;
import com.meetfine.pingyugov.utils.ViewFindUtils;

import java.util.List;

/**
 * Created by Andriod05 on 2016/12/20.
 */
public class PublicDirAdapter extends ArrayAdapter<JSONObject> {

    private LayoutInflater inflater;
    public PublicDirAdapter(Context context, List<JSONObject> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
           final JSONObject item=getItem(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_public_dir_list, parent, false);
            }
            TextView departmentName = ViewFindUtils.hold(convertView, R.id.departmentName);//部门名称
            TextView guide = ViewFindUtils.hold(convertView, R.id.guide);//指南
            TextView directory = ViewFindUtils.hold(convertView, R.id.directory);//目录
            TextView year_report = ViewFindUtils.hold(convertView, R.id.year_report);//年报
            departmentName.setText(item.getString("name"));
                guide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getContext(), DepartmentGuideActivity.class);
                        intent.putExtra("id",item.getString("_id"));
                        getContext().startActivity(intent);
                    }
                });
                directory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getContext(), DepartmentListActivity.class);
                        intent.putExtra("id",item.getString("_id"));
                        intent.putExtra("type",1);//1 代表目录
                        getContext().startActivity(intent);
                    }
                });
                year_report.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getContext(), DepartmentListActivity.class);
                        intent.putExtra("id",item.getString("_id"));
                        intent.putExtra("type",2);//2 代表年报
                        getContext().startActivity(intent);
                    }
                });
        return convertView;
    }
}
