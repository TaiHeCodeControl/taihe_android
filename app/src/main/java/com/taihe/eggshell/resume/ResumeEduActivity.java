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
import com.taihe.eggshell.resume.entity.Resumes;
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
public class ResumeEduActivity extends BaseActivity{
    private static final String TAG = "ResumeEduActivity";

    private Context mContext;

    private TextView commitText,resetText,schoolTimeStart,schoolTimeEnd,resume_name;
    private EditText schoolEdit,industyEdit,positionEdit,contextEdit;
    private CheckBox radioButton;
    private TimeDialog timeDialog;
    private LoadingProgressDialog loading;

    private String schoolName,startTime,endTime,industyName,positionName,contextWord;
    private boolean isStart = false;
    private Resumes eid;
    private TimeDialog.CustomTimeListener customTimeListener = new TimeDialog.CustomTimeListener() {
        @Override
        public void setTime(String time) {
            if(isStart){
                schoolTimeStart.setText(time);
            }else{
                schoolTimeEnd.setText(time);
            }

            timeDialog.dismiss();
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_edu);
        super.initView();

        mContext = this;

        resume_name = (TextView)findViewById(R.id.id_resume_num);
        commitText = (TextView)findViewById(R.id.id_commit);
        resetText = (TextView)findViewById(R.id.id_reset);
        schoolEdit = (EditText)findViewById(R.id.id_company_name);
        industyEdit = (EditText)findViewById(R.id.id_department);
        positionEdit = (EditText)findViewById(R.id.id_position);
        contextEdit = (EditText)findViewById(R.id.id_context);
        schoolTimeStart = (TextView)findViewById(R.id.id_start_time);
        schoolTimeEnd = (TextView)findViewById(R.id.id_end_time);
        radioButton = (CheckBox)findViewById(R.id.id_gender);

        schoolTimeStart.setOnClickListener(this);
        schoolTimeEnd.setOnClickListener(this);
        commitText.setOnClickListener(this);
        resetText.setOnClickListener(this);
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-M-d");
                    String date = sDateFormat.format(new java.util.Date());
                    schoolTimeEnd.setText(date);
                }else{
                    schoolTimeEnd.setText("");
                }
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("写简历");
        eid=getIntent().getParcelableExtra("eid");
        resume_name.setText(eid.getName()+"-教育经历");
        timeDialog = new TimeDialog(mContext,this,customTimeListener);

        loading = new LoadingProgressDialog(mContext,"正在请求...");
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
                schoolName = schoolEdit.getText().toString();
                startTime = schoolTimeStart.getText().toString();
                endTime = schoolTimeEnd.getText().toString();
                industyName = industyEdit.getText().toString();
                positionName = positionEdit.getText().toString();
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
            case R.id.id_reset:
                schoolEdit.setText("");
                schoolTimeStart.setText("");
                schoolTimeEnd.setText("");
                industyEdit.setText("");
                positionEdit.setText("");
                contextEdit.setText("");
                break;
        }
    }
    private boolean isCheck(){
        if(schoolName.length()==0){
            Toast.makeText(mContext, "请填写学校名称!", Toast.LENGTH_LONG).show();
            schoolEdit.setFocusable(true);
            return false;
        }
        if(startTime.length()==0){
            Toast.makeText(mContext,"请选择在校开始时间!",Toast.LENGTH_LONG).show();
            return false;
        }
        if(endTime.length()==0){
            Toast.makeText(mContext,"请选择在校结束时间!",Toast.LENGTH_LONG).show();
            return false;
        }
        if(industyName.length()==0){
            Toast.makeText(mContext,"请填写所学专业!",Toast.LENGTH_LONG).show();
            industyEdit.setFocusable(true);
            return false;
        }
        if(positionName.length()==0){
            Toast.makeText(mContext,"请填写社团职位!",Toast.LENGTH_LONG).show();
            positionEdit.setFocusable(true);
            return false;
        }
        if(contextWord.length()==0){
            Toast.makeText(mContext,"请填写专业描述!",Toast.LENGTH_LONG).show();
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
                    Log.d("edu",jsonObject.toString());
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        try{
                            Intent intent = new Intent(mContext,ResumeEduScanActivity.class);
                            intent.putExtra("eid",eid);
                            intent.putExtra("name",schoolName);
                            intent.putExtra("sdate",startTime);
                            intent.putExtra("edate",endTime);
                            intent.putExtra("specialty",industyName);
                            intent.putExtra("title",positionName);
                            intent.putExtra("content",contextWord);
                            startActivity(intent);
                            finish();
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
                ToastUtils.show(mContext,volleyError.networkResponse.statusCode+"网络错误");
            }
        };

        Map<String,String> map = new HashMap<String,String>();
        map.put("uid", EggshellApplication.getApplication().getUser().getId()+"");//EggshellApplication.getApplication().getUser().getId()+""
        map.put("eid",eid.getRid()+"");
        map.put("name",schoolName);
        map.put("sdate",startTime);
        map.put("edate",endTime);
        map.put("specialty",industyName);
        map.put("title",positionName);
        map.put("content",contextWord);

        RequestUtils.createRequest(mContext, Urls.RESUME_EDU_URL, "", true, map, true, listener, errorListener);
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
