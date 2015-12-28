package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.resume.entity.Resumes;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by wang on 2015/8/13.
 */
public class ResumeMultiActivity extends BaseActivity{

    private static final String TAG = "ResumeMultiActivity";

    private Context mContext;
    private TextView resumeName;
    private RelativeLayout userInfo, workExper,educationExper,trainExper,industryTech,projectExper,certBook,selfEvaluation;
    private Intent intent;
    private Resumes resume;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_mutil);
        super.initView();
        mContext = this;

        resumeName = (TextView)findViewById(R.id.id_resume_name);
        userInfo = (RelativeLayout)findViewById(R.id.id_user_info);
        workExper = (RelativeLayout)findViewById(R.id.id_work_exper);
        educationExper = (RelativeLayout)findViewById(R.id.id_edu_exper);
        trainExper = (RelativeLayout)findViewById(R.id.id_train_exper);
        industryTech = (RelativeLayout)findViewById(R.id.id_industy_tech);
        projectExper = (RelativeLayout)findViewById(R.id.id_project_exper);
        certBook = (RelativeLayout)findViewById(R.id.id_conver_book);
        selfEvaluation = (RelativeLayout)findViewById(R.id.id_self_evalu);

        userInfo.setOnClickListener(this);
        workExper.setOnClickListener(this);
        educationExper.setOnClickListener(this);
        trainExper.setOnClickListener(this);
        industryTech.setOnClickListener(this);
        projectExper.setOnClickListener(this);
        certBook.setOnClickListener(this);
        selfEvaluation.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("写简历");
        resume = getIntent().getParcelableExtra("resume");
        resumeName.setText(resume.getName());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_user_info:
                intent = new Intent(mContext,ResumeWriteActivity.class);
                intent.putExtra("eid",resume.getRid()+"");
                startActivity(intent);
                break;
            case R.id.id_work_exper:
                //intent = new Intent(mContext,ResumeWorkActivity.class);
                intent = new Intent(mContext,ResumeListActivity.class);
                intent.putExtra("eid",resume);
                intent.putExtra("title","工作经历");
                startActivity(intent);
                break;
            case R.id.id_edu_exper:
//                intent = new Intent(mContext,ResumeEduActivity.class);
                intent = new Intent(mContext,ResumeListActivity.class);
                intent.putExtra("eid",resume);
                intent.putExtra("title","教育经历");
                startActivity(intent);
                break;
            case R.id.id_train_exper:
//                intent = new Intent(mContext,ResumeTrainActivity.class);
                intent = new Intent(mContext,ResumeListActivity.class);
                intent.putExtra("eid",resume);
                intent.putExtra("title","培训经历");
                startActivity(intent);
                break;
            case R.id.id_industy_tech:
//                intent = new Intent(mContext,ResumeTechActivity.class);
                intent = new Intent(mContext,ResumeListActivity.class);
                intent.putExtra("eid",resume);
                intent.putExtra("title","专业技能");
                startActivity(intent);
                break;
            case R.id.id_project_exper:
//                intent = new Intent(mContext,ResumeProjectActivity.class);
                intent = new Intent(mContext,ResumeListActivity.class);
                intent.putExtra("eid",resume);
                intent.putExtra("title","项目经历");
                startActivity(intent);
                break;
            case R.id.id_conver_book:
//                intent = new Intent(mContext,ResumeBookActivity.class);
                intent = new Intent(mContext,ResumeListActivity.class);
                intent.putExtra("eid",resume);
                intent.putExtra("title","证书");
                startActivity(intent);
                break;
            case R.id.id_self_evalu:
                intent = new Intent(mContext,ResumeSelfActivity.class);
                intent.putExtra("eid",resume);
                startActivity(intent);
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
