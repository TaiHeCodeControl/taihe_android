package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.resume.adapter.BookAdapter;
import com.taihe.eggshell.resume.adapter.EduAdapter;
import com.taihe.eggshell.resume.adapter.ProjectAdapter;
import com.taihe.eggshell.resume.adapter.TechAdapter;
import com.taihe.eggshell.resume.adapter.TrainAdapter;
import com.taihe.eggshell.resume.adapter.WorkAdapter;
import com.taihe.eggshell.resume.entity.ResumeData;
import com.taihe.eggshell.resume.entity.Resumes;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.taihe.eggshell.widget.MyListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    private LoadingProgressDialog loading;
    private MyListView worklistview,edulistview,techlistview,projectlistview,booklistview,trainlistview;
    private TextView addWork,addEdu,addTech,addProject,addBook,addTrain,addSelf;
    private Resumes eid;
    private Intent intent;
    private ScrollView scrollView;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_scan);
        super.initView();

        mContext = this;

        scrollView = (ScrollView)findViewById(R.id.id_scan_scroll);
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
        worklistview = (MyListView)findViewById(R.id.id_work_list);
        addWork = (TextView)findViewById(R.id.id_to_add_work);
        addWork.setOnClickListener(this);
//        worktime = (TextView)findViewById(R.id.id_time_work);
//        workposition = (TextView)findViewById(R.id.id_position);
//        workcompany = (TextView)findViewById(R.id.id_company_name);
//        workcontent = (TextView)findViewById(R.id.id_work_content);
        //教育
        edulistview = (MyListView)findViewById(R.id.id_edu_list);
        addEdu = (TextView)findViewById(R.id.id_to_add_edu);
        addEdu.setOnClickListener(this);
//        edutime = (TextView)findViewById(R.id.id_time_edu);
//        eduindusty = (TextView)findViewById(R.id.id_professional);
//        eduschool = (TextView)findViewById(R.id.id_school_name);
//        eduposition = (TextView)findViewById(R.id.id_school_posion);
//        edubrief = (TextView)findViewById(R.id.id_prof_brief);
        //专业技能
        techlistview = (MyListView)findViewById(R.id.id_tech_list);
        addTech = (TextView)findViewById(R.id.id_to_add_tech);
        addTech.setOnClickListener(this);
//        techname = (TextView)findViewById(R.id.id_tech_name);
//        techyears = (TextView)findViewById(R.id.id_contron_time);
//        techlevel = (TextView)findViewById(R.id.id_hot_level);
//        techn = (TextView)findViewById(R.id.id_tech);
        //项目经验
        projectlistview = (MyListView)findViewById(R.id.id_project_list);
        addProject = (TextView)findViewById(R.id.id_to_add_project);
        addProject.setOnClickListener(this);
//        projecttime = (TextView)findViewById(R.id.id_time_project);
//        projectpostion = (TextView)findViewById(R.id.id_own_posion);
//        projectname = (TextView)findViewById(R.id.id_project_name);
//        projectbrief = (TextView)findViewById(R.id.id_content);
        //证书
        booklistview = (MyListView)findViewById(R.id.id_book_list);
        addBook = (TextView)findViewById(R.id.id_to_add_book);
        addBook.setOnClickListener(this);
//        booktime = (TextView)findViewById(R.id.id_time_book);
//        bookname = (TextView)findViewById(R.id.id_book_name);
//        bookcompany = (TextView)findViewById(R.id.id_book_from);
//        bookbrief = (TextView)findViewById(R.id.id_book_brief);
        //培训
        trainlistview = (MyListView)findViewById(R.id.id_train_list);
        addTrain = (TextView)findViewById(R.id.id_to_add_train);
        addTrain.setOnClickListener(this);
