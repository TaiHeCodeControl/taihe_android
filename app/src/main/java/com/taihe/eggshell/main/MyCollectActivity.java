package com.taihe.eggshell.main;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.job.activity.JobDetailActivity;
import com.taihe.eggshell.job.adapter.AllJobAdapter;
import com.taihe.eggshell.job.bean.JobInfo;
import com.taihe.eggshell.widget.JobApplyDialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huan on 2015/8/14.
 */
public class MyCollectActivity extends BaseActivity {


    private AllJobAdapter adapter;
    private CheckBox cb_selectAll;


    private List<JobInfo> jobInfos = null;
    private JobInfo jobInfo;

    private ListView list_job_all;
    private Context mContext;

    private LinearLayout lin_back;
    private static final String TAG = "MyCollectActivity";

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
        lin_back = (LinearLayout) findViewById(R.id.lin_back);
        lin_back.setOnClickListener(this);

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
        View footerView = View.inflate(mContext,R.layout.list_job_collect_footer,null);


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
            case R.id.lin_back:
                goBack();

                break;
            case R.id.btn_alljob_shenqing:
                JobApplyDialogUtil.isApplyJob(mContext);
                postJob();
                break;
        }
    }

    private void goBack() {
        Intent intent = new Intent(MyCollectActivity.this, MainActivity.class);
        intent.putExtra("MeFragment", "MeFragment");
        startActivity(intent);
    }

    public void postJob() {
        for(JobInfo jobInfo : jobInfos){
//            System.out.println(jobInfo.getId()+"======"+jobInfo.isChecked());

        }
    }


    //监听返回按钮
    @Override
    public void onBackPressed() {
        goBack();
    }
}
