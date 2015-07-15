package com.taihe.eggshell.personalCenter.activity;

import android.content.Context;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by Thinkpad on 2015/7/15.
 */
public class FeedbackActivity extends BaseActivity{

    private static final String TAG = "FeedbackActivity";
    private Context mContext;

    @Override
    public void initView() {
        setContentView(R.layout.activity_feedback);
        super.initView();

    }

    @Override
    public void initData() {
        super.initData();
        super.initTitle("意见反馈");
    }
}
