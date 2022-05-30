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
import com.meetfine.pingyugov.utils.Config;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.SupportActivity;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;

import java.util.ArrayList;

import mehdi.sakout.dynamicbox.DynamicBox;


/**
 * Created by Tech07 on 2016/1/26.
 */
public abstract class BaseListActivity<T> extends SupportActivity {

    @BindView(id = R.id.list_view)
    protected PullToRefreshListView listView;
    @BindView(id = R.id.title_back, click = true)
    protected TextView title_back;
    @BindView(id = R.id.title_operation1, click = true)
    protected TextView title_operation1;
    @BindView(id = R.id.title_operation2, click = true)
    protected TextView title_operation2;
    @BindView(id = R.id.title_tv)
    protected TextView title_tv;

    private DynamicBox box;
    private SharedPreferences sp;


    protected ArrayList<T> mList;
    protected ArrayAdapter<T> adapter;
    private boolean isInitial = true;
    @Override
    public void setRootView() {
        setContentView(R.layout.activity_pull_to_refresh_list);
    }

    @Override
    public void initData() {
        sp = getSharedPreferences("cache_PYGovNew", Context.MODE_PRIVATE);
        mList = new ArrayList<>();
        iniAdapter();
    }

    @Override
    public void initWidget() {
        box = new DynamicBox(this,listView);
        View emptyCollectionView = getLayoutInflater().inflate(R.layout.empty_data, null, false);
        box.addCustomView(emptyCollectionView,"empty");
        box.showLoadingLayout();
        box.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                box.showLoadingLayout();
                isInitial = true;
                load(1);
            }
        });
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setAdapter(adapter);
        listView.setScrollingWhileRefreshingEnabled(false);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                String label = DateUtils.formatDateTime(activity, System.currentTimeMillis(),
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
        load(1);
        listView.setOnItemClickListener(iniOnItemClickListener());
    }

    private void load(int page) {
        String temp = Config.HOST + getUrl();
        HttpParams params = new HttpParams();
        final String url;
        if(temp.contains("?")){
            url = temp + "&page="+page;
        }else {
            url = temp + "?page="+page;
        }
        new KJHttp.Builder().url(url).params(params).useCache(false).
                callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        dealResult(t);
                        sp.edit().putString(url, t).apply();
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        String cache = sp.getString(url, null);
                        if (cache != null) dealResult(cache);
                        else {
                            box.hideAll();
                            listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            listView.onRefreshComplete();
                            box.showExceptionLayout();
                        }
                    }
                }).request();
    }
    private void dealResult(String t) {
        box.hideAll();
        ArrayList<T> ts = new ArrayList<>();
        doSuccess(t, ts);
        listView.onRefreshComplete();
        if (ts.size() < 20)
            listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        if (isInitial) mList.clear();
        mList.addAll(ts);
        adapter.notifyDataSetChanged();
        if(mList.isEmpty())
            box.showCustomView("empty");
    }

    public void refreshListView() {
        if (listView == null) return;
        listView.setShowViewWhileRefreshing(false);
        listView.setRefreshing(false);
        listView.setShowViewWhileRefreshing(true);
    }

    protected void addParams(HttpParams params) {

    }
    protected abstract String getUrl();

    protected abstract void doSuccess(String t, ArrayList<T> ts);

    protected abstract void iniAdapter();

    protected abstract AdapterView.OnItemClickListener iniOnItemClickListener();
}
