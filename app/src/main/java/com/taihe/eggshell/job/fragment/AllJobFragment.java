package com.taihe.eggshell.job.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.widget.JobApplyDialogUtil;
import com.taihe.eggshell.job.activity.JobDetailActivity;
import com.taihe.eggshell.job.adapter.AllJobAdapter;
import com.taihe.eggshell.job.bean.JobInfo;

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

    private View footerView;
    private ListView list_job_all;
    private View rootView;
    private Context mContext;

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

        for (int i = 0; i < 10; i++) {
            jobInfo = new JobInfo(false, i);
            jobInfos.add(jobInfo);
        }
        list_job_all = (ListView) rootView.findViewById(R.id.list_job_all);
        //给listview增加底部view
        footerView = View.inflate(mContext, R.layout.list_job_all_footer, null);
        footerView.setVisibility(View.GONE);
        list_job_all.addFooterView(footerView);

        list_job_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //listviewItem点击事件

                if (position < jobInfos.size()) {
                    Intent intent = new Intent(mContext, JobDetailActivity.class);
                    startActivity(intent);
                }

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

                                if (absListView.getCount() < 30) {

                                    for (int i = 0; i < 10; i++) {
                                        jobInfo = new JobInfo(false, jobInfos.size() + i);
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
        switch (view.getId()) {
            case R.id.btn_alljob_shenqing:
                JobApplyDialogUtil.isApplyJob(mContext,selectSize,postednum);
                postJob();//申请职位
                break;
        }
    }

    public void postJob() {
        for (JobInfo jobInfo : jobInfos) {
//            System.out.println(jobInfo.getId() + "======" + jobInfo.isChecked());

        }
    }
}
