package com.taihe.eggshell.personalCenter.activity;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by huan on 2015/7/24.
 */
public class MyResumeEditActivity extends BaseActivity{

    @Override
    public void initView() {
        setContentView(R.layout.activity_myresume_edit);
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("编辑个人简历");
    }
}
