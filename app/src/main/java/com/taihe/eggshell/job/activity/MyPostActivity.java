package com.taihe.eggshell.job.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.job.activity.JobDetailActivity;
import com.taihe.eggshell.job.adapter.AllJobAdapter;
import com.taihe.eggshell.job.bean.JobInfo;
import com.taihe.eggshell.personalCenter.adapter.MyPostFragmentPagerAdapter;
import com.taihe.eggshell.widget.JobApplyDialogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by huan on 2015/7/23.
 */
public class MyPostActivity extends BaseActivity{


    private AllJobAdapter adapter;
    private CheckBox cb_selectAll;


    private List<JobInfo> jobInfos = null;
    private JobInfo jobInfo;

    private ListView list_job_all;
    private Context mContext;

    private static final String TAG = "AboutActivity";

    @Override
    public void initView() {
        setContentView(R.layout.activity_job_list);
        super.initView();
        mContext = this;
    }

    @Override
    public void initData() {
        super.initData();
        super.initTitle("收藏职位");
        initListView();
        initListData();
    }


    public void initListView() {
        jobInfos = new ArrayList<JobInfo>();

        for(int i = 0; i < 10;i++){
            jobInfo = new JobInfo(false,i);
            jobInfos.add(jobInfo);
        }
        list_job_all = (ListView)findViewById(R.id.list_job_all);

        list_job_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //listviewItem点击事件

                Intent intent = new Intent(mContext, JobDetailActivity.class);
                startActivity(intent);
            }
        });


    }


    private void initListData() {
        adapter = new AllJobAdapter(mContext,jobInfos,true);
        View footerView = View.inflate(mContext,R.layout.list_job_all_footer,null);


        list_job_all.addFooterView(footerView);

        list_job_all.setAdapter(adapter);

        Button btn_shenqing = (Button)footerView.findViewById(R.id.btn_alljob_shenqing);
        btn_shenqing.setOnClickListener(this);
        cb_selectAll = (CheckBox) footerView.findViewById(R.id.cb_findjob_selectall);

        cb_selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                for (JobInfo info : jobInfos) {
                    info.setIsChecked(isChecked);
                }
                adapter.notifyDataChanged(isChecked);
            }
        });

        adapter.setCheckedListener(new AllJobAdapter.checkedListener() {
            @Override
            public void checkedPosition(int position, boolean isChecked) {
                jobInfos.get(position).setIsChecked(isChecked);
            }
        });

    }





    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_alljob_shenqing:
                JobApplyDialogUtil.isApplyJob(mContext);
                postJob();
                break;
        }
    }

    public void postJob() {
        for(JobInfo jobInfo : jobInfos){
//            System.out.println(jobInfo.getId()+"======"+jobInfo.isChecked());

        }
    }
}
