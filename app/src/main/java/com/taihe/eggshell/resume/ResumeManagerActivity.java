package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.taihe.eggshell.main.MainActivity;
import com.taihe.eggshell.widget.LoadingProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2015/8/13.
 */
public class ResumeManagerActivity extends BaseActivity{

    private static final String TAG = "ResumeManagerActivity";
    private Context mContext;
    private Intent intent;

    private Resumes resume;
    private LoadingProgressDialog loading;
    private TextView createResume,scanResume,edtResume,useResume,delResume;
    private CheckBox checkBox;
    private LinearLayout lin_back;
    private RelativeLayout resumeRelative;
    private View line;

    private boolean isSelected = false;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_manager);
        super.initView();

        mContext = this;

        lin_back = (LinearLayout) findViewById(R.id.lin_back);
        lin_back.setOnClickListener(this);

        resumeRelative = (RelativeLayout)findViewById(R.id.id_resume_list);
        line = findViewById(R.id.id_line_resume);

        createResume = (TextView)findViewById(R.id.id_create_resume);
        scanResume = (TextView)findViewById(R.id.id_scan_resume);
        edtResume = (TextView)findViewById(R.id.id_edt);
        useResume = (TextView)findViewById(R.id.id_use);
        delResume = (TextView)findViewById(R.id.id_del);
        checkBox = (CheckBox)findViewById(R.id.id_check_box);

        createResume.setOnClickListener(this);
        scanResume.setOnClickListener(this);
        edtResume.setOnClickListener(this);
        useResume.setOnClickListener(this);
        delResume.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSelected = isChecked;
                if(isSelected){
//                    EggshellApplication.getApplication().getUser().setResumeid(resume.getRid()+"");
                }else{
//                    EggshellApplication.getApplication().getUser().setResumeid("");
                }
            }
        });
    }

    @Override
    public void initData() {
        super.initData();

        initTitle("简历管理");
        loading = new LoadingProgressDialog(mContext,"正在请求...");
        if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
            loading.show();
            getUserResumeList();
        }else{
            ToastUtils.show(mContext,R.string.check_network);
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            /*case R.id.lin_back:
                goBack();

                break;*/
            case R.id.id_create_resume:
                intent = new Intent(mContext,ResumeWriteActivity.class);
                startActivity(intent);
                break;
            case R.id.id_scan_resume:
                intent = new Intent(mContext,ResumeScanActivity.class);
                intent.putExtra("eid",resume.getRid()+"");
                startActivity(intent);
                break;
            case R.id.id_edt:
                if(isSelected){
                    intent = new Intent(mContext,ResumeWriteActivity.class);
                    intent.putExtra("eid",resume.getRid()+"");
                    startActivity(intent);
                }else{
                    ToastUtils.show(mContext,"请选择简历");
                }
                break;
            case R.id.id_use:
                if(isSelected){
                    ToastUtils.show(mContext,"你使用了简历:"+resume.getName());
                }else{
                    ToastUtils.show(mContext,"请选择简历");
                }
                break;
            case R.id.id_del:
                if(isSelected){
                    if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)){
                        loading.show();
                        deleteResume();
                    }else{
                        ToastUtils.show(mContext,R.string.check_network);
                    }
                }else{
                    ToastUtils.show(mContext,"请选择要删除的简历");
                }
                break;
        }
    }

    private void getUserResumeList(){
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                loading.dismiss();
                Log.v(TAG,(String)o);
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        List<Resumes> resumesList = gson.fromJson(data,new TypeToken<List<Resumes>>(){}.getType());
                        if(resumesList.size()>0){
                            createResume.setEnabled(false);
                            createResume.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.unable_select_background));
                            resumeRelative.setVisibility(View.VISIBLE);
                            line.setVisibility(View.VISIBLE);
                            resume = resumesList.get(0);
                            checkBox.setText(resume.getName());
//                            EggshellApplication.getApplication().getUser().setResumeid(resume.getRid()+"");
                        }else{
                            resumeRelative.setVisibility(View.GONE);
                            line.setVisibility(View.GONE);
                        }
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
        Map<String,String> params = new HashMap<String, String>();
        params.put("uid","65");

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(),Urls.METHOD_GET_RESUME,false,params,true,listener,errorListener);
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
                        resumeRelative.setVisibility(View.GONE);
                        line.setVisibility(View.GONE);
                        createResume.setEnabled(true);
                        createResume.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.msg_vercode_background));
//                        EggshellApplication.getApplication().getUser().setResumeid("");
                        ToastUtils.show(mContext,"删除成功");
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
        Map<String,String> params = new HashMap<String, String>();
        params.put("eid",resume.getRid()+"");

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(),Urls.METHOD_DELETE_RESUME,false,params,true,listener,errorListener);
    }

    private void goBack() {
        Intent intent = new Intent(ResumeManagerActivity.this, MainActivity.class);
        intent.putExtra("MeFragment", "MeFragment");
        startActivity(intent);
        this.finish();
    }

//    监听返回按钮
    @Override
    public void onBackPressed() {
        goBack();
    }
}
