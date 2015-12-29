package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
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
    private TextView selfbrief;
    private LoadingProgressDialog loading;
    private MyListView worklistview,edulistview,techlistview,projectlistview,booklistview,trainlistview;
    private TextView addWork,addEdu,addTech,addProject,addBook,addTrain,addSelf,jobIntention;
    private Resumes eid;
    private Intent intent;
    private ScrollView scrollView;
    private LinearLayout workLinear,eduLinear,skillLinear,projectLinear,bookLinear,trainLinear,selfLinear;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
                        loading.show();
                        getResumeData(eid.getRid()+"");
                    }else{
                        ToastUtils.show(mContext,R.string.check_network);
                    }
                break;
            }
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_scan);
        super.initView();

        mContext = this;

        scrollView = (ScrollView)findViewById(R.id.id_scan_scroll);
        workLinear = (LinearLayout)findViewById(R.id.id_work_linear);
        eduLinear = (LinearLayout)findViewById(R.id.id_edu_linear);
        skillLinear = (LinearLayout)findViewById(R.id.id_skill_linear);
        projectLinear = (LinearLayout)findViewById(R.id.id_project_linear);
        bookLinear = (LinearLayout)findViewById(R.id.id_book_linear);
        trainLinear = (LinearLayout)findViewById(R.id.id_train_linear);
        selfLinear = (LinearLayout)findViewById(R.id.id_self_linear);

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
        jobIntention = (TextView)findViewById(R.id.id_job_intention);
        hopeposition = (TextView)findViewById(R.id.id_hope_position);
        hopeindustry = (TextView)findViewById(R.id.id_hope_industy);
        hopemoney = (TextView)findViewById(R.id.id_want_money);
        hopeaddress = (TextView)findViewById(R.id.id_work_ear);
        hopetime = (TextView)findViewById(R.id.id_come_time);
        staus = (TextView)findViewById(R.id.id_status);
        positiontype = (TextView)findViewById(R.id.id_position_type);
        jobIntention.setOnClickListener(this);
        //工作
        worklistview = (MyListView)findViewById(R.id.id_work_list);
        addWork = (TextView)findViewById(R.id.id_to_add_work);
        addWork.setOnClickListener(this);
        //教育
        edulistview = (MyListView)findViewById(R.id.id_edu_list);
        addEdu = (TextView)findViewById(R.id.id_to_add_edu);
        addEdu.setOnClickListener(this);
        //专业技能
        techlistview = (MyListView)findViewById(R.id.id_tech_list);
        addTech = (TextView)findViewById(R.id.id_to_add_tech);
        addTech.setOnClickListener(this);
        //项目经验
        projectlistview = (MyListView)findViewById(R.id.id_project_list);
        addProject = (TextView)findViewById(R.id.id_to_add_project);
        addProject.setOnClickListener(this);
        //证书
        booklistview = (MyListView)findViewById(R.id.id_book_list);
        addBook = (TextView)findViewById(R.id.id_to_add_book);
        addBook.setOnClickListener(this);
        //培训
        trainlistview = (MyListView)findViewById(R.id.id_train_list);
        addTrain = (TextView)findViewById(R.id.id_to_add_train);
        addTrain.setOnClickListener(this);
        //自我评价
        selfbrief = (TextView)findViewById(R.id.id_self_desc);
        addSelf = (TextView)findViewById(R.id.id_to_add_self);
        addSelf.setOnClickListener(this);

    }

    @Override
    public void initData() {
        super.initData();

        initTitle("简历预览");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
        loading = new LoadingProgressDialog(mContext,"正在请求...");
        /*if(null!=getIntent().getStringExtra("eid") && !TextUtils.isEmpty(getIntent().getStringExtra("eid"))){
            if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
                loading.show();
                getResumeData(getIntent().getStringExtra("eid"));
            }else{
                ToastUtils.show(mContext,R.string.check_network);
            }
        }else{*/
            eid = getIntent().getParcelableExtra("eid");
            if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
                loading.show();
                getResumeData(eid.getRid()+"");
            }else{
                ToastUtils.show(mContext,R.string.check_network);
            }
