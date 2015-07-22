package com.taihe.eggshell.login;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.utils.ToastUtils;

import static com.taihe.eggshell.base.utils.MyUtils.isMobileNO;


public class RegisterActivity extends BaseActivity {
    private EditText phone_num;
    private EditText password;
    private EditText confirm_password;
    private EditText phone_code;
    private Button btn_register;
    private ImageView iv_getCode;

    private String pwd;
    private String p_num;
    private String p_code;
    private String confirm_pwd;

    @Override
    public void initView() {
        setContentView(R.layout.activity_register);
        super.initView();

        phone_num = (EditText) findViewById(R.id.et_regist_phone);
        phone_code = (EditText) findViewById(R.id.et_regist_code);
        password = (EditText) findViewById(R.id.et_regist_pwd);
        confirm_password = (EditText) findViewById(R.id.et_regist_confirm_pwd);
        btn_register = (Button) findViewById(R.id.btn_regist_register);
        iv_getCode = (ImageView) findViewById(R.id.iv_regist_getcode);

        btn_register.setOnClickListener(this);
        iv_getCode.setOnClickListener(this);
    }

    @Override
    public void initData() {
        initTitle("注册");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.iv_regist_getcode:
                getCode();
                break;
            case R.id.btn_regist_register:
                register();
                break;
        }
    }

    //注册
    private void register() {
        p_code = phone_code.getText().toString();
        p_num = phone_num.getText().toString();
        pwd = password.getText().toString();
        confirm_pwd = confirm_password.getText().toString();
        if (!isMobileNO(p_num)) {
            ToastUtils.show(RegisterActivity.this, "手机号格式不正确");
        } else if(p_code.equals(p_num)){
            ToastUtils.show(RegisterActivity.this, "验证码不正确");

        }else if (pwd.length() < 6) {
            ToastUtils.show(RegisterActivity.this, "密码长度太短");
        } else if (!pwd.equals(confirm_pwd)) {
            ToastUtils.show(RegisterActivity.this, "密码不一致");
        } else
            ToastUtils.show(RegisterActivity.this, "正在登陆");
    }

    //获取短信验证码
    private void getCode() {

    }



}
