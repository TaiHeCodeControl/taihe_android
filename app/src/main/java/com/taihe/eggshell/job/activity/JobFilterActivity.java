package com.taihe.eggshell.job.activity;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by huan on 2015/8/11.
 */
public class JobFilterActivity extends BaseActivity {

    private Intent intent;
    private Context mContext;

    private RelativeLayout rl_industry,rl_position,rl_jobcity,rl_salary,rl_edu,rl_jobyears,rl_jobtype,rl_pubtime;

    private ImageView iv_clear;

    private EditText et_keyWord;

    private TextView tv_industry,tv_position,tv_jobcity,tv_salary,tv_edu,tv_jobyears,tv_jobtype,tv_pubtime;

    @Override
    public void initView() {
        setContentView(R.layout.activity_job_filtert);
        super.initView();

        mContext = this;
        iv_clear = (ImageView) findViewById(R.id.iv_jobfilter_clear);
        iv_clear.setOnClickListener(this);

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
        super.onClick(v);
        switch (v.getId()){
            case R.id.iv_jobfilter_clear:
                et_keyWord.setText("");
                break;
            case R.id.rl_jobfilter_edu://学历要求

                break;
            case R.id.rl_jobfilter_industry://行业类别
                intent = new Intent(mContext,IndustryActivity.class);
                intent.putExtra("Filter","industry");
                startActivity(intent);

                break;
            case R.id.rl_jobfilter_jobcity://工作城市

                break;
            case R.id.rl_jobfilter_jobtype://工作类别

                break;
            case R.id.rl_jobfilter_jobyears://工作经验、工作年限

                intent = new Intent(mContext,IndustryActivity.class);
                intent.putExtra("Filter","jobyears");
                startActivity(intent);
                break;

            case R.id.rl_jobfilter_position://职位类别

                break;
            case R.id.rl_jobfilter_pubtime://发布时间

                break;
            case R.id.rl_jobfilter_salary://薪资要求

                break;
        }
    }
}
