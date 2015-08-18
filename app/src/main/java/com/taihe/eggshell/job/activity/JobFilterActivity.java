package com.taihe.eggshell.job.activity;


import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.main.entity.Industry;

/**
 * Created by huan on 2015/8/11.
 */
public class JobFilterActivity extends BaseActivity {


    private Intent intent;
    private Context mContext;
    private RelativeLayout rl_industry, rl_position, rl_jobcity, rl_salary, rl_edu, rl_jobyears, rl_jobtype, rl_pubtime;
    private ImageView iv_clear;
    private EditText et_keyWord;
    private TextView tv_industry, tv_position, tv_jobcity, tv_salary, tv_edu, tv_jobyears, tv_jobtype, tv_pubtime;
    private Button btn_jobfilter_chaxun;

    private LinearLayout lin_back;

    private static final int REQUEST_CODE_INDUSTRYTYPE = 100;
    private static final int REQUEST_CODE_JOBYEAR = 101;
    private static final int REQUEST_CODE_EDU = 102;
    private static final int REQUEST_CODE_POSITION = 103;
    private static final int REQUEST_CODE_JOBCITY = 104;
    private static final int REQUEST_CODE_SALARY = 105;
    private static final int REQUEST_CODE_JOBTYPE = 106;
    private static final int REQUEST_CODE_PUBTIME = 107;

    @Override
    public void initView() {
        setContentView(R.layout.activity_job_filtert);
        super.initView();

        mContext = this;
        iv_clear = (ImageView) findViewById(R.id.iv_jobfilter_clear);
        iv_clear.setOnClickListener(this);

        lin_back = (LinearLayout) findViewById(R.id.lin_back);
        lin_back.setOnClickListener(this);

        btn_jobfilter_chaxun = (Button) findViewById(R.id.btn_jobfilter_chaxun);
        btn_jobfilter_chaxun.setOnClickListener(this);

        et_keyWord = (EditText) findViewById(R.id.et_jobfilter_keyword);

        tv_industry = (TextView) findViewById(R.id.tv_jobfilter_industry);
        tv_position = (TextView) findViewById(R.id.tv_jobfilter_position);
        tv_jobcity = (TextView) findViewById(R.id.tv_jobfilter_jobcity);
        tv_salary = (TextView) findViewById(R.id.tv_jobfilter_salary);
        tv_edu = (TextView) findViewById(R.id.tv_jobfilter_edu);
        tv_jobyears = (TextView) findViewById(R.id.tv_jobfilter_jobyears);
        tv_jobtype = (TextView) findViewById(R.id.tv_jobfilter_jobtype);
        tv_pubtime = (TextView) findViewById(R.id.tv_jobfilter_pubtime);


        rl_industry = (RelativeLayout) findViewById(R.id.rl_jobfilter_industry);
        rl_industry.setOnClickListener(this);

        rl_position = (RelativeLayout) findViewById(R.id.rl_jobfilter_position);
        rl_position.setOnClickListener(this);

        rl_jobcity = (RelativeLayout) findViewById(R.id.rl_jobfilter_jobcity);
        rl_jobcity.setOnClickListener(this);
        rl_salary = (RelativeLayout) findViewById(R.id.rl_jobfilter_salary);
        rl_salary.setOnClickListener(this);

        rl_edu = (RelativeLayout) findViewById(R.id.rl_jobfilter_edu);
        rl_edu.setOnClickListener(this);

        rl_jobyears = (RelativeLayout) findViewById(R.id.rl_jobfilter_jobyears);
        rl_jobyears.setOnClickListener(this);

        rl_jobtype = (RelativeLayout) findViewById(R.id.rl_jobfilter_jobtype);
        rl_jobtype.setOnClickListener(this);

        rl_pubtime = (RelativeLayout) findViewById(R.id.rl_jobfilter_pubtime);
        rl_pubtime.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();

        initTitle("职位筛选");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_back:
                JobFilterActivity.this.finish();
                break;
            case R.id.iv_jobfilter_clear:
                et_keyWord.setText("");
                break;
            case R.id.rl_jobfilter_edu://学历要求
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "edu");
                startActivityForResult(intent, REQUEST_CODE_EDU);
                break;
            case R.id.rl_jobfilter_industry://行业类别
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "industry");
                startActivityForResult(intent, REQUEST_CODE_INDUSTRYTYPE);
                break;
            case R.id.rl_jobfilter_jobcity://工作城市
//                intent = new Intent(mContext,IndustryActivity.class);
//                intent.putExtra("Filter", "jobcity");
//                startActivityForResult(intent,REQUEST_CODE_JOBCITY);
                break;
            case R.id.rl_jobfilter_jobtype://工作类别
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "jobtype");
                startActivityForResult(intent, REQUEST_CODE_JOBTYPE);
                break;
            case R.id.rl_jobfilter_jobyears://工作经验、工作年限

                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "jobyears");
                startActivityForResult(intent, REQUEST_CODE_JOBYEAR);
                break;

            case R.id.rl_jobfilter_position://职位类别
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "position");
                startActivityForResult(intent, REQUEST_CODE_POSITION);
                break;
            case R.id.rl_jobfilter_pubtime://发布时间
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "pubtime");
                startActivityForResult(intent, REQUEST_CODE_PUBTIME);
                break;
            case R.id.rl_jobfilter_salary://薪资要求

                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "salary");
                startActivityForResult(intent, REQUEST_CODE_SALARY);
                break;


            case R.id.btn_jobfilter_chaxun://查询职位

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String str = data.getStringExtra("data");
        if (REQUEST_CODE_INDUSTRYTYPE == requestCode && IndustryActivity.RESULT_CODE_INDUSTRYTYPE == resultCode) {
            tv_industry.setText(str); //行业类型
        }

        if (REQUEST_CODE_POSITION == requestCode && IndustryActivity.RESULT_CODE_POSITION == resultCode) {
            tv_position.setText(str);//职位类别
        }


        if (REQUEST_CODE_JOBYEAR == requestCode && IndustryActivity.RESULT_CODE_JOBYEAR == resultCode) {
            tv_jobyears.setText(str);//工作年限
        }

        if (REQUEST_CODE_SALARY == requestCode && IndustryActivity.RESULT_CODE_SALARY == resultCode) {
            tv_salary.setText(str);//薪资要求
        }

        if (REQUEST_CODE_EDU == requestCode && IndustryActivity.RESULT_CODE_EDU == resultCode) {
            tv_edu.setText(str);//学历要求
        }

        if (REQUEST_CODE_JOBCITY == requestCode && IndustryActivity.RESULT_CODE_JOBCITY == resultCode) {
            tv_jobcity.setText(str);//工作城市
        }

        if (REQUEST_CODE_JOBTYPE == requestCode && IndustryActivity.RESULT_CODE_JOBTYPE == resultCode) {
            tv_jobtype.setText(str); //工作类型
        }


        if (REQUEST_CODE_PUBTIME == requestCode && IndustryActivity.RESULT_CODE_PUBTIME == resultCode) {
            tv_pubtime.setText(str);//发布时间
        }


    }
}
