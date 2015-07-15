package com.taihe.eggshell.login;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.utils.MyUtils;
import com.taihe.eggshell.base.utils.ToastUtils;

public class LoginActivity extends BaseActivity {

    // titleid
    @ViewInject(R.id.btn_left)
    private Button btn_left;
    @ViewInject(R.id.btn_right)
    private Button btn_right;
    @ViewInject(R.id.txt_title)
    private TextView txt_title;
    @ViewInject(R.id.imgbtn_left)
    private ImageButton imgbtn_left;
    @ViewInject(R.id.imgbtn_text)
    private ImageButton imgbtn_text;
    @ViewInject(R.id.imgbtn_right)
    private ImageButton imgbtn_right;

    @ViewInject(R.id.et_login_userphone)
    private EditText et_userphone;
    @ViewInject(R.id.et_login_password)
    private EditText et_password;
    @ViewInject(R.id.btn_login_login)
    private Button btn_login;

    private String userphone;
    private String password;

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
        initTitleBar();

        btn_login.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    public void initTitleBar(){

        btn_left.setVisibility(View.GONE);
        btn_right.setVisibility(View.GONE);
        imgbtn_text.setVisibility(View.GONE);
        imgbtn_right.setVisibility(View.INVISIBLE);
        imgbtn_left.setVisibility(View.INVISIBLE);
        txt_title.setText(R.string.login_title);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
           case R.id.btn_login_login:
            login();
            break;
        }
    }

    public void login(){
        userphone = et_userphone.getText().toString().trim();
        password = et_password.getText().toString().trim();
        if(TextUtils.isEmpty(userphone)||TextUtils.isEmpty(password)){
            ToastUtils.show(getApplicationContext(),R.string.login_login_toast);
        }else if(MyUtils.isMobileNO(userphone)){
            ToastUtils.show(getApplicationContext(),R.string.login_login_phone_toast);
        }



    }
}
