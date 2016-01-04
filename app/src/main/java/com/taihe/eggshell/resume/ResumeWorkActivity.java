package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.taihe.eggshell.widget.ChoiceDialog;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.taihe.eggshell.widget.datepicker.TimeDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang on 2015/8/14.
 */
public class ResumeWorkActivity extends BaseActivity{

    private static final String TAG = "ResumeWorkActivity";

    private Context mContext;

    private TextView commitText,deleteText,workTimeStart,workTimeEnd,resumename;
    private EditText companyEdit,departEdit,positionEdit,contextEdit;
    private ChoiceDialog deleteDialog;
    private CheckBox radioButton;
    private String companyName,startTime,endTime,departName,positionName,contextWord,strType,jobID,strState,strUrl,strTitle;
    private TimeDialog timeDialog;
    private LoadingProgressDialog loading;
    private boolean isStart = false;
    private Resumes eid;
    private TimeDialog.CustomTimeListener customTimeListener = new TimeDialog.CustomTimeListener() {
        @Override
        public void setTime(String time) {
            if(isStart){
                workTimeStart.setText(time);
            }else{
                workTimeEnd.setText(time);
            }

            timeDialog.dismiss();
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_work);
        super.initView();

        mContext = this;

        resumename = (TextView)findViewById(R.id.id_resume_num);
        commitText = (TextView)findViewById(R.id.id_commit);
        deleteText = (TextView)findViewById(R.id.id_delete);
        companyEdit = (EditText)findViewById(R.id.id_company_name);
        departEdit = (EditText)findViewById(R.id.id_department);
        positionEdit = (EditText)findViewById(R.id.id_position);
        contextEdit = (EditText)findViewById(R.id.id_context);
        workTimeStart = (TextView)findViewById(R.id.id_start_time);
        workTimeEnd = (TextView)findViewById(R.id.id_end_time);
        radioButton = (CheckBox)findViewById(R.id.id_gender);

        commitText.setVisibility(View.VISIBLE);
        workTimeStart.setOnClickListener(this);
        workTimeEnd.setOnClickListener(this);
        commitText.setOnClickListener(this);
        deleteText.setOnClickListener(this);
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-M-d");
                    String date = sDateFormat.format(new java.util.Date());
                    workTimeEnd.setText(date);
                }else{
                    workTimeEnd.setText("");
                }
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("写简历");
        eid=getIntent().getParcelableExtra("eid");
        strType=getIntent().getStringExtra("type");
        strState=getIntent().getStringExtra("state");
        strUrl = getIntent().getStringExtra("url");
        strTitle = getIntent().getStringExtra("title");
        resumename.setText(eid.getName()+"-工作经历");
        timeDialog = new TimeDialog(mContext,this,customTimeListener);
        loading = new LoadingProgressDialog(mContext,"正在提交...");
        ResumeData worklists;
        if(!"".equals(strType) && !"add".equals(strState)){
            deleteText.setVisibility(View.VISIBLE);
            worklists =  getIntent().getParcelableExtra("listobj");
            jobID = worklists.getId()+"";
            companyEdit.setText(worklists.getName());
            workTimeStart.setText(worklists.getSdate());
            workTimeEnd.setText(worklists.getEdate());
            departEdit.setText(worklists.getDepartment());
            positionEdit.setText(worklists.getTitle());
            contextEdit.setText(worklists.getContent());
        }else{
            deleteText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_start_time:
                isStart = true;
                timeDialog.show();
                break;
            case R.id.id_end_time:
                isStart = false;
                timeDialog.show();
                break;
            case R.id.id_commit:
                companyName = companyEdit.getText().toString();
                startTime = workTimeStart.getText().toString();
                endTime = workTimeEnd.getText().toString();
                departName = departEdit.getText().toString();
                positionName = positionEdit.getText().toString();
                contextWord = contextEdit.getText().toString();
                if(isCheck()){
                    if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)){
                        loading.show();
                        getInsertData();
                    }else{
                        ToastUtils.show(mContext,R.string.check_network);
                    }
                }
                break;
            case R.id.id_delete:
                deleteDialog = new ChoiceDialog(mContext,new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDialog.dismiss();
                    }
                },new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)){
                            deleteDialog.dismiss();
                            loading.show();
                            deleteResume();
                        }else{
                            ToastUtils.show(mContext,R.string.check_network);
                        }
                    }
                });

                deleteDialog.getTitleText().setText("确定要删除吗？");
                deleteDialog.getRightButton().setText("确定");
                deleteDialog.getLeftButton().setText("取消");
                deleteDialog.show();

                break;
        }
    }
    private void deleteResume(){
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                loading.dismiss();
//                Log.v(TAG,(String)o);
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        ToastUtils.show(mContext,"删除成功");
                        finish();
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
                ToastUtils.show(mContext,volleyError);
            }
        };
        Map<String,String> map = new HashMap<String,String>();
        map.put("uid", EggshellApplication.getApplication().getUser().getId()+"");//EggshellApplication.getApplication().getUser().getId()+""
        map.put("eid",eid.getRid()+"");
        map.put("id",jobID);
        map.put("type",strType);
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(),Urls.METHOD_DELETE_RESUME_ITEM,false,map,true,listener,errorListener);
    }
    private boolean isCheck(){
        if(companyName.length()==0){
            Toast.makeText(mContext,"请填写单位名称!",Toast.LENGTH_LONG).show();
            companyEdit.setFocusable(true);
            return false;
        }
        if(startTime.length()==0){
            Toast.makeText(mContext,"请选择工作开始时间!",Toast.LENGTH_LONG).show();
            return false;
        }
        if(endTime.length()==0){
            Toast.makeText(mContext,"请选择工作结束时间!",Toast.LENGTH_LONG).show();
            return false;
        }
        if(departName.length()==0){
            Toast.makeText(mContext,"请填写所在部门!",Toast.LENGTH_LONG).show();
            departEdit.setFocusable(true);
            return false;
        }
        if(positionName.length()==0){
            Toast.makeText(mContext,"请填写担任职务!",Toast.LENGTH_LONG).show();
            positionEdit.setFocusable(true);
            return false;
        }
        if(contextWord.length()==0){
            Toast.makeText(mContext,"请填写工作内容!",Toast.LENGTH_LONG).show();
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
                            ToastUtils.show(mContext,"提交成功");
                            if("add".equals(strState)){
                                Intent intent = new Intent(mContext,ResumeListActivity.class);
                                intent.putExtra("eid",eid);
                                intent.putExtra("type",strType);
                                intent.putExtra("url", strUrl);
                                intent.putExtra("title",strTitle);
                                startActivity(intent);
                            }
                            finish();

//                            Intent intent = new Intent(mContext,ResumeWorkScanActivity.class);
//                            intent.putExtra("eid",eid);
//                            intent.putExtra("name",companyName);
//                            intent.putExtra("sdate",startTime);
//                            intent.putExtra("edate",endTime);
//                            intent.putExtra("department",departName);
//                            intent.putExtra("title",positionName);
//                            intent.putExtra("content",contextWord);
//                            intent.putExtra("acttitle","work");
//                            startActivity(intent);

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(mContext,msg.toString(),Toast.LENGTH_LONG).show();
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
        map.put("name",companyName);
        map.put("sdate",startTime);
        map.put("edate",endTime);
        map.put("department",departName);
        map.put("title",positionName);
        map.put("content",contextWord);
        if(!"".equals(strType) && !"add".equals(strState)){
            map.put("id",jobID);
        }
        Log.v("FFFFF:",map.toString());
        RequestUtils.createRequest(mContext, Urls.RESUME_WORK_URL, "", true, map, true, listener, errorListener);
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
