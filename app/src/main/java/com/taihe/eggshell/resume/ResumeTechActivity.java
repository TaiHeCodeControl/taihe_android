package com.taihe.eggshell.resume;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by wang on 2015/8/14.
 */
public class ResumeTechActivity extends BaseActivity{

    private static final String TAG = "ResumeTrainActivity";

    private Context mContext;

    private TextView commitText,resetText;
    private EditText techEdit,techtypeEdit,levelEdit,techYear,workTimeEnd;
    private String techName,years,techType,techLevel;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_tech);
        super.initView();

        mContext = this;

        commitText = (TextView)findViewById(R.id.id_commit);
        resetText = (TextView)findViewById(R.id.id_reset);
        techEdit = (EditText)findViewById(R.id.id_tech_name);
        techtypeEdit = (EditText)findViewById(R.id.id_tech_type);
        levelEdit = (EditText)findViewById(R.id.id_tech_level);
        techYear = (EditText)findViewById(R.id.id_year);

        techtypeEdit.setOnClickListener(this);
        levelEdit.setOnClickListener(this);
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
            case R.id.id_tech_type:

                break;
            case R.id.id_tech_level:

                break;
            case R.id.id_commit:
                techName = techEdit.getText().toString();
                years = techYear.getText().toString();
                techType = techtypeEdit.getText().toString();
                techLevel = levelEdit.getText().toString();

                break;
            case R.id.id_reset:
                techEdit.setHint("请填写单位名称");
                techYear.setHint("2015-01-01");
                workTimeEnd.setHint("2015-01-01");
                techtypeEdit.setHint("请填写担任职位");
                levelEdit.setHint("请填写工作内容");

                break;
        }
    }
}
