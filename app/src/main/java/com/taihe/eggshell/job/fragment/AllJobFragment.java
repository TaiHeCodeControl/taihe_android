package com.taihe.eggshell.job.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.widget.JobApplyDialogUtil;
import com.taihe.eggshell.job.activity.JobDetailActivity;
import com.taihe.eggshell.job.adapter.AllJobAdapter;
import com.taihe.eggshell.job.bean.JobInfo;
import com.taihe.eggshell.widget.LoadingProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全部职位列表
 * Created by huan on 2015/8/6.
 */
public class AllJobFragment extends Fragment implements View.OnClickListener {


    private AllJobAdapter adapter;
    public CheckBox cb_selectAll;

    public List<JobInfo> jobInfos = null;
    private JobInfo jobInfo;

    private PullToRefreshGridView list_job_all;
    private View rootView;
    private Context mContext;
    private LoadingProgressDialog dialog;
    private int page = 1;
    private int pageSize = 10;
    //选中条数的统计
    private int selectSize = 0;
    private int postednum = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_job_all, null);

        mContext = getActivity();
        initView();
        initDate();
        return rootView;
    }


    private void initView() {

        jobInfos = new ArrayList<JobInfo>();

        list_job_all = (PullToRefreshGridView) rootView.findViewById(R.id.list_alljob_all);

        list_job_all.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        list_job_all.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                cb_selectAll.setChecked(false);
                page++;
                getList();
                list_job_all.onRefreshComplete();
            }
        });


        list_job_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //listviewItem点击事件

                if (position < jobInfos.size()) {
                    JobInfo job = jobInfos.get(position);
                    Intent intent = new Intent(mContext, JobDetailActivity.class);
                    intent.putExtra("ID",job.getId());
                    Log.i("ID",job.getId() + "");
                    startActivity(intent);
                }

            }
        });

        Button btn_shenqing = (Button) rootView.findViewById(R.id.btn_alljob_shenqing);
        btn_shenqing.setOnClickListener(this);
        cb_selectAll = (CheckBox) rootView.findViewById(R.id.cb_findjob_selectall);

        cb_selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cb_selectAll.isChecked()) {
                    cb_selectAll.setChecked(false);
                    for (JobInfo info : jobInfos) {
                        info.setIsChecked(false);
                    }
                } else {
                    cb_selectAll.setChecked(true);
                    selectSize = jobInfos.size();
                    for (JobInfo info : jobInfos) {
                        info.setIsChecked(true);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });


    }


    private void initDate() {

        if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)){
            dialog = new LoadingProgressDialog(mContext, getResources().getString(
                    R.string.submitcertificate_string_wait_dialog));
            dialog.show();
            getList();
        }else{
            ToastUtils.show(mContext,R.string.check_network);
        }


    }

    private void getList(){

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                dialog.dismiss();
                try {
                    Log.v("HHH:",(String)o);

                    JSONObject jsonObject = new JSONObject((String)o);

                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if(code ==0){
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        List<JobInfo> joblist =  gson.fromJson(data,new TypeToken<List<JobInfo>>(){}.getType());
                        jobInfos.addAll(joblist);
                        adapter = new AllJobAdapter(mContext, jobInfos, true);
                        adapter.setCheckedListener(new AllJobAdapter.checkedListener() {
                            @Override
                            public void checkedPosition(int position, boolean isChecked) {
                                jobInfos.get(position).setIsChecked(isChecked);
                                //如果有listview没有被选中，全选按钮状态为false
                                if (jobInfos.get(position).isChecked()) {
                                    selectSize += 1;
                                    if (selectSize == jobInfos.size()) {
                                        cb_selectAll.setChecked(true);
                                    }
                                } else {
                                    selectSize -= 1;
                                    cb_selectAll.setChecked(false);
                                }


                            }
                        });

                        list_job_all.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }else{
                        ToastUtils.show(mContext,"获取失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                try{
                    if(null!=volleyError.networkResponse.data){
                        Log.v("Forget:", new String(volleyError.networkResponse.data));
                    }
                    ToastUtils.show(mContext,volleyError.networkResponse.statusCode+"");

                }catch(Exception e){
                    ToastUtils.show(mContext,"联网失败");
                }

            }
        };

        Map<String,String> param = new HashMap<String, String>();
        param.put("page",page+"");
        param.put("limit",pageSize+"");

        RequestUtils.createRequest(mContext, "http://195.198.1.84/eggker/interface", Urls.METHOD_JOB_LIST, false, param, true, listener, errorListener);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_alljob_shenqing:
                JobApplyDialogUtil.isApplyJob(mContext,selectSize,postednum);
                postJob();//申请职位
                break;
        }
    }

    public void postJob() {
        for (JobInfo jobInfo : jobInfos) {
            System.out.println(jobInfo.getId() + "======" + jobInfo.isChecked());

        }
    }
}
