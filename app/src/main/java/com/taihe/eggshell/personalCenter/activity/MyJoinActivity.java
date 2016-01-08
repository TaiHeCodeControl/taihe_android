package com.taihe.eggshell.personalCenter.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.main.mode.PlayInfoMode;
import com.taihe.eggshell.meetinginfo.InfoDetailActivity;
import com.taihe.eggshell.personalCenter.adapter.MyActivityAdapter;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2016/1/8.
 */
public class MyJoinActivity extends BaseActivity{

    private Context mContext;

    private TextView id_title,txt_around_tag1,txt_around_tag2;
    private LinearLayout lin_around_tag1,lin_around_tag2;
    private PullToRefreshListView playView;
    private MyActivityAdapter playAdapter;
    int limit=10,page=1,type=2;
    private List<PlayInfoMode> list = new ArrayList<PlayInfoMode>();
    private LoadingProgressDialog loading;

    @Override
    public void initView() {
        setContentView(R.layout.activity_my_join_activity);
        super.initView();

        mContext = this;
        lin_around_tag1 = (LinearLayout)findViewById(R.id.lin_around_tag1);
        lin_around_tag2 = (LinearLayout)findViewById(R.id.lin_around_tag2);
        id_title = (TextView) findViewById(R.id.id_title);
        txt_around_tag1 = (TextView) findViewById(R.id.txt_around_tag1);
        txt_around_tag2 = (TextView) findViewById(R.id.txt_around_tag2);
        playView = (PullToRefreshListView) findViewById(R.id.id_activity_listview);
    }

    @Override
    public void initData() {
        super.initData();

        id_title.setText("已报名活动");
        playView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        playAdapter = new MyActivityAdapter(mContext,list);
        playView.setAdapter(playAdapter);
        lin_around_tag1.setOnClickListener(this);
        lin_around_tag2.setOnClickListener(this);
        loading = new LoadingProgressDialog(mContext,"正在请求...");
        playView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(mContext,InfoDetailActivity.class);
                intent.putExtra("playId",list.get(position-1).getId());
                startActivity(intent);
            }
        });
        playView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loading.show();
                page=1;
                list.clear();
                getListData();
                playView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loading.show();
                page++;
                getListData();
                playView.onRefreshComplete();
            }
        });
        loading.show();
        getListData();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.lin_around_tag1:
                list.clear();
                type=2;//正在进行的活动
                loading.show();
                txt_around_tag1.setTextColor(mContext.getResources().getColor(R.color.include_title_color));
                txt_around_tag2.setTextColor(mContext.getResources().getColor(R.color.font_color_black));
                page=1;
                playView.setVisibility(View.VISIBLE);
                getListData();
                playView.onRefreshComplete();
                break;
            case R.id.lin_around_tag2:
                list.clear();
                type=1;//往期回顾
                loading.show();
                txt_around_tag2.setTextColor(mContext.getResources().getColor(R.color.include_title_color));
                txt_around_tag1.setTextColor(mContext.getResources().getColor(R.color.font_color_black));
                page=1;
                getListData();
                playView.onRefreshComplete();
                break;
        }
    }

    private void getListData() {

        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    loading.dismiss();
                    JSONObject jsonObject = new JSONObject((String) obj);
                    Log.v("ACTIVITYLIST:", (String) obj);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        JSONObject js = new JSONObject(data);
                        String result = js.getString("result");
                        List<PlayInfoMode> playlist = gson.fromJson(result, new TypeToken<List<PlayInfoMode>>(){}.getType());
                        list.addAll(playlist);
                        playAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {//返回值
                loading.dismiss();
                ToastUtils.show(mContext, "网络异常");
//                    String err = new String(volleyError.networkResponse.data);
//                    volleyError.networkResponse.statusCode;
            }
        };
        Map<String,String> map = new HashMap<String,String>();
        map.put("type",""+type);
        map.put("limit",""+limit);
        map.put("page",""+page);

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_ACTIVITY_LIST, true, map, true, listener, errorListener);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

}
