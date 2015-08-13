package com.taihe.eggshell.resume;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by wang on 2015/8/13.
 */
public class ResumWriteActivity extends BaseActivity{

    private static final String TAG = "ResumWriteActivity";
    private Context mContext;

    private TextView createResume,scanResume,edtResume,useResume,delResume;
    private CheckBox checkBox;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_write);
        super.initView();
        mContext = this;

    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){

        }
    }
}
