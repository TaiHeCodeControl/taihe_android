package com.taihe.eggshell.login;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.google.gson.Gson;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.FormatUtils;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.main.MainActivity;
import com.taihe.eggshell.main.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册成功直接登录
 */
public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";

    private Context mContext;
    private EditText phone_num;
    private EditText password;
    private EditText phone_code;
    private Button btn_register;
    private LinearLayout lin_back;

    private TextView tv_getcode;

    private String pwd;
    private String p_num;
    private String p_code;

    private String telphone;
    private String uid;

    @Override
    public void initView() {
        setContentView(R.layout.activity_register);
        super.initView();
//        overridePendingTransition(R.anim.activity_right_to_center, R.anim.activity_center_to_left);
        mContext = this;
        phone_num = (EditText) findViewById(R.id.et_regist_phone);
        phone_code = (EditText) findViewById(R.id.et_regist_code);
        password = (EditText) findViewById(R.id.et_regist_pwd);
        btn_register = (Button) findViewById(R.id.btn_regist_register);
        lin_back = (LinearLayout) findViewById(R.id.lin_back);
        tv_getcode = (TextView) findViewById(R.id.tv_regist_getcode);

        tv_getcode.setOnClickListener(this);

        lin_back.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void initData() {
        initTitle("注册");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_back:
                RegisterActivity.this.finish();
                break;
            case R.id.tv_regist_getcode:
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
        if (!FormatUtils.isMobileNO(p_num)) {
            ToastUtils.show(RegisterActivity.this, "手机号格式不正确");
        } else if (pwd.length() < 6) {
            ToastUtils.show(RegisterActivity.this, "密码长度太短");
        } else {
            //服务器注册
            registerFromNet();
        }
    }

    //获取短信验证码
    private void getCode() {

        p_num = phone_num.getText().toString();
        if (TextUtils.isEmpty(p_num)) {
            ToastUtils.show(mContext, "请输入手机号");
            return;
        } else if (!FormatUtils.isMobileNO(p_num)) {
            ToastUtils.show(RegisterActivity.this, "手机号格式不正确");
            return;
        }
        getCodeFromNet();
        Map<String, String> dataParams = new HashMap<String, String>();
        dataParams.put("telphone", p_num);

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {

                try {
                    Log.v(TAG, (String) obj);
                    JSONObject jsonObject = new JSONObject((String) obj);
                    int code = jsonObject.getInt("code");
                    System.out.println("code=========" + code);
                    if (code == 0) {

                        String msg = jsonObject.getString("message");
                        ToastUtils.show(mContext, msg);

                    } else if (code == 1000) {
                        ToastUtils.show(mContext, "手机号为空");
                    } else if (code == 1002) {
                        ToastUtils.show(mContext, "改手机号已经注册过了");
                    } else {
                        String msg = jsonObject.getString("message");
                        ToastUtils.show(mContext, msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.v("TAG:", volleyError.networkResponse.statusCode + "");
                ToastUtils.show(mContext, volleyError.networkResponse.statusCode + "");

            }
        };


        RequestUtils.createRequest(mContext, "http://195.198.1.211/eggker/interface",Urls.METHOD_REGIST_GETCODE, false, dataParams, true, listener, errorListener);
    }


    //注册
    private void registerFromNet() {

        Map<String, String> dataParams = new HashMap<String, String>();
        dataParams.put("telphone", p_num);
        dataParams.put("password", pwd);
        dataParams.put("code", p_code);

        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    Log.v(TAG, (String) obj);
                    JSONObject jsonObject = new JSONObject((String) obj);
                    int code = jsonObject.getInt("code");
                    System.out.println("code=========" + code);
                    if (code == 0) {
                        ToastUtils.show(mContext, "注册成功");
                        JSONObject data = jsonObject.getJSONObject("data");

                        telphone = data.getString("telphone");
                        uid = data.getString("uid");
                        Log.i(TAG, uid);
                        Log.i(TAG, telphone);

                        Map<String, String> datas = new HashMap<String, String>();
                        datas.put("id", uid);
                        datas.put("phoneNumber", telphone);
                        Gson gson = new Gson();
                        // 将对象转换为JSON数据
                        String userData = gson.toJson(datas);
                        PrefUtils.saveStringPreferences(getApplicationContext(), PrefUtils.CONFIG, PrefUtils.KEY_USER_JSON, userData);

                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        RegisterActivity.this.finish();
                    } else if (code == 1001) {
                        String msg = jsonObject.getString("message");
                        JSONArray data = jsonObject.getJSONArray("data");
                        ToastUtils.show(mContext, msg);
                    } else if (code == 1002) {
                        String msg = jsonObject.getString("message");
                        JSONArray data = jsonObject.getJSONArray("data");
                        ToastUtils.show(mContext, msg);
                    } else {
                        String msg = jsonObject.getString("message");
                        ToastUtils.show(mContext, msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {//返回值
                Log.v("TAG:", volleyError.networkResponse.statusCode + "");
                ToastUtils.show(mContext, volleyError.networkResponse.statusCode + "");
            }
        };
//        String method = "http://195.198.1.197/eggker/interface/register?telphone=" + p_num + "&password=" + pwd + "&code" + p_code;
        RequestUtils.createRequest(mContext, "http://195.198.1.211/eggker/interface", Urls.METHOD_REGIST, false, dataParams, true, listener, errorListener);
    }

    private void getCodeFromNet() {

        tv_getcode.setEnabled(false);
        TimerCount timerCount = new TimerCount(1000 * 60, 1000);
        timerCount.start();
        tv_getcode.setBackgroundResource(R.drawable.msg_count_start);
    }

    private class TimerCount extends CountDownTimer {

        public TimerCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            tv_getcode.setText(l / 1000 + "秒后重发");
        }

        @Override
        public void onFinish() {
            tv_getcode.setText("获取验证码");
            tv_getcode.setEnabled(true);
            tv_getcode.setBackgroundResource(R.drawable.msg_vercode_background);
        }
    }


}
