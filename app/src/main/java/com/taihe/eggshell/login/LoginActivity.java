package com.taihe.eggshell.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.utils.FormatUtils;
import com.taihe.eggshell.base.utils.ToastUtils;

/**
 *
 */
public class LoginActivity extends BaseActivity {

    private EditText et_userphone;
    private EditText et_password;
    private Button btn_login;

    private String userphone;
    private String password;

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
        super.initView();

        et_userphone = (EditText)findViewById(R.id.et_login_userphone);
        et_password = (EditText)findViewById(R.id.et_login_password);
        btn_login = (Button)findViewById(R.id.btn_login_login);
    }

    @Override
    public void initData() {
        super.initData();

        initTitle("登录");
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
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
            return;
        }
        if(!FormatUtils.isMobileNO(userphone)){
            ToastUtils.show(getApplicationContext(),R.string.login_login_phone_toast);
            return;
        }



    }
}
