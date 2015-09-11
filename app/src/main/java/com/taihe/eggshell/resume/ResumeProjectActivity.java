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
public class ResumeProjectActivity extends BaseActivity{

    private static final String TAG = "ResumeTrainActivity";

    private Context mContext;

    private TextView commitText,resetText,schoolTimeStart,schoolTimeEnd,resume_name;
    private EditText projectEdit,invaraEdit,departEdit,contextEdit;
    private CheckBox checkBox;
    private TimeDialog timeDialog;
    private LoadingProgressDialog loading;

    private String techName,startTime,endTime,techLevel,departName,contextWord;
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
        setContentView(R.layout.activity_resume_project);
        super.initView();

        mContext = this;

        resume_name = (TextView)findViewById(R.id.id_resume_num);
        commitText = (TextView)findViewById(R.id.id_commit);
        resetText = (TextView)findViewById(R.id.id_reset);
        projectEdit = (EditText)findViewById(R.id.id_tech_name);
        invaraEdit = (EditText)findViewById(R.id.id_tech_level);
        departEdit = (EditText)findViewById(R.id.id_department);
        contextEdit = (EditText)findViewById(R.id.id_context);
        schoolTimeStart = (TextView)findViewById(R.id.id_start_time);
        schoolTimeEnd = (TextView)findViewById(R.id.id_end_time);
        checkBox = (CheckBox)findViewById(R.id.id_gender);

        schoolTimeStart.setOnClickListener(this);
        schoolTimeEnd.setOnClickListener(this);
        commitText.setOnClickListener(this);
        resetText.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        resume_name.setText(eid.getName()+"-项目经验");
        timeDialog = new TimeDialog(mContext,this,customTimeListener);
        loading = new LoadingProgressDialog(mContext,"正在提交...");
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
                techName = projectEdit.getText().toString();
                startTime = schoolTimeStart.getText().toString();
                endTime = schoolTimeEnd.getText().toString();
                techLevel = invaraEdit.getText().toString();
                departName = departEdit.getText().toString();
                contextWord = contextEdit.getText().toString();
                if(isCheck()){
                    if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
                        loading.show();
                        getInsertData();
                    }else {
                        ToastUtils.show(mContext,R.string.check_network);
                    }

                }
                break;
            case R.id.id_reset:
                projectEdit.setText("");
                schoolTimeStart.setText("");
                schoolTimeEnd.setText("");
                invaraEdit.setText("");
                departEdit.setText("");
                contextEdit.setText("");

                break;
        }
    }
    private boolean isCheck(){
        if(techName.length()==0){
            Toast.makeText(mContext, "请填写项目名称!", Toast.LENGTH_LONG).show();
            projectEdit.setFocusable(true);
            return false;
        }
        if(startTime.length()==0){
            Toast.makeText(mContext,"请选择项目开始时间!",Toast.LENGTH_LONG).show();
            return false;
        }
        if(endTime.length()==0){
            Toast.makeText(mContext,"请选择项目结束时间!",Toast.LENGTH_LONG).show();
            return false;
        }
        if(techLevel.length()==0){
            Toast.makeText(mContext,"请填写项目环境!",Toast.LENGTH_LONG).show();
            invaraEdit.setFocusable(true);
            return false;
        }
        if(departName.length()==0){
            Toast.makeText(mContext,"请填写担任职务!",Toast.LENGTH_LONG).show();
            departEdit.setFocusable(true);
            return false;
        }
        if(contextWord.length()==0){
            Toast.makeText(mContext,"请填写项目内容!",Toast.LENGTH_LONG).show();
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
                    Log.d("project", jsonObject.toString());
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        try{
                            ToastUtils.show(mContext,"添加成功!");
                            Intent intent = new Intent(mContext,ResumeProjectScanActivity.class);
                            intent.putExtra("eid",eid);
                            intent.putExtra("name",techName);
                            intent.putExtra("sdate",startTime);
                            intent.putExtra("edate",endTime);
                            intent.putExtra("specialty",techLevel);
                            intent.putExtra("title",departName);
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
                loading.dismiss();
                ToastUtils.show(mContext,"网络异常");
            }
        };

        Map<String,String> map = new HashMap<String,String>();
        map.put("uid", EggshellApplication.getApplication().getUser().getId()+"");//EggshellApplication.getApplication().getUser().getId()+""
        map.put("eid",eid.getRid()+"");
        map.put("name",techName);
        map.put("sdate",startTime);
        map.put("edate",endTime);
        map.put("sys",techLevel);
        map.put("title",departName);
        map.put("content",contextWord);

        RequestUtils.createRequest(mContext, Urls.RESUME_PROJECT_URL, "", true, map, true, listener, errorListener);
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
