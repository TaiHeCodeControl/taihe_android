package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.resume.adapter.ResumeAdapter;
import com.taihe.eggshell.resume.entity.ResumeData;
import com.taihe.eggshell.resume.entity.Resumes;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.taihe.eggshell.widget.MyListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2015/8/15.
 */
public class ResumeWorkScanActivity extends BaseActivity{

    private static final String TAG = "ResumeWorkScanActivity";

    private Context mContext;

    private TextView commitText,resume_name;
    private Resumes eid;
    String tempTielt;
    private MyListView worklistview;
    private LoadingProgressDialog loading;
    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_work_scan);
        super.initView();
        mContext = this;
        resume_name = (TextView)findViewById(R.id.id_resume_num);
        commitText = (TextView)findViewById(R.id.id_go_on);
        worklistview = (MyListView)findViewById(R.id.id_work_list_look);
        commitText.setOnClickListener(this);
        loading = new LoadingProgressDialog(mContext,"正在请求...");
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("写简历");
        Intent intent = getIntent();
        eid = intent.getParcelableExtra("eid");
        tempTielt = intent.getStringExtra("acttitle");
        String tempT="";
        if("work".equals(tempTielt)){
            tempT="工作经历";
        }else if("edu".equals(tempTielt)){
            tempT="教育经历";
        }else if("train".equals(tempTielt)){
            tempT="培训经历";
        }
        resume_name.setText(eid.getName()+"一"+tempT);
        loading.show();
        getResumeData(eid.getRid()+"");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_go_on:
                Intent intent = null;
                if("work".equals(tempTielt)){
                    intent = new Intent(mContext,ResumeWorkActivity.class);
                }else if("edu".equals(tempTielt)){
                    intent = new Intent(mContext,ResumeEduActivity.class);
                }else if("train".equals(tempTielt)){
                    intent = new Intent(mContext,ResumeTrainActivity.class);
                }
                intent.putExtra("eid",eid);
                startActivity(intent);
                finish();
                break;
        }
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

    private void getResumeData(String id) {
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                Log.v(TAG, (String) o);
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        JSONObject data = jsonObject.getJSONObject("data");

                        String[] tempTag = new String[] {"单位名称:","工作时间:","所在部门:","担任职位:","工作内容:"};
                        Gson gson = new Gson();
                        String worklist = data.getString("work");//工作经验
                        List<ResumeData> worklists = gson.fromJson(worklist,new TypeToken<List<ResumeData>>(){}.getType());
                        worklistview.setAdapter(new ResumeAdapter(mContext,worklists,1,tempTag));
                        if("work".equals(tempTielt)){
                            return;
                        }
                        tempTag = new String[] {"学校名称:","在校时间:","所学专业:","社团职位:","专业描述:"};
                        worklist = data.getString("jy");//教育
                        if(!worklist.equals("[]")){
                            worklists = gson.fromJson(worklist,new TypeToken<List<ResumeData>>(){}.getType());
                            worklistview.setAdapter(new ResumeAdapter(mContext,worklists,2,tempTag));
                        }
                        if("edu".equals(tempTielt)){
                            return;
                        }

                        tempTag = new String[] {"培训中心:","培训时间:","","培训方向:","培训描述:"};
                        worklist = data.getString("training");//培训
                        if(!worklist.equals("[]")){
                            worklists = gson.fromJson(worklist,new TypeToken<List<ResumeData>>(){}.getType());
                            worklistview.setAdapter(new ResumeAdapter(mContext,worklists,3,tempTag));
                        }
                        if("train".equals(tempTielt)){
                            return;
                        }

                        worklistview.setSelection(worklists.size());
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
                ToastUtils.show(mContext, volleyError);
            }
        };

        Map<String,String> map = new HashMap<String,String>();
        map.put("eid",id);

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_RESUME_SCAN, false, map, true, listener, errorListener);
    }
}
