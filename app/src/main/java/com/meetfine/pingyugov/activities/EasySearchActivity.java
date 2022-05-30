package com.meetfine.pingyugov.activities;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.adapter.SearchAdapter;
import com.meetfine.pingyugov.application.CustomApplication;
import com.meetfine.pingyugov.utils.Config;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;

/**
 * Created by Admin on 2017/5/11.
 */

public class EasySearchActivity extends KJActivity {
    @BindView(id = R.id.title_back, click = true)
    private TextView title_back;//返回键
    @BindView(id = R.id.search, click = true)
    private TextView search;//搜索键
    @BindView(id = R.id.keyword)
    private EditText keyword;//关键字
    @BindView(id = R.id.listView)
    private PullToRefreshListView listView;//内容

    private CustomApplication app;
    private KJActivity aty;
    private SearchAdapter adapter;
    private List<JSONObject> mList = new ArrayList<>();
    private DynamicBox box;
    private boolean isInitial = true;
    private long oldTime =0;
    private long currentTime = 0;
    private int searchType;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_easy_search);
    }

    @Override
    public void initData() {
        app = (CustomApplication) getApplication();
        aty = this;
        searchType = getIntent().getIntExtra("SearchType", 0);
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
        adapter = new SearchAdapter(this, mList);
        listView.setAdapter(adapter);
        listView.setScrollingWhileRefreshingEnabled(false);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                String label = DateUtils.formatDateTime(aty, System.currentTimeMillis(),
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
        listView.setOnItemClickListener(iniOnItemClickListener());
        box.showCustomView("empty");
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.search:
                currentTime = System.currentTimeMillis();
                if(currentTime - oldTime < 3000){
                    return;
                }else{
                    oldTime = currentTime;
                }
                if(StringUtils.isEmpty(keyword.getText().toString().trim())) {
                    ViewInject.toast("请输入关键字！");
                    return;
                }
                box.showLoadingLayout();
                load(1);
                break;
        }
    }

    /*获取URI*/
    public String getUri(){
        String uri = null;
        switch (searchType){
            case 0:
                uri = "content";//常规信息
                break;
            case 1:
                uri = "openness";//信息公开
                break;
            case 2:
                uri = "service";//办事服务
                break;
            case 3:
                uri = "feedback";//互动交流
                break;
        }
        return uri;
    }

    /*执行搜索*/
    private void load(int page) {
        HttpParams params = new HttpParams();
        String collection = getUri();
        String keyWords = URLEncoder.encode(keyword.getText().toString().trim());
        String url = Config.HOST + "Search?keyword=" + keyWords + "&page="+page + "&collection=" + collection + "&json=true";
        new KJHttp.Builder().url(url).params(params).useCache(false).
                callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        box.hideAll();
                        listView.onRefreshComplete();
                        JSONObject result = JSON.parseObject(t);
                        JSONArray array = result.getJSONArray("items");
                        if (array.size() < 20)
                            listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        if (isInitial) mList.clear();
                        for(int i=0; i<array.size(); i++){
                            mList.add(array.getJSONObject(i));
                        }
                        adapter.notifyDataSetChanged();
                        if(mList.isEmpty())
                            box.showCustomView("empty");

                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        box.hideAll();
                        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        listView.onRefreshComplete();
                        box.showExceptionLayout();
                    }
                }).request();

    }

    /*点击事件*/
    private AdapterView.OnItemClickListener iniOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject item = (JSONObject) parent.getItemAtPosition(position);
                switch (searchType){
                    case 0://常规信息
//                        gotoContentDetail(item, Config.ContentController);
                        break;
                    case 1://信息公开-公开目录
//                        gotoContentDetail(item, Config.opennesscontentController);
                        break;
                    case 2://办事服务
//                        gotoContentDetail(item, Config.ServiceContentController);
                        break;
                    case 3://互动交流-督察督办
//                        gotoContentDetail(item, Config.ContentController);
                        break;
                }


            }
        };
    }

    /*跳转至文本新闻详情页*/
    public void gotoContentDetail(JSONObject item, int contentType){
        Bundle bundle = new Bundle();
        bundle.putString("ContentId", String.valueOf(item.getIntValue("Id")));
        bundle.putInt("ContentType", contentType);//请求控制器
//        showActivity(ContentDetailActivity.class, bundle);
    }

    /*跳转至图片新闻详情页*/
    public void gotoPicDetail(JSONObject item){
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getIntValue("Id"));
//        showActivity(GovPicNewsDetailActivity.class, bundle);
    }

}