//        traintime = (TextView)findViewById(R.id.id_time_train);
//        traindirection = (TextView)findViewById(R.id.id_train_direction);
//        traincompnay = (TextView)findViewById(R.id.id_train_company);
//        trainbrief = (TextView)findViewById(R.id.id_train_brief);
        //自我评价
        selfbrief = (TextView)findViewById(R.id.id_self_desc);
        addSelf = (TextView)findViewById(R.id.id_to_add_self);
        addSelf.setOnClickListener(this);

    }

    @Override
    public void initData() {
        super.initData();

        initTitle("简历预览");
//        eid = getIntent().getParcelableExtra("eid");
//        Log.v("TTT:",eid.getRid()+"");
//        loading = new LoadingProgressDialog(mContext,"正在请求...");
//        if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
//            loading.show();
//            getResumeData(eid.getRid()+"");
//        }else{
//            ToastUtils.show(mContext,R.string.check_network);
//        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);

        eid = getIntent().getParcelableExtra("eid");
//        Log.v("TTT:",eid.getRid()+"");
        loading = new LoadingProgressDialog(mContext,"正在请求...");
        if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
            loading.show();
            getResumeData(eid.getRid()+"");
        }else{
            ToastUtils.show(mContext,R.string.check_network);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_to_add_work:
                intent = new Intent(mContext,ResumeWorkActivity.class);
                intent.putExtra("eid",eid);
                startActivity(intent);
                break;
            case R.id.id_to_add_edu:
                intent = new Intent(mContext,ResumeEduActivity.class);
                intent.putExtra("eid",eid);
                startActivity(intent);
                break;
            case R.id.id_to_add_tech:
                intent = new Intent(mContext,ResumeTechActivity.class);
                intent.putExtra("eid",eid);
                startActivity(intent);
                break;
            case R.id.id_to_add_project:
                intent = new Intent(mContext,ResumeProjectActivity.class);
                intent.putExtra("eid",eid);
                startActivity(intent);
                break;
            case R.id.id_to_add_train:
                intent = new Intent(mContext,ResumeTrainActivity.class);
                intent.putExtra("eid",eid);
                startActivity(intent);
                break;
            case R.id.id_to_add_book:
                intent = new Intent(mContext,ResumeBookActivity.class);
                intent.putExtra("eid",eid);
                startActivity(intent);
                break;
            case R.id.id_to_add_self:
                intent = new Intent(mContext,ResumeSelfActivity.class);
                intent.putExtra("eid",eid);
                startActivity(intent);
                break;
        }
    }

    private void getResumeData(String id) {
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                loading.dismiss();
//                Log.v(TAG,(String)o);
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
                            schoolLevel.setText(edu.getString("name"));
                        }

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

                        if("null".equals(expect.getString("three_cityid"))){
                            hopeaddress.setText("全城");
                        }else{
                            JSONObject addres = expect.getJSONObject("three_cityid");//地区
                            hopeaddress.setText(addres.getString("name"));
                        }
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
                            worklistview.setAdapter(new WorkAdapter(mContext,worklists));
                            addWork.setVisibility(View.GONE);
