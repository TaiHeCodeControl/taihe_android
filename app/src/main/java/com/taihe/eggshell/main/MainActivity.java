package com.taihe.eggshell.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.widget.CustomViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener {

    private Context mContext;
    private Button toPersonalCenter;
    private Button btn_login;
    private Button btn_register;
    private Intent intent;

    //ViewPager控件
    private CustomViewPager main_viewPager ;
    //RadioGroup控件
    private RadioGroup main_tab_RadioGroup ;
    //RadioButton控件
    private RadioButton radio_chats , radio_contacts , radio_discover , radio_me ;
    //类型为Fragment的动态数组
    private ArrayList<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mContext = this;

        initView();
        initViewPager();
        initData();
    }

    public void initView() {

        main_tab_RadioGroup = (RadioGroup) findViewById(R.id.main_tab_RadioGroup) ;

        radio_chats = (RadioButton) findViewById(R.id.radio_chats) ;
        radio_contacts = (RadioButton) findViewById(R.id.radio_contacts) ;
        radio_discover = (RadioButton) findViewById(R.id.radio_discover) ;
        radio_me = (RadioButton) findViewById(R.id.radio_me) ;

        main_tab_RadioGroup.setOnCheckedChangeListener(this);
    }

    public void initData() {
//        getDataFromNet();
    }

    public void initViewPager() {

        main_viewPager = (CustomViewPager) findViewById(R.id.main_ViewPager);

        fragmentList = new ArrayList<Fragment>();
        Fragment chatsFragment = new IndexFragment();
        Fragment contactsFragment = new PartTimeFragment();
        Fragment discoverFragment = new InternshipFragment();
        Fragment meFragment = new MeFragment();

        //将各Fragment加入数组中
        fragmentList.add(chatsFragment);
        fragmentList.add(contactsFragment);
        fragmentList.add(discoverFragment);
        fragmentList.add(meFragment);

        //设置ViewPager的设配器
        main_viewPager.setAdapter(new MyAdapter(getSupportFragmentManager() , fragmentList));
        //当前为第一个页面
        main_viewPager.setCurrentItem(0);
        main_viewPager.setScanSroll(true);
        //ViewPager的页面改变监听器
//		main_viewPager.setOnPageChangeListener(new MyListner());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //获取当前被选中的RadioButton的ID，用于改变ViewPager的当前页
        int current=0;
        switch(checkedId){
            case R.id.radio_chats:
                current = 0 ;
                break ;
            case R.id.radio_contacts:
                current = 1 ;
                break;
            case R.id.radio_discover:
                current = 2 ;
                break;
            case R.id.radio_me:
                current = 3 ;
                break ;
        }
        if(main_viewPager.getCurrentItem() != current){
            main_viewPager.setCurrentItem(current);
        }
    }

    public class MyAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> list ;
        public MyAdapter(FragmentManager fm , ArrayList<Fragment> list)
        {
            super(fm);
            this.list = list ;
        }
        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
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

    private void getDataFromNet(){
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    JSONObject jsonObject = new JSONObject((String)obj);
                    //String data = jsonObject.getString("data");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {//返回值

            }
        };

        //参数
        Map<String,String> param = new HashMap<String, String>();
        param.put("","");
        //POST请求
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(),Urls.METHOD_LOGIN,false,param,true,listener,errorListener);
    }

    private void getData(){

    }
}
