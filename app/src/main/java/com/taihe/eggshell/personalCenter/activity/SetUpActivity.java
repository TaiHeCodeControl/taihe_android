package com.taihe.eggshell.personalCenter.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.login.LoginActivity;
import com.taihe.eggshell.widget.ChoiceDialog;

/**
 * Created by Thinkpad on 2015/7/14.
 */
public class SetUpActivity extends BaseActivity{

    private static final String TAG = "UserInfoActivity";
    private AlertDialog updateDialog = null;
    private Context mContext;
    private String url;
    private int updateCode;

    private ChoiceDialog dialog;

    private RelativeLayout aboutLayout,helpLayout,feedBackLayout,updateLayout,quiteLayout;
    @Override
    public void initView() {
        setContentView(R.layout.activity_system_setup);
        super.initView();
        overridePendingTransition(R.anim.activity_right_to_center, R.anim.activity_center_to_left);

        mContext = this;
        aboutLayout = (RelativeLayout)findViewById(R.id.id_to_about);
        helpLayout = (RelativeLayout)findViewById(R.id.id_to_help);
        feedBackLayout = (RelativeLayout)findViewById(R.id.id_to_feedback);
        updateLayout = (RelativeLayout)findViewById(R.id.id_to_update);
        quiteLayout = (RelativeLayout)findViewById(R.id.id_to_quite);

        dialog = new ChoiceDialog(mContext,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ToastUtils.show(mContext, "取消");
            }
        },new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                EggshellApplication.getApplication().setUser(null);
                Intent intent = new Intent(SetUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        dialog.getTitleText().setText("确定退出当前账号吗？");
        dialog.getLeftButton().setText("以后再说");
        dialog.getRightButton().setText("确认退出");
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
               dialog.show();
                break;
        }
    }
    private void checkUpdate(){

    }
}
