package com.taihe.eggshell.main;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.adapter.AllJobAdapter;
import com.taihe.eggshell.job.bean.JobInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang on 2015/8/11.
 */
public class CompanyDetailActivity extends BaseActivity{

    private static final String TAG = "CompanyDetailActivity";

    private Context mContext;
    private TextView companyIndustry,companyType,companyScale,companyAddress,companyBrief;
    private PullToRefreshListView jobsListView;

    private List<JobInfo> jobInfos = new ArrayList<JobInfo>();

    @Override
    public void initView() {
        setContentView(R.layout.activity_company_detail);
        super.initView();

        mContext = this;

        companyIndustry = (TextView)findViewById(R.id.id_company_industry);
        companyType = (TextView)findViewById(R.id.id_company_type);
        companyScale = (TextView)findViewById(R.id.id_company_scale);
        companyAddress = (TextView)findViewById(R.id.id_company_addr);
        companyBrief = (TextView)findViewById(R.id.id_company_brief);
        jobsListView = (PullToRefreshListView)findViewById(R.id.id_jobs_list_view);


    }

    @Override
    public void initData() {
        super.initData();
        initTitle("名企详情");

        int companyId = getIntent().getIntExtra("companyId",0);
        ToastUtils.show(mContext,companyId+"哈哈");
        AllJobAdapter jobAdapter = new AllJobAdapter(mContext,jobInfos);
        jobsListView.setAdapter(jobAdapter);

        jobsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.show(mContext,position+"");
            }
        });
    }


}
