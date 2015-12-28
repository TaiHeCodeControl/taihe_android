package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.resume.entity.Resumes;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by wang on 2015/8/13.
 */
public class ResumeListActivity extends BaseActivity{

    private static final String TAG = "ResumeMultiActivity";

    private Context mContext;
    private TextView resumeName,id_resume_list_add;
    private ListView id_resume_list;
    private Intent intent;
    private String strTypeTitle;
    private Resumes resume;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_list);
        super.initView();
        mContext = this;

        resumeName = (TextView)findViewById(R.id.id_resume_list_title);
        id_resume_list = (ListView)findViewById(R.id.id_resume_list);
        id_resume_list_add = (TextView)findViewById(R.id.id_resume_list_add);

    }

    @Override
    public void initData() {
        super.initData();
        initTitle("写简历");
        resume = getIntent().getParcelableExtra("resume");
        strTypeTitle = getIntent().getStringExtra("title");
        resumeName.setText(resume.getName()+"-"+strTypeTitle);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}
