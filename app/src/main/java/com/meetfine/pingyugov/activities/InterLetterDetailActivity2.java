package com.meetfine.pingyugov.activities;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.adapter.InterLetterDetailAdapter;
import com.meetfine.pingyugov.application.CustomApplication;
import com.meetfine.pingyugov.bases.BaseActivity;
import com.meetfine.pingyugov.model.User;
import com.meetfine.pingyugov.utils.Config;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;

import java.util.ArrayList;

import mehdi.sakout.dynamicbox.DynamicBox;

/**
 * Created by Andriod05 on 2016/12/30.
 */
public class InterLetterDetailActivity2 extends BaseActivity {
    @BindView(id = R.id.list_view)
    private PullToRefreshListView listView;

    private DynamicBox box;
    private SharedPreferences sp;
    protected ArrayList<JSONObject> replies = new ArrayList<>();
    protected InterLetterDetailAdapter adapter;
    private boolean isInitial = true;
    private String id;
    private KJActivity activity;
    private JSONObject contentObj;
    private CustomApplication app;
    private User user;
    private String subject;
    @Override
    public void setRootView() {
        setContentView(R.layout.activity_inter_letter_detail);
    }

    @Override
    public void initData() {
        super.initData();
        activity =this;
        title_tv.setText("信件详情");
        id = getIntent().getStringExtra("ContentId");
        subject = getIntent().getStringExtra("subject");
        sp = getSharedPreferences("cache_PYGovNew", Context.MODE_PRIVATE);
        adapter = new InterLetterDetailAdapter(this);

    }


    @Override
    public void initWidget() {
        super.initWidget();

        /*box = new DynamicBox(this,listView);
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
                load(replies.size() / 20 + 1);
            }
        });*/
        load();
    }

    private void load() {
        final String url = Config.HOST + getUrl();
        new KJHttp.Builder().url(url).useCache(false).
                callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
//                        dealResult(t);
                        doSuccess(t);
                        sp.edit().putString(url, t).apply();
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        String cache = sp.getString(url, null);
//                        if (cache != null) dealResult(cache);
//                        else {
//                            box.hideAll();
//                            listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//                            listView.onRefreshComplete();
//                            box.showExceptionLayout();
//                        }
                    }
                }).request();
    }
    protected String getUrl() {
        return "Supervision?id="+id;
    }
    protected void doSuccess(String t/*, ArrayList arrayList*/) {
        JSONObject result = JSON.parseObject(t);
//        JSONArray array = result.getJSONArray("Replys");
//        for(int i =0; i<array.size(); i++){
//            arrayList.add(array.getJSONObject(i));
//        }
        contentObj = result.getJSONObject("Supervision");
        contentObj.put("subject",subject);
        contentObj.put("Question",result.getString("Question"));
//        String replyUser = result.getString("ReplyUser");
//        adapter.setReplyUser(replyUser);
        adapter.setContentData(contentObj);
    }
    private void dealResult(String t) {
        box.hideAll();
        ArrayList<JSONObject> ts = new ArrayList<>();
//        doSuccess(t, ts);
        listView.onRefreshComplete();
        if (ts.size() < 20)
            listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        if (isInitial) replies.clear();
        replies.addAll(ts);
        adapter.setReplies(replies);
        adapter.notifyDataSetChanged();
        if(replies.isEmpty())
            box.showCustomView("empty");
    }
}
