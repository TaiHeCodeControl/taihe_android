package com.taihe.eggshell.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 * Created by Thinkpad on 2015/7/14.
 */
public class BaseActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView();
        initData();
    }

    public void initView(){

    }

    public void initData(){

    }

    @Override
    public void onClick(View v) {

    }
}
