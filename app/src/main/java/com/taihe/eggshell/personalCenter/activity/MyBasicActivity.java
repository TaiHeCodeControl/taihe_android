package com.taihe.eggshell.personalCenter.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.main.MainActivity;
import com.taihe.eggshell.widget.addressselect.AddressSelectActivity;
import com.taihe.eggshell.widget.datepicker.JudgeDate;
import com.taihe.eggshell.widget.datepicker.ScreenInfo;
import com.taihe.eggshell.widget.datepicker.WheelMain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 基本资料界面，个人基本信息的查看和编辑
 * Created by huan on 2015/8/11.
 */
public class MyBasicActivity extends Activity implements View.OnClickListener {


    private Context mContext;
    private TextView tv_birthdate, tv_mybasic_sex, tv_address;

    private String verTime;

    private Intent intent;
    private ImageView iv_back, iv_birthdate, iv_sexSelect, iv_citySelect;

    WheelMain wheelMain;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mybasic_edit);
        mContext = this;
        initView();
    }

    public void initView() {


        iv_back = (ImageView) findViewById(R.id.iv_mybasic_back);
        iv_birthdate = (ImageView) findViewById(R.id.iv_mybasic_date);
        iv_sexSelect = (ImageView) findViewById(R.id.iv_mybasic_sexselect);
        iv_citySelect = (ImageView) findViewById(R.id.iv_mybasic_cityselect);
        tv_birthdate = (TextView) findViewById(R.id.tv_mybasic_birthdate);
        tv_address = (TextView) findViewById(R.id.tv_mybasic_city);
        tv_mybasic_sex = (TextView) findViewById(R.id.tv_mybasic_sex);


        intent = getIntent();
        String address = intent.getStringExtra("Address");
        if (!TextUtils.isEmpty(address)) {

            tv_address.setText(address);
        }

        //初始化当前时间
        Calendar calendar = Calendar.getInstance();
        String CurrentTime = calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + "";
        //设置默认生日时间
        tv_birthdate.setText(CurrentTime);
        verTime = CurrentTime;

        iv_birthdate.setOnClickListener(this);
        iv_sexSelect.setOnClickListener(this);
        iv_citySelect.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_birthdate.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mybasic_back://退出当前页面

                goBack();

                break;

            case R.id.iv_mybasic_cityselect://选择城市

                intent = new Intent(MyBasicActivity.this, AddressSelectActivity.class);
                startActivity(intent);

                break;

            case R.id.iv_mybasic_sexselect://选择性别
                selectSex(b);
                break;
            case R.id.iv_mybasic_date://选择生日日期
                // 选择日期
                selectDate();
                break;
        }
    }

    private void goBack() {
        Intent intent = new Intent(MyBasicActivity.this, MainActivity.class);
        intent.putExtra("MeFragment", "MeFragment");
        startActivity(intent);

        MyBasicActivity.this.finish();
    }


    //-----------------------选择性别 start---------------------------------------

    private AlertDialog sexSelectDialog = null;
    private boolean b = false;

    public void selectSex(boolean a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        View view = View.inflate(mContext, R.layout.dialog_sexselect,
                null);

        RelativeLayout rl_cancel = (RelativeLayout) view.findViewById(R.id.rl_dialogsex_cancel);


        RelativeLayout rl_man = (RelativeLayout) view.findViewById(R.id.rl_dialogsex_man);
        RelativeLayout rl_woman = (RelativeLayout) view.findViewById(R.id.rl_dialogsex_woman);

        final ImageView iv_man = (ImageView) view.findViewById(R.id.iv_dialogsex_man);
        final ImageView iv_woman = (ImageView) view.findViewById(R.id.iv_dialogsex_woman);

        if (a) {
            iv_woman.setVisibility(View.VISIBLE);
            iv_man.setVisibility(View.INVISIBLE);
        } else {
            iv_man.setVisibility(View.VISIBLE);
            iv_woman.setVisibility(View.INVISIBLE);
        }


        rl_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sexSelectDialog.dismiss();
            }
        });

        //选择女
        rl_woman.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tv_mybasic_sex.setText("女");
                b = true;
                sexSelectDialog.dismiss();
            }
        });

        //选择男
        rl_man.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tv_mybasic_sex.setText("男");
                b = false;
                sexSelectDialog.dismiss();
            }
        });
        builder.setView(view);
        sexSelectDialog = builder.create();
        sexSelectDialog.show();
    }


    //-----------------------选择日期 start---------------------------------------
    //日期对话框
    private AlertDialog timeDialog;

    /**
     * 选择日期
     */
    private void selectDate() {

        LayoutInflater inflater = LayoutInflater.from(MyBasicActivity.this);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(MyBasicActivity.this);
        wheelMain = new WheelMain(timepickerview);
        wheelMain.screenheight = screenInfo.getHeight();
        String time = tv_birthdate.getText().toString();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
            try {
                calendar.setTime(dateFormat.parse(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year, month, day);

        Button btn_ok = (Button) timepickerview.findViewById(R.id.btn_timepicker_ok);
        Button btn_cancel = (Button) timepickerview.findViewById(R.id.btn_timepicker_cancel);

        timeDialog = new AlertDialog.Builder(MyBasicActivity.this)
                .setView(timepickerview).show();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDialog.dismiss();
                verTime = wheelMain.getTime();
                tv_birthdate.setText(verTime);

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDialog.dismiss();
            }
        });
    }

    //--------------------------------------------------------------

    //监听返回按钮
    @Override
    public void onBackPressed() {
        goBack();
    }
}
