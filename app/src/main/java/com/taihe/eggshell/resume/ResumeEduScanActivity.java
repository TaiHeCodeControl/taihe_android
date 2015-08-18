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
public class ResumeEduScanActivity extends BaseActivity{

    private static final String TAG = "ResumeEduScanActivity";

    private Context mContext;

    private TextView goonTextView;
    private TextView schoolTextView,industyTextView,positionTextView,contextTextView,schoolTimeStart;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_edu_scan);
        super.initView();

        mContext = this;

        goonTextView = (TextView)findViewById(R.id.id_go_on);
        schoolTextView = (TextView)findViewById(R.id.id_company_name);
        industyTextView = (TextView)findViewById(R.id.id_department);
        positionTextView = (TextView)findViewById(R.id.id_position);
        contextTextView = (TextView)findViewById(R.id.id_context);
        schoolTimeStart = (TextView)findViewById(R.id.id_start_time);

        goonTextView.setOnClickListener(this);
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
