package com.taihe.eggshell.userRegister;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.utils.ToastUtils;

import org.apache.http.client.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class RegisterActivity extends BaseActivity {
    private EditText phone_num=null;
    private EditText password=null;
    private EditText confirm_password=null;
    private String num=null;
    private String pwd=null;
    private String con_pwd=null;
    private Button btn_register;
    //private HashMap<String,String> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_register);
        initView();
        initData();

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_register);
        phone_num = (EditText) findViewById(R.id.phone_num);
        password = (EditText) findViewById(R.id.pwd);
        confirm_password = (EditText) findViewById(R.id.confirm_pwd);
        btn_register = (Button) findViewById(R.id.btn_register);
    }

    @Override
      public void initData() {

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = phone_num.getText().toString();
                pwd = password.getText().toString();
                con_pwd = confirm_password.getText().toString();
                //map.put("username",num);
                //map.put("password",pwd);
              if(num.length()!= 11){
                    ToastUtils.show(RegisterActivity.this,"手机号格式不正确");
                }else if(pwd.length()<6){
                    ToastUtils.show(RegisterActivity.this,"密码长度太短");
                }else if(!pwd.equals(con_pwd)){
                  ToastUtils.show(RegisterActivity.this,"密码不一致");
              }else
                  ToastUtils.show(RegisterActivity.this,"正在登陆");
                    //Register(RegisterActivity.this,map);

            }
        });
    }

//    public void Register(Context mContext, HashMap<String,String> map){
//        JSONObject object = new JSONObject();
//        try{
//            object.put("username",map.get("username"));
//            object.put("password",map.get("password"));
//            HttpUtils http = new HttpUtils();
//            http.send(HttpRequest.HttpMethod.GET, "URL", new RequestCallBack<String>() {
//
//                @Override
//                public void onSuccess(ResponseInfo<String> responseInfo) {
//
//                }
//
//                @Override
//                public void onFailure(HttpException e, String s) {
//
//                }
//            });
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//    }
//    public static int Register(Context mContext, HashMap<String, String> map) {
//        JSONObject object = new JSONObject();
//        try {
//
//            object.put("check_code", MD5Encrypt.encryption("jz365"));
//            object.put("username", map.get("username"));
//            object.put("password", map.get("password"));
//            // object.put("email", map.get("email"));
//            object.put("call", map.get("call"));
//            object.put("type", map.get("type"));
//            object.put("avatar", Utils.FileBase64String(map.get("avatar")));
//
//            JSONObject result = Submit.Request(SvrInfo.REGITSTER_API, object);
//
//            if (result == null) {
//                return SvrInfo.SVR_RESULT_NULL_RESULT;
//            }
//            int status = result.getInt("status");
//            if (status != 0) {
//                return status;
//            }
//
//            return SvrInfo.SVR_RESULT_SUCCESS;
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return SvrInfo.SVR_RESULT_UNKNOWN_ERR;
//        }
//    }

}
