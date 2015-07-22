package com.taihe.eggshell.login;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.utils.ToastUtils;

import static com.taihe.eggshell.base.utils.MyUtils.isMobileNO;

/**
 * Created by huan on 2015/7/22.
 * 忘记密码界面，密码修改成功转到登录界面
 */
public class ForgetPasswordActivity extends BaseActivity {


    private EditText phone_num;
    private EditText password;
    private EditText confirm_password;
    private EditText phone_code;
    private Button btn_comfirm;
    private ImageView iv_getCode;

    private String pwd;
    private String p_num;
    private String p_code;
    private String confirm_pwd;

    @Override
    public void initView() {
        setContentView(R.layout.activity_findpassword);
        super.initView();
        overridePendingTransition(R.anim.activity_right_to_center, R.anim.activity_center_to_left);

        phone_num = (EditText) findViewById(R.id.et_findpass_phone);
        phone_code = (EditText) findViewById(R.id.et_findpass_code);
        password = (EditText) findViewById(R.id.et_findpass_pwd);
        confirm_password = (EditText) findViewById(R.id.et_findpass_confirm_pwd);
        btn_comfirm = (Button) findViewById(R.id.btn_findpass_confirm);
        iv_getCode = (ImageView) findViewById(R.id.iv_findpass_getcode);

        btn_comfirm.setOnClickListener(this);
        iv_getCode.setOnClickListener(this);
    }

    @Override
    public void initData() {
        initTitle("找回密码");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_findpass_getcode:
                getCode();
                break;
            case R.id.btn_findpass_confirm:
                findPassWord();
                break;
        }
    }

    //找回密码
    private void findPassWord() {
        p_code = phone_code.getText().toString();
        p_num = phone_num.getText().toString();
        pwd = password.getText().toString();
        confirm_pwd = confirm_password.getText().toString();
        if (!isMobileNO(p_num)) {
            ToastUtils.show(ForgetPasswordActivity.this, "手机号格式不正确");
        } else if (p_code.equals(p_num)) {
            ToastUtils.show(ForgetPasswordActivity.this, "验证码不正确");

        } else if (pwd.length() < 6) {
            ToastUtils.show(ForgetPasswordActivity.this, "密码长度太短");
        } else if (!pwd.equals(confirm_pwd)) {
            ToastUtils.show(ForgetPasswordActivity.this, "密码不一致");
        } else{
            ToastUtils.show(ForgetPasswordActivity.this, "正在修改");
            //TODO
            //提交服务器
            //提交成功转回登录界面
            Intent intent = new Intent(ForgetPasswordActivity.this,LoginActivity.class);
            startActivity(intent);
            ForgetPasswordActivity.this.finish();
        }
    }

    //获取短信验证码
    private void getCode() {

    }


}