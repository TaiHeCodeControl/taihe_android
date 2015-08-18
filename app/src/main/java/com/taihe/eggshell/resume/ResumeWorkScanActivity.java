package com.taihe.eggshell.resume;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by wang on 2015/8/15.
 */
public class ResumeWorkScanActivity extends BaseActivity{

    private static final String TAG = "ResumeWorkScanActivity";

    private Context mContext;

    private TextView commitText;
    private TextView companyTextView,departTextView,positionTextView,contextTextView,workTimeStart;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_work_scan);
        super.initView();

        mContext = this;

        commitText = (TextView)findViewById(R.id.id_go_on);
        companyTextView = (TextView)findViewById(R.id.id_company_name);
        departTextView = (TextView)findViewById(R.id.id_department);
        positionTextView = (TextView)findViewById(R.id.id_position);
        contextTextView = (TextView)findViewById(R.id.id_context);
        workTimeStart = (TextView)findViewById(R.id.id_start_time);

        commitText.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("写简历");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_go_on:

                break;
        }
    }
}
