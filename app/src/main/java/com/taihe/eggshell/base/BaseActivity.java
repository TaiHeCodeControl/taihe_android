package com.taihe.eggshell.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.ToastUtils;

/**
 * Created by Thinkpad on 2015/7/14.
 */
public class BaseActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "BaseActivity";

    public Context mContext;
    private ImageView backImage;
    private TextView titleTextView;
    private LinearLayout lin_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        initView();
        initData();
    }

    public void initView(){
        backImage = (ImageView)findViewById(R.id.id_back);
        titleTextView = (TextView)findViewById(R.id.id_title);
        lin_back = (LinearLayout)findViewById(R.id.lin_back);
    }

    public void initData(){
//        backImage.setOnClickListener(this);
        lin_back.setOnClickListener(this);
    }

    public void initTitle(String titleName){
        titleTextView.setText(titleName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_back:
                finish();
                break;
        }
    }
}
