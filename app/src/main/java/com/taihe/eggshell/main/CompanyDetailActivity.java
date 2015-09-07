package com.taihe.eggshell.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.job.adapter.CompanyDetailAdapter;
import com.taihe.eggshell.job.bean.JobInfo;
import com.taihe.eggshell.widget.MyListView;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2015/8/11.
 */
public class CompanyDetailActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "CompanyDetailActivity";

    private Context mContext;
    private TextView companyIndustry,companyType,companyScale,companyAddress,companyBrief,upordown;
    private ImageView img_company_detail_logo;
    private ScrollView scrollView;
    private MyListView jobsListView;
    private boolean isMore = false;
    private List<JobInfo> jobInfos = new ArrayList<JobInfo>();
    private String mid,uid;
    int page=1;
    CompanyDetailAdapter jobAdapter;

    @Override
    public void initView() {
        setContentView(R.layout.activity_company_detail);
        super.initView();

        mContext = this;
        scrollView = (ScrollView)findViewById(R.id.id_scroll_view);
        img_company_detail_logo = (ImageView)findViewById(R.id.img_company_detail_logo);
        companyIndustry = (TextView)findViewById(R.id.id_company_industry);
        companyType = (TextView)findViewById(R.id.id_company_type);
        companyScale = (TextView)findViewById(R.id.id_company_scale);
        companyAddress = (TextView)findViewById(R.id.id_company_addr);
        companyBrief = (TextView)findViewById(R.id.id_company_brief);
        upordown = (TextView)findViewById(R.id.id_up_down);
        jobsListView = (MyListView)findViewById(R.id.id_jobs_list_view);
        upordown.setOnClickListener(this);
//        jobsListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        jobAdapter = new CompanyDetailAdapter(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_foot_bottom,null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page++;
                getData();
            }
        });
        jobsListView.addFooterView(view);
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("名企详情");

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0,0);
            }
        });
        Intent intent = getIntent();
        mid = intent.getStringExtra("id");
        uid = intent.getStringExtra("uid");

        companyBrief.post(new Runnable() {
            @Override
            public void run() {
                if (companyBrief.getLineCount() > 2) {
                    isMore = true;
                    companyBrief.setMaxLines(2);
                    upordown.setVisibility(View.VISIBLE);
                } else {
                    upordown.setVisibility(View.GONE);
                }
            }
        });
        getData();
        jobsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

//        jobsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
//                page=1;
//                jobInfos.clear();
//                getData();
//                jobsListView.onRefreshComplete();
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
//                page++;
//                getData();
//                jobsListView.onRefreshComplete();
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_up_down:
                if(isMore){
                    isMore = false;
                    companyBrief.setMaxLines(Integer.MAX_VALUE);
                    upordown.setText("点击收起");
                    Drawable drawable = getResources().getDrawable(R.drawable.up_icon);
                    drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                    upordown.setCompoundDrawables(null,null,drawable,null);
                }else{
                    isMore = true;
                    companyBrief.setMaxLines(2);
                    upordown.setText("查看全部");
                    Drawable drawable = getResources().getDrawable(R.drawable.down_detail_icon);
                    drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                    upordown.setCompoundDrawables(null,null,drawable,null);
                }

                break;
        }
    }
    private void getData() {

        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    JSONObject jsonObject = new JSONObject((String) obj);
//                    Log.e("data", jsonObject.toString());
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        String data = jsonObject.getString("data");
                        data = data.replace("list\":null","list\":[]");
                        try{
                            JSONObject jso = new JSONObject(data);
                            JSONObject jo = jso.getJSONObject("details");
                            JSONArray j1 = jso.getJSONArray("list");
                            JSONObject j2;
                            String imgP = jo.optString("logo");
                            if(imgP.length()>15) {
                                FinalBitmap bitmap = FinalBitmap.create(mContext);
                                bitmap.display(img_company_detail_logo, imgP);
                            }
                            companyIndustry.setText(jo.optString("hy"));
                            companyScale.setText(jo.optString("gm"));
                            companyAddress.setText(jo.optString("address"));
                            String strTemp=jo.optString("content").toString();
                            if(strTemp.contains("null")){
                                companyBrief.setText("");
                            }else {
                                companyBrief.setText(strTemp);
                            }
                            JobInfo vMode;
                            for(int i=0;i<j1.length();i++){
                                vMode = new JobInfo();
                                j2 = j1.getJSONObject(i);
                                vMode.setJob_Id(j2.getInt("id"));
                                vMode.setUid(j2.optString("uid"));
                                vMode.setSalary(j2.optString("salary"));
                                vMode.setEdu(j2.optString("edu"));
                                vMode.setName(j2.optString("name"));
                                vMode.setLastupdate(j2.optString("sdate"));
                                vMode.setProvinceid(j2.optString("provinceid"));
                                jobInfos.add(vMode);
                            }
                            jobAdapter.setComData(jobInfos);
                            jobsListView.setAdapter(jobAdapter);
                            if(jobInfos.size()==0){
                                jobsListView.removeAllViews();
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        // Log.e("data",data);
                    } else {
                        //String msg = jsonObject.getString("message");
//                        ToastUtils.show(getActivity(), "网络连接异常");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {//返回值
//                    String err = new String(volleyError.networkResponse.data);
//                    volleyError.networkResponse.statusCode;
            }
        };

        Map<String,String> map = new HashMap<String,String>();
        map.put("mid",mid);
        map.put("uid","128");//uid
//        map.put("limit",""+limit);
        map.put("page",""+page);
        RequestUtils.createRequest(mContext, Urls.COMPY_DETAIL_URL, "", true, map, true, listener, errorListener);
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
