package com.taihe.eggshell.personalCenter.activity;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by Thinkpad on 2015/7/14.
 */
public class UserInfoActivity extends BaseActivity{

    private Context mContext;

    private RelativeLayout aboutLayout,helpLayout,feedBackLayout,updateLayout,quiteLayout;
    @Override
    public void initView() {
        setContentView(R.layout.activity_user_info);

        aboutLayout = (RelativeLayout)findViewById(R.id.id_to_about);
        helpLayout = (RelativeLayout)findViewById(R.id.id_to_help);
        feedBackLayout = (RelativeLayout)findViewById(R.id.id_to_feedback);
        updateLayout = (RelativeLayout)findViewById(R.id.id_to_update);
        quiteLayout = (RelativeLayout)findViewById(R.id.id_to_quite);
    }

    @Override
    public void initData() {

        aboutLayout.setOnClickListener(this);
        helpLayout.setOnClickListener(this);
        feedBackLayout.setOnClickListener(this);
        updateLayout.setOnClickListener(this);
        quiteLayout.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_to_about:

                break;
            case R.id.id_to_help:

                break;
            case R.id.id_to_feedback:

                break;
            case R.id.id_to_update:

                break;
            case R.id.id_to_quite:

                break;
        }
    }
}
