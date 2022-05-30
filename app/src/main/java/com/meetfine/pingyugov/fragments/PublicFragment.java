package com.meetfine.pingyugov.fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.adapter.ViewPageAdapter;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.KJFragment;

import java.util.ArrayList;

/**
 * Created by Andriod05 on 2017/1/11.
 */
public class PublicFragment extends KJFragment {
    @BindView(id = R.id.tabs)
    private SlidingTabLayout tabs;
    @BindView(id = R.id.vp)
    private ViewPager mViewPager;

    private String[] mTitles = new String[]{"县政府", "县直部门", "乡镇政府"};
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_public, container, false);
    }

    @Override
    protected void initData() {
        super.initData();
        initTab();
    }

    private void initTab() {
        iniFragment();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            mViewPager.setAdapter(new ViewPageAdapter(getChildFragmentManager(), fragments, mTitles));
        else
            mViewPager.setAdapter(new ViewPageAdapter(getFragmentManager(), fragments, mTitles));
        tabs.setViewPager(mViewPager);
    }

    public void iniFragment() {
        fragments.clear();
        Fragment fragment = null;
        for (int i = 0; i < mTitles.length; i++) {
            if (i == 0) {
                fragment = new InfoPublicWithGovFragment();
                fragments.add(fragment);
            }
            if (i==1){
                fragment = new PublicDirFragment();
                fragments.add(fragment);
            }
            if (i==2){
                fragment = new PublicDirFragment1();
                fragments.add(fragment);
            }

        }
    }
}
