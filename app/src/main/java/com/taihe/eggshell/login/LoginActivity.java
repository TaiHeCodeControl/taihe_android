package com.taihe.eggshell.login;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.FormatUtils;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.main.MainActivity;
import com.taihe.eggshell.job.activity.MyCollectActivity;
import com.taihe.eggshell.personalCenter.activity.MyBasicActivity;
import com.taihe.eggshell.job.activity.MyPostActivity;
import com.taihe.eggshell.resume.ResumeManagerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huan on 2015/8/5.
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    private Context mContext;
    private EditText et_userphone;
    private EditText et_password;
    private Button btn_login;
    private TextView tv_forgetPassword;
    private TextView tv_regist;
    private LinearLayout lin_back;

    private String userphone;
    private String password;

    private Intent intent;
    private String loginTag;

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
        super.initView();
        overridePendingTransition(R.anim.activity_right_to_center, R.anim.activity_center_to_left);
        mContext = this;


        et_userphone = (EditText) findViewById(R.id.et_login_userphone);
        et_password = (EditText) findViewById(R.id.et_login_password);
        btn_login = (Button) findViewById(R.id.btn_login_login);
        tv_forgetPassword = (TextView) findViewById(R.id.tv_login_forgetpassword);
        tv_regist = (TextView) findViewById(R.id.tv_login_regist);
        lin_back = (LinearLayout) findViewById(R.id.lin_back);

        lin_back.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.lin_back:
                goBack();
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

    private void goBack() {

//        Intent intents = getIntent();
//        loginTag = intents.getStringExtra("LoginTag");
//        if (loginTag.equals("logout")) {
//            intent = new Intent(LoginActivity.this, MainActivity.class);
//            intent.putExtra("MeFragment", "MeFragment");
//            startActivity(intent);
//        }

        LoginActivity.this.finish();

        //overridePendingTransition(int enterAnim, int exitAnim)
        overridePendingTransition(R.anim.activity_left_to_center, R.anim.activity_center_to_right);
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

//        loginSuccess();
        loginFromNet();


    }

    private void loginFromNet() {

        Map<String, String> dataParams = new HashMap<String, String>();
        dataParams.put("username", userphone);
        dataParams.put("password", password);

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
                        ToastUtils.show(mContext, "登录成功");
                        JSONObject data = jsonObject.getJSONObject("data");

                        String telphone = data.getString("telphone");
                        Log.i(TAG, telphone);
                        String uid = data.getString("uid");
                        Log.i(TAG, uid);

                        //登录成功保存用户登录信息
                        loginSuccess();


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
//                    String err = new String(volleyError.networkResponse.data);
//                    volleyError.networkResponse.statusCode;
            }
        };

        String method = "/login/";
        RequestUtils.createRequest(mContext, Urls.BASE_HYR_MOBILE_URL, method, false, dataParams, true, listener, errorListener);

    }

    //登录成功保存用户登录信息
    private void loginSuccess() {
        String data = "{'id':1,'name':'xx','phoneNumber':'89898'}";
        PrefUtils.saveStringPreferences(getApplicationContext(), PrefUtils.CONFIG, PrefUtils.KEY_USER_JSON, data);

        //登录成功后显示界面的判断
        Intent intents = getIntent();
        loginTag = intents.getStringExtra("LoginTag");
        if (loginTag.equals("meFragment")) {

            intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("Main", "MeFragment");
            startActivity(intent);
        } else if (loginTag.equals("myBasic")) {
            intent = new Intent(LoginActivity.this, MyBasicActivity.class);
            startActivity(intent);
        } else if (loginTag.equals("myPost")) {
            intent = new Intent(LoginActivity.this, MyPostActivity.class);
            startActivity(intent);
        } else if (loginTag.equals("myCollect")) {
            intent = new Intent(LoginActivity.this, MyCollectActivity.class);
            startActivity(intent);
        } else if (loginTag.equals("myResume")) {
            intent = new Intent(LoginActivity.this, ResumeManagerActivity.class);
            startActivity(intent);
        }
        LoginActivity.this.finish();
    }

    //监听返回按钮
    @Override
    public void onBackPressed() {
        goBack();
    }

}
