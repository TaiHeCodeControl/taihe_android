package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.resume.entity.Resumes;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by wang on 2015/8/15.
 */
public class ResumeWorkScanActivity extends BaseActivity{

    private static final String TAG = "ResumeWorkScanActivity";

    private Context mContext;

    private TextView commitText,resume_name;
    private TextView companyTextView,departTextView,positionTextView,contextTextView,workTimeStart;
    private String companyName,startTime,endTime,departName,positionName,contextWord;
    private Resumes eid;
    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_work_scan);
        super.initView();

        mContext = this;

        resume_name = (TextView)findViewById(R.id.id_resume_num);
        commitText = (TextView)findViewById(R.id.id_go_on);
        companyTextView = (TextView)findViewById(R.id.id_company_name);
        departTextView = (TextView)findViewById(R.id.id_department);
        positionTextView = (TextView)findViewById(R.id.id_position);
        contextTextView = (TextView)findViewById(R.id.id_context);
        workTimeStart = (TextView)findViewById(R.id.id_start_time);

        commitText.setOnClickListener(this);

    }

    @Override
    public void initData() {
        super.initData();
        initTitle("写简历");
        Intent intent = getIntent();
        eid = intent.getParcelableExtra("eid");
        resume_name.setText(eid.getName()+"-工作经历");
        companyName = intent.getStringExtra("name");
        startTime = intent.getStringExtra("sdate");
        endTime = intent.getStringExtra("edate");
        departName = intent.getStringExtra("department");
        positionName = intent.getStringExtra("title");
        contextWord = intent.getStringExtra("content");

        companyTextView.setText(companyName);
        departTextView.setText(departName);
        positionTextView.setText(positionName);
        contextTextView.setText(contextWord);
        workTimeStart.setText(startTime+"到"+endTime);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_go_on:
                Intent intent = new Intent(mContext,ResumeWorkActivity.class);
                intent.putExtra("eid",eid);
                startActivity(intent);
                finish();
                break;
        }
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
