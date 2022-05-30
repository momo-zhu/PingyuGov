package com.meetfine.pingyugov.bases;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meetfine.pingyugov.R;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

/**
 * Created by Tech07 on 2016/6/12.
 */
public abstract class BaseActivity extends KJActivity {
    @BindView(id = R.id.rl_back, click = true)
    protected RelativeLayout rl_back;
    @BindView(id = R.id.title_operation1, click = true)
    protected TextView title_operation1;
    @BindView(id = R.id.title_operation2, click = true)
    protected TextView title_operation2;
    @BindView(id = R.id.title_search, click = true)
    protected ImageView title_search;
    @BindView(id = R.id.title_tv)
    protected TextView title_tv;
    @BindView(id = R.id.title_user, click = true)
    protected ImageView title_user;//用户登录

    @Override
    public void initData() {
    }

    @Override
    public void initWidget() {
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
