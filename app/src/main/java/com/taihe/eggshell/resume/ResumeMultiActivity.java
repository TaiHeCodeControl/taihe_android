package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.resume.adapter.ResumeCenterAdapter;
import com.taihe.eggshell.resume.entity.ResumeData;
import com.taihe.eggshell.resume.entity.Resumes;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2015/8/13.
 */
public class ResumeMultiActivity extends BaseActivity{

    private static final String TAG = "ResumeMultiActivity";

    private Context mContext;
    private TextView resumeName;
    private RelativeLayout userInfo, workExper,educationExper,trainExper,industryTech,projectExper,certBook,selfEvaluation;
    private Intent intent;
    private Resumes resume;
    List<ResumeData> worklists;
    private LoadingProgressDialog loading;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_mutil);
        super.initView();
        mContext = this;

        resumeName = (TextView)findViewById(R.id.id_resume_name);
        userInfo = (RelativeLayout)findViewById(R.id.id_user_info);
        workExper = (RelativeLayout)findViewById(R.id.id_work_exper);
        educationExper = (RelativeLayout)findViewById(R.id.id_edu_exper);
        trainExper = (RelativeLayout)findViewById(R.id.id_train_exper);
        industryTech = (RelativeLayout)findViewById(R.id.id_industy_tech);
        projectExper = (RelativeLayout)findViewById(R.id.id_project_exper);
        certBook = (RelativeLayout)findViewById(R.id.id_conver_book);
        selfEvaluation = (RelativeLayout)findViewById(R.id.id_self_evalu);

        userInfo.setOnClickListener(this);
        workExper.setOnClickListener(this);
        educationExper.setOnClickListener(this);
        trainExper.setOnClickListener(this);
        industryTech.setOnClickListener(this);
        projectExper.setOnClickListener(this);
        certBook.setOnClickListener(this);
        selfEvaluation.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("写简历");
        resume = getIntent().getParcelableExtra("resume");
        resumeName.setText(resume.getName());

        loading = new LoadingProgressDialog(mContext,"正在请求...");
        if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
            getData();
        }else{
            ToastUtils.show(mContext, R.string.check_network);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_user_info:
                intent = new Intent(mContext,ResumeWriteActivity.class);
                intent.putExtra("eid",resume.getRid()+"");
                startActivity(intent);
                break;
            case R.id.id_work_exper:
                if(strWork>0){
                    intent = new Intent(mContext,ResumeListActivity.class);
                    intent.putExtra("eid",resume);
                    intent.putExtra("type","1");
                    intent.putExtra("url", Urls.RESUME_WORK_LIST);
                    intent.putExtra("title","工作经历");
                    startActivity(intent);
                }else{
                    intent = new Intent(mContext,ResumeWorkActivity.class);
                    intent.putExtra("eid",resume);
                    intent.putExtra("type","1");
                    intent.putExtra("state","add");
                    intent.putExtra("url", Urls.RESUME_WORK_LIST);
                    intent.putExtra("title","工作经历");
                    startActivity(intent);
                }
                break;
            case R.id.id_edu_exper:
                if(strEdu>0){
                    intent = new Intent(mContext,ResumeListActivity.class);
                    intent.putExtra("eid",resume);
                    intent.putExtra("type","2");
                    intent.putExtra("url", Urls.RESUME_EDU_LIST);
                    intent.putExtra("title","教育经历");
                    startActivity(intent);
                }else{
                    intent = new Intent(mContext,ResumeEduActivity.class);
                    intent.putExtra("eid",resume);
                    intent.putExtra("type","2");
                    intent.putExtra("state","add");
                    intent.putExtra("url", Urls.RESUME_EDU_LIST);
                    intent.putExtra("title","教育经历");
                    startActivity(intent);
                }
                break;
            case R.id.id_train_exper:
                if(strTraining>0){
                    intent = new Intent(mContext,ResumeListActivity.class);
                    intent.putExtra("eid",resume);
                    intent.putExtra("type","3");
                    intent.putExtra("url", Urls.RESUME_TRAINING_LIST);
                    intent.putExtra("title","培训经历");
                    startActivity(intent);
                }else{
                    intent = new Intent(mContext,ResumeTrainActivity.class);
                    intent.putExtra("eid",resume);
                    intent.putExtra("type","3");
                    intent.putExtra("state","add");
                    intent.putExtra("url", Urls.RESUME_TRAINING_LIST);
                    intent.putExtra("title","培训经历");
                    startActivity(intent);
                }
                break;
            case R.id.id_industy_tech:
                if(strSkill>0){
                    intent = new Intent(mContext,ResumeListActivity.class);
                    intent.putExtra("eid",resume);
                    intent.putExtra("type","4");
                    intent.putExtra("url", Urls.RESUME_SKILL_LIST);
                    intent.putExtra("title","专业技能");
                    startActivity(intent);
                }else{
                    intent = new Intent(mContext,ResumeTechActivity.class);
                    intent.putExtra("eid",resume);
                    intent.putExtra("type","4");
                    intent.putExtra("state","add");
                    intent.putExtra("url", Urls.RESUME_SKILL_LIST);
                    intent.putExtra("title","专业技能");
                    startActivity(intent);
                }
                break;
            case R.id.id_project_exper:
                if(strProject>0){
                    intent = new Intent(mContext,ResumeListActivity.class);
                    intent.putExtra("eid",resume);
                    intent.putExtra("type","5");
                    intent.putExtra("url", Urls.RESUME_PROJECT_LIST);
                    intent.putExtra("title","项目经验");
                    startActivity(intent);
                }else{
                    intent = new Intent(mContext,ResumeProjectActivity.class);
                    intent.putExtra("eid",resume);
                    intent.putExtra("type","5");
                    intent.putExtra("state","add");
                    intent.putExtra("url", Urls.RESUME_PROJECT_LIST);
                    intent.putExtra("title","项目经验");
                    startActivity(intent);
                }
                break;
            case R.id.id_conver_book:
                if(strCert>0){
                    intent = new Intent(mContext,ResumeListActivity.class);
                    intent.putExtra("eid",resume);
                    intent.putExtra("type","6");
                    intent.putExtra("url", Urls.RESUME_CERT_LIST);
                    intent.putExtra("title","证书");
                    startActivity(intent);
                }else{
                    intent = new Intent(mContext,ResumeBookActivity.class);
                    intent.putExtra("eid",resume);
                    intent.putExtra("type","6");
                    intent.putExtra("state","add");
                    intent.putExtra("url", Urls.RESUME_CERT_LIST);
                    intent.putExtra("title","证书");
                    startActivity(intent);
                }
                break;
            case R.id.id_self_evalu:
                intent = new Intent(mContext,ResumeSelfActivity.class);
                intent.putExtra("eid",resume);
                startActivity(intent);
                break;
        }
    }

    int strExpect,strWork,strEdu,strTraining,strSkill,strProject,strCert,strOther;
    private void getData() {
        loading.show();
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject((String) obj);
                    Log.d("edu", jsonObject.toString());
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        try{
                            JSONObject jb = new JSONObject(jsonObject.getString("data"));
                            strWork = Integer.parseInt(jb.optString("work"));
                            strEdu = Integer.parseInt(jb.optString("edu"));
                            strTraining = Integer.parseInt(jb.optString("training"));
                            strSkill = Integer.parseInt(jb.optString("skill"));
                            strProject = Integer.parseInt(jb.optString("project"));
                            strCert = Integer.parseInt(jb.optString("cert"));
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(mContext, msg.toString(), Toast.LENGTH_LONG).show();
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
                ToastUtils.show(mContext, "网络异常");
            }
        };

        Map<String,String> map = new HashMap<String,String>();
        map.put("uid", EggshellApplication.getApplication().getUser().getId()+"");
        map.put("eid",resume.getRid()+"");

        RequestUtils.createRequest(mContext, Urls.RESUME_RESUME_LIST, "", true, map, true, listener, errorListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}
