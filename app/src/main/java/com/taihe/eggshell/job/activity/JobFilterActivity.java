package com.taihe.eggshell.job.activity;


import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.job.bean.JobFilterUtils;
import com.taihe.eggshell.main.entity.CityBJ;
import com.taihe.eggshell.main.entity.Industry;
import com.taihe.eggshell.main.entity.StaticData;
import com.taihe.eggshell.widget.CityDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by huan on 2015/8/11.
 */
public class JobFilterActivity extends BaseActivity {


    private Intent intent;
    private Context mContext;
    private RelativeLayout rl_industry, rl_position, rl_jobcity, rl_salary, rl_edu, rl_jobyears, rl_jobtype, rl_pubtime;
    private ImageView iv_clear;
    private EditText et_keyWord;
    private TextView tv_industry, tv_position, tv_jobcity, tv_salary, tv_edu, tv_jobyears, tv_jobtype, tv_pubtime;
    private Button btn_jobfilter_chaxun;
    private StaticData result;
    private CityDialog cityDialog;

    private LinearLayout lin_back;

    private static final int REQUEST_CODE_INDUSTRYTYPE = 100;
    private static final int REQUEST_CODE_JOBYEAR = 101;
    private static final int REQUEST_CODE_EDU = 102;
    private static final int REQUEST_CODE_POSITION = 103;
    private static final int REQUEST_CODE_JOBCITY = 104;
    private static final int REQUEST_CODE_SALARY = 105;
    private static final int REQUEST_CODE_JOBTYPE = 106;
    private static final int REQUEST_CODE_PUBTIME = 107;

    private String keyword = "";
    private String hy = "";
    private String job_post = "";
    private String salary = "";
    private String edu = "";
    private String exp = "";
    private String type = "";
    private String pubtime = "";
    private String city = "";

