package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang on 2015/8/15.
 */
public class ResumeScanActivity extends BaseActivity{

    private static final String TAG = "ResumeScanActivity";
    private Context mContext;

    private TextView createTime,userName,gender,age,schoolLevel,experice,address,telphone,email,resumename;
    private TextView hopeposition,hopeindustry,hopemoney,hopeaddress,hopetime,staus,positiontype;
    private TextView worktime,workposition,workcompany,workcontent;
    private TextView edutime,eduindusty,eduschool,eduposition,edubrief;
    private TextView techname,techyears,techlevel;
    private TextView projecttime,projectpostion,projectname,projectbrief;
    private TextView bookname,booktime,bookcompany,bookbrief;
    private TextView traintime,traindirection,traincompnay,trainbrief;
    private TextView selfbrief;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_scan);
        super.initView();

        mContext = this;
        //基本信息
        resumename = (TextView)findViewById(R.id.id_resume_name);
        createTime = (TextView)findViewById(R.id.id_create_time);
        userName = (TextView)findViewById(R.id.id_user_name);
        gender = (TextView)findViewById(R.id.id_gender);
        age = (TextView)findViewById(R.id.id_age);
        schoolLevel = (TextView)findViewById(R.id.id_level);
        experice = (TextView)findViewById(R.id.id_experince);
        address = (TextView)findViewById(R.id.id_address);
        telphone = (TextView)findViewById(R.id.id_telphone);
        email = (TextView)findViewById(R.id.id_email);
        //期望
        hopeposition = (TextView)findViewById(R.id.id_hope_position);
        hopeindustry = (TextView)findViewById(R.id.id_hope_industy);
        hopemoney = (TextView)findViewById(R.id.id_want_money);
        hopeaddress = (TextView)findViewById(R.id.id_work_ear);
        hopetime = (TextView)findViewById(R.id.id_come_time);
        staus = (TextView)findViewById(R.id.id_status);
        positiontype = (TextView)findViewById(R.id.id_position_type);
        //工作
        worktime = (TextView)findViewById(R.id.id_time_work);
        workposition = (TextView)findViewById(R.id.id_position);
        workcompany = (TextView)findViewById(R.id.id_company_name);
        workcontent = (TextView)findViewById(R.id.id_work_content);
        //教育
        edutime = (TextView)findViewById(R.id.id_time_edu);
        eduindusty = (TextView)findViewById(R.id.id_professional);
        eduschool = (TextView)findViewById(R.id.id_school_name);
        eduposition = (TextView)findViewById(R.id.id_school_posion);
        edubrief = (TextView)findViewById(R.id.id_prof_brief);
        //专业技能
        techname = (TextView)findViewById(R.id.id_tech_name);
        techyears = (TextView)findViewById(R.id.id_contron_time);
        techlevel = (TextView)findViewById(R.id.id_hot_level);
        //项目经验
        projecttime = (TextView)findViewById(R.id.id_time_project);
        projectpostion = (TextView)findViewById(R.id.id_own_posion);
        projectname = (TextView)findViewById(R.id.id_project_name);
        projectbrief = (TextView)findViewById(R.id.id_content);
        //证书
        booktime = (TextView)findViewById(R.id.id_time_book);
        bookname = (TextView)findViewById(R.id.id_book_name);
        bookcompany = (TextView)findViewById(R.id.id_book_from);
        bookbrief = (TextView)findViewById(R.id.id_book_brief);
        //培训
        traintime = (TextView)findViewById(R.id.id_time_train);
        traindirection = (TextView)findViewById(R.id.id_train_direction);
        traincompnay = (TextView)findViewById(R.id.id_train_company);
        trainbrief = (TextView)findViewById(R.id.id_train_brief);
        //自我评价
        selfbrief = (TextView)findViewById(R.id.id_self_desc);

    }

    @Override
    public void initData() {
        super.initData();

        initTitle("简历预览");
        String eid = getIntent().getStringExtra("eid");
        getResumeData(eid);
    }
    private void getResumeData(String id) {
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                Log.v(TAG,(String)o);
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject area = data.getJSONObject("area");
                        String addres = area.getString("name");//地区
                        hopeaddress.setText(addres);
                        JSONObject info = data.getJSONObject("info");
                        String name = info.getString("name");//姓名
                        userName.setText(name);
                        String emails = info.getString("email");//邮箱
                        email.setText(emails);
                        String edu = info.getString("edu");//学历
                        schoolLevel.setText(edu);
                        String experince = info.getString("exp");//工作经验
                        experice.setText(experince);
                        String sexs = info.getString("sex");
                        gender.setText(sexs);
                        String addre = info.getString("address");
                        address.setText(addre);

                        String hy = data.getString("hy");//期望行业
                        hopeindustry.setText(hy);
                        JSONObject job = data.getJSONObject("job");//期望职位
                        String hopeposiont = job.getString("name");
                        hopeposition.setText(hopeposiont);

                        String salary = data.getString("salary");//薪资
                        hopemoney.setText(salary);
                        String status = data.getString("jobst");//求职状态
                        staus.setText(status);
                        String worktype = data.getString("ctype");//职位性质
                        positiontype.setText(worktype);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        };

        Map<String,String> map = new HashMap<String,String>();
        map.put("id",id);

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_RESUME_SCAN, false, map, true, listener, errorListener);
    }
}
