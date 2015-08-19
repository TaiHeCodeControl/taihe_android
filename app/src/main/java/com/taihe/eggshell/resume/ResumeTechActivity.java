package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.job.activity.IndustryActivity;

/**
 * Created by wang on 2015/8/14.
 */
public class ResumeTechActivity extends BaseActivity{

    private static final String TAG = "ResumeTrainActivity";

    private Context mContext;

    private Intent intent;
    private TextView commitText,resetText,techtypeEdit,levelEdit;
    private EditText techEdit,techYear,workTimeEnd;
    private String techName,years,techType,techLevel;

    private static final int RESULT_INDUSTRY = 20;
    private static final int RESULT_LEVEL = 21;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_tech);
        super.initView();

        mContext = this;

        commitText = (TextView)findViewById(R.id.id_commit);
        resetText = (TextView)findViewById(R.id.id_reset);
        techEdit = (EditText)findViewById(R.id.id_tech_name);
        techtypeEdit = (TextView)findViewById(R.id.id_tech_type);
        levelEdit = (TextView)findViewById(R.id.id_tech_level);
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
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "industry");
                startActivityForResult(intent, RESULT_INDUSTRY);
                break;
            case R.id.id_tech_level:
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "techlevel");
                startActivityForResult(intent, RESULT_LEVEL);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            String result = data.getStringExtra("data");
            if(TextUtils.isEmpty(result)){
                return;
            }
            switch (requestCode){
                case RESULT_INDUSTRY:
                    techtypeEdit.setText(result);
                    break;
                case RESULT_LEVEL:
                    levelEdit.setText(result);
                    break;
            }
        }
    }
}
