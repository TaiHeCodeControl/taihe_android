package com.taihe.eggshell.company;

import android.content.Context;
import android.widget.Button;
import android.widget.CheckBox;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by wang on 2015/11/24.
 */
public class CompanyJobListActivity extends BaseActivity{

    private Context mContext;

    private PullToRefreshListView jobListView;
    private CheckBox checkBox;
    private Button stopButton,refreshButton,deleteButton;

    @Override
    public void initView() {
        setContentView(R.layout.activity_company_job_list);
        super.initView();

        mContext = this;

    }

    @Override
    public void initData() {
        super.initData();
    }
}
