package com.taihe.eggshell.meetinginfo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.meetinginfo.adapter.DiscussAdapter;
import com.taihe.eggshell.meetinginfo.entity.DiscussInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang on 2016/2/17.
 */
public class DiscussListActivity extends BaseActivity{

    private Context mContext;
    private PullToRefreshListView listView;
    private DiscussAdapter discussAdapter;
    private ArrayList<DiscussInfo> discussList;
    private int pagesize = 1;

    @Override
    public void initView() {

        setContentView(R.layout.activity_discuss_list);
        super.initView();

        mContext = this;
        listView = (PullToRefreshListView)findViewById(R.id.id_discuss_list);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ToastUtils.show(mContext,i+"");
            }
        });
    }

    @Override
    public void initData() {
        super.initData();

        initTitle("评论回复");

        discussList = new ArrayList<DiscussInfo>();
        for(int i=0;i<10;i++){
            DiscussInfo info = new DiscussInfo();
            info.setId(i+"");
            info.setDiscuss("扩散到了肯定是"+i);
            info.setAnswerTime("2015-11-12");
            info.setName("王璐"+i+"回复了你");
            discussList.add(info);
        }
        discussAdapter = new DiscussAdapter(mContext,discussList);
        listView.setAdapter(discussAdapter);
        discussAdapter.notifyDataSetChanged();

        getDiscussData();
    }

    private void getDiscussData(){

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                Log.v("LISt:",(String)o);

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                        Log.v("EEE:",new String(volleyError.networkResponse.data));
            }
        };

        Map<String,String> params = new HashMap<String,String>();
        params.put("uid", EggshellApplication.getApplication().getUser().getId()+"");
        params.put("page",pagesize+"");
        Log.v("PARA:",params.toString());
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(),Urls.METHOD_DISCUSS,false,params,true,listener,errorListener);

    }

}
