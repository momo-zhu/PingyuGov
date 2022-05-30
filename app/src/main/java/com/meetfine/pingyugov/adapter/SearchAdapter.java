package com.meetfine.pingyugov.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.utils.Utils;
import com.meetfine.pingyugov.utils.ViewFindUtils;

import java.util.List;

/**
 * Created by Andriod05 on 2016/12/8.
 */
public class SearchAdapter extends ArrayAdapter<JSONObject> {
    private Context context;
    private LayoutInflater inflater;
    private StringBuilder columnBuilder = new StringBuilder();

    public SearchAdapter(Context context, List<JSONObject> news) {
        super(context,0,news);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject item = getItem(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_search_list,parent,false);
        }
        TextView subject = ViewFindUtils.hold(convertView,R.id.subject);//标题
        TextView description = ViewFindUtils.hold(convertView,R.id.description);//简述
        TextView column = ViewFindUtils.hold(convertView, R.id.column);//所属栏目
        TextView date = ViewFindUtils.hold(convertView, R.id.date);//日期

        String descriptionStr = null;
        if(item.getString("Description") != null)
            descriptionStr = Utils.delHTMLTag(item.getString("Description"));
        String titleStr = null;
        if(item.getString("Title") != null)
            titleStr = Utils.delHTMLTag(item.getString("Title"));
        subject.setText(titleStr);
        description.setText(descriptionStr == null ? titleStr : descriptionStr);
        columnBuilder.delete(0, columnBuilder.length());
        columnBuilder.append("[信息类别]").append(item.getString("Collection") == null ? "未定义" : item.getString("Collection"));
        column.setText(columnBuilder.toString());

        date.setText(item.getString("ReleaseDate"));
        return convertView;
    }

}
