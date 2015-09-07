package com.taihe.eggshell.job.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.GsonUtils;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.adapter.AllJobAdapter;
import com.taihe.eggshell.job.bean.JobDetailInfo;
import com.taihe.eggshell.job.bean.JobInfo;
import com.taihe.eggshell.job.fragment.AllJobFragment;
import com.taihe.eggshell.job.fragment.FujinFragment;
import com.taihe.eggshell.login.LoginActivity;
import com.taihe.eggshell.main.entity.User;
import com.taihe.eggshell.widget.CustomViewPager;
import com.taihe.eggshell.widget.JobApplyDialogUtil;
import com.taihe.eggshell.widget.LoadingProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by huan on 2015/8/6.
 */
public class FindJobActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "FINDJOBACTIVITY";
    private Intent intent;

    private TextView tv_allJob, tv_fujin;
    private ImageView iv_quancheng, iv_fujin, iv_filter, iv_search;
    private RelativeLayout rl_qc, rl_fujin, iv_back;


    private AllJobAdapter adapter;
    public CheckBox cb_selectAll;

    public List<JobInfo> jobInfos = null;
    private JobInfo jobInfo;

    private PullToRefreshGridView list_job_all;
    private Context mContext;
    private LoadingProgressDialog dialog;
    private int page = 1;
