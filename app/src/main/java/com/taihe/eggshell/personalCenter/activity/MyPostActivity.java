package com.taihe.eggshell.personalCenter.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.personalCenter.adapter.MyPostFragmentPagerAdapter;


/**
 * Created by huan on 2015/7/23.
 */
public class MyPostActivity extends FragmentActivity implements View.OnClickListener{

    // 设置评价类型标题字的颜色
    private static final int VALUE_TEXT_COLOR = 0xFFCA3031;
    private static final int ITEMS_TEXT_COLOR = 0xFF000000;

    private ImageView iv_bottomLine;
    private ViewPager myview_pager;
    private TextView tv_mypostSuccess,tv_mypost,tv_mypostFailure;

    private int currIndex = 0;
    private int position_one;
    private int position_two;


    private ImageView iv_back;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mine_post);

        initWidth();
        initView();
        initDate();
    }

    private void initWidth() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();

        position_one = (int) (width / 3.0);
        position_two = position_one * 2;
    }

    private void initDate() {

    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.id_back);
        tv_title = (TextView) findViewById(R.id.id_title);

        tv_mypostSuccess = (TextView) findViewById(R.id.tv_mypost_success);
        tv_mypost = (TextView) findViewById(R.id.tv_mypost_post);
        tv_mypostFailure = (TextView) findViewById(R.id.tv_mypost_failure);
        myview_pager = (ViewPager) findViewById(R.id.vPager);
        iv_bottomLine = (ImageView) findViewById(R.id.iv_bottom_mypost_line);

        iv_back.setOnClickListener(this);
        tv_title.setText("我的投递");
        tv_mypost.setTextColor(VALUE_TEXT_COLOR);
        tv_mypostFailure.setTextColor(ITEMS_TEXT_COLOR);
        tv_mypostSuccess.setTextColor(ITEMS_TEXT_COLOR);

        MyPostFragmentPagerAdapter adapter = new MyPostFragmentPagerAdapter(
                getSupportFragmentManager());
        myview_pager.setAdapter(adapter);

        myview_pager.setOnPageChangeListener(new MyOnPageChangeListener());
        //Viewpager中每次显示出来一个页面Fragment时，都会把旁边的一个页面也预加载了，
        myview_pager.setOffscreenPageLimit(0);//控制预加载的页面数量（默认情况下参数为1）


        tv_mypostSuccess.setOnClickListener(this);
        tv_mypostFailure.setOnClickListener(this);
        tv_mypost.setOnClickListener(this);
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_back:
                MyPostActivity.this.finish();
                break;
            case R.id.tv_mypost_post:
                myview_pager.setCurrentItem(0);
                break;
            case R.id.tv_mypost_success:
                myview_pager.setCurrentItem(1);
                break;
            case R.id.tv_mypost_failure:
                myview_pager.setCurrentItem(2);
                break;
        }
    }


    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;

            switch (arg0) {

                case 0:

                    if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, 0, 0, 0);
                        tv_mypostSuccess.setTextColor(ITEMS_TEXT_COLOR);

                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(position_two, 0, 0, 0);
                        tv_mypostFailure.setTextColor(ITEMS_TEXT_COLOR);

                    }
                    tv_mypost.setTextColor(VALUE_TEXT_COLOR);
                    break;
                case 1:

                    if (currIndex == 0) {
                        animation = new TranslateAnimation(0, position_one, 0, 0);
                        tv_mypost.setTextColor(ITEMS_TEXT_COLOR);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(position_two,
                                position_one, 0, 0);
                        tv_mypostFailure.setTextColor(ITEMS_TEXT_COLOR);
                    }
                    tv_mypostSuccess.setTextColor(VALUE_TEXT_COLOR);
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(0, position_two, 0, 0);
                        tv_mypost.setTextColor(ITEMS_TEXT_COLOR);

                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one,
                                position_two, 0, 0);
                        tv_mypostSuccess.setTextColor(ITEMS_TEXT_COLOR);
                    }
                    tv_mypostFailure.setTextColor(VALUE_TEXT_COLOR);
                    break;

            }

            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            iv_bottomLine.startAnimation(animation);
        }
    }


}
