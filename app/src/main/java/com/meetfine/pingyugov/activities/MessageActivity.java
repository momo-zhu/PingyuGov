package com.meetfine.pingyugov.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.adapter.ViewPageAdapter;
import com.meetfine.pingyugov.bases.BaseActivity;
import com.meetfine.pingyugov.fragments.MessageCreatFragment;
import com.meetfine.pingyugov.fragments.ReminderFragment;

import org.kymjs.kjframe.ui.BindView;

import java.util.ArrayList;

/**
 * Created by Andriod05 on 2017/1/12.
 */
public class MessageActivity extends BaseActivity {
    @BindView(id = R.id.tabs)
    private SlidingTabLayout tabs;
    @BindView(id = R.id.vp)
    private ViewPager mViewPager;

    private String[] mTitles = new String[]{"温馨提示", "我要写信"};
    private String[] titles = new String[]{"咨询", "投诉","举报", "建议"};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private int type;
    @Override
    public void setRootView() {
        setContentView(R.layout.activity_attract_investment);
    }
    @Override
    public void initData() {
        super.initData();
        type=getIntent().getIntExtra("type",0);
        title_tv.setText(titles[type]);
        initTab();
    }
    private void initTab() {
        iniFragment();
        mViewPager.setAdapter(new ViewPageAdapter(getFragmentManager(), fragments, mTitles));
        tabs.setViewPager(mViewPager);
    }

    private void iniFragment(){
        /*温馨提示*/
        Fragment insFragment = new ReminderFragment();
        Bundle bundle = new Bundle();
        fragments.add(insFragment);
         /*我要写信*/
        Fragment messageFragment = new MessageCreatFragment();
        bundle.putInt("type", type);
        messageFragment.setArguments(bundle);
        fragments.add(messageFragment);
    }


}
