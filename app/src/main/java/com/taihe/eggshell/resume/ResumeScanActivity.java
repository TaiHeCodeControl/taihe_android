package com.taihe.eggshell.resume;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by wang on 2015/8/15.
 */
public class ResumeScanActivity extends BaseActivity{

    private static final String TAG = "ResumeScanActivity";
    private Context mContext;

    private TextView createTime,userName,gender,age,schoolLevel,experice,address,telphone,email;
    private TextView hopeposition,hopeindustry,hopemoney,hopeaddress,hopetime,staus,positiontype;
    private TextView worktime,workposition,workcompany,workcontent;
    private TextView edutime,eduindusty,eduschool,eduposition,edubrief;
    private TextView techname,techyears,techlevel;
    private TextView projecttime,projectpostion,projectname,projectbrief;
    private TextView bookname,booktime,bookcompany,bookbrief;
    private TextView traintime,traindirection,traincompnay,trainbrief;
    private TextView selfbrief;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_scan);
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();

        initTitle("预览简历");
    }

}
