package com.taihe.eggshell.job.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.widget.JobApplyDialogUtil;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.adapter.AllJobAdapter;
import com.taihe.eggshell.job.adapter.JobDescAdapter;
import com.taihe.eggshell.job.bean.JobInfo;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.taihe.eggshell.widget.MyListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2015/8/10.
 */
public class JobDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "JobDetailActivity";
    private Context mContext;
    private TextView titleView, jobtitle, jobcompany, jobstart, jobend, jobtype, joblevel, jobyears, jobaddress, jobmoney, jobnum, updown, shouqi, company_jieshao;
    private Button applyButton;
    private MyListView jobDescListView, moreJobListView;
    private ImageView collectionImg;
    private LoadingProgressDialog dialog;
    private String jobId = "23";

    private List<JobInfo> jobInfos = null;
    private boolean isCollect = false;

    @Override
    public void initView() {

        setContentView(R.layout.activity_job_detail);
        super.initView();
        mContext = this;

        titleView = (TextView) findViewById(R.id.id_title);
        collectionImg = (ImageView) findViewById(R.id.id_other);
        jobtitle = (TextView) findViewById(R.id.id_job_name);
        jobcompany = (TextView) findViewById(R.id.id_job_company);
        jobstart = (TextView) findViewById(R.id.id_start_time);
        jobend = (TextView) findViewById(R.id.id_end_time);
        jobtype = (TextView) findViewById(R.id.id_job_type);
        joblevel = (TextView) findViewById(R.id.id_school_leve);
        jobyears = (TextView) findViewById(R.id.id_work_time);
        jobaddress = (TextView) findViewById(R.id.id_job_addres);
        jobmoney = (TextView) findViewById(R.id.id_money);
        jobnum = (TextView) findViewById(R.id.id_pepple_num);
        updown = (TextView) findViewById(R.id.id_see_all);
        shouqi = (TextView) findViewById(R.id.id_see_shouqi);
        company_jieshao = (TextView) findViewById(R.id.id_company_jieshao);

        jobDescListView = (MyListView) findViewById(R.id.id_job_desc);
        moreJobListView = (MyListView) findViewById(R.id.id_other_position);
        applyButton = (Button) findViewById(R.id.id_apply_button);

        applyButton.setOnClickListener(this);
        collectionImg.setOnClickListener(this);
        updown.setOnClickListener(this);
        shouqi.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        titleView.setText("职位详情");
        collectionImg.setVisibility(View.VISIBLE);

        dialog = new LoadingProgressDialog(mContext, getResources().getString(
                R.string.submitcertificate_string_wait_dialog));

        if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)){
            dialog.show();
            getDetail();
        }else{
            ToastUtils.show(mContext,R.string.check_network);
        }

        jobInfos = new ArrayList<JobInfo>();
        //该公司其他职位信息
        for (int i = 0; i < 2; i++) {
            JobInfo jobInfo = new JobInfo(false, i);
            jobInfo.setId(i);
            jobInfos.add(jobInfo);
        }
        //该公司其他职位的点击事件
        moreJobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //listviewItem点击事件

                if (position < jobInfos.size()) {
                    Intent intent = new Intent(mContext, JobDetailActivity.class);
                    startActivity(intent);
                }

            }
        });
        //填充listview
        AllJobAdapter jobAdapter = new AllJobAdapter(mContext, jobInfos, false);
        moreJobListView.setAdapter(jobAdapter);

        //职位描述列表
        List<String> desc = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            desc.add(i + "、你每年考试的记录看到");
        }


        //职位描述信息填充
        JobDescAdapter jobDescAdapter = new JobDescAdapter(mContext, desc);
        jobDescListView.setAdapter(jobDescAdapter);

        //公司介绍
        company_jieshao.setText("\u3000\u3000" + "深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路");
        //查看全部和点击收起
        company_jieshao.post(new Runnable() {
            @Override
            public void run() {
                if (company_jieshao.getLineCount() > 2) {
                    company_jieshao.setMaxLines(2);
                    updown.setVisibility(View.VISIBLE);
                    shouqi.setVisibility(View.GONE);
                } else {
                    updown.setVisibility(View.GONE);
                    shouqi.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_apply_button:
                JobApplyDialogUtil.isApplyJob(mContext,10,2);
                break;
            case R.id.id_see_all:
                company_jieshao.setMaxLines(Integer.MAX_VALUE);
                updown.setVisibility(View.GONE);
                shouqi.setVisibility(View.VISIBLE);
                break;
            case R.id.id_see_shouqi:
                company_jieshao.setMaxLines(2);
                updown.setVisibility(View.VISIBLE);
                shouqi.setVisibility(View.GONE);
                break;
            case R.id.id_other:
                if (isCollect) {
                    collectionImg.setImageResource(R.drawable.shoucang);
                    ToastUtils.show(mContext, "取消收藏");
                    isCollect = false;
                }else{
                    collectionImg.setImageResource(R.drawable.shoucang2);
                    ToastUtils.show(mContext, "收藏");
                    isCollect = true;
                }
                break;
        }
    }

    private void getDetail(){
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                dialog.dismiss();
                try {
                    Log.v(TAG, (String) o);
                    JSONObject jsonObject = new JSONObject((String)o);

                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if(code ==0){
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                    }else{
                        ToastUtils.show(mContext,"获取失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                if(null!=volleyError.networkResponse.data){
                    Log.v("Forget:", new String(volleyError.networkResponse.data));
                }
                ToastUtils.show(mContext,volleyError.networkResponse.statusCode+"");
            }
        };

        Map<String,String> param = new HashMap<String, String>();
        param.put("id",jobId);///id/23/pid/7

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_JOB_DETAIL, false, param, true, listener, errorListener);

    }
}
