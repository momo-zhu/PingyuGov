package com.meetfine.pingyugov.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.utils.ViewFindUtils;

import org.kymjs.kjframe.utils.StringUtils;

import java.util.List;

/**
 * Created by Andriod05 on 2016/12/21.
 */
public class InterOnlineAdapter extends ArrayAdapter<JSONObject> {
    private LayoutInflater inflater;
    private Context context;
    private InterOnlineListener listener;

    public InterOnlineAdapter(Context context, List<JSONObject> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final JSONObject item = getItem(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_inter_online,parent,false);
        }
        ImageView thumb = ViewFindUtils.hold(convertView, R.id.thumb);//图片
        TextView subject = ViewFindUtils.hold(convertView, R.id.subject);//标题
        TextView createDate = ViewFindUtils.hold(convertView, R.id.createDate);//访谈日期
        TextView guests = ViewFindUtils.hold(convertView, R.id.guests);//访谈嘉宾
//        TextView manager = ViewFindUtils.hold(convertView,R.id.manager);//主持人
        TextView inter_photo = ViewFindUtils.hold(convertView, R.id.inter_photo);//访谈图片
        TextView inter_txt = ViewFindUtils.hold(convertView, R.id.inter_txt);//访谈正文

        String imageUrl = item.getString("photo");
        if (!StringUtils.isEmpty(imageUrl)) {
            Glide.with(context).load(imageUrl).error(R.drawable.icon_error_pic).into(thumb);
        }
        subject.setText(item.getString("title"));
        createDate.setText(item.getString("time"));
        guests.setText(item.getString("guests")==null ? "保密" : item.getString("guests"));

        if(listener != null){
            thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.viewPhoto(item);
                }
            });
            subject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.viewTxt(item);
                }
            });
            inter_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.viewPhoto(item);
                }
            });
            inter_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.viewTxt(item);
                }
            });
        }
        return convertView;
    }

    public void setOnlineListener(InterOnlineListener listener){
        this.listener =listener;
    }

    public interface InterOnlineListener{
        void viewPhoto(JSONObject item);
        void viewTxt(JSONObject item);
    }
}
