package com.taihe.eggshell.meetinginfo;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.taihe.eggshell.widget.ChoiceDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2016/2/17.
 */
public class DiscussListActivity extends BaseActivity{

    private Context mContext;
    private PullToRefreshListView listView;
    private DiscussAdapter discussAdapter;
    private ChoiceDialog deleteDialog;
    private ArrayList<DiscussInfo> discussList;
    private int pagesize = 1;
    private DiscussInfo deletInfo;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    deletInfo = (DiscussInfo)msg.obj;
                    deleteDialog.show();
                break;
            }
        }
    };

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
                DiscussInfo info = discussList.get(i-1);
                if(info.getIsread().equals("1")){//未阅读
                    readRequest(info);
                }else{
                    Intent intent = new Intent(mContext,InfoDetailActivity.class);
                    intent.putExtra("playId",info.getAid());
                    startActivity(intent);
                }
            }
        });

        deleteDialog = new ChoiceDialog(mContext,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        },new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)){
                    deleteDialog.dismiss();
                    if(deletInfo!=null){
                        discussList.remove(deletInfo);
                        discussAdapter.notifyDataSetChanged();
                    }

                }else{
                    ToastUtils.show(mContext,R.string.check_network);
                }
            }
        });

        deleteDialog.getTitleText().setText("确定要删除吗？");
        deleteDialog.getRightButton().setText("确定");
        deleteDialog.getLeftButton().setText("取消");

    }

    @Override
    public void initData() {
        super.initData();

        initTitle("评论回复");

        discussList = new ArrayList<DiscussInfo>();
        discussAdapter = new DiscussAdapter(mContext,discussList,handler);
        listView.setAdapter(discussAdapter);
        discussAdapter.notifyDataSetChanged();

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pagesize +=1;
                getDiscussData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        discussList.clear();
        pagesize = 1;
        getDiscussData();
    }

    private void getDiscussData(){

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
//                Log.v("LISt:", (String) o);
                listView.onRefreshComplete();
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        String data = jsonObject.getString("data");
                        if(data.equals("[]")){
                            ToastUtils.show(mContext,"没有了");
                        }else{
                            List<DiscussInfo> discussInfoList = gson.fromJson(data,new TypeToken<List<DiscussInfo>>(){}.getType());
                            discussList.addAll(discussInfoList);
                        }

                        discussAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                        Log.v("EEE:",new String(volleyError.networkResponse.data));
            }
        };

        Map<String,String> params = new HashMap<String,String>();
        params.put("uid", EggshellApplication.getApplication().getUser().getId()+"");
        params.put("page",pagesize+"");
//        Log.v("PARA:",params.toString());
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(),Urls.METHOD_DISCUSS,false,params,true,listener,errorListener);

    }


    private void readRequest(final DiscussInfo info){

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        Intent intent = new Intent(mContext,InfoDetailActivity.class);
                        intent.putExtra("playId",info.getAid());
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                Log.v("EEE:",new String(volleyError.networkResponse.data));
            }
        };

        Map<String,String> params = new HashMap<String,String>();
        params.put("id", info.getId());
        params.put("type", info.getType());
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(),Urls.METHOD_IS_READ,false,params,true,listener,errorListener);

    }
}
