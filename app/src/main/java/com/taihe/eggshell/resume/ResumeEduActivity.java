package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.widget.datepicker.TimeDialog;

/**
 * Created by wang on 2015/8/14.
 */
public class ResumeEduActivity extends BaseActivity{
    private static final String TAG = "ResumeEduActivity";

    private Context mContext;

    private TextView commitText,resetText,schoolTimeStart,schoolTimeEnd;
    private EditText schoolEdit,industyEdit,positionEdit,contextEdit;
    private CheckBox radioButton;
    private TimeDialog timeDialog;

    private String schoolName,startTime,endTime,industyName,positionName,contextWord;
    private boolean isStart = false;

    private TimeDialog.CustomTimeListener customTimeListener = new TimeDialog.CustomTimeListener() {
        @Override
        public void setTime(String time) {
            if(isStart){
                schoolTimeStart.setText(time);
            }else{
                schoolTimeEnd.setText(time);
            }

            timeDialog.dismiss();
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_edu);
        super.initView();

        mContext = this;

        commitText = (TextView)findViewById(R.id.id_commit);
        resetText = (TextView)findViewById(R.id.id_reset);
        schoolEdit = (EditText)findViewById(R.id.id_company_name);
        industyEdit = (EditText)findViewById(R.id.id_department);
        positionEdit = (EditText)findViewById(R.id.id_position);
        contextEdit = (EditText)findViewById(R.id.id_context);
        schoolTimeStart = (TextView)findViewById(R.id.id_start_time);
        schoolTimeEnd = (TextView)findViewById(R.id.id_end_time);
        radioButton = (CheckBox)findViewById(R.id.id_gender);

        schoolTimeStart.setOnClickListener(this);
        schoolTimeEnd.setOnClickListener(this);
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

        timeDialog = new TimeDialog(mContext,this,customTimeListener);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_start_time:
                isStart = true;
                timeDialog.show();
                break;
            case R.id.id_end_time:
                isStart = false;
                timeDialog.show();
                break;
            case R.id.id_commit:
                schoolName = schoolEdit.getText().toString();
                startTime = schoolTimeStart.getText().toString();
                endTime = schoolTimeEnd.getText().toString();
                industyName = industyEdit.getText().toString();
                positionName = positionEdit.getText().toString();
                contextWord = contextEdit.getText().toString();
                startActivity(new Intent(mContext,ResumeEduScanActivity.class));
                break;
            case R.id.id_reset:
                schoolEdit.setHint("请填写学校名称");
                schoolTimeStart.setHint("2015-01-01");
                schoolTimeEnd.setHint("2015-01-01");
                industyEdit.setHint("请填写所学专业");
                positionEdit.setHint("请填写社团职位");
                contextEdit.setHint("请填写专业描述");

                break;
        }
    }
}
