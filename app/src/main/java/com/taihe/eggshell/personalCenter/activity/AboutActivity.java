package com.taihe.eggshell.personalCenter.activity;

import android.util.Log;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by Thinkpad on 2015/7/14.
 */
public class AboutActivity extends BaseActivity{

    private static final String TAG = "AboutActivity";

    @Override
    public void initView() {
        setContentView(R.layout.activity_about);
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
        super.initTitle("关于蛋壳儿");
    }
}
