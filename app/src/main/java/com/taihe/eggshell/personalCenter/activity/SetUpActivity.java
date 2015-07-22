package com.taihe.eggshell.personalCenter.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by Thinkpad on 2015/7/14.
 */
public class SetUpActivity extends BaseActivity{

    private static final String TAG = "UserInfoActivity";
    private AlertDialog updateDialog = null;
    private Context mContext;
    private String url;
    private int updateCode;

    private RelativeLayout aboutLayout,helpLayout,feedBackLayout,updateLayout,quiteLayout;
    @Override
    public void initView() {
        setContentView(R.layout.activity_system_setup);
        super.initView();

        mContext = this;
        aboutLayout = (RelativeLayout)findViewById(R.id.id_to_about);
        helpLayout = (RelativeLayout)findViewById(R.id.id_to_help);
        feedBackLayout = (RelativeLayout)findViewById(R.id.id_to_feedback);
        updateLayout = (RelativeLayout)findViewById(R.id.id_to_update);
        quiteLayout = (RelativeLayout)findViewById(R.id.id_to_quite);
    }

    @Override
    public void initData() {
        super.initData();
        super.initTitle("个人中心");

        aboutLayout.setOnClickListener(this);
        helpLayout.setOnClickListener(this);
        feedBackLayout.setOnClickListener(this);
        updateLayout.setOnClickListener(this);
        quiteLayout.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){
            case R.id.id_to_about:
                Intent aboutIntent = new Intent(mContext,AboutActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.id_to_help:
                Intent HelpIntent = new Intent(mContext,HelpActivity.class);
                startActivity(HelpIntent);
                break;
            case R.id.id_to_feedback:

                break;
            case R.id.id_to_update:
                checkUpdate();
                break;
            case R.id.id_to_quite:

                break;
        }
    }
    private void checkUpdate(){

    }
}
