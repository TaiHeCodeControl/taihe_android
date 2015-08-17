package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.main.MainActivity;

/**
 * Created by wang on 2015/8/13.
 */
public class ResumeManagerActivity extends BaseActivity{

    private static final String TAG = "ResumeManagerActivity";
    private Context mContext;
    private Intent intent;

    private TextView createResume,scanResume,edtResume,useResume,delResume;
    private CheckBox checkBox;
    private LinearLayout lin_back;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_manager);
        super.initView();

        mContext = this;

        lin_back = (LinearLayout) findViewById(R.id.lin_back);
        lin_back.setOnClickListener(this);

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

        initTitle("简历管理");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            /*case R.id.lin_back:
                goBack();

                break;*/
            case R.id.id_create_resume:
                intent = new Intent(mContext,ResumeWriteActivity.class);
                startActivity(intent);
                break;
            case R.id.id_scan_resume:
                intent = new Intent(mContext,ResumeScanActivity.class);
                startActivity(intent);
                break;
            case R.id.id_edt:
                intent = new Intent(mContext,ResumeWriteActivity.class);
                startActivity(intent);
                break;
            case R.id.id_use:
                break;
            case R.id.id_del:
                break;
        }
    }

    private void goBack() {
        Intent intent = new Intent(ResumeManagerActivity.this, MainActivity.class);
        intent.putExtra("MeFragment", "MeFragment");
        startActivity(intent);
        this.finish();
    }

//    监听返回按钮
    @Override
    public void onBackPressed() {
        goBack();
    }
}
