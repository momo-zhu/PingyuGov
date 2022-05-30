package com.meetfine.pingyugov.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.utils.Config;
import com.meetfine.pingyugov.utils.ViewFindUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Andriod05 on 2016/12/16.
 */
public class GovImgInfoListAdapter extends ArrayAdapter<JSONObject> {
    private Context context;
    private LayoutInflater inflater;

    public GovImgInfoListAdapter(Context context, List<JSONObject> news) {
        super(context, 0, news);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject item = getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gov_img_list, parent, false);
        }
        String imageUrl = item.getString("thumb_name");
        ImageView thumb = ViewFindUtils.hold(convertView, R.id.thumb);
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(context).load(imageUrl).error(R.drawable.icon_error_pic).into(thumb);
        }

        TextView subject = ViewFindUtils.hold(convertView, R.id.subject);
        subject.setText(item.getString("title"));

        TextView description = ViewFindUtils.hold(convertView, R.id.description);
        String descriptionStr = item.getString("description");
        if(descriptionStr == null || descriptionStr.length() ==0){
            description.setText(Config.YEAR.format(new Date(Long.parseLong(item.getString("release_date"))*1000)));
        }else{
            description.setText(item.getString("description"));
        }
        return convertView;
    }
}
