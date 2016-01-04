package com.taihe.eggshell.resume;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.resume.entity.Resumes;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang on 2015/8/14.
 */
public class ResumeSelfActivity extends BaseActivity{

    private static final String TAG = "ResumeSelfActivity";

    private Context mContext;

    private TextView commitText,resetText,resume_name;
    private EditText contextEdit;
    private String content;
    private Resumes eid;
    private LoadingProgressDialog loading;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_self);
        super.initView();

        mContext = this;

        resume_name = (TextView)findViewById(R.id.id_resume_num);
        commitText = (TextView)findViewById(R.id.id_commit);
        contextEdit = (EditText)findViewById(R.id.id_context);

        commitText.setVisibility(View.VISIBLE);
        commitText.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        eid=getIntent().getParcelableExtra("eid");
        resume_name.setText(eid.getName()+"-自我评价");
        initTitle("自我评价");
        loading = new LoadingProgressDialog(mContext,"正在提交...");
        if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
            loading.show();
            getSelfInfoFromNet();
        }else{
            ToastUtils.show(mContext, R.string.check_network);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_commit:
                content = contextEdit.getText().toString();
                if(isCheck()){
                    if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
                        loading.show();
                        getInsertData();
                    }else{
                        ToastUtils.show(mContext, R.string.check_network);
                    }

                }
                break;
        }
    }
    private boolean isCheck(){
        if(content.length()==0){
            Toast.makeText(mContext,"请填写自己评价!",Toast.LENGTH_LONG).show();
            contextEdit.setFocusable(true);
            return false;
        }
        return true;
    }

    private void getSelfInfoFromNet(){
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                loading.dismiss();

                try {
                    JSONObject data = new JSONObject((String)o);
//                    Log.v(TAG,(String)o);
                    int code = data.getInt("code");
                    if(code == 0){
                        JSONObject jsonObject = data.getJSONObject("data");
                        String descripe = jsonObject.getString("description");
                        contextEdit.setText(descripe);
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
//                Log.v(TAG,new String(volleyError.networkResponse.data));
            }
        };

        Map<String,String> params = new HashMap<String,String>();
        params.put("eid",eid.getRid()+"");
        params.put("uid", EggshellApplication.getApplication().getUser().getId()+"");
        RequestUtils.createRequest(mContext,Urls.getMopHostUrl(),Urls.METHOD_GET_SELF,true,params,true,listener,errorListener);
    }

    private void getInsertData() {
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject((String) obj);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        try{
                            ToastUtils.show(mContext,"提交成功!");
                            finish();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(mContext,"提交失败，网络异常!"+msg.toString(),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {//返回值
                loading.dismiss();
//                Log.v("TTT:",new String(volleyError.networkResponse.data));
                ToastUtils.show(mContext,volleyError);
            }
        };

        Map<String,String> map = new HashMap<String,String>();
        map.put("uid", EggshellApplication.getApplication().getUser().getId()+"");//EggshellApplication.getApplication().getUser().getId()+""
        map.put("eid",eid.getRid()+"");
        map.put("content",content);

        RequestUtils.createRequest(mContext, Urls.RESUME_OTHER_URL, "", true, map, true, listener, errorListener);
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
