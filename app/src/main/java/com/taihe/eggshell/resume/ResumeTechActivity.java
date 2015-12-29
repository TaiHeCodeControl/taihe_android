package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.activity.IndustryActivity;
import com.taihe.eggshell.main.entity.StaticData;
import com.taihe.eggshell.resume.entity.ResumeData;
import com.taihe.eggshell.resume.entity.Resumes;
import com.taihe.eggshell.widget.LoadingProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang on 2015/8/14.
 */
public class ResumeTechActivity extends BaseActivity{

    private static final String TAG = "ResumeTechActivity";

    private Context mContext;

    private Intent intent;
    private TextView commitText,deleteText,techtypeEdit,levelEdit,resume_name;
    private EditText techEdit,techYear,workTimeEnd;
    private LoadingProgressDialog loading;
    private Resumes resume;
    private String techName,years,techType,techLevel,strLB,strSLD,strType,jobID;
    private int id_skill,id_level;

    private Map<String,String> params = new HashMap<String, String>();

    private static final int RESULT_INDUSTRY = 20;
    private static final int RESULT_LEVEL = 21;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_tech);
        super.initView();

        mContext = this;

        resume_name = (TextView)findViewById(R.id.id_resume_num);
        commitText = (TextView)findViewById(R.id.id_commit);
        deleteText = (TextView)findViewById(R.id.id_delete);
        techEdit = (EditText)findViewById(R.id.id_tech_name);
        techtypeEdit = (TextView)findViewById(R.id.id_tech_type);
        levelEdit = (TextView)findViewById(R.id.id_tech_level);
        techYear = (EditText)findViewById(R.id.id_year);

        commitText.setVisibility(View.VISIBLE);
        techtypeEdit.setOnClickListener(this);
        levelEdit.setOnClickListener(this);
        commitText.setOnClickListener(this);
        deleteText.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("写简历");

        resume = getIntent().getParcelableExtra("eid");
        strType=getIntent().getStringExtra("type");
        resume_name.setText(resume.getName()+"-专业技能");
        loading = new LoadingProgressDialog(mContext,"正在提交...");
        ResumeData worklists;
        if(!"".equals(strType)){
            commitText.setVisibility(View.VISIBLE);
            deleteText.setVisibility(View.VISIBLE);
            worklists =  getIntent().getParcelableExtra("listobj");
            jobID = worklists.getId()+"";
            techEdit.setText(worklists.getName());
            techtypeEdit.setText(worklists.getSkill());
            levelEdit.setText(worklists.getSpecialty());
            techYear.setText(worklists.getIng());
        }else{
            commitText.setVisibility(View.GONE);
            deleteText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_tech_type:
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "skill");
                startActivityForResult(intent, RESULT_INDUSTRY);
                break;
            case R.id.id_tech_level:
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "techlevel");
                startActivityForResult(intent, RESULT_LEVEL);
                break;
            case R.id.id_commit:
                techName = techEdit.getText().toString();
                years = techYear.getText().toString();
                techType = techtypeEdit.getText().toString();
                techLevel = levelEdit.getText().toString();

                if(TextUtils.isEmpty(techName) || TextUtils.isEmpty(techLevel) ||
                        TextUtils.isEmpty(techType) || TextUtils.isEmpty(years)){
                    ToastUtils.show(mContext,"请填写完整");
                }else{
                    params.put("uid", EggshellApplication.getApplication().getUser().getId()+"");
                    params.put("eid",resume.getRid()+"");
                    params.put("name",techName);
                    params.put("skill",id_skill+"");
                    params.put("ing",id_level+"");
                    params.put("longtime",years);
                    if(!"".equals(strType)){
                        params.put("id",jobID);
                    }
                    if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)){
                        loading.show();
                        submitToServer();
                    }else{
                        ToastUtils.show(mContext,R.string.check_network);
                    }

                }

                break;
            case R.id.id_reset:
                techEdit.setText ("");
                techYear.setText("");
                workTimeEnd.setText("");
                techtypeEdit.setText("");
                levelEdit.setText("");

                break;
        }
    }

    private void submitToServer(){
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                loading.dismiss();
//                Log.v(TAG,(String)o);
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        ToastUtils.show(mContext,"创建成功");
//                        Intent intent = new Intent(mContext,ResumeTechScanActivity.class);
//                        intent.putExtra("eid",resume);
//                        intent.putExtra("name",techName);
//                        intent.putExtra("specialty",strLB);
//                        intent.putExtra("title",strSLD);
//                        intent.putExtra("sdate",years);
//                        startActivity(intent);
                        finish();
                    }else{
                        ToastUtils.show(mContext,"创建失败");
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
                ToastUtils.show(mContext,"网络异常");
            }
        };

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(),Urls.METHOD_RESUME_TECH,false,params,true,listener,errorListener);
    }
    StaticData result;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            result = data.getParcelableExtra("data");
            if(null == result){
                return;
            }
            switch (requestCode){
                case RESULT_INDUSTRY:
                    strLB=result.getName();
                    techtypeEdit.setText(result.getName());
                    id_skill = result.getId();
                    break;
                case RESULT_LEVEL:
                    strSLD=result.getName();
                    levelEdit.setText(result.getName());
                    id_level = result.getId();
                    break;
            }
        }
    }
}
