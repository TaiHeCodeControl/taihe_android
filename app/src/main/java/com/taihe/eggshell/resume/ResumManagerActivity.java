package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by wang on 2015/8/13.
 */
public class ResumManagerActivity extends BaseActivity{

    private static final String TAG = "ResumeManagerActivity";
    private Context mContext;

    private TextView createResume,scanResume,edtResume,useResume,delResume;
    private CheckBox checkBox;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_manager);
        super.initView();

        mContext = this;
        createResume = (TextView)findViewById(R.id.id_create_resume);
        scanResume = (TextView)findViewById(R.id.id_scan_resume);
        edtResume = (TextView)findViewById(R.id.id_edt);
        useResume = (TextView)findViewById(R.id.id_use);
        delResume = (TextView)findViewById(R.id.id_del);
        checkBox = (CheckBox)findViewById(R.id.id_check_box);

        createResume.setOnClickListener(this);
        scanResume.setOnClickListener(this);
        edtResume.setOnClickListener(this);
        useResume.setOnClickListener(this);
        delResume.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_create_resume:
                Intent intent = new Intent(mContext,ResumWriteActivity.class);
                startActivity(intent);
                break;
            case R.id.id_scan_resume:
                break;
            case R.id.id_edt:
                break;
            case R.id.id_use:
                break;
            case R.id.id_del:
                break;
        }
    }
}
