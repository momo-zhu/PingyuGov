package com.meetfine.pingyugov.bases;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.utils.Config;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.KJFragment;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;


/**
 * Created by Tech07 on 2016/1/26.
 */
public abstract class BaseGridFragment<T> extends KJFragment {
    @BindView(id = R.id.grid_view)
    protected PullToRefreshGridView gridView;
    protected ArrayList<T> mList;
    protected ArrayAdapter<T> adapter;
    private boolean isInitial = true;
    protected KJActivity aty;
    private DynamicBox box;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.pull_to_refresh_grid, container, false);
    }

    @Override
    protected void initData() {
        aty = (KJActivity) getActivity();
        mList = new ArrayList<>();
        adapter = iniAdapter();
    }

    @Override
    protected void initWidget(View parentView) {
        if (gridView == null)
            gridView = iniGridView();
        box = new DynamicBox(aty, gridView);
        View emptyCollectionView = aty.getLayoutInflater().inflate(R.layout.empty_data, null, false);
        box.addCustomView(emptyCollectionView, "empty");
        box.showLoadingLayout();
        box.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                box.showLoadingLayout();
                isInitial = true;
                load(1);
            }
        });
        gridView.setAdapter(adapter);
        gridView.setScrollingWhileRefreshingEnabled(false);
        gridView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {

                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL);
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // Do work to refresh the msgList here.
                isInitial = true;
//                gridView.setMode(PullToRefreshBase.Mode.BOTH);//不允许上拉加载
                load(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                isInitial = false;
                load(mList.size() / 20 + 1);
            }
        });
        load(1);
        gridView.setOnItemClickListener(iniOnItemClickListener());
    }


    private void load(int page) {
        String temp = Config.HOST + getUrl();
        HttpParams params = new HttpParams();
        String url;
        if(temp.contains("?")){
            url = temp + "&page="+page;
        }else {
            url = temp + "?page="+page;
        }
        addParams(params);
        new KJHttp.Builder().url(url).params(params).useCache(false).
                callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        box.hideAll();
                        dealResult(t);
                    }
                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        gridView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        gridView.onRefreshComplete();
                        box.showExceptionLayout();
                    }
                }).request();
    }

    protected void addParams(HttpParams params) {

    }

    private void dealResult(String t) {
        List<T> ts = doSuccess(t);
        gridView.onRefreshComplete();
        if (ts.size() < 20)
            gridView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        if (isInitial) mList.clear();
        mList.addAll(ts);
        adapter.notifyDataSetChanged();
        if (mList.isEmpty())
            box.showCustomView("empty");
    }

    public void refreshListView() {
        if (gridView == null) return;
        gridView.setShowViewWhileRefreshing(false);
        gridView.setRefreshing(false);
        gridView.setShowViewWhileRefreshing(true);
    }

    protected abstract String getUrl();

    protected abstract List<T> doSuccess(String t);


    protected PullToRefreshGridView iniGridView() {
        return null;
    }

    ;

    protected abstract ArrayAdapter<T> iniAdapter();

    protected abstract AdapterView.OnItemClickListener iniOnItemClickListener();
}
