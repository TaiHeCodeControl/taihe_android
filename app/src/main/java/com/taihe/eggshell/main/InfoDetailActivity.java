package com.taihe.eggshell.main;

import android.content.Context;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by wang on 2015/8/12.
 */
public class InfoDetailActivity extends BaseActivity{

    private static final String TAG = "InforDetailActivity";
    private Context mContext;

    private TextView mainPlat,startTime,address,telPhone,callPerson,comeWay,jobBrief;

    @Override
    public void initView() {
        setContentView(R.layout.activity_info_detail);
        super.initView();

        mainPlat = (TextView)findViewById(R.id.id_info_company);
        startTime = (TextView)findViewById(R.id.id_info_time);
        address = (TextView)findViewById(R.id.id_info_addre);
        telPhone = (TextView)findViewById(R.id.id_info_phone);
        callPerson = (TextView)findViewById(R.id.id_person);
        comeWay = (TextView)findViewById(R.id.id_info_way);

        jobBrief = (TextView)findViewById(R.id.id_company_brief);
    }

    @Override
    public void initData() {
        super.initData();

        initTitle("详情");


    }
}
