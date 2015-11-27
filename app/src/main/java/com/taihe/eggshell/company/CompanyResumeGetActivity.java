package com.taihe.eggshell.company;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.taihe.eggshell.company.mode.ComResumeMode;
import com.taihe.eggshell.job.bean.JobInfo;
import com.taihe.eggshell.widget.LoadingProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2015/11/23.
 */
public class CompanyResumeGetActivity extends BaseActivity{

    private Context mContext;
    private PullToRefreshGridView meetingView;
    int limit=6,page=1,type=1;
    List<ComResumeMode> list;
    private LoadingProgressDialog loading;
    private ComResumeAdapter comResumeAdapter;
    private LinearLayout linTop1,linTop2,linTop3,linTop4,com_lin_allcheck;
    private TextView txt_com_top1,txt_com_top2,txt_com_top3,txt_com_top4,com_txt_del;
    boolean isCheck = false;
    private ImageView com_img_allcheck;
    ComResumeMode info;
    @Override
    public void initView() {
        setContentView(R.layout.activity_com_resume_list);
        super.initView();
        mContext = this;
        meetingView = (PullToRefreshGridView)findViewById(R.id.id_com_listview);
        linTop1 = (LinearLayout) findViewById(R.id.lin_com_top1);
        linTop2 = (LinearLayout) findViewById(R.id.lin_com_top2);
        linTop3 = (LinearLayout) findViewById(R.id.lin_com_top3);
        linTop4 = (LinearLayout) findViewById(R.id.lin_com_top4);
        com_lin_allcheck = (LinearLayout) findViewById(R.id.com_lin_allcheck);
        txt_com_top1 = (TextView) findViewById(R.id.txt_com_top1);
        txt_com_top2 = (TextView) findViewById(R.id.txt_com_top2);
        txt_com_top3 = (TextView) findViewById(R.id.txt_com_top3);
        txt_com_top4 = (TextView) findViewById(R.id.txt_com_top4);
        com_txt_del = (TextView) findViewById(R.id.com_txt_del);
        com_img_allcheck = (ImageView) findViewById(R.id.com_img_allcheck);
        linTop1.setOnClickListener(this);
        linTop2.setOnClickListener(this);
        linTop3.setOnClickListener(this);
        linTop4.setOnClickListener(this);
        com_txt_del.setOnClickListener(this);
        com_lin_allcheck.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("蛋壳招聘");
        Intent intent = getIntent();
        type = intent.getIntExtra("is_browse",1);
        meetingView.setMode(PullToRefreshBase.Mode.BOTH);
        loading = new LoadingProgressDialog(mContext,"正在请求...");
        comResumeAdapter = new ComResumeAdapter(mContext);
        list = new ArrayList<ComResumeMode>();
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
                com_img_allcheck.setBackgroundResource(R.drawable.xuankuang);
                getListData();
                meetingView.onRefreshComplete();
            }
        });
        meetingView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int temp=0;
                for (ComResumeMode info : list) {
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
                com_img_allcheck.setBackgroundResource(R.drawable.xuankuang);
                comResumeAdapter.notifyDataSetChanged();
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
                                ComResumeMode vMode = new ComResumeMode();
                                vMode.setIsChecked(false);
                                vMode.setId(j2.optString("id").toString());
                                vMode.setUid(j2.optString("uid").toString());
                                vMode.setJob_id(j2.optString("job_id").toString());
                                vMode.setJob_name(j2.optString("job_name").toString());
                                vMode.setEid(j2.optString("eid").toString());
                                vMode.setSalary(j2.optString("salary").toString());
                                vMode.setExp(j2.optString("exp").toString());
                                vMode.setName(j2.optString("name").toString());
                                vMode.setCom_id(j2.optString("com_id").toString());
                                vMode.setDatetime(j2.optString("datetime").toString());
                                list.add(vMode);
                            }
                            meetingView.setVisibility(View.VISIBLE);
                            comResumeAdapter.setPlayData(list, type);
                            meetingView.setAdapter(comResumeAdapter);
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
        map.put("uid", "116");//EggshellApplication.getApplication().getUser().getId()
        map.put("is_browse",""+type);
        map.put("page",""+limit);
        map.put("pageIndex",""+page);
        String url = Urls.COMPY_GET_RESUME_URL;
        RequestUtils.createRequest(mContext, url, "", true, map, true, listener, errorListener);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.lin_com_top1:
                list.clear();
                type=1;
                txt_com_top1.setTextColor(getResources().getColor(R.color.font_color_red));
                txt_com_top2.setTextColor(getResources().getColor(R.color.font_color_black));
                txt_com_top3.setTextColor(getResources().getColor(R.color.font_color_black));
                txt_com_top4.setTextColor(getResources().getColor(R.color.font_color_black));
                page=1;
                meetingView.setVisibility(View.VISIBLE);
                getListData();
                meetingView.onRefreshComplete();
                break;
            case R.id.lin_com_top2:
                list.clear();
                type=2;
                txt_com_top2.setTextColor(getResources().getColor(R.color.font_color_red));
                txt_com_top1.setTextColor(getResources().getColor(R.color.font_color_black));
                txt_com_top3.setTextColor(getResources().getColor(R.color.font_color_black));
                txt_com_top4.setTextColor(getResources().getColor(R.color.font_color_black));
                page=1;
                meetingView.setVisibility(View.VISIBLE);
                getListData();
                meetingView.onRefreshComplete();
                break;
            case R.id.lin_com_top3:
                list.clear();
                type=3;
                txt_com_top3.setTextColor(getResources().getColor(R.color.font_color_red));
                txt_com_top1.setTextColor(getResources().getColor(R.color.font_color_black));
                txt_com_top2.setTextColor(getResources().getColor(R.color.font_color_black));
                txt_com_top4.setTextColor(getResources().getColor(R.color.font_color_black));
                page=1;
                meetingView.setVisibility(View.VISIBLE);
                getListData();
                meetingView.onRefreshComplete();
                break;
            case R.id.lin_com_top4:
                list.clear();
                type=4;
                txt_com_top4.setTextColor(getResources().getColor(R.color.font_color_red));
                txt_com_top1.setTextColor(getResources().getColor(R.color.font_color_black));
                txt_com_top2.setTextColor(getResources().getColor(R.color.font_color_black));
                txt_com_top3.setTextColor(getResources().getColor(R.color.font_color_black));
                page=1;
                meetingView.setVisibility(View.VISIBLE);
                getListData();
                meetingView.onRefreshComplete();
                break;
            case R.id.com_lin_allcheck:
                if(!isCheck) {
                    for (ComResumeMode info : list) {
                        info.setIsChecked(true);
                    }
                    isCheck = true;
                    com_img_allcheck.setBackgroundResource(R.drawable.xuankuang_red);
                }else{
                    for (ComResumeMode info : list) {
                        info.setIsChecked(false);
                    }
                    isCheck = false;
                    com_img_allcheck.setBackgroundResource(R.drawable.xuankuang);
                }
                comResumeAdapter.notifyDataSetChanged();
                meetingView.onRefreshComplete();
                break;
            case R.id.com_txt_del:
                String tempEid="",tempJobID="";
                for(int i=0;i<list.size();i++){
                    if(list.get(i).isChecked()) {
                        tempEid = tempEid + list.get(i).getEid() + ",";
                        tempJobID = tempJobID + list.get(i).getJob_id() + ",";
                    }
                }
                tempEid = tempEid.substring(0,tempEid.length()-1);
                tempJobID = tempJobID.substring(0,tempJobID.length()-1);
                delResumeData("116",tempEid,tempJobID);
                break;
        }
    }
    private void delResumeData(String c_uid,String strEid,String strJobID) {
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
        map.put("job_id",strJobID);
        String url = Urls.COMPY_DEL_RESUME_URL;
        RequestUtils.createRequest(mContext, url, "", true, map, true, listener, errorListener);
    } 
}
