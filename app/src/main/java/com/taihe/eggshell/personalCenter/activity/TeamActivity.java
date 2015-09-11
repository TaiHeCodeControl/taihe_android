package com.taihe.eggshell.personalCenter.activity;


import android.content.Context;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by huan on 2015/8/17.
 */
public class TeamActivity extends BaseActivity{

    private Context mContext;
    @Override
    public void initView() {
        setContentView(R.layout.activity_hezuoqudao);
        super.initView();

        mContext = this;
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("合作渠道");
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
