package com.meetfine.pingyugov.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.activities.EnjoyPYListActivity;
import com.meetfine.pingyugov.adapter.GridAdapter;
import com.meetfine.pingyugov.model.GridItem;
import com.meetfine.pingyugov.utils.Config;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.KJFragment;

/**
 * Created by Andriod05 on 2017/1/11.
 */
public class GotoPYFragment extends KJFragment {
    @BindView(id = R.id.gridView, click = true)
    private GridView gridView;
    private GridAdapter gridAdapter;
    private KJActivity aty;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_enjoyhs_grid, container, false);
    }

    @Override
    protected void initData() {
        super.initData();
        aty = (KJActivity) getActivity();
        iniGridData();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridItem item = (GridItem) parent.getItemAtPosition(position);
                doItemClick(item, position);
            }
        });
    }

    /*设置九宫格数据*/
    public void iniGridData() {
        gridAdapter = new GridAdapter(aty, Config.getEnjoyHSGridList1());
        if (gridAdapter != null) {
            gridView.setAdapter(gridAdapter);
        }


    }

    /*处理点击操作*/
    public void doItemClick(GridItem item, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("channelId", item.getId());
        bundle.putString("title", item.getName());
        bundle.putInt("type", position);
        aty.showActivity(EnjoyPYListActivity.class, bundle);
    }

}
