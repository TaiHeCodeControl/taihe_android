package com.taihe.eggshell.meetinginfo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2015/11/24.
 */
public class VActivity extends BaseActivity{

    private PullToRefreshGridView jingyingListView,dashiListView;
    private JYAdapter vAdapter;
    int limit=5,page=1,type=2;
    private ArrayList<PersonModel> list = new ArrayList<PersonModel>();
    private ArrayList<PersonModel> dslist = new ArrayList<PersonModel>();
    private LoadingProgressDialog loading;
    private Context mContext;

    @Override
    public void initView() {
        setContentView(R.layout.activity_v_list);
        super.initView();
        mContext = this;

        jingyingListView = (PullToRefreshGridView)findViewById(R.id.id_jingying_listview);
        dashiListView = (PullToRefreshGridView)findViewById(R.id.id_dashi_listview);

        jingyingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext,StudentDetailActivity.class);
                intent.putExtra("student",list.get(i));
                intent.putExtra("type","蛋壳精英");
                startActivity(intent);
            }
        });
        dashiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext,StudentDetailActivity.class);
                intent.putExtra("student",dslist.get(i));
                intent.putExtra("type","蛋壳大使");
                startActivity(intent);
            }
        });
    }

    public void initData(){
        super.initData();
        initTitle("V达人");
        loading = new LoadingProgressDialog(mContext,"正在请求...");
        jingyingListView.setMode(PullToRefreshBase.Mode.DISABLED);
        dashiListView.setMode(PullToRefreshBase.Mode.DISABLED);

        getJYListData();

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
        }
    }

    private void getJYListData() {
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    Log.v("VA:",(String)obj);
                    loading.dismiss();
                    JSONObject jsonObject = new JSONObject((String) obj);
                    int code = jsonObject.getInt("code");
                    Gson gson = new Gson();
                    if (code == 0) {
                        String data = jsonObject.getString("data");
                        JSONObject ej = new JSONObject(data);
                        String e = ej.getString("Elite");

                        List<PersonModel> jylist = gson.fromJson(e,new TypeToken<List<PersonModel>>(){}.getType());
                        list.addAll(jylist);
                        vAdapter = new JYAdapter(mContext,list);
                        jingyingListView.setAdapter(vAdapter);
                        vAdapter.notifyDataSetChanged();

                        String d = ej.getString("Envoy");
                        List<PersonModel> ds = gson.fromJson(d,new TypeToken<List<PersonModel>>(){}.getType());
                        dslist.addAll(ds);
                        DRAdapter dadapter = new DRAdapter(mContext,dslist);
                        dashiListView.setAdapter(dadapter);
                        dadapter.notifyDataSetChanged();
                    } else {

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
            }
        };

        loading.show();
        Map<String,String> map = new HashMap<String,String>();
        map.put("page",""+page);
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_GET_VJY, true, map, true, listener, errorListener);
    }

    private void getDSListData(){
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    Log.v("VA:",(String)obj);
                    loading.dismiss();
                    JSONObject jsonObject = new JSONObject((String) obj);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        String data = jsonObject.getString("data");
                    } else {
                        if(list.size()==0) {
                            dashiListView.setVisibility(View.GONE);
                        }
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
            }
        };

        loading.show();
        Map<String,String> map = new HashMap<String,String>();
        map.put("page",""+page);
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_GET_VJY, true, map, true, listener, errorListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}
