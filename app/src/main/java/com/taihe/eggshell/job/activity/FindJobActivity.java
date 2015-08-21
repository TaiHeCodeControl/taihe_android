package com.taihe.eggshell.job.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.job.fragment.AllJobFragment;
import com.taihe.eggshell.job.fragment.FujinFragment;
import com.taihe.eggshell.widget.CustomViewPager;


/**
 * Created by huan on 2015/8/6.
 */
public class FindJobActivity extends FragmentActivity implements View.OnClickListener {

    private Intent intent;
    private CustomViewPager vp_pager;

    private TextView tv_allJob, tv_fujin;
    private ImageView iv_quancheng, iv_fujin, iv_filter, iv_search;
    private RelativeLayout rl_qc,rl_fujin, iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_findjob);

        initView();
        initData();

    }

    private void initView() {
        vp_pager = (CustomViewPager) findViewById(R.id.vp_findjob_pager);

        rl_fujin = (RelativeLayout) findViewById(R.id.rl_findjob_fujin);
        rl_qc = (RelativeLayout) findViewById(R.id.rl_findjob_qc);
        rl_fujin.setOnClickListener(this);
        rl_qc.setOnClickListener(this);

        tv_allJob = (TextView) findViewById(R.id.tv_findjob_all);
        tv_fujin = (TextView) findViewById(R.id.tv_findjob_fujin);

        iv_search = (ImageView) findViewById(R.id.iv_findjob_search);
        iv_filter = (ImageView) findViewById(R.id.iv_findjob_filter);
        iv_back = (RelativeLayout) findViewById(R.id.iv_findjob_back);

        iv_fujin = (ImageView) findViewById(R.id.iv_findjob_fj);
        iv_quancheng = (ImageView) findViewById(R.id.iv_findjob_qc);

        iv_search.setOnClickListener(this);
        iv_filter.setOnClickListener(this);
        iv_back.setOnClickListener(this);


        MyJobFragmentPagerAdapter adapter = new MyJobFragmentPagerAdapter(
                getSupportFragmentManager());
        vp_pager.setAdapter(adapter);
        //Viewpager中每次显示出来一个页面Fragment时，都会把旁边的一个页面也预加载了，
//        vp_pager.setOffscreenPageLimit(0);//控制预加载的页面数量（默认情况下参数为1）
    }

    private void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_findjob_back:
                FindJobActivity.this.finish();
                break;
            case R.id.rl_findjob_qc:
                vp_pager.setCurrentItem(0);
                iv_quancheng.setImageResource(R.drawable.quancheng01);
                iv_fujin.setImageResource(R.drawable.fujin01);

                tv_allJob.setTextColor(getResources().getColor(R.color.font_color_red));
                tv_fujin.setTextColor(getResources().getColor(R.color.font_color_black));
                break;
            case R.id.rl_findjob_fujin:
                vp_pager.setCurrentItem(1);
                iv_quancheng.setImageResource(R.drawable.quancheng02);
                iv_fujin.setImageResource(R.drawable.fujin02);

                tv_allJob.setTextColor(getResources().getColor(R.color.font_color_black));
                tv_fujin.setTextColor(getResources().getColor(R.color.font_color_red));
                break;
            case R.id.iv_findjob_search:
                intent = new Intent(FindJobActivity.this,JobSearchActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_findjob_filter:
                intent = new Intent(FindJobActivity.this, JobFilterActivity.class);
                startActivity(intent);
                break;
        }
    }


    class MyJobFragmentPagerAdapter extends FragmentPagerAdapter {

        private Fragment fragment;

        public MyJobFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int index) {
            switch (index) {
                case 0://
                    Fragment oneFragment = new AllJobFragment();
                    return oneFragment;
                case 1://
                    Fragment twoFragment = new FujinFragment();
                    return twoFragment;

            }
            return null;
        }
    }



}
