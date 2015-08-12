package com.taihe.eggshell.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.main.MainActivity;

/**
 * Created by wang on 2015/8/12.
 */
public class RestPwdActivity extends BaseActivity implements View.OnClickListener{

    private EditText pwdone;
    private EditText pwdtwo;
    private TextView resetpwd;

    @Override
    public void initView() {
        setContentView(R.layout.activity_restpwd);
        super.initView();

        pwdone = (EditText) findViewById(R.id.id_pwd_one);
        pwdtwo = (EditText) findViewById(R.id.id_pwd_two);
        resetpwd = (TextView) findViewById(R.id.id_rest_pwd);

        resetpwd.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("找回密码");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_rest_pwd:
                resetPwd();
                break;
        }
    }

    private void resetPwd(){
        String onepwd = pwdone.getText().toString();
        String twopwd = pwdtwo.getText().toString();
        if(TextUtils.isEmpty(onepwd) || TextUtils.isEmpty(twopwd)){
            return;
        }else if(!onepwd.equals(twopwd)){
            ToastUtils.show(this,"密码不一致");
            return;
        }else{
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
