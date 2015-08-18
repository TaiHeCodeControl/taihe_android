package com.taihe.eggshell.resume;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by wang on 2015/8/14.
 */
public class ResumeProjectActivity extends BaseActivity{

    private static final String TAG = "ResumeTrainActivity";

    private Context mContext;

    private TextView commitText,resetText;
    private EditText projectEdit,timeEdit,invaraEdit,positonEdit,contextEdit;
    private String techName,years,techType,techLevel;

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

        commitText.setOnClickListener(this);
        resetText.setOnClickListener(this);
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
