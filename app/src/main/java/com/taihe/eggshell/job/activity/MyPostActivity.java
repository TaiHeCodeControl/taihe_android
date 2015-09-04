package com.taihe.eggshell.job.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.job.adapter.AllJobAdapter;
import com.taihe.eggshell.job.bean.JobInfo;
import com.taihe.eggshell.login.LoginActivity;
import com.taihe.eggshell.main.MainActivity;
import com.taihe.eggshell.widget.JobApplyDialogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by huan on 2015/7/23.
 */
public class MyPostActivity extends BaseActivity {


    private AllJobAdapter adapter;
    private CheckBox cb_selectAll;


    private List<JobInfo> jobInfos = null;
    private JobInfo jobInfo;

    private ListView list_job_all;
    private Context mContext;


    private View footerView;
    private static final String TAG = "MyPostActivity";

    //选中条数的统计
    public int selectSize = 0;

    @Override
    public void initView() {
        setContentView(R.layout.activity_job_list);
        super.initView();
        mContext = this;
    }

    @Override
    public void initData() {
        super.initData();
        super.initTitle("投递职位");
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.lin_back:
                goBack();

                break;
            case R.id.btn_alljob_shenqing:
                JobApplyDialogUtil.isApplyJob(mContext,10,2);
                postJob();
                break;
        }
    }

    private void goBack() {
        Intent intent = new Intent(MyPostActivity.this, MainActivity.class);
        intent.putExtra("MeFragment", "MeFragment");
        startActivity(intent);
        this.finish();
    }

    public void postJob() {
        for (JobInfo jobInfo : jobInfos) {
//            System.out.println(jobInfo.getId()+"======"+jobInfo.isChecked());

        }
    }

    //监听返回按钮
    @Override
    public void onBackPressed() {
        goBack();
    }
}
