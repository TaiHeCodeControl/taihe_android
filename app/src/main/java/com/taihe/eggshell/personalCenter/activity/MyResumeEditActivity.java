package com.taihe.eggshell.personalCenter.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.widget.datepicker.JudgeDate;
import com.taihe.eggshell.widget.datepicker.ScreenInfo;
import com.taihe.eggshell.widget.datepicker.WheelMain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.TextView;

/**
 * Created by huan on 2015/7/24.
 */
public class MyResumeEditActivity extends BaseActivity{

    private TextView tv_birthdate;

    private String verTime;

    WheelMain wheelMain;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    @Override
    public void initView() {
        setContentView(R.layout.activity_myresume_edit);

        tv_birthdate = (TextView) findViewById(R.id.tv_resume_birthdate);
        super.initView();
        Calendar calendar = Calendar.getInstance();
        String CurrentTime = calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + "";
        tv_birthdate.setText(CurrentTime);
        verTime = CurrentTime;

        tv_birthdate.setOnClickListener(this);
    }


   private AlertDialog  timeDialog;

    /**
     * 选择日期
     */
    private void selectDate() {
        LayoutInflater inflater = LayoutInflater.from(MyResumeEditActivity.this);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(MyResumeEditActivity.this);
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

        timeDialog =  new AlertDialog.Builder(MyResumeEditActivity.this)
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

    @Override
    public void initData() {
        super.initData();
        initTitle("编辑个人简历");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_resume_birthdate:
                // 选择日期
                selectDate();
                break;
        }
    }
}
