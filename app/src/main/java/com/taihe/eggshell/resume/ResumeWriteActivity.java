package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.activity.IndustryActivity;
import com.taihe.eggshell.widget.datepicker.TimeDialog;

/**
 * Created by wang on 2015/8/13.
 */
public class ResumeWriteActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

    private static final String TAG = "ResumWriteActivity";
    private Context mContext;

    private Intent intent;
    private TextView forIndusty,forPosition,forMoney,forCity,forWorkType,forTime,forStatus,forBirthday,forTopSchool,forWorkExper,commitTextView;
    private EditText resumeName,userName,phoneNum,email,address;
    private RadioGroup radioGroup;
    private RadioButton girlRadio,boyRadio;
    private TimeDialog timeDialog;
    private boolean isBirthday = false;

    private static final int RESULT_INDUSTRY = 10;
    private static final int RESULT_POSITION = 11;
    private static final int RESULT_MONEY = 12;
    private static final int RESULT_WORK = 13;
    private static final int RESULT_TIME = 14;
    private static final int RESULT_STATUS = 15;
    private static final int RESULT_BIRTHDAY = 16;
    private static final int RESULT_SCHOOL = 17;
    private static final int RESULT_EXPERICE = 18;

    private TimeDialog.CustomTimeListener customTimeListener = new TimeDialog.CustomTimeListener() {
        @Override
        public void setTime(String time) {
            if(isBirthday){
                forBirthday.setText(time);
            }else{
                forTime.setText(time);
            }

            timeDialog.dismiss();
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_write);
        super.initView();
        mContext = this;
        forIndusty = (TextView)findViewById(R.id.id_industry_for);
        forPosition = (TextView)findViewById(R.id.id_position_for);
        forMoney = (TextView)findViewById(R.id.id_money_for);
        forCity = (TextView)findViewById(R.id.id_city_for);
        forWorkType = (TextView)findViewById(R.id.id_type_for);
        forTime = (TextView)findViewById(R.id.id_time_for);
        forStatus = (TextView)findViewById(R.id.id_status_for);
        forBirthday = (TextView)findViewById(R.id.id_birthday);
        forTopSchool = (TextView)findViewById(R.id.id_top_school);
        forWorkExper = (TextView)findViewById(R.id.id_work_exper);
        commitTextView = (TextView)findViewById(R.id.id_commit);

        resumeName = (EditText)findViewById(R.id.id_resume_name);
        userName = (EditText)findViewById(R.id.id_name);
        phoneNum = (EditText)findViewById(R.id.id_phone_num);
        email = (EditText)findViewById(R.id.id_email);
        address = (EditText)findViewById(R.id.id_addres_now);

        radioGroup = (RadioGroup)findViewById(R.id.id_gender_radio);
        boyRadio = (RadioButton)findViewById(R.id.id_gender_boy);
        girlRadio = (RadioButton)findViewById(R.id.id_gender_girl);

        radioGroup.setOnCheckedChangeListener(this);
        forIndusty.setOnClickListener(this);
        forPosition.setOnClickListener(this);
        forMoney.setOnClickListener(this);
        forCity.setOnClickListener(this);
        forWorkType.setOnClickListener(this);
        forTime.setOnClickListener(this);
        forStatus.setOnClickListener(this);
        forBirthday.setOnClickListener(this);
        forTopSchool.setOnClickListener(this);
        forWorkExper.setOnClickListener(this);
        commitTextView.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();

        initTitle("写简历");
        timeDialog = new TimeDialog(mContext,this,customTimeListener);
        if(true){
            //填充数据
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_industry_for:
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "industry");
                startActivityForResult(intent, RESULT_INDUSTRY);
                break;
            case R.id.id_position_for:
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "position");
                startActivityForResult(intent, RESULT_POSITION);
                break;
            case R.id.id_money_for:
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "salary");
                startActivityForResult(intent, RESULT_MONEY);
                break;
            case R.id.id_city_for:
                break;
            case R.id.id_type_for:
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "jobtype");
                startActivityForResult(intent, RESULT_WORK);
                break;
            case R.id.id_time_for:
                isBirthday = false;
                timeDialog.show();
                break;
            case R.id.id_status_for:
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "status");
                startActivityForResult(intent, RESULT_STATUS);
                break;
            case R.id.id_birthday:
                isBirthday = true;
                timeDialog.show();
                break;
            case R.id.id_top_school:
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "edu");
                startActivityForResult(intent, RESULT_SCHOOL);
                break;
            case R.id.id_work_exper:
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "jobyears");
                startActivityForResult(intent, RESULT_EXPERICE);
                break;
            case R.id.id_commit:
                String industy = forIndusty.getText().toString();
                String positon = forPosition.getText().toString();
                String money = forMoney.getText().toString();
                String city = forCity.getText().toString();
                String type = forWorkType.getText().toString();
                String time = forTime.getText().toString();
                String status = forStatus.getText().toString();
                String birthday = forBirthday.getText().toString();
                String school = forTopSchool.getText().toString();
                String workexper = forWorkExper.getText().toString();

                if(TextUtils.isEmpty(industy)||TextUtils.isEmpty(positon)||TextUtils.isEmpty(money)||
                        TextUtils.isEmpty(city)||TextUtils.isEmpty(type)||TextUtils.isEmpty(time)||
                        TextUtils.isEmpty(status)||TextUtils.isEmpty(birthday)||
                        TextUtils.isEmpty(school)||TextUtils.isEmpty(workexper)){
                    ToastUtils.show(mContext,"还有空着的");
                    intent = new Intent(mContext,ResumeMultiActivity.class);
                    startActivity(intent);
                    return;
                }else{
                    intent = new Intent(mContext,ResumeMultiActivity.class);
                    startActivity(intent);
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            String result = data.getStringExtra("data");
            if(TextUtils.isEmpty(result)){
                return;
            }
            switch (requestCode){
                case RESULT_INDUSTRY:
                    forIndusty.setText(result);
                    break;
                case RESULT_POSITION:
                    forPosition.setText(result);
                    break;
                case RESULT_MONEY:
                    forMoney.setText(result);
                    break;
                case RESULT_WORK:
                    forWorkType.setText(result);
                    break;
                case RESULT_STATUS:
                    forStatus.setText(result);
                    break;
                case RESULT_SCHOOL:
                    forTopSchool.setText(result);
                    break;
                case RESULT_EXPERICE:
                    forWorkExper.setText(result);
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.id_gender_boy:
                        girlRadio.setChecked(false);
                        break;
                    case R.id.id_gender_girl:
                        boyRadio.setChecked(false);
                        break;
                }
    }
}
