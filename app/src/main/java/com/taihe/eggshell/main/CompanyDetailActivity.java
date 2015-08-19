package com.taihe.eggshell.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.adapter.AllJobAdapter;
import com.taihe.eggshell.job.bean.JobInfo;
import com.taihe.eggshell.widget.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang on 2015/8/11.
 */
public class CompanyDetailActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "CompanyDetailActivity";

    private Context mContext;
    private TextView companyIndustry,companyType,companyScale,companyAddress,companyBrief,upordown;
    private MyListView jobsListView;
    private ScrollView scrollView;

    private boolean isMore = false;
    private List<JobInfo> jobInfos = new ArrayList<JobInfo>();

    @Override
    public void initView() {
        setContentView(R.layout.activity_company_detail);
        super.initView();

        mContext = this;
        scrollView = (ScrollView)findViewById(R.id.id_scroll_view);
        companyIndustry = (TextView)findViewById(R.id.id_company_industry);
        companyType = (TextView)findViewById(R.id.id_company_type);
        companyScale = (TextView)findViewById(R.id.id_company_scale);
        companyAddress = (TextView)findViewById(R.id.id_company_addr);
        companyBrief = (TextView)findViewById(R.id.id_company_brief);
        upordown = (TextView)findViewById(R.id.id_up_down);
        jobsListView = (MyListView)findViewById(R.id.id_jobs_list_view);
//        jobsListView.setMode(PullToRefreshBase.Mode.DISABLED);
        upordown.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("名企详情");

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0,0);
            }
        });

        int companyId = getIntent().getIntExtra("companyId",0);
        ToastUtils.show(mContext,companyId+"哈哈");

        companyBrief.setText("年快递记录看到了镀镍，的历史的看到路上看到了快点接受电视卡佛恩打开了时空的顺利打开n.lksdod离开时记得哦哦申能打开的肌肤放，四道口恩看到京阿尼多了。");
        companyBrief.post(new Runnable() {
            @Override
            public void run() {
                if (companyBrief.getLineCount() > 2) {
                    isMore = true;
                    companyBrief.setMaxLines(2);
                    upordown.setVisibility(View.VISIBLE);
                } else {
                    upordown.setVisibility(View.GONE);
                }
            }
        });
        getDataFromNet();
        AllJobAdapter jobAdapter = new AllJobAdapter(mContext,jobInfos,false);
        jobsListView.setAdapter(jobAdapter);

        jobsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.show(mContext, position + "");

            }
        });

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_foot_bottom,null);
        jobsListView.addFooterView(view);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_up_down:
                if(isMore){
                    isMore = false;
                    companyBrief.setMaxLines(Integer.MAX_VALUE);
                    upordown.setText("点击收起");
                    Drawable drawable = getResources().getDrawable(R.drawable.up_icon);
                    drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                    upordown.setCompoundDrawables(null,null,drawable,null);
                }else{
                    isMore = true;
                    companyBrief.setMaxLines(2);
                    upordown.setText("查看全部");
                    Drawable drawable = getResources().getDrawable(R.drawable.down_detail_icon);
                    drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                    upordown.setCompoundDrawables(null,null,drawable,null);
                }

                break;
        }
    }

    private void getDataFromNet(){
        jobInfos.clear();
        for(int i=0;i<3;i++){
            jobInfos.add(new JobInfo(false,i));
        }
    }


}
