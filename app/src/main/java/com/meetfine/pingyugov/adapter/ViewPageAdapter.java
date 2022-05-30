package com.meetfine.pingyugov.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Tech07 on 2016/1/20.
 */
public class ViewPageAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;
    private String[] titles;

    public ViewPageAdapter(FragmentManager fm, @NonNull List<Fragment> fragments, @NonNull String[] titles) {
        super(fm);
        if (fragments.size() != titles.length)
            throw new IllegalArgumentException("Fragments must have same size with titles");
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
