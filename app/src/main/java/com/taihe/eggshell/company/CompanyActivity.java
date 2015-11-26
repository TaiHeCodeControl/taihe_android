package com.taihe.eggshell.company;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.main.MainActivity;

/**
 * Created by wang on 2015/11/23.
 */
public class CompanyActivity extends BaseActivity{

    private Context mContext;
    private TextView toPerson;
    private LinearLayout com_lin_topl,com_lin_topr,com_lin_footl,com_lin_footc,com_lin_footr;

    @Override
    public void initView() {
        setContentView(R.layout.activity_company_main);
        super.initView();

        mContext = this;
        toPerson = (TextView)findViewById(R.id.id_to_person);
        com_lin_topl = (LinearLayout)findViewById(R.id.com_lin_topl);
        com_lin_topr = (LinearLayout)findViewById(R.id.com_lin_topr);
        com_lin_footl = (LinearLayout)findViewById(R.id.com_lin_footl);
        com_lin_footc = (LinearLayout)findViewById(R.id.com_lin_footc);
        com_lin_footr = (LinearLayout)findViewById(R.id.com_lin_footr);

        toPerson.setOnClickListener(this);
        com_lin_topl.setOnClickListener(this);
        com_lin_topr.setOnClickListener(this);
        com_lin_footl.setOnClickListener(this);
        com_lin_footc.setOnClickListener(this);
        com_lin_footr.setOnClickListener(this);
    }

    @Override
    public void initData() {
//        super.initData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()){
            case R.id.id_to_person:
                intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.com_lin_topl:
                intent = new Intent(mContext, CompanyResumeGetActivity.class);
                intent.putExtra("selid",1);
                startActivity(intent);
                break;
            case R.id.com_lin_footl:
                intent = new Intent(mContext, CompanyJobListActivity.class);
                intent.putExtra("type",0);
                startActivity(intent);
                break;
            case R.id.com_lin_footc:
                intent = new Intent(mContext, CompanyJobListActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
                break;
            case R.id.com_lin_footr:
                intent = new Intent(mContext, CompanyJobListActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
                break;
        }
    }
}
