package com.taihe.eggshell.login;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.utils.FormatUtils;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.main.MainActivity;
import com.taihe.eggshell.main.entity.User;

/**
 * 注册成功直接登录
 */
public class RegisterActivity extends BaseActivity {
    private EditText phone_num;
    private EditText password;
    private EditText confirm_password;
    private EditText phone_code;
    private Button btn_register;
    private ImageView iv_getCode;
    private ImageView iv_back;

    private String pwd;
    private String p_num;
    private String p_code;
    private String confirm_pwd;

    @Override
    public void initView() {
        setContentView(R.layout.activity_register);
        super.initView();
//        overridePendingTransition(R.anim.activity_right_to_center, R.anim.activity_center_to_left);

        phone_num = (EditText) findViewById(R.id.et_regist_phone);
        phone_code = (EditText) findViewById(R.id.et_regist_code);
        password = (EditText) findViewById(R.id.et_regist_pwd);
        confirm_password = (EditText) findViewById(R.id.et_regist_confirm_pwd);
        btn_register = (Button) findViewById(R.id.btn_regist_register);
        iv_getCode = (ImageView) findViewById(R.id.iv_regist_getcode);
        iv_back = (ImageView) findViewById(R.id.id_back);

        iv_back.setOnClickListener(this);
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
            case R.id.id_back:
                RegisterActivity.this.finish();
                break;
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
        if (!FormatUtils.isMobileNO(p_num)) {
            ToastUtils.show(RegisterActivity.this, "手机号格式不正确");
        } else if(!p_code.equals(p_num)){
            ToastUtils.show(RegisterActivity.this, "验证码不正确");

        }else if (pwd.length() < 6) {
            ToastUtils.show(RegisterActivity.this, "密码长度太短");
        } else if (!pwd.equals(confirm_pwd)) {
            ToastUtils.show(RegisterActivity.this, "密码不一致");
        } else{

            ToastUtils.show(RegisterActivity.this, "正在注册中...");
            //TODO
            //服务器注册
            //注册成功自动登录跳转到信息完善界面
            //保存用户登录信息
//            EggshellApplication.getApplication().setUser(new User(1,"hh","18810309233"));
            PrefUtils.saveStringPreferences(getApplicationContext(), PrefUtils.CONFIG, PrefUtils.KEY_USER_JSON, "{id,1,name,'xx',phoneNumber:'89898'}");
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            RegisterActivity.this.finish();
        }
    }

    //获取短信验证码
    private void getCode() {

    }



}
