package com.taihe.eggshell.personalCenter.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Constants;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.utils.MyUtils;
import com.taihe.eggshell.base.utils.RequestUtils;

import org.apache.http.client.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
        setContentView(R.layout.activity_user_info);
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
