package com.taihe.eggshell.company;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.company.adapter.ComResumeAdapter;
import com.taihe.eggshell.company.adapter.PersonResumeAdapter;
import com.taihe.eggshell.company.mode.ComResumeMode;
import com.taihe.eggshell.company.mode.PersonResumeMode;
import com.taihe.eggshell.widget.LoadingProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2015/11/23.
 */
public class CompanyPersonResumeActivity extends BaseActivity{

    private Context mContext;
    private PullToRefreshGridView meetingView;
    int limit=6,page=1,type=1;
    List<PersonResumeMode> list;
    private LoadingProgressDialog loading;
    private PersonResumeAdapter personResumeAdapter;
    private LinearLayout person_lin_allcheck;
    private TextView person_txt_del;
    boolean isCheck = false;
    private ImageView person_img_allcheck;
    ComResumeMode info;
    @Override
    public void initView() {
        setContentView(R.layout.activity_person_resume_list);
        super.initView();
        mContext = this;
        meetingView = (PullToRefreshGridView)findViewById(R.id.id_person_listview);
        person_lin_allcheck = (LinearLayout) findViewById(R.id.person_lin_allcheck);
        person_txt_del = (TextView) findViewById(R.id.person_txt_del);
        person_img_allcheck = (ImageView) findViewById(R.id.person_img_allcheck);
        person_txt_del.setOnClickListener(this);
        person_lin_allcheck.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("人才库");
        meetingView.setMode(PullToRefreshBase.Mode.BOTH);
        loading = new LoadingProgressDialog(mContext,"正在请求...");
        personResumeAdapter = new PersonResumeAdapter(mContext);
        list = new ArrayList<PersonResumeMode>();
        getListData();
        meetingView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                page=1;
                list.clear();
                getListData();
                meetingView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                page++;
                isCheck = false;
                person_img_allcheck.setBackgroundResource(R.drawable.xuankuang);
                getListData();
                meetingView.onRefreshComplete();
            }
        });
        meetingView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int temp=0;
                for (PersonResumeMode info : list) {
                    if(temp==i) {
                        if(!list.get(i).isChecked()) {
                            info.setIsChecked(true);
                        }else{
                            info.setIsChecked(false);
                        }
                        break;
                    }
                    temp++;
                }
                isCheck = false;
                person_img_allcheck.setBackgroundResource(R.drawable.xuankuang);
                personResumeAdapter.notifyDataSetChanged();
                meetingView.onRefreshComplete();
            }
        });
    }
    private void getListData() {
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    loading.dismiss();
                    JSONObject jsonObject = new JSONObject((String) obj);

                    int code = jsonObject.getInt("code");
                    meetingView.setSelection(list.size()-1);
                    if (code == 0) {
                        String data = jsonObject.getString("data");
                        try{
                            JSONArray j1 = new JSONArray(data);
                            JSONObject j2;
                            for(int i=0;i<j1.length();i++){
                                j2 = j1.getJSONObject(i);
                                PersonResumeMode vMode = new PersonResumeMode();
                                vMode.setIsChecked(false);
                                vMode.setUid(j2.optString("uid").toString());
                                vMode.setHopeprofess(j2.optString("hopeprofess").toString());
                                vMode.setEid(j2.optString("eid").toString());
                                vMode.setSalary(j2.optString("salary").toString());
                                vMode.setExp(j2.optString("exp").toString());
                                vMode.setName(j2.optString("name").toString());
                                vMode.setCom_id(j2.optString("com_id").toString());
                                vMode.setCtime(j2.optString("ctime").toString());
                                list.add(vMode);
                            }
                            meetingView.setVisibility(View.VISIBLE);
                            personResumeAdapter.setPlayData(list, type);
                            meetingView.setAdapter(personResumeAdapter);
                            if(list.size()==0){
                                meetingView.setVisibility(View.GONE);
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    } else {
                        if(list.size()==0) {
                            meetingView.setVisibility(View.GONE);
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
        map.put("com_id", "83");//EggshellApplication.getApplication().getUser().getId()
        map.put("page",""+limit);
        map.put("pageIndex",""+page);
        String url = Urls.PERSON_GET_RESUME_URL;
        RequestUtils.createRequest(mContext, url, "", true, map, true, listener, errorListener);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.person_lin_allcheck:
                if(!isCheck) {
                    for (PersonResumeMode info : list) {
                        info.setIsChecked(true);
                    }
                    isCheck = true;
                    person_img_allcheck.setBackgroundResource(R.drawable.xuankuang_red);
                }else{
                    for (PersonResumeMode info : list) {
                        info.setIsChecked(false);
                    }
                    isCheck = false;
                    person_img_allcheck.setBackgroundResource(R.drawable.xuankuang);
                }
                personResumeAdapter.notifyDataSetChanged();
                meetingView.onRefreshComplete();
                break;
            case R.id.person_txt_del:
                String tempEid="";
                for(int i=0;i<list.size();i++){
                    if(list.get(i).isChecked()) {
                        tempEid = tempEid + list.get(i).getEid() + ",";
                    }
                }
                tempEid = tempEid.substring(0,tempEid.length()-1);
                delResumeData("83",tempEid);
                break;
        }
    }
    private void delResumeData(String c_uid,String strEid) {
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    loading.dismiss();
                    JSONObject jsonObject = new JSONObject((String) obj);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        meetingView.setSelection(list.size()-1);
                        try{
                            list.clear();
                            getListData();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        // Log.e("data",data);
                    } else {
                        ToastUtils.show(mContext,jsonObject.optString("message"));
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
        map.put("com_id", c_uid);//EggshellApplication.getApplication().getUser().getId()
        map.put("eid",strEid);
        String url = Urls.PERSON_DEL_RESUME_URL;
        RequestUtils.createRequest(mContext, url, "", true, map, true, listener, errorListener);
    }
}
