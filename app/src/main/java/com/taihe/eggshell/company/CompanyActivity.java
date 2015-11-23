package com.taihe.eggshell.company;

import android.content.Context;
import android.content.Intent;
import android.view.View;
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

    @Override
    public void initView() {
        setContentView(R.layout.activity_company_main);
        super.initView();

        mContext = this;
        toPerson = (TextView)findViewById(R.id.id_to_person);

        toPerson.setOnClickListener(this);
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
        }
    }
}
