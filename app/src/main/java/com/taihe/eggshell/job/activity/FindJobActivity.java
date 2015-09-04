package com.taihe.eggshell.job.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.adapter.AllJobAdapter;
import com.taihe.eggshell.job.bean.JobInfo;
import com.taihe.eggshell.job.fragment.AllJobFragment;
import com.taihe.eggshell.job.fragment.FujinFragment;
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

    private Intent intent;

    private TextView tv_allJob, tv_fujin;
    private ImageView iv_quancheng, iv_fujin, iv_filter, iv_search;
    private RelativeLayout rl_qc,rl_fujin, iv_back;


    private AllJobAdapter adapter;
    public CheckBox cb_selectAll;

    public List<JobInfo> jobInfos = null;
    private JobInfo jobInfo;

    private PullToRefreshGridView list_job_all;
    private Context mContext;
    private LoadingProgressDialog dialog;
    private int page = 1;
    private int pageSize = 10;
    //选中条数的统计
    private int selectSize = 0;
    private int postednum = 0;

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

        list_job_all = (PullToRefreshGridView)findViewById(R.id.list_alljob_all);

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
                    intent.putExtra("ID",job.getId());
                    intent.putExtra("UID",job.getUid());
                    Log.i("ID", job.getId() + "");
                    startActivity(intent);
                }

            }
        });

        Button btn_shenqing = (Button)findViewById(R.id.btn_alljob_shenqing);
        btn_shenqing.setOnClickListener(this);
        cb_selectAll = (CheckBox)findViewById(R.id.cb_findjob_selectall);

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
        if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)){
            dialog = new LoadingProgressDialog(mContext, getResources().getString(
                    R.string.submitcertificate_string_wait_dialog));
            dialog.show();
            getList();
        }else{
            ToastUtils.show(mContext, R.string.check_network);
        }


    }

    private void getList(){

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                dialog.dismiss();
                try {
                    Log.v("HHH:",(String)o);

                    JSONObject jsonObject = new JSONObject((String)o);

                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if(code ==0){
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        List<JobInfo> joblist =  gson.fromJson(data,new TypeToken<List<JobInfo>>(){}.getType());
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
                try{
                    if(null!=volleyError.networkResponse.data){
                        Log.v("Forget:", new String(volleyError.networkResponse.data));
                    }
                    ToastUtils.show(mContext,volleyError.networkResponse.statusCode+"");

                }catch(Exception e){
                    ToastUtils.show(mContext,"联网失败");
                }

            }
        };

        Map<String,String> param = new HashMap<String, String>();
        param.put("page",page+"");
        param.put("limit",pageSize+"");

        RequestUtils.createRequest(mContext, "http://195.198.1.83/eggker/interface", Urls.METHOD_JOB_LIST, false, param, true, listener, errorListener);

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_findjob_back:
                FindJobActivity.this.finish();
                break;
            case R.id.rl_findjob_qc:
                jobInfos.clear();
                getList();
                iv_quancheng.setImageResource(R.drawable.quancheng01);
                iv_fujin.setImageResource(R.drawable.fujin01);

                tv_allJob.setTextColor(getResources().getColor(R.color.font_color_red));
                tv_fujin.setTextColor(getResources().getColor(R.color.font_color_black));
                break;
            case R.id.rl_findjob_fujin:
                jobInfos.clear();
                getList();
                iv_quancheng.setImageResource(R.drawable.quancheng02);
                iv_fujin.setImageResource(R.drawable.fujin02);

                tv_allJob.setTextColor(getResources().getColor(R.color.font_color_black));
                tv_fujin.setTextColor(getResources().getColor(R.color.font_color_red));
                break;
            case R.id.iv_findjob_search:
                intent = new Intent(FindJobActivity.this,JobSearchActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_findjob_filter:
                intent = new Intent(FindJobActivity.this, JobFilterActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_alljob_shenqing:
                JobApplyDialogUtil.isApplyJob(mContext, selectSize, postednum);
                postJob();//申请职位
                break;

        }
    }


    public void postJob() {
        for (JobInfo jobInfo : jobInfos) {
            System.out.println(jobInfo.getId() + "======" + jobInfo.isChecked());

        }
    }


}
