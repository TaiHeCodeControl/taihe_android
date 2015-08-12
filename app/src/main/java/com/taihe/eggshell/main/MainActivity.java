package com.taihe.eggshell.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.login.LoginActivity;
import com.taihe.eggshell.main.entity.User;
import com.taihe.eggshell.videoplay.PolyvDemoService;
import com.taihe.eggshell.widget.ChoiceDialog;
import com.taihe.eggshell.widget.CustomViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Context mContext;

    private CustomViewPager main_viewPager ;
    private RadioGroup main_tab_RadioGroup ;
    private RadioButton radio_index , radio_social , radio_openclass , radio_me ;
    private ArrayList<Fragment> fragmentList;
    private ChoiceDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mContext = this;

        initView();
        initViewPager();
        initData();
    }

    public void initView() {

        main_tab_RadioGroup = (RadioGroup) findViewById(R.id.main_tab_RadioGroup);
        radio_index = (RadioButton) findViewById(R.id.id_radio_index);
        radio_social = (RadioButton) findViewById(R.id.id_radio_social);
        radio_openclass = (RadioButton) findViewById(R.id.id_radio_openclass);
        radio_me = (RadioButton) findViewById(R.id.id_radio_me);

        main_tab_RadioGroup.setOnCheckedChangeListener(this);

        dialog = new ChoiceDialog(mContext,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ToastUtils.show(mContext, "取消");
                //虚拟点击事件
                radio_index.performClick();
            }
        },new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);

            }
        });

        dialog.getTitleText().setText("亲，您还没有登录呢~");
        dialog.getLeftButton().setText("取消");
        dialog.getRightButton().setText("登录");
    }

    public void initData() {

    }

    public void initViewPager() {

        main_viewPager = (CustomViewPager) findViewById(R.id.main_ViewPager);

        fragmentList = new ArrayList<Fragment>();
        Fragment indexFragment = new IndexFragment();
        Fragment partTimeFragment = new NearbyFragment();
        Fragment internshipFragment = new InternshipFragment();
        Fragment meFragment = new MeFragment();

        fragmentList.add(indexFragment);
        fragmentList.add(partTimeFragment);
        fragmentList.add(internshipFragment);
        fragmentList.add(meFragment);

        main_viewPager.setAdapter(new MyAdapter(getSupportFragmentManager(), fragmentList));
        main_viewPager.setCurrentItem(0);
        main_viewPager.setScanSroll(true);
        main_viewPager.setOffscreenPageLimit(4);
        //ViewPager的页面改变监听器
//		main_viewPager.setOnPageChangeListener(new MyListner());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int current=0;
        switch(checkedId){
            case R.id.id_radio_index:
                current = 0 ;
                break ;
            case R.id.id_radio_social:
                current = 1 ;
                break;
            case R.id.id_radio_openclass:
                current = 2 ;
                break;
            case R.id.id_radio_me:
                current = 3 ;
                break ;
        }

        if(main_viewPager.getCurrentItem() != current){
            if(current==3){
                if(null!= EggshellApplication.getApplication().getUser()){
                    main_viewPager.setCurrentItem(current);
                }else{
                    dialog.show();
                }
            }else{
                main_viewPager.setCurrentItem(current);
            }

        }
    }

    public class MyAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> list ;
        public MyAdapter(FragmentManager fm , ArrayList<Fragment> list){
            super(fm);
            this.list = list ;
        }
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
//                return new IndexFragment();
                return list.get(position);
            } else if (position == 1) {
                return list.get(position);
            } else if (position == 2) {
//                return new InternshipFragment();
                return list.get(position);
            } else if (position == 3) {
//                return new MeFragment();
                return list.get(position);
            }
            return null;
        }
        @Override
        public int getCount() {
            return list.size();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }

    private boolean isExit = false;
    @Override
    public void onBackPressed() {
        if(isExit){
            finish();
        }else{
            isExit = true;
            ToastUtils.show(mContext,"再按一次退出应用");

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            },2000);

        }
    }
}
