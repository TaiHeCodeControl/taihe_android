package com.taihe.eggshell.resume;

import android.content.Context;
import android.util.Log;
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
        resetText = (TextView)findViewById(R.id.id_reset);
        contextEdit = (EditText)findViewById(R.id.id_context);

        commitText.setOnClickListener(this);
        resetText.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        eid=getIntent().getParcelableExtra("eid");
        resume_name.setText(eid.getName()+"-自我评价");
        initTitle("写简历");
        loading = new LoadingProgressDialog(mContext,"正在提交...");
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
            case R.id.id_reset:
                contextEdit.setText("");
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
    private void getInsertData() {
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject((String) obj);
                    Log.d("work", jsonObject.toString());
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        try{
                            ToastUtils.show(mContext,"添加成功!");
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
                ToastUtils.show(mContext,"网络异常");
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
