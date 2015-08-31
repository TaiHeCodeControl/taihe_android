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
        initListView();
        initListData();
    }


    public void initListView() {

        jobInfos = new ArrayList<JobInfo>();

        for (int i = 0; i < 10; i++) {
            jobInfo = new JobInfo(false, i);
            jobInfos.add(jobInfo);
        }
        list_job_all = (ListView) findViewById(R.id.list_job_all);

        //给listview增加底部view
        footerView = View.inflate(mContext, R.layout.list_job_all_footer, null);
        footerView.setVisibility(View.GONE);
        list_job_all.addFooterView(footerView);

        list_job_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //listviewItem点击事件

                Intent intent = new Intent(mContext, JobDetailActivity.class);
                startActivity(intent);
            }
        });
        list_job_all.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView absListView, int scrollState) {
                //当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //判断是否滚动到底部
                    if (absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                        //加载更多
                        footerView.setVisibility(View.VISIBLE);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                TextView tv = (TextView) footerView.findViewById(R.id.tv_alljob_footer_txt);
                                ProgressBar pb = (ProgressBar) footerView.findViewById(R.id.pb_alljob_footer_loading);
                                pb.setVisibility(View.GONE);

                                if (absListView.getCount() < 20) {

                                    for (int i = 0; i < 10; i++) {
                                        jobInfo = new JobInfo(false, i);
                                        jobInfos.add(jobInfo);
                                    }
                                    pb.setVisibility(View.VISIBLE);
                                    adapter.notifyDataSetChanged();
                                    cb_selectAll.setChecked(false);
                                } else {
                                    tv.setText("没有更多了");
                                }


                            }
                        }, 2000);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });


        Button btn_shenqing = (Button) findViewById(R.id.btn_alljob_shenqing);
        btn_shenqing.setOnClickListener(this);
        cb_selectAll = (CheckBox) findViewById(R.id.cb_findjob_selectall);


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


    private void initListData() {
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
