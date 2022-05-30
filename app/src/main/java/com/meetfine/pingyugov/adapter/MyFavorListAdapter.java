package com.meetfine.pingyugov.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.model.MyFavor;
import com.meetfine.pingyugov.utils.ViewFindUtils;

import java.util.List;

/**
 * Created by Andriod05 on 2017/1/6.
 */
public class MyFavorListAdapter extends ArrayAdapter<MyFavor> {

    private LayoutInflater inflater;
    private boolean deleteShow = false;
    private Context context;
    private DeleteListener listener;

    public MyFavorListAdapter(Context context, List<MyFavor> objects) {
        super(context, 0, objects);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void showDeleteFlag(boolean deleteShow){
        this.deleteShow = deleteShow;
    }
    public void setDeleteListener(DeleteListener listener){
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyFavor item = getItem(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_myfavor_list,parent,false);
        }
        ImageView delete = ViewFindUtils.hold(convertView,R.id.delete);//删除
        ImageView thumb = ViewFindUtils.hold(convertView,R.id.thumb);//封面图片
        TextView subject = ViewFindUtils.hold(convertView,R.id.subject);//标题
        TextView createDate = ViewFindUtils.hold(convertView,R.id.createDate);//日期
        if(deleteShow){
            delete.setVisibility(View.VISIBLE);
        }else{
            delete.setVisibility(View.GONE);
        }
        if(item.getAvator() != null && item.getAvator().length() != 0){
            Glide.with(context).load(item.getAvator()).error(R.drawable.icon_error_pic).into(thumb);
        }
        subject.setText(item.getSubject());
        createDate.setText(item.getMill());

        if(listener != null){
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.doDelete(item, position);
                }
            });
        }
        return convertView;
    }
    public interface DeleteListener{
        void doDelete(MyFavor item, int position);
    }

}
