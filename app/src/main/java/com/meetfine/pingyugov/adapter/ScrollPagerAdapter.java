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
 * Created by RxRead on 2015/9/24.
 */
public class ScrollPagerAdapter extends InfinitePagerAdapter {

    private final LayoutInflater mInflater;
    private final Context mContext;

    private List<JSONObject> mList;
    private View.OnClickListener listener;

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public ScrollPagerAdapter(Context context, List<JSONObject> pagerItems) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mList = pagerItems;
    }


    @Override
    public View getView(int position, View view, ViewGroup container) {
        if (view == null)
            view = mInflater.inflate(R.layout.item_infinite_viewpager, container, false);
        JSONObject item = mList.get(position);
        TextView title = ViewFindUtils.hold(view, R.id.title);
        title.setText(item.getString("title"));//图片标题
        ImageView image = ViewFindUtils.hold(view, R.id.item_image);
        Glide.with(mContext).load(item.getString("thumb_name")).error(R.drawable.icon_error_pic).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(image);//图片地址
        view.setOnClickListener(listener);
        return view;
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
}
