package com.taihe.eggshell.main;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.login.LoginActivity;
import com.taihe.eggshell.personalCenter.activity.UserInfoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private Context mContext;
    private Button toPersonalCenter;
    private Button btn_login;

    private Intent intent;

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        mContext = this;

        toPersonalCenter = (Button)findViewById(R.id.id_personal_center);
        btn_login = (Button)findViewById(R.id.btn_login);
    }

    @Override
    public void initData() {
        toPersonalCenter.setOnClickListener(this);
        btn_login.setOnClickListener(this);
//        getDataFromNet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_personal_center:
                intent = new Intent(mContext, UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getDataFromNet(){
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    JSONObject jsonObject = new JSONObject((String)obj);
                    //String data = jsonObject.getString("data");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {//返回值

            }
        };

        //参数
        Map<String,String> param = new HashMap<String, String>();
        param.put("","");
        //POST请求
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(),Urls.METHOD_LOGIN,false,param,true,listener,errorListener);
    }

    private void getData(){

    }
}
