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
 * Created by Andriod05 on 2016/12/15.
 */
public class InteractionFragment extends KJFragment {
    @BindView(id = R.id.tabs)
    private SlidingTabLayout tabs;
    @BindView(id = R.id.vp)
    private ViewPager mViewPager;

    private String[] mTitles = new String[]{"县长信箱","部门信箱", "平舆论坛", "在线访谈", "民意征集","县长热线"};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private HomeActivity aty;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_home, container, false);
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
            Fragment fragment = null;
            Bundle bundle = new Bundle();
            if (i==0) {//县长邮箱
                fragment = new InterDefaultFragment();
                fragment.setArguments(bundle);
            }else if (i==1){//部门信箱
                fragment = new InterDefaultFragment2();
                fragment.setArguments(bundle);
            }else if (i==2){//平舆论坛
                fragment = new BBSListFragment();
                fragment.setArguments(bundle);
            }else {//其他
                bundle.putInt("type_interaction", i);
                bundle.putInt("jump", 2);//代表政民互动的Fragment
                fragment = new InfoFragment();
                fragment.setArguments(bundle);
            }
            fragments.add(fragment);
        }
    }
}
