package com.meetfine.pingyugov.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.bases.BaseActivity;
import com.meetfine.pingyugov.fragments.TxtInfoListFragment;

import org.kymjs.kjframe.ui.BindView;

/**
 * Created by Admin on 2017/4/1.
 */

public class ServiceTempListActivity extends BaseActivity {
    @BindView(id = R.id.service_container)
    private FrameLayout service_container;

    private String parentId ;
    private String title;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_service_temp_list);
    }

    @Override
    public void initData() {
        super.initData();
        parentId = getIntent().getStringExtra("parentId");
        title = getIntent().getStringExtra("title");
        title_tv.setText(title);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        TxtInfoListFragment fragment = new TxtInfoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("parentId", parentId);
        bundle.putString("title", title);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.service_container, fragment);
        fragmentTransaction.commit();
    }
}
