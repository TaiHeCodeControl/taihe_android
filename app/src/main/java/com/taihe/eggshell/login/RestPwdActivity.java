package com.taihe.eggshell.login;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.push.exception.MqttInvalidTopicNameException;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.main.MainActivity;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang on 2015/8/12.
 */
public class RestPwdActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext;

    private EditText pwdone;
    private EditText pwdtwo;
    private TextView resetpwd;
    private LoadingProgressDialog loading;
    private String phonenum = "";
    private String onepwd = "";

    @Override
    public void initView() {
        setContentView(R.layout.activity_restpwd);
        super.initView();

        mContext = this;
        pwdone = (EditText) findViewById(R.id.id_pwd_one);
        pwdtwo = (EditText) findViewById(R.id.id_pwd_two);
        resetpwd = (TextView) findViewById(R.id.id_rest_pwd);

        resetpwd.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("找回密码");
        phonenum = getIntent().getStringExtra("phonenum");

        loading = new LoadingProgressDialog(mContext, getResources().getString(
                R.string.submitcertificate_string_wait_dialog));
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
        onepwd = pwdone.getText().toString();
        String twopwd = pwdtwo.getText().toString();
        if(TextUtils.isEmpty(onepwd) || TextUtils.isEmpty(twopwd)){
            return;
        }else if(!onepwd.equals(twopwd)){
            ToastUtils.show(this,"密码不一致");
            return;
        }else{
            if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)){
                loading.show();
                resetPwdFromNet();
            }else{
                ToastUtils.show(mContext,R.string.check_network);
            }
        }
    }

    private void resetPwdFromNet(){

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                loading.dismiss();
                try {
//                    Log.v("TEDS:",(String)o);
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if(code ==0){
                        ToastUtils.show(mContext,"修改成功");
                        startActivity(new Intent(mContext,LoginActivity.class));
                        finish();
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
                    Log.v("Forget:", new String(volleyError.networkResponse.data));
                }
                ToastUtils.show(mContext,volleyError.networkResponse.statusCode+mContext.getResources().getString(R.string.error_server));
            }
        };

        Map<String,String> param = new HashMap<String, String>();
        param.put("telphone",phonenum);
        param.put("newpwd",onepwd);
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_RESET_PASSWORD, false, param, true, listener, errorListener);
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
