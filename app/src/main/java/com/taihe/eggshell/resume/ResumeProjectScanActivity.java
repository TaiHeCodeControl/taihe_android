package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.resume.entity.Resumes;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by wang on 2015/8/15.
 */
public class ResumeProjectScanActivity extends BaseActivity{

    private static final String TAG = "ResumeProjectScanActivity";

    private Context mContext;

    private TextView goonTextView,resume_name;
    private TextView schoolTextView,industyTextView,positionTextView,contextTextView,schoolTimeStart;
    private String schoolName,startTime,endTime,industyName,positionName,contextWord;
    private Resumes eid;
    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_project_scan);
        super.initView();

        mContext = this;

        resume_name = (TextView)findViewById(R.id.id_resume_num);
        goonTextView = (TextView)findViewById(R.id.id_go_on);
        schoolTextView = (TextView)findViewById(R.id.id_company_name);
        industyTextView = (TextView)findViewById(R.id.id_department);
        positionTextView = (TextView)findViewById(R.id.id_position);
        contextTextView = (TextView)findViewById(R.id.id_context);
        schoolTimeStart = (TextView)findViewById(R.id.id_start_time);

        goonTextView.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("写简历");
        Intent intent = getIntent();
        eid = intent.getParcelableExtra("eid");
        resume_name.setText(eid.getName()+"-项目经验");
        schoolName = intent.getStringExtra("name");
        startTime = intent.getStringExtra("sdate");
        endTime = intent.getStringExtra("edate");
        industyName = intent.getStringExtra("specialty");
        positionName = intent.getStringExtra("title");
        contextWord = intent.getStringExtra("content");

        schoolTextView.setText(schoolName);
        industyTextView.setText(industyName);
        positionTextView.setText(positionName);
        contextTextView.setText(contextWord);
        schoolTimeStart.setText(startTime+"到"+endTime);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_go_on:
                Intent intent = new Intent(mContext,ResumeProjectActivity.class);
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
