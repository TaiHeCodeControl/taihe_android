package com.taihe.eggshell.resume;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.FormatUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.resume.entity.ResumeData;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
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
    private TextView techname,techyears,techlevel,techn;
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
        techn = (TextView)findViewById(R.id.id_tech);
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

                        JSONObject info = data.getJSONObject("info");
                        String name = info.getString("uname");//姓名
                        userName.setText(name);
                        String emails = info.getString("email");//邮箱
                        email.setText(emails);
                        JSONObject edu = info.getJSONObject("edu");//学历
                        schoolLevel.setText(edu.getString("name"));
                        JSONObject experince = info.getJSONObject("exp");//工作经验
                        experice.setText(experince.getString("name"));
                        JSONObject sexs = info.getJSONObject("sex");//性别
                        gender.setText(sexs.getString("name"));
                        String birthday = info.getString("birthday");//年龄
                        age.setText(birthday);
                        String addre = info.getString("address");
                        address.setText(addre);
                        String tel = info.getString("telphone");//电话
                        telphone.setText(tel);

                        JSONObject expect = data.getJSONObject("expect");
                        String rename = expect.getString("name");//简历名称
                        resumename.setText(rename);
                        JSONObject addres = expect.getJSONObject("three_cityid");//地区
                        hopeaddress.setText(addres.getString("name"));
                        JSONObject hy = expect.getJSONObject("hy");//期望行业
                        hopeindustry.setText(hy.getString("name"));
                        JSONObject hopeposiont = expect.getJSONObject("job");//职位
                        hopeposition.setText(hopeposiont.getString("name"));
                        JSONObject salary = expect.getJSONObject("salary");//薪资
                        hopemoney.setText(salary.getString("name"));
                        JSONObject status = expect.getJSONObject("jobst");//求职状态
                        staus.setText(status.getString("name"));
                        JSONObject worktype = expect.getJSONObject("ctype");//职位性质
                        positiontype.setText(worktype.getString("name"));
                        JSONObject dgtime = expect.getJSONObject("dgtime");//到岗时间
                        hopetime.setText(dgtime.getString("name"));

                        Gson gson = new Gson();
                        String worklist = data.getString("work");//工作经验
                        if(!worklist.equals("[]")){
                            List<ResumeData> worklists = gson.fromJson(worklist,new TypeToken<List<ResumeData>>(){}.getType());
                            ResumeData work = worklists.get(0);
                            worktime.setText(FormatUtils.timestampToDatetime(work.getSdate())+"——"+FormatUtils.timestampToDatetime(work.getEdate()));
                            workposition.setText(work.getName());
                            workcompany.setText(work.getDepartment());
                            workcontent.setText(work.getContent());
                        }
                        String jylist = data.getString("jy");//教育
                        if(!jylist.equals("[]")){
                            List<ResumeData> jylists = gson.fromJson(jylist,new TypeToken<List<ResumeData>>(){}.getType());
                            ResumeData jy = jylists.get(0);
                            edutime.setText(FormatUtils.timestampToDatetime(jy.getSdate())+"——"+FormatUtils.timestampToDatetime(jy.getEdate()));
                            eduindusty.setText(jy.getSpecialty());
                            eduposition.setText(jy.getTitle());
                            eduschool.setText(jy.getName());
                            edubrief.setText(jy.getContent());
                        }
                        String skilllist = data.getString("skill");//技能
                        if(!skilllist.equals("[]")){
                            List<ResumeData> skilllists = gson.fromJson(skilllist,new TypeToken<List<ResumeData>>(){}.getType());
                            ResumeData skill = skilllists.get(0);
                            techlevel.setText(skill.getIng());
                            techyears.setText(skill.getLongtime()+"年");
                            techname.setText(skill.getSkill());
                            techn.setText(skill.getSkill());
                        }
                        String projectlist = data.getString("project");//项目
                        if(!projectlist.equals("[]")){
                            List<ResumeData> projectlists = gson.fromJson(projectlist,new TypeToken<List<ResumeData>>(){}.getType());
                            ResumeData project = projectlists.get(0);
                            projecttime.setText(FormatUtils.timestampToDatetime(project.getSdate())+"——"+FormatUtils.timestampToDatetime(project.getEdate()));
                            projectname.setText(project.getName());
                            projectbrief.setText(project.getContent());
                            projectpostion.setText(project.getTitle());
                        }
                        String trainlist = data.getString("training");//培训
                        if(!trainlist.equals("[]")){
                            List<ResumeData> trainlists = gson.fromJson(trainlist,new TypeToken<List<ResumeData>>(){}.getType());
                            ResumeData train = trainlists.get(0);
                            traintime.setText(FormatUtils.timestampToDatetime(train.getSdate())+"——"+FormatUtils.timestampToDatetime(train.getEdate()));
                            traincompnay.setText(train.getName());
                            traindirection.setText(train.getTitle());
                            trainbrief.setText(train.getContent());
                        }
                        String booklist = data.getString("cert");//证书
                        if(!booklist.equals("[]")){
                            List<ResumeData> booklists = gson.fromJson(booklist,new TypeToken<List<ResumeData>>(){}.getType());
                            ResumeData book = booklists.get(0);
                            booktime.setText(FormatUtils.timestampToDatetime(book.getSdate()));
                            bookcompany.setText(book.getTitle());
                            bookname.setText(book.getName());
                            bookbrief.setText(book.getContent());
                        }
                        String ohter = data.getString("other");//自我评价
                        if(!ohter.equals("[]")){
                            JSONObject other = data.getJSONObject("other");
                            selfbrief.setText(other.getString("content"));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.show(mContext,volleyError.networkResponse.statusCode+"服务器错误");
            }
        };

        Map<String,String> map = new HashMap<String,String>();
        map.put("eid","73");

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_RESUME_SCAN, false, map, true, listener, errorListener);
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
