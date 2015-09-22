package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.FormatUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.activity.IndustryActivity;
import com.taihe.eggshell.main.entity.CityBJ;
import com.taihe.eggshell.main.entity.StaticData;
import com.taihe.eggshell.resume.entity.Resumes;
import com.taihe.eggshell.widget.CityDialog;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.taihe.eggshell.widget.datepicker.TimeDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang on 2015/8/13.
 */
public class ResumeWriteActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

    private static final String TAG = "ResumWriteActivity";
    private Context mContext;

    private Intent intent;
    private TextView forIndusty,forPosition,forMoney,forCity,forProvince,forICity,forCounty,forWorkType,forTime,forStatus,forBirthday,forTopSchool,forWorkExper,commitTextView;
    private EditText resumeName,userName,phoneNum,email,address;
    private RadioGroup radioGroup;
    private RadioButton girlRadio,boyRadio;
    private TimeDialog timeDialog;
    private CityDialog cityDialog;
    private LoadingProgressDialog loading;
    private boolean isBirthday = false;
    private Map<String,String> params = new HashMap<String, String>();
    private StaticData result;
    private String city;
    private String sex = "6";
    private int id_industry,id_positon,id_money,id_type,id_status,id_school,id_workex,id_time;

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
        forProvince = (TextView)findViewById(R.id.id_province);
        forICity = (TextView)findViewById(R.id.id_city);
        forCounty = (TextView)findViewById(R.id.id_county);
        forWorkType = (TextView)findViewById(R.id.id_type_for);
        forTime = (TextView)findViewById(R.id.id_time_for);
        forStatus = (TextView)findViewById(R.id.id_status_for);
        forBirthday = (TextView)findViewById(R.id.id_birthday);
        forTopSchool = (TextView)findViewById(R.id.id_top_school);
        forWorkExper = (TextView)findViewById(R.id.id_work_exper);
        commitTextView = (TextView)findViewById(R.id.id_commit);

        resumeName = (EditText)findViewById(R.id.id_resume_name);
        userName = (EditText)findViewById(R.id.id_name);
        phoneNum = (EditText)findViewById(R.id.id_phone);
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
        forProvince.setOnClickListener(this);
        forICity.setOnClickListener(this);
        forCounty.setOnClickListener(this);
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
        loading = new LoadingProgressDialog(mContext,"正在提交...");

        //点击编辑简历执行该方法
        if(null!=getIntent().getStringExtra("eid")){
            getInfo(getIntent().getStringExtra("eid"));
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
                intent.putExtra("flag","pos");
                startActivityForResult(intent, RESULT_POSITION);
                break;
            case R.id.id_money_for:
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "salary");
                startActivityForResult(intent, RESULT_MONEY);
                break;
            case R.id.id_province:
                break;
            case R.id.id_city:
                break;
            case R.id.id_county:
                cityDialog = new CityDialog(mContext,new CityDialog.CityClickListener() {
                    @Override
                    public void city(CityBJ c) {
                        forCounty.setText(c.getName());
                        city = c.getId()+"";
                        cityDialog.dismiss();
                    }
                });
                cityDialog.show();
                break;
            case R.id.id_type_for:
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "jobtype");
                startActivityForResult(intent, RESULT_WORK);
                break;
            case R.id.id_time_for:
                intent = new Intent(mContext, IndustryActivity.class);
                intent.putExtra("Filter", "dgtime");
                startActivityForResult(intent, RESULT_TIME);
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
                String resumname = resumeName.getText().toString();
                String uname = userName.getText().toString();
                String phonenu = phoneNum.getText().toString();
                String emails = email.getText().toString();
                String addr = address.getText().toString();
                String industy = forIndusty.getText().toString();
                String positon = forPosition.getText().toString();
                String money = forMoney.getText().toString();
                String cityname = forCounty.getText().toString();
                String type = forWorkType.getText().toString();
                String time = forTime.getText().toString();
                String status = forStatus.getText().toString();
                String birthday = forBirthday.getText().toString();
                String school = forTopSchool.getText().toString();
                String workexper = forWorkExper.getText().toString();

                if(TextUtils.isEmpty(resumname)||TextUtils.isEmpty(uname)||TextUtils.isEmpty(phonenu)||
                        TextUtils.isEmpty(emails)||TextUtils.isEmpty(addr)||TextUtils.isEmpty(industy)||
                        TextUtils.isEmpty(positon)||TextUtils.isEmpty(money)||
                        TextUtils.isEmpty(cityname)||TextUtils.isEmpty(type)||TextUtils.isEmpty(time)||
                        TextUtils.isEmpty(status)||TextUtils.isEmpty(birthday)||
                        TextUtils.isEmpty(school)||TextUtils.isEmpty(workexper)){
                    ToastUtils.show(mContext,"每项必填");
                    return;
                }else if(!FormatUtils.isMobileNO(phonenu)){
                    ToastUtils.show(mContext,"请填写正确的手机号");
                }else if(!FormatUtils.isEmail(emails)){
                    ToastUtils.show(mContext,"请填写正确的邮箱");
                }else{
                    params.put("uid", EggshellApplication.getApplication().getUser().getId()+"");
                    params.put("name", resumname);
                    params.put("hy", id_industry+"");
                    params.put("job_classid", id_positon+"");
                    params.put("salary", id_money+"");
                    params.put("provinceid", "2");
                    params.put("type", id_type+"");
                    params.put("report", id_time+"");
                    params.put("jobstatus", id_status+"");
                    params.put("uname", uname);
                    params.put("birthday", birthday);
                    params.put("edu", id_school+"");
                    params.put("exp", id_workex+"");
                    params.put("telphone", phonenu);
                    params.put("email", emails);
                    params.put("address", addr);
                    params.put("sex",sex);
                    params.put("three_cityid",city);
                    params.put("cityid","52");
                    loading.show();
                    submitInfoToServer();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            result = data.getParcelableExtra("data");
            if(null == result){
                return;
            }
            switch (requestCode){
                case RESULT_INDUSTRY:
                    forIndusty.setText(result.getName());
                    id_industry = result.getId();
                    break;
                case RESULT_POSITION:
                    forPosition.setText(result.getName());
                    id_positon = result.getId();
                    break;
                case RESULT_MONEY:
                    forMoney.setText(result.getName());
                    id_money = result.getId();
                    break;
                case RESULT_WORK:
                    forWorkType.setText(result.getName());
                    id_type = result.getId();
                    break;
                case RESULT_STATUS:
                    forStatus.setText(result.getName());
                    id_status = result.getId();
                    break;
                case RESULT_SCHOOL:
                    forTopSchool.setText(result.getName());
                    id_school = result.getId();
                    break;
                case RESULT_EXPERICE:
                    forWorkExper.setText(result.getName());
                    id_workex = result.getId();
                    break;
                case RESULT_TIME:
                    forTime.setText(result.getName());
                    id_time = result.getId();
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.id_gender_boy:
                        sex = "6";
                        girlRadio.setChecked(false);
                        break;
                    case R.id.id_gender_girl:
                        sex = "7";
                        boyRadio.setChecked(false);
                        break;
                }
    }

    private void getInfo(String id){
        //编辑后提交需要简历id
        params.put("eid",id);
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                Log.v(TAG,(String)o);
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        JSONObject data = jsonObject.getJSONObject("data");

                        JSONObject info = data.getJSONObject("info");
                        String name = info.getString("uname");//姓名
                        userName.setText(name);
                        String emails = info.getString("email");//邮箱
                        email.setText(emails);

                        if(!"null".equals(info.getString("edu"))){
                            JSONObject edu = info.getJSONObject("edu");//学历
                            forTopSchool.setText(edu.getString("name"));
                            id_school = edu.getInt("id");
                        }else{
                            forTopSchool.setText("");
                            id_school = 15;
                        }

                        JSONObject experince = info.getJSONObject("exp");//工作经验
                        forWorkExper.setText(experince.getString("name"));
                        id_workex = experince.getInt("id");
                        JSONObject sexs = info.getJSONObject("sex");
                        if("男".equals(sexs.getString("name"))){
                            boyRadio.setChecked(true);
                            girlRadio.setChecked(false);
                        }else{
                            boyRadio.setChecked(false);
                            girlRadio.setChecked(true);
                        }
                        sex = sexs.getString("id");
                        String addre = info.getString("address");
                        address.setText(addre);
                        String tel = info.getString("telphone");//电话
                        phoneNum.setText(tel);

                        JSONObject expect = data.getJSONObject("expect");
                        String rename = expect.getString("name");//简历名称
                        resumeName.setText(rename);
                        if("null".equals(expect.getString("three_cityid"))){
                            forCounty.setText("全城");
                            city = "0";
                        }else{
                            JSONObject addres = expect.getJSONObject("three_cityid");//地区
                            forCounty.setText(addres.getString("name"));
                            city = addres.getString("id");
                        }
                        JSONObject hy = expect.getJSONObject("hy");//期望行业
                        forIndusty.setText(hy.getString("name"));
                        id_industry = hy.getInt("id");
                        JSONObject hopeposiont = expect.getJSONObject("job");//职位
                        forPosition.setText(hopeposiont.getString("name"));
                        id_positon = hopeposiont.getInt("id");
                        JSONObject salary = expect.getJSONObject("salary");//薪资
                        forMoney.setText(salary.getString("name"));
                        id_money = salary.getInt("id");
                        JSONObject status = expect.getJSONObject("jobst");//求职状态
                        forStatus.setText(status.getString("name"));
                        id_status = status.getInt("id");
                        JSONObject worktype = expect.getJSONObject("ctype");//职位性质
                        forWorkType.setText(worktype.getString("name"));
                        id_type = worktype.getInt("id");
                        String bir = expect.getString("birthday");//职位性质
                        forBirthday.setText(bir);
                        JSONObject dgtime = expect.getJSONObject("dgtime");
                        forTime.setText(dgtime.getString("name"));
                        id_time = dgtime.getInt("id");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                Log.v(TAG,new String(volleyError.networkResponse.data));
                ToastUtils.show(mContext,volleyError);
            }
        };

        Map<String,String> map = new HashMap<String,String>();
        map.put("eid",id);

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_RESUME_SCAN, false, map, true, listener, errorListener);

    }

    private void submitInfoToServer(){
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                loading.dismiss();
//                Log.v(TAG, (String) o);
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        String resumeId = jsonObject.getString("data");
                        ToastUtils.show(mContext,"提交成功");
                        intent = new Intent(mContext,ResumeMultiActivity.class);
                        Resumes resume = new Resumes();
                        resume.setRid(Integer.valueOf(resumeId));
                        resume.setName(resumeName.getText().toString());
                        intent.putExtra("resume", resume);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loading.dismiss();
//                Log.v("FFF:",new String(volleyError.networkResponse.data));
                ToastUtils.show(mContext,volleyError);
            }
        };
//        Log.v("PAR:",params.toString());
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_CREATE_RESUME, false, params, true, listener, errorListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}
