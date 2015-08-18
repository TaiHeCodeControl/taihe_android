package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by wang on 2015/8/14.
 */
public class ResumeWorkActivity extends BaseActivity{

    private static final String TAG = "ResumeWorkActivity";

    private Context mContext;

    private TextView commitText,resetText;
    private EditText companyEdit,departEdit,positionEdit,contextEdit,workTimeStart,workTimeEnd;
    private CheckBox radioButton;
    private String companyName,startTime,endTime,departName,positionName,contextWord;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_work);
        super.initView();

        mContext = this;

        commitText = (TextView)findViewById(R.id.id_commit);
        resetText = (TextView)findViewById(R.id.id_reset);
        companyEdit = (EditText)findViewById(R.id.id_company_name);
        departEdit = (EditText)findViewById(R.id.id_department);
        positionEdit = (EditText)findViewById(R.id.id_position);
        contextEdit = (EditText)findViewById(R.id.id_context);
        workTimeStart = (EditText)findViewById(R.id.id_start_time);
        workTimeEnd = (EditText)findViewById(R.id.id_end_time);

        radioButton = (CheckBox)findViewById(R.id.id_gender);

        commitText.setOnClickListener(this);
        resetText.setOnClickListener(this);
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
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
            case R.id.id_commit:
                companyName = companyEdit.getText().toString();
                startTime = workTimeStart.getText().toString();
                endTime = workTimeEnd.getText().toString();
                departName = departEdit.getText().toString();
                positionName = positionEdit.getText().toString();
                contextWord = contextEdit.getText().toString();
                startActivity(new Intent(mContext,ResumeWorkScanActivity.class));
                break;
            case R.id.id_reset:
                companyEdit.setHint("请填写单位名称");
                workTimeStart.setHint("2015-01-01");
                workTimeEnd.setHint("2015-01-01");
                departEdit.setHint("请填写所在部门");
                positionEdit.setHint("请填写担任职位");
                contextEdit.setHint("请填写工作内容");

                break;
        }
    }
}
