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
    private CompanyJobAdapter adapter;

    private int pagesize = 3;
    private int pagenum = 1;

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
        initTitle("全部职位");

        stopButton.setOnClickListener(this);
        refreshButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {
                        if(flag){

                        }else{

                        }
            }
        });


        adapter = new CompanyJobAdapter(mContext,joblist);
        jobListView.setAdapter(adapter);

        if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)){
            getCompanyJobList();
        }else{
            ToastUtils.show(mContext,R.string.error_network);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_stop:
                break;
            case R.id.btn_refresh:
                break;
            case R.id.btn_del:
                break;
        }
    }

    private void getCompanyJobList(){

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {

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

            }
        };

        HashMap<String,String> param = new HashMap<>();
        param.put("uid","1869");
        param.put("jobstatus","0");
        param.put("pagenum",pagenum+"");
        param.put("pagesize",pagesize+"");

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_GET_COMPANY_JOB, false, param, true, listener, errorListener);
    }
}