//        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_job_intention:
                intent = new Intent(mContext,ResumeWriteActivity.class);
                intent.putExtra("eid",eid.getRid()+"");
                startActivity(intent);
                break;
            case R.id.id_to_add_work:
                intent = new Intent(mContext,ResumeWorkActivity.class);
                intent.putExtra("eid",eid);
                intent.putExtra("type","");
                intent.putExtra("listobj","");
                startActivity(intent);
                break;
            case R.id.id_to_add_edu:
                intent = new Intent(mContext,ResumeEduActivity.class);
                intent.putExtra("eid",eid);
                intent.putExtra("type","");
                intent.putExtra("listobj","");
                startActivity(intent);
                break;
            case R.id.id_to_add_tech:
                intent = new Intent(mContext,ResumeTechActivity.class);
                intent.putExtra("eid",eid);
                intent.putExtra("type","");
                intent.putExtra("listobj","");
                startActivity(intent);
                break;
            case R.id.id_to_add_project:
                intent = new Intent(mContext,ResumeProjectActivity.class);
                intent.putExtra("eid",eid);
                intent.putExtra("type","");
                intent.putExtra("listobj","");
                startActivity(intent);
                break;
            case R.id.id_to_add_train:
                intent = new Intent(mContext,ResumeTrainActivity.class);
                intent.putExtra("eid",eid);
                intent.putExtra("type","");
                intent.putExtra("listobj","");
                startActivity(intent);
                break;
            case R.id.id_to_add_book:
                intent = new Intent(mContext,ResumeBookActivity.class);
                intent.putExtra("eid",eid);
                intent.putExtra("type","");
                intent.putExtra("listobj", "");
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
//                Log.v(TAG, (String) o);
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
                        String ctime = expect.getString("ctime");
                        createTime.setText("创建于"+ctime);
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
                            worklistview.setAdapter(new WorkAdapter(mContext,worklists,eid,handler));
                            workLinear.setVisibility(View.VISIBLE);
                        }else{
                            workLinear.setVisibility(View.GONE);
                        }
                        String jylist = data.getString("jy");//教育
                        if(!jylist.equals("[]")){
                            List<ResumeData> jylists = gson.fromJson(jylist,new TypeToken<List<ResumeData>>(){}.getType());
                            edulistview.setAdapter(new EduAdapter(mContext,jylists,eid,handler));
                            eduLinear.setVisibility(View.VISIBLE);
                        }else{
                            eduLinear.setVisibility(View.GONE);
                        }
                        String skilllist = data.getString("skill");//技能
                        if(!skilllist.equals("[]")){
                            List<ResumeData> skilllists = gson.fromJson(skilllist,new TypeToken<List<ResumeData>>(){}.getType());
                            techlistview.setAdapter(new TechAdapter(mContext,skilllists,eid,handler));
                            skillLinear.setVisibility(View.VISIBLE);
                        }else{
                            skillLinear.setVisibility(View.GONE);
                        }
                        String projectlist = data.getString("project");//项目
                        if(!projectlist.equals("[]")){
                            List<ResumeData> projectlists = gson.fromJson(projectlist,new TypeToken<List<ResumeData>>(){}.getType());
                            projectlistview.setAdapter(new ProjectAdapter(mContext,projectlists,eid,handler));
                            projectLinear.setVisibility(View.VISIBLE);
                        }else{
                            projectLinear.setVisibility(View.GONE);
                        }
                        String trainlist = data.getString("training");//培训
                        if(!trainlist.equals("[]")){
                            List<ResumeData> trainlists = gson.fromJson(trainlist,new TypeToken<List<ResumeData>>(){}.getType());
                            trainlistview.setAdapter(new TrainAdapter(mContext,trainlists,eid,handler));
                            trainLinear.setVisibility(View.VISIBLE);
                        }else{
                            trainLinear.setVisibility(View.GONE);
                        }
                        String booklist = data.getString("cert");//证书
                        if(!booklist.equals("[]")){
                            List<ResumeData> booklists = gson.fromJson(booklist,new TypeToken<List<ResumeData>>(){}.getType());
                            booklistview.setAdapter(new BookAdapter(mContext,booklists,eid,handler));
                            bookLinear.setVisibility(View.VISIBLE);
                        }else{
                            bookLinear.setVisibility(View.GONE);
                        }
                        String ohter = data.getString("other");//自我评价
                        if(!ohter.equals("[]")){
                            JSONObject other = data.getJSONObject("other");
                            selfbrief.setText(other.getString("content"));
                            selfbrief.setVisibility(View.VISIBLE);
                            selfLinear.setVisibility(View.VISIBLE);
                        }else{
                            selfbrief.setVisibility(View.GONE);
                            selfLinear.setVisibility(View.GONE);
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
                ToastUtils.show(mContext,volleyError);
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
