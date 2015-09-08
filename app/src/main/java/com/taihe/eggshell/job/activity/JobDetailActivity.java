package com.taihe.eggshell.job.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.GsonUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.job.bean.JobDetailInfo;
import com.taihe.eggshell.login.LoginActivity;
import com.taihe.eggshell.main.entity.User;
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
    private String com_id;
    private LinearLayout ll_moreposition;
    private TextView tv_allzhiwei, tv_shouqizhiwei;

    private ScrollView sc_detail;
    private List<JobInfo> jobInfos = null;
    private boolean isCollect = false;

    private Intent intent;

    private String address, cj_name, com_name, content, description, edu, exp, hy, id, lastupdate, linkmail, linktel, mun, name, provinceid, salary, type;
    private String collect;

    private TextView tv_jobdetail_description;

    private int UserId;
    private User user;

    private Handler jobDetailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 201://收藏
                    String data = (String) msg.obj;
                    ToastUtils.show(mContext, data);
                    if (data.equals("收藏成功")) {//收藏
                        collectionImg.setImageResource(R.drawable.shoucang2);//已收藏图标
                    } else {// 取消收藏
                        collectionImg.setImageResource(R.drawable.shoucang);
                    }
                    break;
                case 101://职位详情
                    try {
                        JobDetailInfo jobDetaiInfo = (JobDetailInfo) msg.obj;
                        address = jobDetaiInfo.data.address;
                        if (address.length() > 6) {

                            address = address.substring(0, 6);
                        }
//                        address = jobDetaiInfo.data.address.split("区")[0].toString();
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

                        collect = jobDetaiInfo.data.iscollect;
                        if (collect.equals("1")) {//已收藏收藏

                            collectionImg.setImageResource(R.drawable.shoucang2);
                        } else {//未收藏
                            collectionImg.setImageResource(R.drawable.shoucang);
                        }
                        //职位详情信息填充
                        jobtitle.setText(cj_name);
                        jobcompany.setText(com_name);
                        jobstart.setText(lastupdate);//发布时间
                        jobend.setText(jobDetaiInfo.data.edate);//有效时间
                        jobtype.setText(type);//工作性质
                        joblevel.setText(edu);//学历要求
                        jobyears.setText(exp);//工作年限
                        jobaddress.setText(address);
                        jobmoney.setText(salary);
                        jobnum.setText(hy);//招聘人数


                        byte[] bytes = Base64.decode(content, Base64.DEFAULT);
                        content = new String(bytes, "UTF-8");
                        company_jieshao.setText(Html.fromHtml(content));//公司介绍

                        byte[] descriptionbytes = Base64.decode(description, Base64.DEFAULT);
                        description = new String(descriptionbytes, "UTF-8");
                        tv_jobdetail_description.setText(Html.fromHtml(description));//职责描述


                        int size = jobDetaiInfo.data.lists.size();
                        if (size < 1) {//如果没有更多职位，隐藏更多职位
                            ll_moreposition.setVisibility(View.GONE);
                        }
                        //该公司其他职位信息
                        for (int i = 0; i < size; i++) {
                            JobInfo jobInfo = new JobInfo();

                            jobInfo.setJob_Id(jobDetaiInfo.data.lists.get(i).id);
                            jobInfo.setCom_name(jobDetaiInfo.data.lists.get(i).com_name);
                            jobInfo.setEdu(jobDetaiInfo.data.lists.get(i).edu);
                            jobInfo.setName(jobDetaiInfo.data.lists.get(i).name);
                            jobInfo.setLastupdate(jobDetaiInfo.data.lists.get(i).lastupdate);
                            jobInfo.setProvinceid(jobDetaiInfo.data.lists.get(i).provinceid);
                            jobInfo.setSalary(jobDetaiInfo.data.lists.get(i).salary);
                            jobInfo.setUid(jobDetaiInfo.data.lists.get(i).uid);


                            jobInfos.add(jobInfo);
                        }


                        //填充listview
                        AllJobAdapter jobAdapter = new AllJobAdapter(mContext, jobInfos, false);
                        moreJobListView.setAdapter(jobAdapter);

                        new GetLinesAsyncTask().execute();
                    } catch (Exception e) {

                    }
                    break;
            }

        }
    };

    @Override
    public void initView() {

        setContentView(R.layout.activity_job_detail);
        super.initView();
        mContext = this;
        jobInfos = new ArrayList<JobInfo>();

        user = EggshellApplication.getApplication().getUser();
        if (user != null) {
            UserId = EggshellApplication.getApplication().getUser().getId();
            Log.i("USERID", UserId + "");
        }


        intent = getIntent();
        jobId = intent.getIntExtra("ID", 1);
        com_id = intent.getStringExtra("com_id");
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
        moreJobListView.setFocusable(false);
        applyButton = (Button) findViewById(R.id.id_apply_button);

        applyButton.setOnClickListener(this);
        collectionImg.setOnClickListener(this);
        updown.setOnClickListener(this);
        shouqi.setOnClickListener(this);

        tv_jobdetail_description = (TextView) findViewById(R.id.tv_jobdetail_description);
        tv_allzhiwei = (TextView) findViewById(R.id.id_see_allzhize);
        tv_allzhiwei.setOnClickListener(this);

        tv_shouqizhiwei = (TextView) findViewById(R.id.id_see_shouqizhize);
        tv_shouqizhiwei.setOnClickListener(this);
        ll_moreposition = (LinearLayout) findViewById(R.id.ll_jobdetail_moreposition);

        sc_detail = (ScrollView) findViewById(R.id.sv_detail);
        sc_detail.smoothScrollTo(0, 20);
    }

    @Override
    public void initData() {
        super.initData();
        titleView.setText("职位详情");
        collectionImg.setVisibility(View.VISIBLE);


        //该公司其他职位的点击事件
        moreJobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //listviewItem点击事件

                if (position < jobInfos.size()) {
                    Intent intent = new Intent(mContext, JobDetailActivity.class);
                    intent.putExtra("ID", jobInfos.get(position).getJob_Id());
                    intent.putExtra("com_id", jobInfos.get(position).getUid());
                    startActivity(intent);
                    JobDetailActivity.this.finish();
                }

            }
        });

        dialog = new LoadingProgressDialog(mContext, getResources().getString(
                R.string.submitcertificate_string_wait_dialog));

        if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
            dialog.show();

            getDetail();

        } else {
            ToastUtils.show(mContext, R.string.check_network);
        }


