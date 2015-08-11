package com.taihe.eggshell.job.activity;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.utils.JobApplyDialogUtil;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.adapter.AllJobAdapter;
import com.taihe.eggshell.job.adapter.JobDescAdapter;
import com.taihe.eggshell.job.bean.JobInfo;
import com.taihe.eggshell.widget.ApplyJobDialog;
import com.taihe.eggshell.widget.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang on 2015/8/10.
 */
public class JobDetailActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "JobDetailActivity";
    private Context mContext;
    private TextView titleView,jobtitle,jobcompany,jobstart,jobend,jobtype,joblevel,jobyears,jobaddress,jobmoney,jobnum,updown,jobbrief;
    private Button applyButton;
    private MyListView jobDescListView,moreJobListView;
    private ImageView collectionImg;

    @Override
    public void initView() {

        setContentView(R.layout.activity_job_detail);
        super.initView();
        mContext = this;

        titleView = (TextView)findViewById(R.id.id_title);
        collectionImg = (ImageView)findViewById(R.id.id_other);
        jobtitle = (TextView)findViewById(R.id.id_job_name);
        jobcompany = (TextView)findViewById(R.id.id_job_company);
        jobstart = (TextView)findViewById(R.id.id_start_time);
        jobend = (TextView)findViewById(R.id.id_end_time);
        jobtype = (TextView)findViewById(R.id.id_job_type);
        joblevel = (TextView)findViewById(R.id.id_school_leve);
        jobyears = (TextView)findViewById(R.id.id_work_time);
        jobaddress = (TextView)findViewById(R.id.id_job_addres);
        jobmoney = (TextView)findViewById(R.id.id_money);
        jobnum = (TextView)findViewById(R.id.id_pepple_num);
        updown = (TextView)findViewById(R.id.id_see_all);
        jobbrief = (TextView)findViewById(R.id.id_company_brief);

        jobDescListView = (MyListView)findViewById(R.id.id_job_desc);
        moreJobListView = (MyListView)findViewById(R.id.id_other_position);
        applyButton = (Button)findViewById(R.id.id_apply_button);

        applyButton.setOnClickListener(this);
        collectionImg.setOnClickListener(this);
        updown.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        titleView.setText("职位详情");
        collectionImg.setVisibility(View.VISIBLE);

        List<JobInfo> jobInfos = new ArrayList<JobInfo>();
        for (int i=0;i<2;i++){
            JobInfo jobInfo = new JobInfo(false,i);
            jobInfo.setId(i);
            jobInfos.add(jobInfo);
        }

        AllJobAdapter jobAdapter = new AllJobAdapter(mContext,jobInfos,false);
        moreJobListView.setAdapter(jobAdapter);

        List<String> desc = new ArrayList<String>();
        for (int i=0;i<5;i++){
            desc.add(i+"、你每年考试的记录看到");
        }
        JobDescAdapter jobDescAdapter = new JobDescAdapter(mContext,desc);
        jobDescListView.setAdapter(jobDescAdapter);

        jobbrief.setText("\u3000\u3000" + "深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路深刻的历史的李开复快递费的路口附近道路");
        jobbrief.post(new Runnable() {
            @Override
            public void run() {
                if(jobbrief.getLineCount()>2){
                    jobbrief.setMaxLines(2);
                    updown.setVisibility(View.VISIBLE);
                }else{
                    updown.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_apply_button:
                JobApplyDialogUtil.isApplyJob(mContext);
                break;
            case R.id.id_see_all:
                jobbrief.setMaxLines(Integer.MAX_VALUE);
                updown.setVisibility(View.GONE);
                break;
            case R.id.id_other:
                ToastUtils.show(mContext,"收藏");
                break;
        }
    }
}