    @Override
    public void initView() {
        setContentView(R.layout.activity_job_filtert);
        super.initView();

        mContext = this;

        iv_clear = (ImageView) findViewById(R.id.iv_jobfilter_clear);
        iv_clear.setOnClickListener(this);

        lin_back = (LinearLayout) findViewById(R.id.lin_back);
        lin_back.setOnClickListener(this);

        btn_jobfilter_chaxun = (Button) findViewById(R.id.btn_jobfilter_chaxun);
        btn_jobfilter_chaxun.setOnClickListener(this);

        et_keyWord = (EditText) findViewById(R.id.et_jobfilter_keyword);

        tv_industry = (TextView) findViewById(R.id.tv_jobfilter_industry);
        tv_position = (TextView) findViewById(R.id.tv_jobfilter_position);
        tv_jobcity = (TextView) findViewById(R.id.tv_jobfilter_jobcity);
        tv_salary = (TextView) findViewById(R.id.tv_jobfilter_salary);
        tv_edu = (TextView) findViewById(R.id.tv_jobfilter_edu);
        tv_jobyears = (TextView) findViewById(R.id.tv_jobfilter_jobyears);
        tv_jobtype = (TextView) findViewById(R.id.tv_jobfilter_jobtype);
        tv_pubtime = (TextView) findViewById(R.id.tv_jobfilter_pubtime);


        rl_industry = (RelativeLayout) findViewById(R.id.rl_jobfilter_industry);
        rl_industry.setOnClickListener(this);

        rl_position = (RelativeLayout) findViewById(R.id.rl_jobfilter_position);
        rl_position.setOnClickListener(this);

        rl_jobcity = (RelativeLayout) findViewById(R.id.rl_jobfilter_jobcity);
        rl_jobcity.setOnClickListener(this);
        rl_salary = (RelativeLayout) findViewById(R.id.rl_jobfilter_salary);
        rl_salary.setOnClickListener(this);

        rl_edu = (RelativeLayout) findViewById(R.id.rl_jobfilter_edu);
        rl_edu.setOnClickListener(this);

        rl_jobyears = (RelativeLayout) findViewById(R.id.rl_jobfilter_jobyears);
        rl_jobyears.setOnClickListener(this);

        rl_jobtype = (RelativeLayout) findViewById(R.id.rl_jobfilter_jobtype);
        rl_jobtype.setOnClickListener(this);

        rl_pubtime = (RelativeLayout) findViewById(R.id.rl_jobfilter_pubtime);
        rl_pubtime.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();

        initTitle("职位筛选");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_back:
                JobFilterActivity.this.finish();
                break;
            case R.id.iv_jobfilter_clear:
                et_keyWord.setText("");
                break;
            case R.id.rl_jobfilter_edu://学历要求
                intent = new Intent(mContext, PositionActivity.class);
                intent.putExtra("Filter", "edu");
                startActivityForResult(intent, REQUEST_CODE_EDU);
                break;
            case R.id.rl_jobfilter_industry://行业类别
                intent = new Intent(mContext, PositionActivity.class);
                intent.putExtra("Filter", "industry");
                startActivityForResult(intent, REQUEST_CODE_INDUSTRYTYPE);
                break;
            case R.id.rl_jobfilter_jobcity://工作城市
                cityDialog = new CityDialog(mContext,new CityDialog.CityClickListener() {
                    @Override
                    public void city(CityBJ c) {
                        tv_jobcity.setText(c.getName());
                        city = c.getId()+"";
                        cityDialog.dismiss();
                    }
                });
                cityDialog.show();
                break;
            case R.id.rl_jobfilter_jobtype://工作类别
                intent = new Intent(mContext, PositionActivity.class);
                intent.putExtra("Filter", "jobtype");
                startActivityForResult(intent, REQUEST_CODE_JOBTYPE);
                break;
            case R.id.rl_jobfilter_jobyears://工作经验、工作年限

                intent = new Intent(mContext, PositionActivity.class);
                intent.putExtra("Filter", "jobyears");
                startActivityForResult(intent, REQUEST_CODE_JOBYEAR);
                break;

            case R.id.rl_jobfilter_position://职位类别
                intent = new Intent(mContext, PositionActivity.class);
                intent.putExtra("Filter", "position");
                intent.putExtra("flag","pos");
                startActivityForResult(intent, REQUEST_CODE_POSITION);
                break;
            case R.id.rl_jobfilter_pubtime://发布时间
                intent = new Intent(mContext, PositionActivity.class);
                intent.putExtra("Filter", "pubtime");
                startActivityForResult(intent, REQUEST_CODE_PUBTIME);
                break;
            case R.id.rl_jobfilter_salary://薪资要求

                intent = new Intent(mContext, PositionActivity.class);
                intent.putExtra("Filter", "salary");
                startActivityForResult(intent, REQUEST_CODE_SALARY);
                break;

            case R.id.btn_jobfilter_chaxun://查询职位

                //keyword=>关键字 page=>页数 hy=>工作行业 职位类别=>job_post 月薪范围=>salary 学历要求=>edu 工作年限=>exp 工作性质=>type

                keyword = et_keyWord.getText().toString().trim();

                //保存职位筛选的字段
                JobFilterUtils.filterJob(mContext,keyword,type,"",hy,job_post,salary,edu,exp,city,pubtime,"搜索结果");

                intent = new Intent();
                setResult(101,intent);
                this.finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            result = data.getParcelableExtra("data");
            if(null == result){
                return;
            }
            switch (requestCode){
                case REQUEST_CODE_INDUSTRYTYPE:
                    tv_industry.setText(result.getName()); //行业类型
                    hy = result.getId()+"";
                    break;
                case REQUEST_CODE_POSITION:
                    tv_position.setText(result.getName());//职位类别
                    job_post = result.getId()+"";
                    break;
                case REQUEST_CODE_JOBYEAR:
                    tv_jobyears.setText(result.getName());//工作年限
                    exp = result.getId()+"";
                    break;
                case REQUEST_CODE_SALARY:
                    tv_salary.setText(result.getName());//薪资要求
                    salary = result.getId()+"";
                    break;
                case REQUEST_CODE_EDU:
                    tv_edu.setText(result.getName());//学历要求
                    edu = result.getId()+"";
                    break;
                case REQUEST_CODE_JOBCITY:
//                    tv_jobcity.setText(result.getName());//工作城市
//                    city = result.getId()+"";
//                    break;
                case REQUEST_CODE_JOBTYPE:
                    tv_jobtype.setText(result.getName()); //工作类型
                    type = result.getId()+"";
                    break;
                case REQUEST_CODE_PUBTIME:
                    tv_pubtime.setText(result.getName());//发布时间
                    pubtime = result.getId()+"";
                    break;
            }
        }

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
