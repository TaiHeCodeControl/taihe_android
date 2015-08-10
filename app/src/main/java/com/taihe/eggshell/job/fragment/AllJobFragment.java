package com.taihe.eggshell.job.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.activity.JobDetailActivity;
import com.taihe.eggshell.job.adapter.AllJobAdapter;
import com.taihe.eggshell.job.bean.JobInfo;
import com.taihe.eggshell.widget.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 全部职位列表
 * Created by huan on 2015/8/6.
 */
public class AllJobFragment extends Fragment implements View.OnClickListener{

    private AlertDialog isApplyDialog = null;
    private AllJobAdapter adapter;
    private CheckBox cb_selectAll;


    private List<JobInfo> jobInfos = null;
    private JobInfo jobInfo;

    private MyListView list_job_all;
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
            jobInfo = new JobInfo(true,i);
            jobInfos.add(jobInfo);
        }
        list_job_all = (MyListView) rootView.findViewById(R.id.list_job_all);

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
        adapter = new AllJobAdapter(mContext,jobInfos);
        View footerView = View.inflate(mContext,R.layout.list_job_all_footer,null);
        list_job_all.addFooterView(footerView);

        list_job_all.setAdapter(adapter);

        Button btn_shenqing = (Button)footerView.findViewById(R.id.btn_alljob_shenqing);
        btn_shenqing.setOnClickListener(this);
        cb_selectAll = (CheckBox) footerView.findViewById(R.id.cb_findjob_selectall);

        cb_selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {

                    for (JobInfo jobInfo : jobInfos) {
                        jobInfo.setIsChecked(true);
                    }
                    // 刷新view
                    adapter.notifyDataSetChanged();
                } else {
                    for (JobInfo jobInfo : jobInfos) {
                        jobInfo.setIsChecked(false);
                    }
                    // 刷新view
                    adapter.notifyDataSetChanged();
                }
            }
        });



    }

    public void isApplyJob(){


        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        View view = View.inflate(mContext, R.layout.dialog_isapplyjob,
                null);

        final ImageView iv_cancel = (ImageView) view.findViewById(R.id.iv_isapplyjob_cancel);
        final Button btn_ok = (Button) view.findViewById(R.id.btn_isapplyjob_ok);


        iv_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isApplyDialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ToastUtils.show(mContext, "职位投递成功");
            }
        });

        builder.setView(view);
        isApplyDialog = builder.create();
        isApplyDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_alljob_shenqing:
                isApplyJob();
                break;
        }
    }
}
