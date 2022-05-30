package com.meetfine.pingyugov.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.activities.ContentDetailActivity;
import com.meetfine.pingyugov.activities.GovPicNewsDetailActivity;
import com.meetfine.pingyugov.activities.InterOnlineDetailActivity;
import com.meetfine.pingyugov.activities.InterOnlinePicDetailActivity;
import com.meetfine.pingyugov.activities.ReplysDetailActivity;
import com.meetfine.pingyugov.activities.SolicitationDetailActivity;
import com.meetfine.pingyugov.adapter.GovImgInfoListAdapter;
import com.meetfine.pingyugov.adapter.InterOnlineAdapter;
import com.meetfine.pingyugov.adapter.ScrollPagerAdapter;
import com.meetfine.pingyugov.adapter.TxtInfoListAdapter;
import com.meetfine.pingyugov.bases.BaseContentListFragment;
import com.meetfine.pingyugov.utils.Config;
import com.zanlabs.widget.infiniteviewpager.InfiniteViewPager;
import com.zanlabs.widget.infiniteviewpager.indicator.CirclePageIndicator;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.Request;
import org.kymjs.kjframe.ui.BindView;

import java.util.ArrayList;
import java.util.List;

//import com.meetfine.pingyugov.activities.ContentDetailActivity;
//import com.meetfine.pingyugov.activities.GovPicNewsDetailActivity;
//import com.meetfine.pingyugov.activities.VideoDetailActivity;

/**
 * Created by Andriod05 on 2017/1/11.
 */
public class InfoFragment extends BaseContentListFragment<JSONObject>{
    @BindView(id = R.id.viewpager)
    private InfiniteViewPager myPager;
    @BindView(id = R.id.indicator)
    private CirclePageIndicator indicator;
    @BindView(id = R.id.list_view)
    private PullToRefreshListView list_view;

