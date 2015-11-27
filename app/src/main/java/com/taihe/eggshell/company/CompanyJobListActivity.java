package com.taihe.eggshell.company;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.taihe.eggshell.widget.datepicker.TimeDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wang on 2015/11/24.
 */
public class CompanyJobListActivity extends BaseActivity{

    private static final String TAG = "CompanyJobListActivity";

    private Context mContext;

    private PullToRefreshListView jobListView;
    private CheckBox checkBox;
    private Button stopButton,refreshButton,deleteButton;
    private ArrayList<CompanyJob> joblist = new ArrayList<CompanyJob>();
    private ArrayList<CompanyJob> selectedjoblist = new ArrayList<CompanyJob>();
    private CompanyJobAdapter adapter;
    private TimeDialog timeDialog;
    private LoadingProgressDialog loadingDialog;

    private int pagesize = 10;
    private int pagenum = 1;
    private int type = 0;
    private int status = 0;//状态
    private String delaytime = "";

    @Override
    public void initView() {
        setContentView(R.layout.activity_company_job_list);
        super.initView();

        mContext = this;
        jobListView = (PullToRefreshListView)findViewById(R.id.id_company_job_all);
        jobListView.setMode(PullToRefreshBase.Mode.BOTH);

        checkBox = (CheckBox)findViewById(R.id.id_select_all);
        stopButton = (Button)findViewById(R.id.btn_stop);
        refreshButton = (Button)findViewById(R.id.btn_refresh);
        deleteButton = (Button)findViewById(R.id.btn_del);

    }

    @Override
    public void initData() {
        super.initData();

        String title = "";
        type = getIntent().getIntExtra("type",0);
        if(0==type){
            title = "全部职位";
            stopButton.setText("暂停招聘");
            refreshButton.setText("职位刷新");
            deleteButton.setText("删除");
        }else if(1==type){
            title = "招聘中职位";
            stopButton.setText("职位延期");
            refreshButton.setText("职位刷新");
            deleteButton.setVisibility(View.GONE);
        }else if(2==type){
            title = "过期职位";
            stopButton.setText("职位延期");
            refreshButton.setVisibility(View.GONE);
            deleteButton.setText("删除");
        }
        initTitle(title);

        stopButton.setOnClickListener(this);
        refreshButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {
                        if(flag){
                            for(CompanyJob job : joblist){
                                job.setIsSelected(true);
                            }
                            selectedjoblist.clear();
                            selectedjoblist.addAll(joblist);
                        }else{
                            for(CompanyJob job : joblist){
                                job.setIsSelected(false);
                            }
                            selectedjoblist.clear();
                        }
                adapter.notifyDataSetChanged();

            }
        });

        adapter = new CompanyJobAdapter(mContext,joblist);
        jobListView.setAdapter(adapter);
        adapter.setListener(new CompanyJobAdapter.JobCheckClicklistener() {
            @Override
            public void addOrDel(CompanyJob job, boolean flag) {
                if(flag){
                    if(!selectedjoblist.contains(job)){
                        selectedjoblist.add(job);
                    }
                }else{
                    selectedjoblist.remove(job);
                }
            }
        });

        loadingDialog =  new LoadingProgressDialog(mContext,"正在请求...");

        if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)){
            loadingDialog.show();
            getCompanyJobList("1869");
        }else{
            ToastUtils.show(mContext,R.string.error_network);
        }

        timeDialog = new TimeDialog(mContext,this,new TimeDialog.CustomTimeListener() {
            @Override
            public void setTime(String time) {
                delaytime = time;
                updateCompanyJobList(status,getSelectedPosition());
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_stop:

                if(0==type){//暂停招聘
                    status = 0;
                    updateCompanyJobList(status,getSelectedPosition());
                }else{//职位延期
                    status = 3;
                    timeDialog.show();
                }
                break;
            case R.id.btn_refresh:
                status = 1;//刷新
                updateCompanyJobList(status,getSelectedPosition());
                break;
            case R.id.btn_del:
                status = 2;//删除
                updateCompanyJobList(status,getSelectedPosition());
                break;
        }
    }

    private void getCompanyJobList(String id){

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                loadingDialog.dismiss();
                Log.v(TAG,(String)o);
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code==0){
                        Gson gson = new Gson();
                        String jobstring = jsonObject.getString("data");
                        List<CompanyJob> list = gson.fromJson(jobstring,new TypeToken<List<CompanyJob>>(){}.getType());
                        joblist.addAll(list);
                        adapter.notifyDataSetChanged();

                    }else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loadingDialog.dismiss();
            }
        };

        HashMap<String,String> param = new HashMap<>();
        param.put("uid",id);
        param.put("jobstatus",type+"");
        param.put("pagenum",pagenum+"");
        param.put("pagesize",pagesize+"");

        Log.v(TAG,param.toString());
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_GET_COMPANY_JOB, false, param, true, listener, errorListener);
    }

    private void updateCompanyJobList(int status,String jobids){
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {

//                Log.v(TAG+"UPDATE",(String)o);
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code==0){
                        String count = jsonObject.getString("data");
                        ToastUtils.show(mContext,jsonObject.getString("message"));
                    }else{

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                        ToastUtils.show(mContext,volleyError);
            }
        };

        HashMap<String,String> param = new HashMap<>();
        param.put("uid","1869");
        param.put("changestatus",status+"");
        param.put("jobid",jobids);
        param.put("totime",delaytime);

        Log.v(TAG, param.toString());
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_UPDATE_COMPANY_JOB, false, param, true, listener, errorListener);
    }

    private String getSelectedPosition(){

        ArrayList<CompanyJob> list = new ArrayList<CompanyJob>();
        StringBuilder builder = new StringBuilder("");
        for(CompanyJob job : selectedjoblist){
            if(!list.contains(job)){
                list.add(job);
            }
        }

        for(CompanyJob j : list){
            builder.append(j.getCj_id()+",");
        }

        return builder.toString().substring(0, builder.toString().lastIndexOf(","));
    }
}
