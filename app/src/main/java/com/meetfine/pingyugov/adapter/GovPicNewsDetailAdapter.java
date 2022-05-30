package com.meetfine.pingyugov.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.utils.ViewFindUtils;
import com.zanlabs.widget.infiniteviewpager.InfinitePagerAdapter;

import java.util.List;

/**
 * Created by Andriod05 on 2016/12/26.
 */
public class GovPicNewsDetailAdapter extends InfinitePagerAdapter {

    private final LayoutInflater mInflater;
    private final Context mContext;

    private List<JSONObject> mList;

    public GovPicNewsDetailAdapter(Context context, List<JSONObject> pagerItems) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mList = pagerItems;
    }
    @Override
    public View getView(int position, View view, ViewGroup container) {
        JSONObject item = mList.get(position);
        if (view == null)
            view = mInflater.inflate(R.layout.item_gov_pic_detail, container, false);
        TextView title = ViewFindUtils.hold(view, R.id.title);
        ImageView image = ViewFindUtils.hold(view, R.id.item_image);
        TextView number = ViewFindUtils.hold(view, R.id.number);

        title.setText(item.getString("title"));
        String str=item.getString("large_thumb");
        if(str == null){
            image.setImageResource(R.drawable.icon_error_pic);
        }else{
            Glide.with(mContext).load("http://file.pingyu.gov.cn//"+str.substring(0,8)+"/"+str).error(R.drawable.icon_error_pic).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(image);
        }

        number.setText(String.format("%d/%d", position+1,mList.size()));
        return view;
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
}
