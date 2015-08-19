package com.taihe.eggshell.resume;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.widget.datepicker.TimeDialog;

/**
 * Created by wang on 2015/8/14.
 */
public class ResumeBookActivity extends BaseActivity{

    private static final String TAG = "ResumeBookActivity";

    private Context mContext;

    private TextView commitText,resetText,timeEdit;
    private EditText bookEdit,companyEdit,positonEdit,contextEdit;
    private TimeDialog timeDialog;

    private String techName,years,techType,techLevel;

    private TimeDialog.CustomTimeListener customTimeListener = new TimeDialog.CustomTimeListener() {
        @Override
        public void setTime(String time) {
            timeEdit.setText(time);
            timeDialog.dismiss();
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_book);
        super.initView();

        mContext = this;

        commitText = (TextView)findViewById(R.id.id_commit);
        resetText = (TextView)findViewById(R.id.id_reset);
        bookEdit = (EditText)findViewById(R.id.id_tech_name);
        timeEdit = (TextView)findViewById(R.id.id_tech_type);
        companyEdit = (EditText)findViewById(R.id.id_tech_level);
        contextEdit = (EditText)findViewById(R.id.id_context);

        timeEdit.setOnClickListener(this);
        commitText.setOnClickListener(this);
        resetText.setOnClickListener(this);
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
            case R.id.id_tech_type:
                timeDialog.show();
                break;
            case R.id.id_commit:
                techName = bookEdit.getText().toString();
                years = contextEdit.getText().toString();
                techType = timeEdit.getText().toString();
                techLevel = companyEdit.getText().toString();

                break;
            case R.id.id_reset:
                bookEdit.setHint("请填写单位名称");
                contextEdit.setHint("2015-01-01");
                timeEdit.setHint("请填写担任职位");
                companyEdit.setHint("请填写工作内容");

                break;
        }
    }
}