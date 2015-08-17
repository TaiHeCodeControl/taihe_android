package com.taihe.eggshell.meetinginfo;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

import net.tsz.afinal.FinalBitmap;

/**
 * Created by wang on 2015/8/12.
 */
public class InfoDetailActivity extends BaseActivity{

    private static final String TAG = "InforDetailActivity";
    private Context mContext;

    private TextView mainPlat,startTime,address,telPhone,callPerson,comeWay,jobBrief;
    private ImageView imgLog;
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
        imgLog = (ImageView)findViewById(R.id.id_info_logo);

    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        initTitle("详情");
        mainPlat.setText(intent.getStringExtra("title"));
        startTime.setText(intent.getStringExtra("starttime") + "至" + intent.getStringExtra("endtime"));
        address.setText(intent.getStringExtra("address"));
        telPhone.setText(intent.getStringExtra("telphone"));
        callPerson.setText(intent.getStringExtra("user"));
        comeWay.setText(intent.getStringExtra("traffic_route"));
        jobBrief.setText(intent.getStringExtra("content"));
        FinalBitmap bitmap = FinalBitmap.create(mContext);
        bitmap.display(imgLog,intent.getStringExtra("logo"));
    }
}
