package com.taihe.eggshell.resume;

import android.content.Context;
import android.view.View;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by wang on 2015/8/14.
 */
public class ResumeWorkActivity extends BaseActivity{

    private static final String TAG = "ResumeWorkActivity";

    private Context mContext;
    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_work);
        super.initView();

        mContext = this;
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
