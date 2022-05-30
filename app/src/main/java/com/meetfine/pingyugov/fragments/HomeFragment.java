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
public class HomeFragment extends KJFragment {
    @BindView(id = R.id.tabs)
    private SlidingTabLayout tabs;
    @BindView(id = R.id.vp)
    private ViewPager mViewPager;

    private String[] mTitles = new String[]{"平舆要闻","政务动态", "部门动态", "乡镇动态", "国务院信息","省政府信息", "图片新闻","通知公告", "热点导读"};
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
            if (i==4){
                bundle.putString("LinkUrl", "http://www.gov.cn/pushinfo/v150203/index.htm");
                fragment = new ContentWebUrlDetailFragment();
                fragment.setArguments(bundle);
            }/*else if (i==5){
                bundle.putString("LinkUrl", "http://pingyu.i.my71.com/news");
                fragment = new ContentWebUrlDetailFragment();
                fragment.setArguments(bundle);
            }*/else {
                bundle.putInt("type", i);
                bundle.putInt("jump", 1);//代表首页的Fragment
                fragment = new InfoFragment();
                fragment.setArguments(bundle);
            }
            fragments.add(fragment);
        }
    }
}
