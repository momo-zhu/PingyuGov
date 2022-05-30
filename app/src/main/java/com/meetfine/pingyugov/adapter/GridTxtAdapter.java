package com.meetfine.pingyugov.adapter;

import android.content.Context;
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
 * Created by Andriod05 on 2016/12/15.
 */
public class GridTxtAdapter extends ArrayAdapter<JSONObject> {

    private LayoutInflater inflater;

    public GridTxtAdapter(Context context, List<JSONObject> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject item = getItem(position);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_enjoyhs_grid_txt,parent,false);
        }
        TextView title = ViewFindUtils.hold(convertView, R.id.title);
        title.setText(item.getString("name"));

        return convertView;
    }
}
