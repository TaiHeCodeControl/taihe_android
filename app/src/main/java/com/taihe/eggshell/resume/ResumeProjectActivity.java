package com.taihe.eggshell.resume;

import android.content.Context;
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
public class ResumeProjectActivity extends BaseActivity{

    private static final String TAG = "ResumeTrainActivity";

    private Context mContext;

    private TextView commitText,resetText,schoolTimeStart,schoolTimeEnd;
    private EditText projectEdit,timeEdit,invaraEdit,positonEdit,contextEdit;
    private CheckBox checkBox;
    private TimeDialog timeDialog;

    private String techName,years,techType,techLevel;
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
        setContentView(R.layout.activity_resume_project);
        super.initView();

        mContext = this;

        commitText = (TextView)findViewById(R.id.id_commit);
        resetText = (TextView)findViewById(R.id.id_reset);
        projectEdit = (EditText)findViewById(R.id.id_tech_name);
        timeEdit = (EditText)findViewById(R.id.id_tech_type);
        invaraEdit = (EditText)findViewById(R.id.id_tech_level);
        positonEdit = (EditText)findViewById(R.id.id_year);
        contextEdit = (EditText)findViewById(R.id.id_context);
        schoolTimeStart = (TextView)findViewById(R.id.id_start_time);
        schoolTimeEnd = (TextView)findViewById(R.id.id_end_time);
        checkBox = (CheckBox)findViewById(R.id.id_gender);

        schoolTimeStart.setOnClickListener(this);
        schoolTimeEnd.setOnClickListener(this);
        commitText.setOnClickListener(this);
        resetText.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                techName = projectEdit.getText().toString();
                years = contextEdit.getText().toString();
                techType = timeEdit.getText().toString();
                techLevel = invaraEdit.getText().toString();

                break;
            case R.id.id_reset:
                projectEdit.setHint("请填写单位名称");
                contextEdit.setHint("2015-01-01");
                timeEdit.setHint("请填写担任职位");
                invaraEdit.setHint("请填写工作内容");

                break;
        }
    }
}
