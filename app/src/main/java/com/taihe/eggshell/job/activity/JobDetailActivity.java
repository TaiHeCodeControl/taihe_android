package com.taihe.eggshell.job.activity;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

import java.util.List;

/**
 * Created by wang on 2015/8/10.
 */
public class JobDetailActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "JobDetailActivity";
    private Context mContext;
    private TextView jobtitle,jobcompany,jobstart,jobend,jobtype,joblevel,jobyears,jobaddress,jobmoney,jobnum,updown,jobbrief;
    private Button applyButton;
    private ListView jobDescListView,moreJobListView;

    @Override
    public void initView() {

        setContentView(R.layout.activity_job_detail);
        super.initView();
        mContext = this;

        jobtitle = (TextView)findViewById(R.id.id_job_name);
        jobcompany = (TextView)findViewById(R.id.id_job_name);
        jobstart = (TextView)findViewById(R.id.id_job_name);
        jobend = (TextView)findViewById(R.id.id_job_name);
        jobtype = (TextView)findViewById(R.id.id_job_name);
        joblevel = (TextView)findViewById(R.id.id_job_name);
        jobyears = (TextView)findViewById(R.id.id_job_name);
        jobaddress = (TextView)findViewById(R.id.id_job_name);
        jobmoney = (TextView)findViewById(R.id.id_job_name);
        jobnum = (TextView)findViewById(R.id.id_job_name);
        updown = (TextView)findViewById(R.id.id_job_name);
        jobbrief = (TextView)findViewById(R.id.id_job_name);

        jobDescListView = (ListView)findViewById(R.id.id_job_desc);
        moreJobListView = (ListView)findViewById(R.id.id_other_position);
        applyButton = (Button)findViewById(R.id.id_apply_button);

        applyButton.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();

//        JobDescAdapter jobDescAdapter = new JobDescAdapter(mContext);
//        jobDescListView.setAdapter(jobDescAdapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_apply_button:
                break;
            case R.id.id_see_all:
                break;
        }
    }
}
