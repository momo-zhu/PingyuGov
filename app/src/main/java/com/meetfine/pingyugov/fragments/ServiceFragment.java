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
import com.meetfine.pingyugov.activities.HomeActivity;
import com.meetfine.pingyugov.adapter.ViewPageAdapter;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.KJFragment;

import java.util.ArrayList;

/**
 * Created by Andriod05 on 2017/1/11.
 */
public class ServiceFragment extends KJFragment {

    @BindView(id = R.id.tabs)
    private SlidingTabLayout tabs;
    @BindView(id = R.id.vp)
    private ViewPager mViewPager;

    private String[] mTitles = new String[]{"重点领域服务", "民生领域服务", "特殊人群", "市民办事", "企业办事"};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private HomeActivity aty;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_public, container, false);
    }

    @Override
    protected void initData() {
        super.initData();
        aty = (HomeActivity) getActivity();
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

    public void iniFragment(){
        fragments.clear();
        for(int i = 0; i<mTitles.length; i++){
            Fragment fragment;
            Bundle bundle = new Bundle();
            bundle.putInt("type",i);
            fragment = new ServiceTxtGridFragment();
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }

    }
}
