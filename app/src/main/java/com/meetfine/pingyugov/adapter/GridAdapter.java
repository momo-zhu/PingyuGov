package com.meetfine.pingyugov.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.model.GridItem;
import com.meetfine.pingyugov.utils.ViewFindUtils;

import java.util.List;

/**
 * Created by Andriod05 on 2016/12/15.
 */
public class GridAdapter extends ArrayAdapter<GridItem> {

    private LayoutInflater inflater;

    public GridAdapter(Context context, List<GridItem> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridItem item = getItem(position);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_enjoyhs_grid,parent,false);
        }
        ImageView picture = ViewFindUtils.hold(convertView, R.id.picture);
        TextView title = ViewFindUtils.hold(convertView, R.id.title);
        picture.setImageResource(item.getPicResId());
        title.setText(item.getName());
        return convertView;
    }
}
