package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.main.mode.PlayInfoMode;
import com.taihe.eggshell.widget.datepicker.TimeDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang on 2015/8/14.
 */
public class ResumeWorkActivity extends BaseActivity{

    private static final String TAG = "ResumeWorkActivity";

    private Context mContext;

    private TextView commitText,resetText,workTimeStart,workTimeEnd,resumename;
    private EditText companyEdit,departEdit,positionEdit,contextEdit;
    private CheckBox radioButton;
    private String companyName,startTime,endTime,departName,positionName,contextWord;
    private TimeDialog timeDialog;
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
        resetText = (TextView)findViewById(R.id.id_reset);
        companyEdit = (EditText)findViewById(R.id.id_company_name);
        departEdit = (EditText)findViewById(R.id.id_department);
        positionEdit = (EditText)findViewById(R.id.id_position);
        contextEdit = (EditText)findViewById(R.id.id_context);
        workTimeStart = (TextView)findViewById(R.id.id_start_time);
        workTimeEnd = (TextView)findViewById(R.id.id_end_time);
        radioButton = (CheckBox)findViewById(R.id.id_gender);

        workTimeStart.setOnClickListener(this);
        workTimeEnd.setOnClickListener(this);
        commitText.setOnClickListener(this);
        resetText.setOnClickListener(this);
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
        resumename.setText(eid.getName()+"-工作经历");
        timeDialog = new TimeDialog(mContext,this,customTimeListener);
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
                    getInsertData();
                }
                break;
            case R.id.id_reset:
                companyEdit.setText("");
                workTimeStart.setText("");
                workTimeEnd.setText("");
                departEdit.setText("");
                positionEdit.setText("");
                contextEdit.setText("");
                break;
        }
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
                try {
                    JSONObject jsonObject = new JSONObject((String) obj);
                    Log.d("work", jsonObject.toString());
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        try{
                            Intent intent = new Intent(mContext,ResumeWorkScanActivity.class);
                            intent.putExtra("eid",eid);
                            intent.putExtra("name",companyName);
                            intent.putExtra("sdate",startTime);
                            intent.putExtra("edate",endTime);
                            intent.putExtra("department",departName);
                            intent.putExtra("title",positionName);
                            intent.putExtra("content",contextWord);
                            startActivity(intent);
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
            }
        };

        Map<String,String> map = new HashMap<String,String>();
        map.put("uid", "65");//EggshellApplication.getApplication().getUser().getId()+""
        map.put("eid",eid.getRid()+"");
        map.put("name",companyName);
        map.put("sdate",startTime);
        map.put("edate",endTime);
        map.put("department",departName);
        map.put("title",positionName);
        map.put("content",contextWord);

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
