package com.taihe.eggshell;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.personalCenter.UserInfoActivity;

public class MainActivity extends BaseActivity {

    private Context mContext;
    private Button toPersonalCenter;

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        mContext = this;

        toPersonalCenter = (Button)findViewById(R.id.id_personal_center);
    }

    @Override
    public void initData() {
        toPersonalCenter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_personal_center:
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
