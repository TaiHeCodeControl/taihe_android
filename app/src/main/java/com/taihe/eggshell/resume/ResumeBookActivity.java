package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
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
import com.taihe.eggshell.resume.entity.ResumeData;
import com.taihe.eggshell.resume.entity.Resumes;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.taihe.eggshell.widget.datepicker.TimeDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang on 2015/8/14.
 */
public class ResumeBookActivity extends BaseActivity{

    private static final String TAG = "ResumeBookActivity";

    private Context mContext;

    private TextView commitText,deleteText,timeEdit,resume_name;
    private EditText bookEdit,techLevelEdit,contextEdit;
    private TimeDialog timeDialog;
    private LoadingProgressDialog loading;

    private int itemid = -1;
    private String techName,years,techType,contextWord,strType,strState,strUrl,strTitle;
    private Resumes eid;
    private TimeDialog.CustomTimeListener customTimeListener = new TimeDialog.CustomTimeListener() {
        @Override
        public void setTime(String time) {
            timeEdit.setText(time);
            timeDialog.dismiss();
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_book);
        super.initView();

        mContext = this;

        resume_name = (TextView)findViewById(R.id.id_resume_num);
        commitText = (TextView)findViewById(R.id.id_commit);
        deleteText = (TextView)findViewById(R.id.id_delete);
        bookEdit = (EditText)findViewById(R.id.id_tech_name);
        timeEdit = (TextView)findViewById(R.id.id_tech_type);
        techLevelEdit = (EditText)findViewById(R.id.id_tech_level);
        contextEdit = (EditText)findViewById(R.id.id_context);

        commitText.setVisibility(View.VISIBLE);
        timeEdit.setOnClickListener(this);
        commitText.setOnClickListener(this);
        deleteText.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("证书");
        eid=getIntent().getParcelableExtra("eid");
        strType = getIntent().getStringExtra("type");
        strState=getIntent().getStringExtra("state");
        strUrl = getIntent().getStringExtra("url");
        strTitle = getIntent().getStringExtra("title");
        resume_name.setText(eid.getName()+"-证书");
        timeDialog = new TimeDialog(mContext,this,customTimeListener);
        loading = new LoadingProgressDialog(mContext,"正在提交...");
        if(!"".equals(strType) && !"add".equals(strState)){
            deleteText.setVisibility(View.VISIBLE);
            ResumeData resumeData = getIntent().getParcelableExtra("listobj");
            itemid = resumeData.getId();
            bookEdit.setText(resumeData.getName());
            timeEdit.setText(resumeData.getSdate());
            techLevelEdit.setText(resumeData.getTitle());
            contextEdit.setText(resumeData.getContent());
        }else{
            deleteText.setVisibility(View.GONE);
        }
//        if(TextUtils.isEmpty(type)){
//            deleteText.setVisibility(View.GONE);
//        }else{
//            deleteText.setVisibility(View.VISIBLE);
//        }
//
//        if(null!=getIntent().getParcelableExtra("listobj")){
//            ResumeData resumeData = getIntent().getParcelableExtra("listobj");
//            itemid = resumeData.getId();
//            bookEdit.setText(resumeData.getName());
//            timeEdit.setText(resumeData.getSdate());
//            techLevelEdit.setText(resumeData.getTitle());
//            contextEdit.setText(resumeData.getContent());
//        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_tech_type:
                timeDialog.show();
                break;
            case R.id.id_commit:
                techName = bookEdit.getText().toString();
                years = timeEdit.getText().toString();
                techType = techLevelEdit.getText().toString();
                contextWord = contextEdit.getText().toString();
                if(isCheck()){
                    if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
                        loading.show();
                        getInsertData();
                    }else{
                        ToastUtils.show(mContext, R.string.check_network);
                    }

                }
                break;
            case R.id.id_delete:
                if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)){
                    loading.show();
                    deleteBook();
                }
                break;
        }
    }
    private boolean isCheck(){
        if(techName.length()==0){
            Toast.makeText(mContext, "请填写证书名称!", Toast.LENGTH_LONG).show();
            bookEdit.setFocusable(true);
            return false;
        }
        if(years.length()==0){
            Toast.makeText(mContext,"请选择颁发时间!",Toast.LENGTH_LONG).show();
            return false;
        }
        if(techType.length()==0){
            Toast.makeText(mContext,"请填写颁发单位!",Toast.LENGTH_LONG).show();
            techLevelEdit.setFocusable(true);
            return false;
        }
        if(contextWord.length()==0){
            Toast.makeText(mContext,"请填写证书描述!",Toast.LENGTH_LONG).show();
            contextEdit.setFocusable(true);
            return false;
        }
        return true;
    }

    private void deleteBook(){
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject((String) obj);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        ToastUtils.show(mContext,"删除成功");
                        finish();
                    }else{
                        ToastUtils.show(mContext, "删除失败");
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
            }
        };

        Map<String,String> params = new HashMap<String,String>();
        params.put("uid", EggshellApplication.getApplication().getUser().getId()+"");
        params.put("eid",eid.getRid()+"");
        params.put("id",itemid+"");
        params.put("type","6");

        RequestUtils.createRequest(mContext,Urls.getMopHostUrl(),Urls.METHOD_DELETE_RESUME_ITEM,true,params,true,listener,errorListener);
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
                            Toast.makeText(mContext,"提交成功!",Toast.LENGTH_LONG).show();
                            if("add".equals(strState)){
                                Intent intent = new Intent(mContext,ResumeListActivity.class);
                                intent.putExtra("eid",eid);
                                intent.putExtra("type",strType);
                                intent.putExtra("url", strUrl);
                                intent.putExtra("title",strTitle);
                                startActivity(intent);
                            }
                            finish();
//                            Intent intent = new Intent(mContext,ResumeBookScanActivity.class);
//                            intent.putExtra("eid",eid);
//                            intent.putExtra("name",techName);
//                            intent.putExtra("sdate",years);
//                            intent.putExtra("title",techType);
//                            intent.putExtra("content",contextWord);
//                            startActivity(intent);
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
                Log.v(TAG, new String(volleyError.networkResponse.data));
                ToastUtils.show(mContext,"网络异常");
            }
        };

        Map<String,String> map = new HashMap<String,String>();
        map.put("uid", EggshellApplication.getApplication().getUser().getId()+"");//EggshellApplication.getApplication().getUser().getId()+""
        map.put("eid",eid.getRid()+"");
        if(-1 != itemid && !"add".equals(strState)){
            map.put("id",itemid+"");
        }
        map.put("name",techName);
        map.put("sdate",years);
        map.put("title",techType);
        map.put("content",contextWord);
        RequestUtils.createRequest(mContext, Urls.RESUME_BOOK_URL, "", true, map, true, listener, errorListener);
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
