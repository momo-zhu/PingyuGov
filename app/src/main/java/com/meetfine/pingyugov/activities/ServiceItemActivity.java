package com.meetfine.pingyugov.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.adapter.ViewPageAdapter;
import com.meetfine.pingyugov.bases.BaseActivity;

import org.kymjs.kjframe.ui.BindView;

import java.util.ArrayList;

/**
 * Created by Admin on 2017/5/12.
 */

public class ServiceItemActivity extends BaseActivity {
    @BindView(id = R.id.tabs)
    private SlidingTabLayout tabs;
    @BindView(id = R.id.vp)
    private ViewPager mViewPager;

    private String[] mTitles = new String[]{"办事信息", "服务事项"};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String title;
    private int id;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_service_item);
    }

    @Override
    public void initData() {
        super.initData();
        title = getIntent().getStringExtra("title");
        id = getIntent().getIntExtra("ContentId", -1);
        title_tv.setText(title);
//        initTab();
    }

    private void initTab() {
        iniFragment();
        mViewPager.setAdapter(new ViewPageAdapter(getFragmentManager(), fragments, mTitles));
        tabs.setViewPager(mViewPager);
    }

    private void iniFragment(){
        for(int i=0; i<mTitles.length; i++){
            Bundle bundle = new Bundle();
            bundle.putInt("ContentId", id);
            bundle.putInt("Type", i);
//            Fragment fragment = new ServiceChildFragment();
//            fragment.setArguments(bundle);
//            fragments.add(fragment);
        }
    }

}
