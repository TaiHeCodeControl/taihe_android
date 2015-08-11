package com.taihe.eggshell.job.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.JobApplyDialogUtil;
import com.taihe.eggshell.job.activity.JobDetailActivity;
import com.taihe.eggshell.job.adapter.AllJobAdapter;
import com.taihe.eggshell.job.bean.JobInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 全部职位列表
 * Created by huan on 2015/8/6.
 */
public class AllJobFragment extends Fragment implements View.OnClickListener{


    private AllJobAdapter adapter;
    private CheckBox cb_selectAll;


    private List<JobInfo> jobInfos = null;
    private JobInfo jobInfo;

    private ListView list_job_all;
    private View rootView;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_job_all, null) ;

        mContext = getActivity();
        initView();
        initDate();
        return rootView ;
    }



    private void initView() {
        jobInfos = new ArrayList<JobInfo>();

        for(int i = 0; i < 10;i++){
            jobInfo = new JobInfo(false,i);
            jobInfos.add(jobInfo);
        }
        list_job_all = (ListView) rootView.findViewById(R.id.list_job_all);

        list_job_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //listviewItem点击事件

                Intent intent = new Intent(mContext, JobDetailActivity.class);
                startActivity(intent);
            }
        });


    }


    private void initDate() {
        adapter = new AllJobAdapter(mContext,jobInfos,true);
        View footerView = View.inflate(mContext,R.layout.list_job_all_footer,null);


        list_job_all.addFooterView(footerView);

        list_job_all.setAdapter(adapter);

        Button btn_shenqing = (Button)footerView.findViewById(R.id.btn_alljob_shenqing);
        btn_shenqing.setOnClickListener(this);
        cb_selectAll = (CheckBox) footerView.findViewById(R.id.cb_findjob_selectall);

        cb_selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                adapter.notifyDataChanged(isChecked);
            }
        });



    }





    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_alljob_shenqing:
                JobApplyDialogUtil.isApplyJob(mContext);
                break;
        }
    }

    public void postJob() {
        for(JobInfo jobInfo : jobInfos){


        }
    }
}
