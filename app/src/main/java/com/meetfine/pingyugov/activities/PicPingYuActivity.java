package com.meetfine.pingyugov.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.adapter.ViewPageAdapter;
import com.meetfine.pingyugov.bases.BaseActivity;
import com.meetfine.pingyugov.fragments.InfoFragment;

import org.kymjs.kjframe.ui.BindView;

import java.util.ArrayList;

/**
 * Created by MF201701 on 2017/8/10.
 */

public class PicPingYuActivity extends BaseActivity {
    @BindView(id = R.id.tabs)
    private SlidingTabLayout tabs;
    @BindView(id = R.id.vp)
    private ViewPager mViewPager;

    private String[] mTitles = new String[]{"城市风景", "人文记录", "人文故事", "新闻摄影"};
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_attract_investment);
    }

    @Override
    public void initData() {
        super.initData();
        title_tv.setText("图片平舆");
        initTab();
    }

    private void initTab() {
        iniFragment();
        mViewPager.setAdapter(new ViewPageAdapter(getFragmentManager(), fragments, mTitles));
        tabs.setViewPager(mViewPager);
    }

    public void iniFragment() {
        fragments.clear();
        for (int i = 0; i < mTitles.length; i++) {
            Fragment fragment = null;
            Bundle bundle = new Bundle();
            bundle.putInt("type_pic", i);
            bundle.putInt("jump", 4);
            fragment = new InfoFragment();
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
    }
}
