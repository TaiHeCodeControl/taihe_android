package com.taihe.eggshell.resume;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.utils.ToastUtils;

/**
 * Created by wang on 2015/8/13.
 */
public class ResumeMultiActivity extends BaseActivity{

    private static final String TAG = "ResumeMultiActivity";

    private Context mContext;
    private TextView resumeName;
    private RelativeLayout workExper,educationExper,trainExper,industryTech,projectExper,certBook,selfEvaluation;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_mutil);
        super.initView();
        mContext = this;
        workExper = (RelativeLayout)findViewById(R.id.id_work_exper);
        educationExper = (RelativeLayout)findViewById(R.id.id_edu_exper);
        trainExper = (RelativeLayout)findViewById(R.id.id_train_exper);
        industryTech = (RelativeLayout)findViewById(R.id.id_industy_tech);
        projectExper = (RelativeLayout)findViewById(R.id.id_project_exper);
        certBook = (RelativeLayout)findViewById(R.id.id_conver_book);
        selfEvaluation = (RelativeLayout)findViewById(R.id.id_self_evalu);

        workExper.setOnClickListener(this);
        educationExper.setOnClickListener(this);
        trainExper.setOnClickListener(this);
        industryTech.setOnClickListener(this);
        projectExper.setOnClickListener(this);
        certBook.setOnClickListener(this);
        selfEvaluation.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_work_exper:
                ToastUtils.show(mContext,"你");
                break;
            case R.id.id_edu_exper:
                ToastUtils.show(mContext,"你");
                break;
            case R.id.id_train_exper:
                ToastUtils.show(mContext,"你");
                break;
            case R.id.id_industy_tech:
                ToastUtils.show(mContext,"你");
                break;
            case R.id.id_project_exper:
                ToastUtils.show(mContext,"你");
                break;
            case R.id.id_conver_book:
                ToastUtils.show(mContext,"你");
                break;
            case R.id.id_self_evalu:
                ToastUtils.show(mContext,"你");
                break;
        }
    }
}
