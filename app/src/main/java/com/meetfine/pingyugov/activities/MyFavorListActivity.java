package com.meetfine.pingyugov.activities;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.adapter.MyFavorListAdapter;
import com.meetfine.pingyugov.bases.BaseActivity;
import com.meetfine.pingyugov.model.MyFavor;
import com.meetfine.pingyugov.utils.Config;

import org.kymjs.kjframe.KJDB;
import org.kymjs.kjframe.ui.BindView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andriod05 on 2017/1/6.
 */
public class MyFavorListActivity extends BaseActivity {
    public static final int ASK_FOR_DETAIL = 1001;
    public static final int NEED_REFRESH = 1002;
    @BindView(id = R.id.list_view)
    private PullToRefreshListView list_view;
    @BindView(id = R.id.empty_layout)
    private RelativeLayout empty_layout;//空数据

    private KJDB favordb;
    private List<MyFavor> myFavors = new ArrayList<>();
    private MyFavorListAdapter adapter;
    private boolean showDelete = false;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_myfavor_list);
    }

    @Override
    public void initData() {
        super.initData();

        title_tv.setText("我的收藏");
        title_operation1.setVisibility(View.VISIBLE);
        title_operation1.setText("删除");
        favordb = KJDB.create(this, Config.favorDBName, true);
        myFavors.addAll(favordb.findAll(MyFavor.class));
        if(myFavors.size() == 0){
            title_operation1.setClickable(false);
            empty_layout.setVisibility(View.VISIBLE);
        }else{
            title_operation1.setClickable(true);
            empty_layout.setVisibility(View.GONE);
        }
        adapter = new MyFavorListAdapter(this, myFavors);
        list_view.setAdapter(adapter);
        list_view.setMode(PullToRefreshBase.Mode.DISABLED);
        list_view.setScrollingWhileRefreshingEnabled(false);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyFavor item = (MyFavor) parent.getItemAtPosition(position);
                doItemClicked(item, position-1);
            }
        });
        list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                myFavors.clear();
                myFavors.addAll(favordb.findAll(MyFavor.class));
                adapter.notifyDataSetChanged();
                refreshView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });

        adapter.setDeleteListener(new MyFavorListAdapter.DeleteListener() {
            @Override
            public void doDelete(MyFavor item, int position) {
                deleteItem(item, position);
            }
        });
    }

    /*处理点击操作*/
    public void doItemClicked(MyFavor item, int position) {
        if (item.getType() == Config.favorTypes[0]) {
            Intent intent = new Intent(this,ContentDetailActivity.class);
            intent.putExtra("ContentId", item.getContentId());
            intent.putExtra("ContentType", item.getContentType());
            intent.putExtra("SiteId", item.getSiteId());
            intent.putExtra("MyFavorPosition", position);
            startActivityForResult(intent, ASK_FOR_DETAIL);

        } else if (item.getType() == Config.favorTypes[1]) {
            Intent intent = new Intent(this,GovPicNewsDetailActivity.class);
            intent.putExtra("id", item.getId());
            intent.putExtra("MyFavorPosition", position);
            intent.putExtra("jump", item.getJump());
            startActivityForResult(intent, ASK_FOR_DETAIL);
        } else if (item.getType() == Config.favorTypes[2]) {
                Intent intent = new Intent(this, ServiceItemDetailActivity.class);
                intent.putExtra("parentId", item.getContentId());
                intent.putExtra("MyFavorPosition", position);
                startActivityForResult(intent, ASK_FOR_DETAIL);
        }else if (item.getType() == Config.favorTypes[3]){
            Intent intent = new Intent(this, SolicitationDetailActivity.class);
            intent.putExtra("id", item.getContentId());
            intent.putExtra("MyFavorPosition", position);
            intent.putExtra("jump", item.getJump());
            startActivityForResult(intent, ASK_FOR_DETAIL);
        }

    }

    /*删除Item*/
    public void deleteItem(MyFavor temp, int position){
        myFavors.remove(position);
        if(myFavors.size() == 0){
            title_operation1.setVisibility(View.GONE);
            showDelete = false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("type = %d",temp.getType()));
        if(temp.getContentId() != null){
            sb.append(String.format(" and ContentId = \"%s\"", temp.getContentId()));
        }
        if(temp.getContentType() != -1){
            sb.append(String.format(" and ContentType = %d", temp.getContentType()));
        }
        if(temp.getSiteId() != null){
            sb.append(String.format(" and SiteId = \"%s\"", temp.getSiteId()));
        }
        if(temp.getId() != null){
            sb.append(String.format(" and id = \"%s\"", temp.getId()));
        }
        favordb.deleteByWhere(MyFavor.class, sb.toString());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.title_operation1:
                if(showDelete){
                    adapter.showDeleteFlag(false);
                    showDelete = false;
                    adapter.notifyDataSetChanged();
                    title_operation1.setText("删除");
                }else{
                    adapter.showDeleteFlag(true);
                    showDelete = true;
                    adapter.notifyDataSetChanged();
                    title_operation1.setText("取消");
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(showDelete){
            adapter.showDeleteFlag(false);
            showDelete = false;
            adapter.notifyDataSetChanged();
            title_operation1.setText("删除");

        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ASK_FOR_DETAIL:
                if(resultCode == NEED_REFRESH){
                    int position = data.getIntExtra("MyFavorPosition", -1);
                    myFavors.remove(position);
                    adapter.notifyDataSetChanged();
                }
                break;
        }

    }
}
