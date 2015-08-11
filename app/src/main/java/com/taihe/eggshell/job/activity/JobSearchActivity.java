package com.taihe.eggshell.job.activity;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by huan on 2015/8/11.
 */
public class JobSearchActivity extends BaseActivity{

    @Override
    public void initView() {
        setContentView(R.layout.activity_job_search);
        super.initView();


    }

    @Override
    public void initData() {
        super.initData();

        initTitle("职位搜索");
    }
}