//    private int pageSize = 10;
    //选中条数的统计
    private int selectSize = 0;
    private int postednum = 0;

    private String Longitude = "";
    private String Latitude = "";
    private String keyword = "";
    private String hy = "", job_post = "", salary = "", edu = "", exp = "", type = "",cityid = "",fbtime = "";

    private User user;
    private int userId;

    private Handler jobListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1002:
                    ToastUtils.show(mContext, "没有职位");
                    adapter = new AllJobAdapter(mContext, jobInfos, true);
                    list_job_all.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                case 1001:
                    List<JobInfo> joblist = (List<JobInfo>) msg.obj;
                    jobInfos.addAll(joblist);
                    adapter = new AllJobAdapter(mContext, jobInfos, true);
                    adapter.setCheckedListener(new AllJobAdapter.checkedListener() {
                        @Override
                        public void checkedPosition(int position, boolean isChecked) {
                            jobInfos.get(position).setIsChecked(isChecked);
                            //如果有listview没有被选中，全选按钮状态为false
                            if (jobInfos.get(position).isChecked()) {
                                selectSize += 1;
                                if (selectSize == jobInfos.size()) {
                                    cb_selectAll.setChecked(true);
                                }
                            } else {
                                selectSize -= 1;
                                cb_selectAll.setChecked(false);
                            }


                        }
                    });

                    list_job_all.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_findjob);
        mContext = this;
        initView();
        initData();

    }

    private void initView() {

        keyword = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "keyword", "");
        hy = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "hy", "");
        job_post = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "job_post", "");
        salary = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "salary", "");
        edu = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "edu", "");
        exp = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "exp", "");
        type = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "type", "");
        cityid = PrefUtils.getStringPreference(mContext,PrefUtils.CONFIG,"cityid","");
        fbtime = PrefUtils.getStringPreference(mContext,PrefUtils.CONFIG,"fbtime","");

        user = EggshellApplication.getApplication().getUser();

        rl_fujin = (RelativeLayout) findViewById(R.id.rl_findjob_fujin);
        rl_qc = (RelativeLayout) findViewById(R.id.rl_findjob_qc);
        rl_fujin.setOnClickListener(this);
        rl_qc.setOnClickListener(this);

        tv_allJob = (TextView) findViewById(R.id.tv_findjob_all);
        tv_fujin = (TextView) findViewById(R.id.tv_findjob_fujin);

        iv_search = (ImageView) findViewById(R.id.iv_findjob_search);
        iv_filter = (ImageView) findViewById(R.id.iv_findjob_filter);
        iv_back = (RelativeLayout) findViewById(R.id.iv_findjob_back);

        iv_fujin = (ImageView) findViewById(R.id.iv_findjob_fj);
        iv_quancheng = (ImageView) findViewById(R.id.iv_findjob_qc);

        iv_search.setOnClickListener(this);
        iv_filter.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        jobInfos = new ArrayList<JobInfo>();

        list_job_all = (PullToRefreshGridView) findViewById(R.id.list_alljob_all);

        list_job_all.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        list_job_all.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                cb_selectAll.setChecked(false);
                page++;
                getList();
                list_job_all.onRefreshComplete();
            }
        });


        list_job_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //listviewItem点击事件

                if (position < jobInfos.size()) {
                    JobInfo job = jobInfos.get(position);
                    Intent intent = new Intent(mContext, JobDetailActivity.class);
                    intent.putExtra("ID", job.getJob_Id());
                    intent.putExtra("UID", job.getUid());
                    Log.i("ID", job.getJob_Id() + "");
                    startActivity(intent);
                }

            }
        });

        Button btn_shenqing = (Button) findViewById(R.id.btn_alljob_shenqing);
        btn_shenqing.setOnClickListener(this);
        cb_selectAll = (CheckBox) findViewById(R.id.cb_findjob_selectall);

        cb_selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cb_selectAll.isChecked()) {
                    cb_selectAll.setChecked(false);
                    for (JobInfo info : jobInfos) {
                        info.setIsChecked(false);
                    }
                } else {
                    cb_selectAll.setChecked(true);
                    selectSize = jobInfos.size();
                    for (JobInfo info : jobInfos) {
                        info.setIsChecked(true);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void initData() {
        if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
            dialog = new LoadingProgressDialog(mContext, getResources().getString(
                    R.string.submitcertificate_string_wait_dialog));
            dialog.show();
            getList();
        } else {
            ToastUtils.show(mContext, R.string.check_network);
        }


    }


    //全城职位列表
    private void getList() {

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                dialog.dismiss();
                try {
                    Log.v("Job:", (String) o);

                    JSONObject jsonObject = new JSONObject((String) o);

                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if (code == 0) {

                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        List<JobInfo> joblist = gson.fromJson(data, new TypeToken<List<JobInfo>>() {
                        }.getType());

                        Message msg = new Message();
                        msg.obj = joblist;
                        msg.what = 1001;
                        jobListHandler.sendMessage(msg);

                    } else if (code == 4001) {
                        Message msg = new Message();
                        msg.what = 1002;
                        jobListHandler.sendMessage(msg);

                    } else {
                        ToastUtils.show(mContext, "获取失败");
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
                try {
                    if (null != volleyError.networkResponse.data) {
                        Log.v("Job:", new String(volleyError.networkResponse.data));
                    }
                    ToastUtils.show(mContext, volleyError.networkResponse.statusCode + "");

                } catch (Exception e) {
                    ToastUtils.show(mContext, "联网失败");
                }

            }
        };

        //http://195.198.1.83/eggker/interface/Position/nearbycompany?
        // longitude=116.404916&dimensionality=39.927471&keyword=工程师&page=1
        //    传值项： longitude=>经度  dimensionality=>纬度 keyword=>关键字
        //    page=>页数 hy=>工作行业 职位类别=>job_post 月薪范围=>salary 学历要求=>edu 工作年限=>exp
        // 工作性质=>type

        Map<String, String> param = new HashMap<String, String>();
        param.put("longitude", Longitude);
        param.put("dimensionality", Latitude);
        param.put("keyword", keyword);//关键字
        param.put("page", page + "");
        param.put("hy", hy);//工作行业
        param.put("job_post", job_post);//职位类别
        param.put("salary", salary);
        param.put("edu", edu);
        param.put("exp", exp);//工作年限
        param.put("type", type);//工作性质
        param.put("fbtime ", fbtime);//工作性质
        param.put("cityid", cityid);//工作性质



        RequestUtils.createRequest(mContext, "", Urls.METHOD_JOB_LIST, false, param, true, listener, errorListener);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_findjob_back:
                FindJobActivity.this.finish();
                break;
            case R.id.rl_findjob_qc://全城
                jobInfos.clear();
                page = 1;
                Longitude = "";
                Latitude = "";

                getList();
                iv_quancheng.setImageResource(R.drawable.quancheng01);
                iv_fujin.setImageResource(R.drawable.fujin01);

                tv_allJob.setTextColor(getResources().getColor(R.color.font_color_red));
                tv_fujin.setTextColor(getResources().getColor(R.color.font_color_black));
                break;
            case R.id.rl_findjob_fujin://附近
                jobInfos.clear();
                page = 1;
                Longitude = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "Longitude", "");
                Latitude = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "Latitude", "");
                Log.i("job--Longitude ", Longitude + "Latitude====" + Latitude);
                getList();
                iv_quancheng.setImageResource(R.drawable.quancheng02);
                iv_fujin.setImageResource(R.drawable.fujin02);

                tv_allJob.setTextColor(getResources().getColor(R.color.font_color_black));
                tv_fujin.setTextColor(getResources().getColor(R.color.font_color_red));
                break;
            case R.id.iv_findjob_search://关键字搜索

                intent = new Intent(FindJobActivity.this, JobSearchActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_findjob_filter://职位筛选

                intent = new Intent(FindJobActivity.this, JobFilterActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_alljob_shenqing:
                //判断登录状态，

                if (null == user) {//登录
                    EggshellApplication.getApplication().setLoginTag("findJob");
                    intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                } else {
                    userId = EggshellApplication.getApplication().getUser().getId();

                    if (selectSize > 0) {

                        postJob();//申请职位
                    } else {
                        ToastUtils.show(mContext, "请选择您想要申请的职位");
                    }
                }
                break;

        }
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

                        int sucNum = Integer.valueOf(jsonObject.getString("data"));
                        postednum = selectSize - sucNum;

                        JobApplyDialogUtil.isApplyJob(mContext, selectSize, postednum);

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
//        param.put("uid", 6 + "");//UserID       userId
        param.put("uid", userId + "");//UserID       userId
        param.put("job_id", jobIds);
        RequestUtils.createRequest(mContext, "", Urls.METHOD_JOB_POST, false, param, true, listener, errorListener);

    }

    @Override
    protected void onStop() {
        super.onStop();


        //hy=>工作行业 职位类别=>job_post 月薪范围=>salary 学历要求=>edu 工作年限=>exp
        // 工作性质=>type

        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "keyword","");
        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "hy", "");
        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "job_post","");
        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "salary", "");
        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "edu", "");
        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "exp", "");
        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "type", "");
        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "cityid", "");//工作城市
        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "fbtime", "");//发布时间
    }
}
