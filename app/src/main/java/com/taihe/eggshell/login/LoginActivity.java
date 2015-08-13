package com.taihe.eggshell.login;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.FormatUtils;

import android.widget.ImageView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.utils.HttpsUtils;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.main.MainActivity;
import com.taihe.eggshell.main.entity.User;

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
    private ImageView iv_back;

    private String userphone;
    private String password;

    private Intent intent;

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
//                intent = new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(intent);
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
       final String url = "http://195.198.1.195/index.php?m=api&username=18810309239&pw=111111";

//        new Thread(){
//            public void run(){
//                String str = HttpsUtils.doGet(url);
//                System.out.println(str + "=============------------------");
//
//            };
//        }.start();

//        HttpLogin();
//        loginFromNet();
        //保存用户登录信息

        PrefUtils.saveStringPreferences(getApplicationContext(), PrefUtils.CONFIG, PrefUtils.KEY_USER_JSON, "{'id':1,'name':'xx','phoneNumber':'89898'}");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();

    }

    private void loginFromNet() {

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
                        String data = jsonObject.getString("data");

                        PrefUtils.saveStringPreferences(getApplicationContext(), PrefUtils.CONFIG, PrefUtils.KEY_USER_JSON, "{'id':1,'name':'xx','phoneNumber':'89898'}");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    } else {
                        String msg = jsonObject.getString("msg");
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
        String method = "&username=" + userphone + "&pw=" + password;
        RequestUtils.createRequest_GET(mContext, Urls.getMopHostUrl(), method, false, "", "", listener, errorListener);
    }


    private void HttpLogin() {

        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("username", userphone);
//        params.addQueryStringParameter("pw", password);
        params.addBodyParameter("username", userphone);
        params.addBodyParameter("pw", password);

        final String str = "&username=" + userphone + "&pw=" + password;
        String url = "http://195.198.1.195/index.php?m=api&username=18810309239&pw=111111";
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                System.out.println("LoginResult========= " + result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                System.out.println("请求服务器失败");
            }
        });
    }
}