//        //职位描述列表
//        List<String> desc = new ArrayList<String>();
//        for (int i = 0; i < 5; i++) {
//            desc.add(i + "、你每年考试的记录看到");
//        }
//
//        //岗位职责
//        tv_jobdetail_description.post(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
//        //职位描述信息填充
//        JobDescAdapter jobDescAdapter = new JobDescAdapter(mContext, desc);
//        jobDescListView.setAdapter(jobDescAdapter);
        //公司介绍
//        company_jieshao.setText("\u3000\u3000" + "深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路");
//        //查看全部和点击收起
//        company_jieshao.post(new Runnable() {
//            @Override
//            public void run() {
//                Log.i("jieshao_line", company_jieshao.getLineCount() + "");
//                if (company_jieshao.getLineCount() > 3) {
//                    company_jieshao.setMaxLines(2);
//                    updown.setVisibility(View.VISIBLE);
//                    shouqi.setVisibility(View.GONE);
//                } else {
//                    updown.setVisibility(View.GONE);
//                    shouqi.setVisibility(View.GONE);
//                }
//            }
//        });


    }


    //申请职位
    public void postJob() {


        StringBuilder sb = new StringBuilder();//选择的职位
        for (JobInfo jobInfo : jobInfos) {
            System.out.println(jobInfo.getJob_Id() + "======" + jobInfo.isChecked());
            if (jobInfo.isChecked()) {
                sb.append(jobInfo.getJob_Id());
                sb.append(",");
            }
        }

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                dialog.dismiss();
                try {
                    Log.v(TAG, (String) o);

                    JSONObject jsonObject = new JSONObject((String) o);

                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if (code == 0) {//申请成功

//                        int sucNum = Integer.valueOf(jsonObject.getString("data"));
//                        postednum = 1 - sucNum;
//
//                        JobApplyDialogUtil.isApplyJob(mContext, 1, postednum);
                        ToastUtils.show(mContext,"申请成功");

                    } else if (code == 1) {//请先创建简历
                        ToastUtils.show(mContext, "请先创建简历");

                    } else if (code == 2) {//不能重复申请

                        ToastUtils.show(mContext, "你选的职位已申请过，一周内不能重复申请");
                    } else {
                        ToastUtils.show(mContext, "申请失败");
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
                        Log.v("jobPost:", new String(volleyError.networkResponse.data));
                    }
                    ToastUtils.show(mContext, volleyError.networkResponse.statusCode + "");
                } catch (Exception e) {
                    ToastUtils.show(mContext, "联网失败");
                }

            }
        };

        String jobIds = sb.toString();
        Map<String, String> param = new HashMap<String, String>();
        param.put("uid", UserId + "");//UserID       userId
        param.put("job_id", jobIds);
        RequestUtils.createRequest(mContext, "", Urls.METHOD_JOB_POST, false, param, true, listener, errorListener);

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_apply_button://申请职位

                if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
                    dialog = new LoadingProgressDialog(mContext, getResources().getString(
                            R.string.submitcertificate_string_wait_dialog));
                    dialog.show();
                    postJob();
                } else {
                    ToastUtils.show(mContext, R.string.check_network);
                }

