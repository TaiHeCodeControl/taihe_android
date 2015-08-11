package com.taihe.eggshell.job.activity;

import android.content.Context;
import android.widget.GridView;
import android.widget.ListView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.job.adapter.HotJobAdapter;
import com.taihe.eggshell.job.adapter.SearchHistoryAdapter;

import java.util.List;

/**
 * Created by huan on 2015/8/11.
 */
public class JobSearchActivity extends BaseActivity{

    private GridView gv_hotjob;
    private ListView lv_searchHistory;

    private Context mContext;
//    private List<String> hotjobLists = null;

    @Override
    public void initView() {
        setContentView(R.layout.activity_job_search);
        super.initView();

        mContext = this;

        gv_hotjob = (GridView) findViewById(R.id.gv_jobsearch_hotjob);
        gv_hotjob.setAdapter(new HotJobAdapter(mContext));

        lv_searchHistory = (ListView) findViewById(R.id.lv_jobsearch_searchhistory);
        lv_searchHistory.setAdapter(new SearchHistoryAdapter(mContext));

    }

    @Override
    public void initData() {
        super.initData();

        initTitle("职位搜索");
    }

}
