package com.taihe.eggshell.login;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.utils.FormatUtils;
import com.taihe.eggshell.base.utils.ToastUtils;


/**
 * Created by huan on 2015/7/22.
 * 忘记密码界面，密码修改成功转到登录界面
 */
public class ForgetPasswordActivity extends BaseActivity {

    private EditText phone_num;
    private EditText phone_code;
    private TextView getCode,nextStep;

    private String pwd;
    private String p_num;
    private String p_code;
    private String confirm_pwd;

    @Override
    public void initView() {
        setContentView(R.layout.activity_findpassword);
        super.initView();
        overridePendingTransition(R.anim.activity_right_to_center, R.anim.activity_center_to_left);

        phone_num = (EditText) findViewById(R.id.id_phone_num);
        phone_code = (EditText) findViewById(R.id.id_vercode);
        getCode = (TextView) findViewById(R.id.id_get_code);
        nextStep = (TextView) findViewById(R.id.id_next_step);

        getCode.setOnClickListener(this);
        nextStep.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("找回密码");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_get_code:
                getCode();
                break;
            case R.id.id_next_step:
                nextStep();
                break;
        }
    }

    //获取短信验证码
    private void getCode() {
        p_num = phone_num.getText().toString();
        if (!FormatUtils.isMobileNO(p_num)) {
            ToastUtils.show(ForgetPasswordActivity.this, "手机号格式不正确");
            return;
        }

        getCodeFromNet();
    }

    private void nextStep() {
        p_code = phone_code.getText().toString();
        p_num = phone_num.getText().toString();

        if (!FormatUtils.isMobileNO(p_num)) {
            ToastUtils.show(ForgetPasswordActivity.this, "手机号格式不正确");
        } else if (p_code.equals(p_num)) {
            ToastUtils.show(ForgetPasswordActivity.this, "验证码不正确");
        } else{
            ToastUtils.show(ForgetPasswordActivity.this, "正在修改");
            Intent intent = new Intent(ForgetPasswordActivity.this,RestPwdActivity.class);
            startActivity(intent);
            ForgetPasswordActivity.this.finish();
        }
    }

    private void getCodeFromNet(){

        getCode.setEnabled(false);
        TimerCount timerCount = new TimerCount(1000*60,1000);
        timerCount.start();
        getCode.setBackgroundResource(R.drawable.msg_count_start);
    }

    private class TimerCount extends CountDownTimer{

        public TimerCount(long millisInFuture,long countDownInterval){
            super(millisInFuture,countDownInterval);
        }
        @Override
        public void onTick(long l) {
            getCode.setText(l/1000+"秒后重发");
        }

        @Override
        public void onFinish() {
                getCode.setText("获取验证码");
                getCode.setEnabled(true);
                getCode.setBackgroundResource(R.drawable.msg_vercode_background);
        }
    }


}