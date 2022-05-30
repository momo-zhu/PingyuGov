package com.meetfine.pingyugov.bases;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.application.CustomApplication;
import com.meetfine.pingyugov.utils.Config;
import com.meetfine.pingyugov.utils.ViewFindUtils;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.KJFragment;

import java.util.ArrayList;


/**
 * Created by Tech07 on 2016/1/26.
 */
public abstract class BaseContentListFragment<T> extends KJFragment {
    protected ArrayList<T> mList;
    protected ArrayAdapter<T> adapter;
    protected PullToRefreshListView listView;
    private boolean isInitial = true;
    protected KJActivity acy;
    private SharedPreferences sp;
    private View emptyView;
    private TextView emptyText;
    private CustomApplication app;

    @Override
    protected void initData() {
        acy = (KJActivity) getActivity();
        app = (CustomApplication) acy.getApplication();
        sp = acy.getSharedPreferences("cache_PYGovNew", Context.MODE_PRIVATE);
        mList = new ArrayList<>();
        adapter = iniAdapter();
        listView = iniListView();
        listView.setAdapter(adapter);
        listView.setScrollingWhileRefreshingEnabled(false);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL);
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // Do work to refresh the msgList here.
                isInitial = true;
                listView.setMode(PullToRefreshBase.Mode.BOTH);
                load(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isInitial = false;
                load(mList.size() / 20 + 1);
            }
        });
        emptyView = acy.getLayoutInflater().inflate(R.layout.empty_data, null);
        emptyText = ViewFindUtils.find(emptyView, R.id.title);
        emptyText.setText("加载中...");
        listView.setEmptyView(emptyView);
        load(1);
        listView.setOnItemClickListener(iniOnItemClickListener());
    }


    private void load(int page) {
        String temp = Config.HOST+getUrl();
        final String url;
        if(temp.contains("?")){
            url = temp + "&page="+page;
        }else {
            url = temp + "?page="+page;
        }
        HttpParams params = new HttpParams();
        params.putHeaders("Authorization", app.getAuth());
        new KJHttp.Builder().url(url).params(params).useCache(false).
                callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        dealResult(t);
                        sp.edit().putString(url, t).apply();
                    }

                    @Override
                    public void onFinish() {
                        emptyText.setText("没有数据");
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        String cache = sp.getString(url, null);
                        if (cache != null) dealResult(cache);
                        else {
                            listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            listView.onRefreshComplete();
                        }
                    }
                }).request();
    }

    private void dealResult(String t) {
        ArrayList<T> ts = new ArrayList<>();
        doSucess(t, ts);
        listView.onRefreshComplete();
        if (ts.size() < 20)
            listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        if (isInitial) mList.clear();
        mList.addAll(ts);
        adapter.notifyDataSetChanged();
    }

    public void refreshListView() {
        if (listView == null) return;
        listView.setShowViewWhileRefreshing(false);
        listView.setRefreshing(false);
        listView.setShowViewWhileRefreshing(true);
    }

    protected abstract String getUrl();

    protected abstract void doSucess(String t, ArrayList<T> ts);

    protected abstract PullToRefreshListView iniListView();

    protected abstract ArrayAdapter<T> iniAdapter();

    protected abstract AdapterView.OnItemClickListener iniOnItemClickListener();
}
