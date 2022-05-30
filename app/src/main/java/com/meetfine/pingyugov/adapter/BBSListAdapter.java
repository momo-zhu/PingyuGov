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
import com.meetfine.pingyugov.utils.Utils;
import com.meetfine.pingyugov.utils.ViewFindUtils;

import org.kymjs.kjframe.utils.StringUtils;

import java.util.List;

/**
 * Created by Andriod05 on 2016/12/21.
 */
public class BBSListAdapter extends ArrayAdapter<JSONObject> {
    private LayoutInflater inflater;
    private Context context;
    private JSONObject groupDir;

    public BBSListAdapter(Context context, List<JSONObject> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }
    public void setGroupDir(JSONObject groupDir){
        this.groupDir = groupDir;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final JSONObject item = getItem(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_bbs_list,parent,false);
        }
        ImageView thumb = ViewFindUtils.hold(convertView, R.id.thumb);//图片
        TextView subject = ViewFindUtils.hold(convertView, R.id.subject);//标题
        TextView description = ViewFindUtils.hold(convertView, R.id.description);//简述
        TextView name = ViewFindUtils.hold(convertView, R.id.name);//姓名
        TextView typeName = ViewFindUtils.hold(convertView, R.id.typeName);//平台名称
        TextView views = ViewFindUtils.hold(convertView, R.id.views);//浏览量

        String imageUrl = item.getString("image_url");
        if (!StringUtils.isEmpty(imageUrl)) {
            Glide.with(context).load(imageUrl).error(R.drawable.icon_bbs_def).into(thumb);
        }else{
            thumb.setImageResource(R.drawable.icon_bbs_def);
        }
        subject.setText(item.getString("title"));
        name.setText(item.getString("creator"));
        views.setText((item.getString("views")));
        String desTemp = item.getString("body");
        if(!StringUtils.isEmpty(desTemp)){
            description.setText(Utils.delHTMLTag(desTemp));
        }
        if(groupDir != null){
            String groupName = groupDir.getString(item.getString("group_id"));
            if(groupName == null) groupName = "";
            typeName.setText(groupName);
        }

        return convertView;
    }

}
