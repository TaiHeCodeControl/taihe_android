package com.taihe.eggshell.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.taihe.eggshell.R;
import com.taihe.eggshell.main.adapter.GuidePageAdapter;

import java.util.ArrayList;

/**
 * Created by Thinkpad on 2015/7/15.
 */
public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener{

    private Context mContext;

    private ViewPager viewPager;
    private ArrayList<View> views = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        mContext = this;

        initView();
        initData();
    }

    private void initView(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        viewPager = (ViewPager)findViewById(R.id.id_guide);

        views = new ArrayList<View>();
        // 初始化引导图片列表
        views.add(inflater.inflate(R.layout.guide_one, null));
        views.add(inflater.inflate(R.layout.guide_two, null));
        views.add(inflater.inflate(R.layout.guide_three, null));
        views.add(inflater.inflate(R.layout.guide_fouth, null));

    }

    private void initData(){
        GuidePageAdapter pageAdapter = new GuidePageAdapter(mContext,views);
        viewPager.setAdapter(pageAdapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
