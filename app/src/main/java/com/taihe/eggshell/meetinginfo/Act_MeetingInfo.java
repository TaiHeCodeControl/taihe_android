package com.taihe.eggshell.meetinginfo;

import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by Administrator on 2015/8/12.
 */
public class Act_MeetingInfo extends BaseActivity {
    @Override
    public void initView() {
        setContentView(R.layout.activity_meetinginfo_list);
        super.initView();
        super.initData();
    }
    public void initData(){
        initTitle("信息台");
    }
}
