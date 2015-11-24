package com.taihe.eggshell.login;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.FormatUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by huan on 2015/7/22.
 * 忘记密码界面，密码修改成功转到登录界面
 */
public class ForgetPasswordActivity extends BaseActivity {

    private Context mContext;

    private EditText phone_num;
    private EditText phone_code;
    private TextView getCode,nextStep,loginType;
    private LoadingProgressDialog loading;

    private String pwd,ltype;
    private String p_num;
    private String p_code;
    private String confirm_pwd;

    @Override
    public void initView() {
        setContentView(R.layout.activity_findpassword);
        super.initView();
        overridePendingTransition(R.anim.activity_right_to_center, R.anim.activity_center_to_left);

        mContext = this;
        phone_num = (EditText) findViewById(R.id.id_phone_num);
        phone_code = (EditText) findViewById(R.id.id_vercode);
        getCode = (TextView) findViewById(R.id.id_get_code);
        nextStep = (TextView) findViewById(R.id.id_next_step);

        loginType = (TextView) findViewById(R.id.id_name_type);

        getCode.setOnClickListener(this);
        nextStep.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("找回密码");

        ltype = getIntent().getStringExtra("usertype");
        if(ltype.equals("1")){//个人
            loginType.setText("手机号");
            phone_num.setHint("请输入手机号");
        }else if(ltype.equals("2")){//企业
            loginType.setText("邮  箱");
            phone_num.setHint("请输入企业邮箱");
        }

        loading = new LoadingProgressDialog(mContext, getResources().getString(
                R.string.submitcertificate_string_wait_dialog));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_get_code:
                if(ltype.equals("1")){
                    getCode();
                }else if(ltype.equals("2")){
                    getEmailCode();
                }
                break;
            case R.id.id_next_step:
                if(ltype.equals("1")){
                    nextStep();
                }else if(ltype.equals("2")){
                    loginType.setText("邮  箱");
                    phone_num.setHint("请输入企业邮箱");
                }
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
        if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)){
            loading.show();
            getCodeFromNet();
        }else{
            ToastUtils.show(mContext,R.string.check_network);
        }
    }

    //获取邮箱验证码
    private void getEmailCode() {
        p_num = phone_num.getText().toString();
        if (!FormatUtils.isEmail(p_num)) {
            ToastUtils.show(ForgetPasswordActivity.this, "邮箱格式不正确");
            return;
        }
        if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)){
            loading.show();
            getCodeFromNet();
        }else{
            ToastUtils.show(mContext,R.string.check_network);
        }
    }

    private void nextStep() {
        p_code = phone_code.getText().toString();
        p_num = phone_num.getText().toString();

        if (!FormatUtils.isMobileNO(p_num)) {
            ToastUtils.show(ForgetPasswordActivity.this, "手机号格式不正确");
        } else if (p_code.equals(p_num)) {
            ToastUtils.show(ForgetPasswordActivity.this, "验证码不正确");
        } else{
            if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
                loading.show();
                getCheckCode();
            }else{
                ToastUtils.show(mContext,R.string.check_network);
            }
        }
    }

    private void getCodeFromNet(){

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject((String)o);

                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if(code ==0){
                        ToastUtils.show(mContext,"发送成功");
                        getCode.setEnabled(false);
                        TimerCount timerCount = new TimerCount(1000*60,1000);
                        timerCount.start();
                        getCode.setBackgroundResource(R.drawable.msg_count_start);
                    }else{
                        ToastUtils.show(mContext,"获取失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loading.dismiss();
                if(null!=volleyError.networkResponse.data){
                    Log.v("Forget:",new String(volleyError.networkResponse.data));
                }
                ToastUtils.show(mContext,"网络异常");
            }
        };

        Map<String,String> param = new HashMap<String, String>();
        param.put("telphone",p_num);

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(),Urls.METHOD_GET_CODE,false,param,true,listener,errorListener);
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


    private void getCheckCode(){
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                loading.dismiss();
                try {
                    Log.v("TAD:",(String)o);
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if(code ==0){
                        Intent intent = new Intent(ForgetPasswordActivity.this,RestPwdActivity.class);
                        intent.putExtra("phonenum",p_num);
                        intent.putExtra("type",ltype);//1个人，2企业
                        startActivity(intent);
                        ForgetPasswordActivity.this.finish();
                    }else{
                        ToastUtils.show(mContext,jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loading.dismiss();
                if(null!=volleyError.networkResponse.data){
                    Log.v("Forget:",new String(volleyError.networkResponse.data));
                }
                ToastUtils.show(mContext,"网络异常");
            }
        };

        Map<String,String> param = new HashMap<String, String>();
        param.put("telphone",p_num);
        param.put("code",p_code);
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(),Urls.METHOD_CHECK_CODE,false,param,true,listener,errorListener);

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}