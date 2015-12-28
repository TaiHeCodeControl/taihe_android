package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.taihe.eggshell.resume.adapter.ResumeListAdapter;
import com.taihe.eggshell.resume.entity.Resumes;
import com.taihe.eggshell.widget.ChoiceDialog;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

    private LoadingProgressDialog loading;
    private TextView createResume,edtResume,useResume,delResume,numResume;
    private ChoiceDialog deleteDialog;
    private LinearLayout lin_back;
    private ListView resumelistview;
    private ResumeListAdapter adapter;
    private List<Resumes> resumelist = new ArrayList<Resumes>();
    private List<Resumes> selectedresumelist = new ArrayList<Resumes>();

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_manager);
        super.initView();

        mContext = this;

        lin_back = (LinearLayout) findViewById(R.id.lin_back);
        lin_back.setOnClickListener(this);

        numResume = (TextView)findViewById(R.id.id_resume_num);
        resumelistview = (ListView)findViewById(R.id.id_resume_list);
        createResume = (TextView)findViewById(R.id.id_create_resume);
        edtResume = (TextView)findViewById(R.id.id_edt);
        useResume = (TextView)findViewById(R.id.id_use);
        delResume = (TextView)findViewById(R.id.id_del);

        createResume.setOnClickListener(this);
        edtResume.setOnClickListener(this);
        useResume.setOnClickListener(this);
        delResume.setOnClickListener(this);

    }

    @Override
    public void initData() {
        super.initData();

        initTitle("简历管理");
        loading = new LoadingProgressDialog(mContext,"正在请求...");

        adapter = new ResumeListAdapter(mContext,resumelist,new ResumeListAdapter.ResumeSelectedListener() {
            @Override
            public void selectedResume(Resumes resume) {
                if(!selectedresumelist.contains(resume)){
                    selectedresumelist.add(resume);
                }
            }

            @Override
            public void deleteResume(Resumes resumes) {
                selectedresumelist.remove(resumes);
            }
        });
        resumelistview.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
        selectedresumelist.clear();
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
            case R.id.id_create_resume:
                intent = new Intent(mContext,ResumeWriteActivity.class);
                startActivity(intent);
                break;
            case R.id.id_edt:
                if(selectedresumelist.size()==1){
                    intent = new Intent(mContext,ResumeMultiActivity.class);
                    intent.putExtra("resume",selectedresumelist.get(0));
                    startActivity(intent);
                }else{
                    ToastUtils.show(mContext,"请选择一份简历");
                }
                break;
            case R.id.id_use:
                if(selectedresumelist.size()==1){
                    sendResumeId();
                }else{
                    ToastUtils.show(mContext, "请选择一份简历");
                }
                break;
            case R.id.id_del:
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

                if(selectedresumelist.size()>0){
                    deleteDialog.show();
                }else{
                    ToastUtils.show(mContext,"请选择要删除的简历");
                }
                break;
        }
    }

    private void sendResumeId(){
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                loading.dismiss();
//                Log.v(TAG,(String)o);
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        ToastUtils.show(mContext,"你使用了简历:"+selectedresumelist.get(0).getName());
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
        Map<String,String> params = new HashMap<String, String>();
        params.put("eid", selectedresumelist.get(0).getRid()+"");
        params.put("uid", EggshellApplication.getApplication().getUser().getId()+"");
//        Log.v(TAG,params.toString());
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(),Urls.METHOD_RESUME_USE,false,params,true,listener,errorListener);

    }

    private void getUserResumeList(){
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                loading.dismiss();
//                Log.v(TAG,(String)o);
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        List<Resumes> resumesList = gson.fromJson(data,new TypeToken<List<Resumes>>(){}.getType());
                        if(resumesList.size()>0){
                            if(resumesList.size()>=5){
                                createResume.setEnabled(false);
                                createResume.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.unable_select_background));
                            }else{
                                createResume.setEnabled(true);
                                createResume.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.msg_vercode_background));
                            }
                            numResume.setText("我的简历("+resumesList.size()+")");
                            resumelist.clear();
                            resumelist.addAll(resumesList);
                            adapter.notifyDataSetChanged();
//                            EggshellApplication.getApplication().getUser().setResumeid(resume.getRid()+"");
                        }else{
                            resumelist.clear();
                            adapter.notifyDataSetChanged();
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
//                Log.v(TAG,new String(volleyError.networkResponse.data));
                    ToastUtils.show(mContext,volleyError);
            }
        };
        Map<String,String> params = new HashMap<String, String>();
        params.put("uid", EggshellApplication.getApplication().getUser().getId()+"");

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
                        getUserResumeList();
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
//                Log.v(TAG,new String(volleyError.networkResponse.data));
                ToastUtils.show(mContext,volleyError);
            }
        };
        Map<String,String> params = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        for(Resumes r : selectedresumelist){
            sb.append(r.getRid()+",");
        }
        params.put("eid", sb.toString().substring(0, sb.toString().lastIndexOf(",")));
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

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}