//                            ResumeData work = worklists.get(0);
//                            worktime.setText(FormatUtils.timestampToDatetime(work.getSdate())+"——"+FormatUtils.timestampToDatetime(work.getEdate()));
//                            workposition.setText(work.getName());
//                            workcompany.setText(work.getDepartment());
//                            workcontent.setText(work.getContent());
                        }else{
                            addWork.setVisibility(View.VISIBLE);
                        }
                        String jylist = data.getString("jy");//教育
                        if(!jylist.equals("[]")){
                            List<ResumeData> jylists = gson.fromJson(jylist,new TypeToken<List<ResumeData>>(){}.getType());
                            edulistview.setAdapter(new EduAdapter(mContext,jylists));
                            addEdu.setVisibility(View.GONE);
//                            ResumeData jy = jylists.get(0);
//                            edutime.setText(FormatUtils.timestampToDatetime(jy.getSdate())+"——"+FormatUtils.timestampToDatetime(jy.getEdate()));
//                            eduindusty.setText(jy.getSpecialty());
//                            eduposition.setText(jy.getTitle());
//                            eduschool.setText(jy.getName());
//                            edubrief.setText(jy.getContent());
                        }else{
                            addEdu.setVisibility(View.VISIBLE);
                        }
                        String skilllist = data.getString("skill");//技能
                        if(!skilllist.equals("[]")){
                            List<ResumeData> skilllists = gson.fromJson(skilllist,new TypeToken<List<ResumeData>>(){}.getType());
                            techlistview.setAdapter(new TechAdapter(mContext,skilllists));
                            addTech.setVisibility(View.GONE);
//                            ResumeData skill = skilllists.get(0);
//                            techlevel.setText(skill.getIng());
//                            techyears.setText(skill.getLongtime()+"年");
//                            techname.setText(skill.getSkill());
//                            techn.setText(skill.getSkill());
                        }else{
                            addTech.setVisibility(View.VISIBLE);
                        }
                        String projectlist = data.getString("project");//项目
                        if(!projectlist.equals("[]")){
                            List<ResumeData> projectlists = gson.fromJson(projectlist,new TypeToken<List<ResumeData>>(){}.getType());
                            projectlistview.setAdapter(new ProjectAdapter(mContext,projectlists));
                            addProject.setVisibility(View.GONE);
//                            ResumeData project = projectlists.get(0);
//                            projecttime.setText(FormatUtils.timestampToDatetime(project.getSdate())+"——"+FormatUtils.timestampToDatetime(project.getEdate()));
//                            projectname.setText(project.getName());
//                            projectbrief.setText(project.getContent());
//                            projectpostion.setText(project.getTitle());
                        }else{
                            addProject.setVisibility(View.VISIBLE);
                        }
                        String trainlist = data.getString("training");//培训
                        if(!trainlist.equals("[]")){
                            List<ResumeData> trainlists = gson.fromJson(trainlist,new TypeToken<List<ResumeData>>(){}.getType());
                            trainlistview.setAdapter(new TrainAdapter(mContext,trainlists));
                            addTrain.setVisibility(View.GONE);
//                            ResumeData train = trainlists.get(0);
//                            traintime.setText(FormatUtils.timestampToDatetime(train.getSdate())+"——"+FormatUtils.timestampToDatetime(train.getEdate()));
//                            traincompnay.setText(train.getName());
//                            traindirection.setText(train.getTitle());
//                            trainbrief.setText(train.getContent());
                        }else{
                            addTrain.setVisibility(View.VISIBLE);
                        }
                        String booklist = data.getString("cert");//证书
                        if(!booklist.equals("[]")){
                            List<ResumeData> booklists = gson.fromJson(booklist,new TypeToken<List<ResumeData>>(){}.getType());
                            booklistview.setAdapter(new BookAdapter(mContext,booklists));
                            addBook.setVisibility(View.GONE);
//                            ResumeData book = booklists.get(0);
//                            booktime.setText(FormatUtils.timestampToDatetime(book.getSdate()));
//                            bookcompany.setText(book.getTitle());
//                            bookname.setText(book.getName());
//                            bookbrief.setText(book.getContent());
                        }else{
                            addBook.setVisibility(View.VISIBLE);
                        }
                        String ohter = data.getString("other");//自我评价
                        if(!ohter.equals("[]")){
                            JSONObject other = data.getJSONObject("other");
                            selfbrief.setText(other.getString("content"));
                            addSelf.setVisibility(View.GONE);
                            selfbrief.setVisibility(View.VISIBLE);
                        }else{
                            selfbrief.setVisibility(View.GONE);
                            addSelf.setVisibility(View.VISIBLE);
                        }

                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.scrollTo(0,0);
                            }
                        });
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
//                Log.v(TAG,new String(volleyError.networkResponse.data));
                ToastUtils.show(mContext,"网络异常");
            }
        };

        Map<String,String> map = new HashMap<String,String>();
        map.put("eid",id);

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_RESUME_SCAN, false, map, true, listener, errorListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}
