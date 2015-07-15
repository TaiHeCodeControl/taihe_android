package com.taihe.eggshell.personalCenter.activity;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by Thinkpad on 2015/7/15.
 */
public class HelpActivity extends BaseActivity{

    @Override
    public void initView() {
        setContentView(R.layout.activity_help);
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
        super.initTitle("使用帮助");
    }
}
