package com.taihe.eggshell.company;

import android.content.Context;
import android.widget.Button;
import android.widget.CheckBox;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

import java.util.ArrayList;

/**
 * Created by wang on 2015/11/24.
 */
public class CompanyJobListActivity extends BaseActivity{

    private Context mContext;

    private PullToRefreshListView jobListView;
    private CheckBox checkBox;
    private Button stopButton,refreshButton,deleteButton;
    private ArrayList<CompanyJob> joblist = new ArrayList<CompanyJob>();

    @Override
    public void initView() {
        setContentView(R.layout.activity_company_job_list);
        super.initView();

        mContext = this;
        jobListView = (PullToRefreshListView)findViewById(R.id.id_company_job_all);
        jobListView.setMode(PullToRefreshBase.Mode.BOTH);

    }

    @Override
    public void initData() {
        super.initData();
        initTitle("全部职位");
        for(int i=0;i<5;i++){
            CompanyJob job = new CompanyJob();
            job.setId(i+"");
            joblist.add(job);
        }

        CompanyJobAdapter adapter = new CompanyJobAdapter(mContext,joblist);
        jobListView.setAdapter(adapter);
    }
}