    private int type;//0 平舆要闻，1 政务动态，2 部门动态，3 乡镇动态，/* 4国务院信息，5 省政府信息*/，6 图片新闻，7 通知公告，8 热点导读
    private int type_interaction;//0 县长邮箱,1 部门邮箱,2 平舆论坛, 3 在线访谈,4 民意征集,5 留言反馈
    private int type_attract;//0 招商动态, 1 重点项目, 2 投资环境, 3 优惠政策, 4 项目推介 , 5 企业风采
    private int type_pic;//0 城市风景, 1 人文记录, 2 人文故事, 3 新闻摄影
    private int jump;//1 代表HomeFragment 跳转过来的， 2 代表政民互动跳转过来的
    private List<JSONObject> pagerItems;
    private ScrollPagerAdapter pagerAdapter;
    private SharedPreferences sp;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_info,container,false);
    }

    @Override
    protected void initData() {
        type = getArguments().getInt("type",-1);
        type_interaction = getArguments().getInt("type_interaction",-1);
        type_attract = getArguments().getInt("type_attract",-1);
        type_pic = getArguments().getInt("type_pic",-1);
        jump=getArguments().getInt("jump",-1);
        super.initData();
        sp = acy.getSharedPreferences("cache_PYGovNew", Context.MODE_PRIVATE);
        switch (jump){
            case 1:
                if(type != 0){
                    myPager.setVisibility(View.GONE);
                    indicator.setVisibility(View.GONE);
                }else{
                    myPager.setVisibility(View.VISIBLE);
                    indicator.setVisibility(View.VISIBLE);
                    iniScrollPage();
                }
                break;
            case 2:
                myPager.setVisibility(View.GONE);
                indicator.setVisibility(View.GONE);
                break;
            case 3:
                if(type_attract != 0){
                    myPager.setVisibility(View.GONE);
                    indicator.setVisibility(View.GONE);
                }else{
                    myPager.setVisibility(View.VISIBLE);
                    indicator.setVisibility(View.VISIBLE);
                    iniScrollPage();
                }
                break;
            case 4:
                myPager.setVisibility(View.GONE);
                indicator.setVisibility(View.GONE);
                break;
        }

    }
    /*设置滚动的ViewPager*/
    private void iniScrollPage() {
        pagerItems = new ArrayList<>();
        pagerAdapter = new ScrollPagerAdapter(acy, pagerItems);
        pagerAdapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject item = pagerItems.get(myPager.getCurrentItem());
                if (jump==1){
                    gotoContentDetail(item);
                }
                if (jump==3){
                    gotoSolicitationDetail(item);
                }

            }
        });
        myPager.setAdapter(pagerAdapter);
        myPager.setAutoScrollTime(5000);
        indicator.setViewPager(myPager, 2);
        getScrollData();
    }

    private void getScrollData() {
         String url=null;
        if (jump==1){
            url= Config.HOST+"ContentPictures?channelId=541a95e49a05c2b642f0dfa1&limit=5";
        }
        if (jump==3){
            url= Config.HOST+"ContentSlideShows?channelId=541ab9369a05c2984e994b99";
        }
        final String spUrl = url;
        new KJHttp.Builder().url(url).httpMethod(Request.HttpMethod.GET)
                .useCache(false)
                .callback(new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        dealScrollData(t);
                        sp.edit().putString(spUrl, t).apply();
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        String cache = sp.getString(spUrl, null);
                        if (cache != null) {
                            dealScrollData(cache);
                        }
                    }
                }).request();

    }

    /*处理滚动页面数据*/
    public void dealScrollData(String t) {
        JSONObject result = JSON.parseObject(t);
        JSONArray array = null;
        if (jump==1){
            array = result.getJSONArray("contents");
        }if (jump==3){
            array = result.getJSONArray("contents");
        }
        
        for(int i = 0; i < array.size() && i < 8; i++){
            JSONObject content = array.getJSONObject(i);
            pagerItems.add(content);
        }
        pagerAdapter.notifyDataSetChanged();
        myPager.startAutoScroll();
    }

    @Override
    protected String getUrl() {
        String string=null;
        if (jump==1){
            string=Config.GovInfo[type];
        }else if (jump==2){
            string=Config.GovInfo_Interaction[type_interaction];
        }if (jump==3){
            string=Config.GovInfo_Attract[type_attract];
        }else if (jump==4){
            string=Config.GovInfo_Pic[type_pic];
        }else {

        }
        return string;
    }

    @Override
    protected void doSucess(String t, ArrayList<JSONObject> jsonObjects) {
        JSONObject result = JSON.parseObject(t);
        JSONArray array = null;
        if (jump==1){
            array = result.getJSONArray("contents");
        }else if (jump==2){
            switch (type_interaction){
                case 3://在线访谈
                case 4://民意征集
                    array = result.getJSONArray("contents");
                    break;
                case 5://留言反馈
                    array = result.getJSONArray("siteFeedback");
                    break;
            }
        }else if (jump==3){
            //0 招商动态, 1 重点项目, 2 投资环境, 3 优惠政策, 4 项目推介 , 5 企业风采
            array = result.getJSONArray("contents");
        }else if (jump==4){
            //0 城市风景, 1 人文记录, 2 人文故事, 3 新闻摄影
            array = result.getJSONArray("contents");
        }else {

        }
        for(int i=0; i<array.size(); i++){
            jsonObjects.add(array.getJSONObject(i));
        }

    }

    @Override
    protected PullToRefreshListView iniListView() {
        return list_view;
    }

    @Override
    protected ArrayAdapter<JSONObject> iniAdapter() {
        ArrayAdapter arrayAdapter = null;
        if (jump==1){
            switch (type){
                case 0://平舆要闻
                case 1://政务动态
                case 2://部门动态
                case 3://乡镇动态
                case 5://省政府信息
                    arrayAdapter = new TxtInfoListAdapter(acy, mList);
                    break;
                case 6://图片新闻
                    arrayAdapter = new GovImgInfoListAdapter(acy,mList);
                    break;
                case 7://通知公告
                case 8://热点导读
                    arrayAdapter = new TxtInfoListAdapter(acy, mList);
                    break;
            }
        }else if (jump==2){
            switch (type_interaction){
                case 3://在线访谈
                    arrayAdapter = new InterOnlineAdapter(acy, mList);//在线访谈
                    ((InterOnlineAdapter)arrayAdapter).setOnlineListener(new InterOnlineAdapter.InterOnlineListener() {
                        @Override
                        public void viewPhoto(JSONObject item) {
                            doViewPhoto(item);
                        }

                        @Override
                        public void viewTxt(JSONObject item) {
                            doViewTxt(item);
                        }
                    });
                    break;
                case 4://民意征集
                case 5://留言反馈
                    arrayAdapter = new TxtInfoListAdapter(acy, mList);
                    break;
            }
        }else if (jump==3){
            //0 招商动态, 1 重点项目, 2 投资环境, 3 优惠政策, 4 项目推介 , 5 企业风采
            arrayAdapter = new TxtInfoListAdapter(acy, mList);
        }else if (jump==4){
            //0 城市风景, 1 人文记录, 2 人文故事, 3 新闻摄影
            arrayAdapter = new GovImgInfoListAdapter(acy, mList);
        }else {

        }
        return arrayAdapter;
    }

    private void doViewTxt(JSONObject item) {
        Bundle bundle = new Bundle();
        bundle.putString("id",item.getString("_id"));
        acy.showActivity(InterOnlineDetailActivity.class, bundle);
    }


    private void doViewPhoto(JSONObject item) {
        Bundle bundle = new Bundle();
        bundle.putString("id",item.getString("_id"));
        acy.showActivity(InterOnlinePicDetailActivity.class, bundle);
    }

    @Override
    protected AdapterView.OnItemClickListener iniOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject item = (JSONObject) parent.getItemAtPosition(position);
                if (jump==1){
                    switch (type){
                        case 0://平舆要闻
                        case 1://政务动态
                        case 2://部门动态
                        case 3://部门动态
                        case 5://省政府信息
                            gotoContentDetail(item);
                            break;
                        case 6://图片新闻
                            gotoPicDetail(item);
                            break;
                        case 7://部门动态
                        case 8://通知公告
                            gotoContentDetail(item);
                            break;
                    }
                }else if (jump==2){
                    switch (type_interaction){
                        case 4://民意征集
                            gotoSolicitationDetail(item);
                            break;
                        case 5://留言反馈
                            gotoReplyDetail(item);
                            break;
                    }
                }else if (jump==3){
                    //0 招商动态, 1 重点项目, 2 投资环境, 3 优惠政策, 4 项目推介 , 5 企业风采
                    gotoSolicitationDetail(item);
                }else if (jump==4){
                    //0 城市风景, 1 人文记录, 2 人文故事, 3 新闻摄影
                    gotoPicDetail(item);
                }else {

                }
            }
        };
    }

    private void gotoReplyDetail(JSONObject item) {
        Bundle bundle = new Bundle();
        bundle.putString("id", item.getString("_id"));
        bundle.putString("subject", item.getString("title"));
        acy.showActivity(ReplysDetailActivity.class, bundle);
    }

    private void gotoSolicitationDetail(JSONObject item) {
        Bundle bundle = new Bundle();
        bundle.putString("id", item.getString("_id"));
        bundle.putInt("jump", jump);
        acy.showActivity(SolicitationDetailActivity.class, bundle);
    }

    /*跳转至文本新闻详情页*/
    public void gotoContentDetail(JSONObject item){
        Bundle bundle = new Bundle();
        bundle.putString("ContentId", item.getString("_id"));
        bundle.putInt("ContentType", Config.ContentController);//请求Content控制器
        acy.showActivity(ContentDetailActivity.class, bundle);
    }

    /*跳转至图片新闻详情页*/
    public void gotoPicDetail(JSONObject item){
        Bundle bundle = new Bundle();
        bundle.putString("id", item.getString("_id"));
        bundle.putInt("jump", jump);
        acy.showActivity(GovPicNewsDetailActivity.class, bundle);
    }

    @Override
    public void onPause() {
        super.onPause();
        myPager.stopAutoScroll();
    }

    @Override
    public void onStart() {
        super.onStart();
        myPager.startAutoScroll();
    }

}