//                JobApplyDialogUtil.isApplyJob(mContext, 10, 2);
                break;
            case R.id.id_see_all:
                company_jieshao.setMaxLines(Integer.MAX_VALUE);
                updown.setVisibility(View.GONE);
                shouqi.setVisibility(View.VISIBLE);
                break;
            case R.id.id_see_shouqi:
                company_jieshao.setMaxLines(3);
                updown.setVisibility(View.VISIBLE);
                shouqi.setVisibility(View.GONE);
                break;
            case R.id.id_see_allzhize:
                tv_jobdetail_description.setMaxLines(Integer.MAX_VALUE);
                tv_allzhiwei.setVisibility(View.GONE);
                tv_shouqizhiwei.setVisibility(View.VISIBLE);
                break;
            case R.id.id_see_shouqizhize:
                tv_jobdetail_description.setMaxLines(3);
                tv_allzhiwei.setVisibility(View.VISIBLE);
                tv_shouqizhiwei.setVisibility(View.GONE);
                break;
            case R.id.id_other:

                if (user == null) {
                    EggshellApplication.getApplication().setLoginTag("jobDetail");
                    Intent intent = new Intent(JobDetailActivity.this, LoginActivity.class);
//                    intent.putExtra("LoginTag","jobDetail");
                    intent.putExtra("ID", jobId);
                    intent.putExtra("UID", com_id);
                    startActivity(intent);
                } else {
                    //收藏&取消收藏
                    if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
                        dialog = new LoadingProgressDialog(mContext, getResources().getString(
                                R.string.submitcertificate_string_wait_dialog));
                        dialog.show();
                        collectPosition();
                    } else {
                        ToastUtils.show(mContext, R.string.check_network);
                    }

                }
                break;
        }
    }

    //收藏职位
    private void collectPosition() {
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                dialog.dismiss();
                try {
                    Log.v(TAG, (String) o);
                    JSONObject jsonObject = new JSONObject((String) o);
                    int code = jsonObject.getInt("code");
                    System.out.println("collectCode=========" + code);
                    if (code == 0) {
                        String data = jsonObject.getString("data");
                        Message msg = new Message();
                        msg.obj = data;
                        msg.what = 201;
                        jobDetailHandler.sendMessage(msg);

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
                        Log.v("jobDetailCollect:", new String(volleyError.networkResponse.data));
                    }
                    ToastUtils.show(mContext, volleyError.networkResponse.statusCode + "");
                } catch (Exception e) {
                    ToastUtils.show(mContext, "联网失败");
                }

            }
        };

        Map<String, String> param = new HashMap<String, String>();
        param.put("uid", UserId + "");
        param.put("job_id", jobId + "");

        RequestUtils.createRequest(mContext, "", Urls.METHOD_JOB_COLLECT, false, param, true, listener, errorListener);

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

                        Message msg = new Message();
                        msg.obj = jobDetaiInfo;
                        msg.what = 101;
                        jobDetailHandler.sendMessage(msg);

                    } else {
                        ToastUtils.show(mContext, "获取详情信息失败");
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
                        Log.v("jobDetail:", new String(volleyError.networkResponse.data));
                    }
                    ToastUtils.show(mContext, volleyError.networkResponse.statusCode + "");
                } catch (Exception e) {
                    ToastUtils.show(mContext, "联网失败");
                }

            }
        };

        Map<String, String> param = new HashMap<String, String>();
        param.put("id", com_id + "");//职位列表中的uid
        param.put("pid", jobId + "");
        param.put("uid", UserId + "");
        Log.i("ID", jobId + "");
        Log.i("com_id", com_id);
        RequestUtils.createRequest(mContext, "", Urls.METHOD_JOB_DETAIL, false, param, true, listener, errorListener);

    }

    //获取职位职责和公司介绍的总行数
    private class GetLinesAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            int lineCount = company_jieshao.getLineCount();
            int zhiweiline = tv_jobdetail_description.getLineCount();
            if (lineCount <= 3) {
                updown.setVisibility(View.GONE);
                shouqi.setVisibility(View.GONE);
            } else {
                company_jieshao.setMaxLines(3);
                updown.setVisibility(View.VISIBLE);
                shouqi.setVisibility(View.GONE);
            }
            if (zhiweiline <= 3) {
                tv_shouqizhiwei.setVisibility(View.GONE);
                tv_allzhiwei.setVisibility(View.GONE);
            } else {
                tv_jobdetail_description.setMaxLines(3);
                tv_shouqizhiwei.setVisibility(View.GONE);
                tv_allzhiwei.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }
}
