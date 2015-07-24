package com.taihe.eggshell.personalCenter.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.login.LoginActivity;
import com.taihe.eggshell.widget.ChoiceDialog;

/**
 * Created by Thinkpad on 2015/7/14.
 */
public class SetUpActivity extends BaseActivity {

    private static final String TAG = "UserInfoActivity";
    private AlertDialog updateDialog = null;
    private Context mContext;
    private String url;
    private int updateCode;

    private ChoiceDialog dialog;

    private RelativeLayout aboutLayout, helpLayout, feedBackLayout, updateLayout, changePwd;
    private Button btn_logout;

    @Override
    public void initView() {
        setContentView(R.layout.activity_system_setup);
        super.initView();
        overridePendingTransition(R.anim.activity_right_to_center, R.anim.activity_center_to_left);

        mContext = this;
        aboutLayout = (RelativeLayout) findViewById(R.id.rl_set_about);
        helpLayout = (RelativeLayout) findViewById(R.id.rl_set_help);
        feedBackLayout = (RelativeLayout) findViewById(R.id.rl_set_feedback);
        updateLayout = (RelativeLayout) findViewById(R.id.rl_set_update);

        changePwd = (RelativeLayout) findViewById(R.id.rl_set_changepwd);
        btn_logout = (Button) findViewById(R.id.btn_set_logout);

        dialog = new ChoiceDialog(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ToastUtils.show(mContext, "取消");
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                EggshellApplication.getApplication().setUser(null);
                PrefUtils.saveStringPreferences(getApplicationContext(), PrefUtils.CONFIG, PrefUtils.KEY_USER_JSON, "");

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
        changePwd.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.rl_set_about://关于我们
                Intent aboutIntent = new Intent(mContext, AboutActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.rl_set_help://使用帮助
                Intent HelpIntent = new Intent(mContext, HelpActivity.class);
                startActivity(HelpIntent);
                break;
            case R.id.rl_set_feedback://意见反馈

                break;
            case R.id.rl_set_update://检查更新
                checkUpdate();
                break;
            case R.id.rl_set_changepwd://更改密码

                break;
            case R.id.btn_set_logout:
                dialog.show();
                break;
        }
    }

    private void checkUpdate() {

    }
}
