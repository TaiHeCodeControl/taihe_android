package com.taihe.eggshell.job.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
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
import com.taihe.eggshell.base.utils.GsonUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.job.bean.JobDetailInfo;
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
    private int jobId;
    private String uid;

    private List<JobInfo> jobInfos = null;
    private boolean isCollect = false;

    private Intent intent;

    private String address, cj_name, com_name, content, description, edu, exp, hy, id, lastupdate, linkmail, linktel, mun, name, provinceid, salary, type;

    private TextView tv_jobdetail_description;

    @Override
    public void initView() {

        setContentView(R.layout.activity_job_detail);
        super.initView();
        mContext = this;
        jobInfos = new ArrayList<JobInfo>();
        intent = getIntent();
        jobId = intent.getIntExtra("ID", 1);
        uid = intent.getStringExtra("UID");
        Log.i("ID", jobId + "");
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

        tv_jobdetail_description = (TextView) findViewById(R.id.tv_jobdetail_description);
    }

    @Override
    public void initData() {
        super.initData();
        titleView.setText("职位详情");
        collectionImg.setVisibility(View.VISIBLE);

        dialog = new LoadingProgressDialog(mContext, getResources().getString(
                R.string.submitcertificate_string_wait_dialog));

        if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
            dialog.show();

            getDetail();

        } else {
            ToastUtils.show(mContext, R.string.check_network);
        }


        //该公司其他职位的点击事件
        moreJobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //listviewItem点击事件

                if (position < jobInfos.size()) {
                    Intent intent = new Intent(mContext, JobDetailActivity.class);
                    intent.putExtra("ID", jobInfos.get(position).getId());
                    intent.putExtra("UID", jobInfos.get(position).getUid());
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

        //岗位职责
        tv_jobdetail_description.post(new Runnable() {
            @Override
            public void run() {

            }
        });
        //职位描述信息填充
        JobDescAdapter jobDescAdapter = new JobDescAdapter(mContext, desc);
        jobDescListView.setAdapter(jobDescAdapter);

        //公司介绍
        company_jieshao.setText("\u3000\u3000" + "深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路");
        //查看全部和点击收起
        company_jieshao.post(new Runnable() {
            @Override
            public void run() {
                Log.i("jieshao_line", company_jieshao.getLineCount() + "");
                if (company_jieshao.getLineCount() > 3) {
                    company_jieshao.setMaxLines(2);
                    updown.setVisibility(View.VISIBLE);
                    shouqi.setVisibility(View.GONE);
                } else {
                    updown.setVisibility(View.GONE);
                    shouqi.setVisibility(View.GONE);
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_apply_button:
                JobApplyDialogUtil.isApplyJob(mContext, 10, 2);
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
                } else {
                    collectionImg.setImageResource(R.drawable.shoucang2);
                    ToastUtils.show(mContext, "收藏");
                    isCollect = true;
                }
                break;
        }
    }

    //获取职位详情
    private void getDetail() {
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                dialog.dismiss();
                try {
                    Log.v(TAG, (String) o);

                    JobDetailInfo jobDetaiInfo = GsonUtils
                            .changeGsonToBean(o.toString(),
                                    JobDetailInfo.class);
                    int code = jobDetaiInfo.code;
                    if (code == 0) {
                        address = jobDetaiInfo.data.address.substring(0, 6);
                        cj_name = jobDetaiInfo.data.cj_name;
                        com_name = jobDetaiInfo.data.com_name;
                        content = jobDetaiInfo.data.content;
                        description = jobDetaiInfo.data.cj_description;
                        Log.i("desctiption", description);
                        edu = jobDetaiInfo.data.edu;
                        exp = jobDetaiInfo.data.exp;
                        hy = jobDetaiInfo.data.hy;
                        id = jobDetaiInfo.data.id;
                        lastupdate = jobDetaiInfo.data.lastupdate;
                        linkmail = jobDetaiInfo.data.linkmail;
                        linktel = jobDetaiInfo.data.linktel;
                        mun = jobDetaiInfo.data.mun;
                        name = jobDetaiInfo.data.name;
                        provinceid = jobDetaiInfo.data.provinceid;
                        salary = jobDetaiInfo.data.salary;
                        type = jobDetaiInfo.data.type;
                        //54不限 55全职 56兼职
                        if (type.equals("55")) {
                            type = "全职";
                        } else if (type.equals("56")) {
                            type = "兼职";
                        } else {
                            type = "不限";
                        }

                        //职位详情信息填充
                        jobtitle.setText(cj_name);
                        jobcompany.setText(com_name);
                        jobstart.setText(lastupdate);//发布时间
                        jobend.setText(lastupdate);//有效时间
                        jobtype.setText(type);//工作性质
                        joblevel.setText(edu);//学历要求
                        jobyears.setText(exp);//工作年限
                        jobaddress.setText(address);
                        jobmoney.setText(salary);
                        jobnum.setText(hy);//招聘人数
                        company_jieshao.setText(Html.fromHtml(content));//公司介绍
                        tv_jobdetail_description.setText(Html.fromHtml(description));//职责描述

                        int size = jobDetaiInfo.data.lists.size();
                        //该公司其他职位信息
                        for (int i = 0; i < size; i++) {
                            JobInfo jobInfo = new JobInfo();

                            jobInfo.setId(jobDetaiInfo.data.lists.get(i).id);
                            jobInfo.setCom_name(jobDetaiInfo.data.lists.get(i).com_name);
                            jobInfo.setEdu(jobDetaiInfo.data.lists.get(i).edu);
                            jobInfo.setName(jobDetaiInfo.data.lists.get(i).name);
                            jobInfo.setLastupdate(jobDetaiInfo.data.lists.get(i).lastupdate);
                            jobInfo.setProvinceid(jobDetaiInfo.data.lists.get(i).provinceid);
                            jobInfo.setSalary(jobDetaiInfo.data.lists.get(i).salary);
                            jobInfo.setUid(jobDetaiInfo.data.lists.get(i).uid);


                            jobInfos.add(jobInfo);
                        }


                    } else {
                        ToastUtils.show(mContext, "获取失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                try {
                    if (null != volleyError.networkResponse.data) {
                        Log.v("Forget:", new String(volleyError.networkResponse.data));
                    }
                    ToastUtils.show(mContext, volleyError.networkResponse.statusCode + "");
                } catch (Exception e) {
                    ToastUtils.show(mContext, "联网失败");
                }

            }
        };

        Map<String, String> param = new HashMap<String, String>();
        param.put("id", uid + "");///id/23/pid/7
        param.put("pid", jobId + "");

        RequestUtils.createRequest(mContext, "http://195.198.1.84/eggker/interface", Urls.METHOD_JOB_DETAIL, false, param, true, listener, errorListener);

    }
}
