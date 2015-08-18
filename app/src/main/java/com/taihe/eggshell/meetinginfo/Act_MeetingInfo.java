package com.taihe.eggshell.meetinginfo;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2015/8/12.
 */
public class Act_MeetingInfo extends BaseActivity implements View.OnClickListener {
    private LinearLayout lin_meetinginfo_top1,lin_meetinginfo_top2;
    private TextView txt_meetinginfo_top1,txt_meetinginfo_top2;
    private ImageView img_meetinginfo_top1,img_meetinginfo_top2;
    @Override
    public void initView() {
        setContentView(R.layout.activity_meetinginfo_list);
        super.initView();
        super.initData();

        lin_meetinginfo_top1 = (LinearLayout)findViewById(R.id.lin_meetinginfo_top1);
        lin_meetinginfo_top2 = (LinearLayout)findViewById(R.id.lin_meetinginfo_top2);
        txt_meetinginfo_top1 = (TextView)findViewById(R.id.txt_meetinginfo_top1);
        txt_meetinginfo_top2 = (TextView)findViewById(R.id.txt_meetinginfo_top2);
        img_meetinginfo_top1 = (ImageView)findViewById(R.id.img_meetinginfo_top1);
        img_meetinginfo_top2 = (ImageView)findViewById(R.id.img_meetinginfo_top2);

        lin_meetinginfo_top1.setOnClickListener(this);
        lin_meetinginfo_top2.setOnClickListener(this);
    }

    public void initData(){
        initTitle("信息台");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lin_meetinginfo_top1:
                img_meetinginfo_top1.setBackgroundResource(R.drawable.zhaopinhui);
                img_meetinginfo_top2.setBackgroundResource(R.drawable.shuangxuanhuick);
                txt_meetinginfo_top1.setTextColor(getResources().getColor(R.color.font_color_red));
                txt_meetinginfo_top2.setTextColor(getResources().getColor(R.color.font_color_black));
                break;
            case R.id.lin_meetinginfo_top2:
                img_meetinginfo_top1.setBackgroundResource(R.drawable.zhaopinhuick);
                img_meetinginfo_top2.setBackgroundResource(R.drawable.shuangxuanhui);
                txt_meetinginfo_top2.setTextColor(getResources().getColor(R.color.font_color_red));
                txt_meetinginfo_top1.setTextColor(getResources().getColor(R.color.font_color_black));
                break;
        }
    }
}
