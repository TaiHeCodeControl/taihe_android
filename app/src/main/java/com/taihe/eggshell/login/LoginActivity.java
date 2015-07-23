package com.taihe.eggshell.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.utils.FormatUtils;

import android.widget.ImageView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.main.MainActivity;
import com.taihe.eggshell.main.entity.User;

/**
 *
 */
public class LoginActivity extends BaseActivity {

    private EditText et_userphone;
    private EditText et_password;
    private Button btn_login;
    private TextView tv_forgetPassword;
    private TextView tv_regist;
    private ImageView iv_back;

    private String userphone;
    private String password;

    private Intent intent;

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
        super.initView();
        overridePendingTransition(R.anim.activity_right_to_center, R.anim.activity_center_to_left);

        et_userphone = (EditText) findViewById(R.id.et_login_userphone);
        et_password = (EditText) findViewById(R.id.et_login_password);
        btn_login = (Button) findViewById(R.id.btn_login_login);
        tv_forgetPassword = (TextView) findViewById(R.id.tv_login_forgetpassword);
        tv_regist = (TextView) findViewById(R.id.tv_login_regist);
        iv_back = (ImageView) findViewById(R.id.id_back);

        iv_back.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_forgetPassword.setOnClickListener(this);
        tv_regist.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();

        initTitle("登录");

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_back:
                LoginActivity.this.finish();
                break;
            case R.id.btn_login_login:
                login();
                break;
            case R.id.tv_login_forgetpassword:
                intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_login_regist:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
//                this.finish();
                break;
        }
    }

    public void login() {
        userphone = et_userphone.getText().toString().trim();
        password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(userphone) || TextUtils.isEmpty(password)) {
            ToastUtils.show(getApplicationContext(), R.string.login_login_toast);
            return;
        }
        if (!FormatUtils.isMobileNO(userphone)) {
            ToastUtils.show(getApplicationContext(), R.string.login_login_phone_toast);
            return;
        }

        //保存用户登录信息
//        EggshellApplication.getApplication().setUser(new User(1,"hh","18810309233"));
//        User user = EggshellApplication.getApplication().getUser();
//        System.out.println(user + "save=====================");
        PrefUtils.saveStringPreferences(getApplicationContext(), PrefUtils.CONFIG, PrefUtils.KEY_USER_JSON, "{id,1,name,'xx',phoneNumber:'89898'}");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();

    }
}
