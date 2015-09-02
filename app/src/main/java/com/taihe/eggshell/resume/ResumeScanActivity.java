package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang on 2015/8/15.
 */
public class ResumeScanActivity extends BaseActivity{

    private static final String TAG = "ResumeScanActivity";
    private Context mContext;

    private TextView createTime,userName,gender,age,schoolLevel,experice,address,telphone,email;
    private TextView hopeposition,hopeindustry,hopemoney,hopeaddress,hopetime,staus,positiontype;
    private TextView worktime,workposition,workcompany,workcontent;
    private TextView edutime,eduindusty,eduschool,eduposition,edubrief;
    private TextView techname,techyears,techlevel;
    private TextView projecttime,projectpostion,projectname,projectbrief;
    private TextView bookname,booktime,bookcompany,bookbrief;
    private TextView traintime,traindirection,traincompnay,trainbrief;
    private TextView selfbrief;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_scan);
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();

        initTitle("简历预览");
        getInsertData();
    }
    private void getInsertData() {
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    JSONObject jsonObject = new JSONObject((String) obj);
                    Log.d("look", jsonObject.toString());
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        try{

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    } else {
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(mContext, "网络异常!" + msg.toString(), Toast.LENGTH_LONG).show();
                    }
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

        Map<String,String> map = new HashMap<String,String>();

        RequestUtils.createRequest(mContext, Urls.RESUME_LOOK_URL+"?id=18", "", true, map, true, listener, errorListener);
    }
}
