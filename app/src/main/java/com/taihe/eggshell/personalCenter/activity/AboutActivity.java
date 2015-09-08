package com.taihe.eggshell.personalCenter.activity;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by Thinkpad on 2015/7/14.
 */
public class AboutActivity extends BaseActivity{

    private static final String TAG = "AboutActivity";
    private TextView txt_about_tell;
    @Override
    public void initView() {
        setContentView(R.layout.activity_about);
        txt_about_tell = (TextView)findViewById(R.id.txt_about_tell);
        super.initView();
        txt_about_tell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4000084111" ));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        super.initTitle("关于蛋壳儿");
    }
}
