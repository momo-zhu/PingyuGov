package com.meetfine.pingyugov.fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.adapter.ViewPageAdapter;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.KJFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andriod05 on 2017/1/16.
 */
public class InfoPublicWithGovFragment extends KJFragment {
    @BindView(id = R.id.tabs)
    private SegmentTabLayout tabs;
    @BindView(id = R.id.vp)
    private ViewPager mViewPager;

    private String[] mTitles = {"目录", "年报","指南","制度", "依申请公开"};
    private List<Fragment> fragments = new ArrayList<>(mTitles.length);
    private KJActivity aty;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_info_public_gov, container, false);
    }

    @Override
    protected void initData() {
        super.initData();
        aty = (KJActivity) getActivity();
        initTabs();
    }

    private void initTabs() {
        iniFragment();
        tabs.setTabData(mTitles);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            mViewPager.setAdapter(new ViewPageAdapter(getChildFragmentManager(), fragments, mTitles));

        else
            mViewPager.setAdapter(new ViewPageAdapter(getFragmentManager(), fragments, mTitles));

        tabs.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    public void iniFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("branchId", "5417e7499a05c26641f6e0d5");
         /*目录*/
        Fragment fragment1 = new PublicDirectoryFragment();
        fragment1.setArguments(bundle);
        fragments.add(fragment1);

         /*年报*/
        Fragment fragment2 = new PublicAnnualReportFragment();
        fragment2.setArguments(bundle);
        fragments.add(fragment2);

        /*指南*/
        Fragment fragment4 = new ContentDetailFragment();
        fragment4.setArguments(bundle);
        fragments.add(fragment4);
        /*制度*/
        Fragment fragment3 = new PublicRulesFragment();
        fragment3.setArguments(bundle);
        fragments.add(fragment3);
        /*依申请公开*/
        Fragment fragment5 = new PublicRequestFragment();
//        fragment5.setArguments(bundle);
        fragments.add(fragment5);

    }
}
