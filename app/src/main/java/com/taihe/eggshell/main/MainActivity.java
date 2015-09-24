package com.taihe.eggshell.main;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chinaway.framework.swordfish.DbUtils;
import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.DbHelper;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.FormatUtils;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.main.entity.CityBJ;
import com.taihe.eggshell.main.entity.StaticData;
import com.taihe.eggshell.widget.CustomViewPager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, IndexFragment.ChangeViewPagerListener {

    private static final String TAG = "MainActivity";
    private Context mContext;

    private CustomViewPager main_viewPager;
    private RadioGroup main_tab_RadioGroup;
    public RadioButton radio_index;
    private RadioButton radio_social, radio_openclass, radio_me;
    private ArrayList<Fragment> fragmentList;
    private DbUtils db;
    private int current = 0;

    public static List<StaticData> hylist = new ArrayList<StaticData>();
    public static List<StaticData> paylist = new ArrayList<StaticData>();
    public static List<StaticData> typelist = new ArrayList<StaticData>();
    public static List<StaticData> experiencelist = new ArrayList<StaticData>();
    public static List<StaticData> dgtimelist = new ArrayList<StaticData>();
    public static List<StaticData> jobstatuslist = new ArrayList<StaticData>();
    public static List<StaticData> educationlist = new ArrayList<StaticData>();
    public static List<StaticData> skilllist = new ArrayList<StaticData>();
    public static List<StaticData> inglists = new ArrayList<StaticData>();
    public static List<StaticData> pubtimelist = new ArrayList<StaticData>();

    public static List<StaticData> job_hylist = new ArrayList<StaticData>();
    public static List<StaticData> job_paylist = new ArrayList<StaticData>();
    public static List<StaticData> job_typelist = new ArrayList<StaticData>();
    public static List<StaticData> job_experiencelist = new ArrayList<StaticData>();
    public static List<StaticData> job_dgtimelist = new ArrayList<StaticData>();
    public static List<StaticData> job_jobstatuslist = new ArrayList<StaticData>();
    public static List<StaticData> job_educationlist = new ArrayList<StaticData>();
    public static List<StaticData> job_skilllist = new ArrayList<StaticData>();
    public static List<StaticData> job_inglists = new ArrayList<StaticData>();
    public static List<StaticData> job_pubtimelist = new ArrayList<StaticData>();


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
    }

    public void initData() {

        db = DbHelper.getDbUtils(DbHelper.DB_TYPE_USER);

        getStaticDataFromNet();
        getJobStaticDataFromNet();
    }

    private void getStaticDataFromNet() {
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {

                Log.v(TAG,(String)o);
                try {
                    JSONObject jsonObject = new JSONObject((String) o);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String hy = data.getString("hy");
                        Gson gson = new Gson();
                        List<StaticData> industryList = gson.fromJson(hy, new TypeToken<List<StaticData>>() {
                        }.getType());

                        for (int i = 0; i < industryList.size(); i++) {
                            industryList.get(i).setTypese("hy");
                        }
                        hylist.clear();
                        hylist.addAll(industryList);
//                        db.saveOrUpdateAll(industryList);

                        String pay = data.getString("pay");
                        List<StaticData> salaryList = gson.fromJson(pay, new TypeToken<List<StaticData>>() {
                        }.getType());
                        for (int i = 0; i < salaryList.size(); i++) {
                            salaryList.get(i).setTypese("pay");
                        }
                        paylist.clear();
                        paylist.addAll(salaryList);
//                        db.saveOrUpdateAll(salaryList);

                        String type = data.getString("type");
                        if (!type.equals("false")) {
                            List<StaticData> workTypeList = gson.fromJson(type, new TypeToken<List<StaticData>>() {
                            }.getType());
                            for (int i = 0; i < workTypeList.size(); i++) {
                                workTypeList.get(i).setTypese("types");
                            }
                            typelist.clear();
                            typelist.addAll(workTypeList);
//                            db.saveOrUpdateAll(workTypeList);
                        }

                        String workexper = data.getString("experience");
                        List<StaticData> workExperinceList = gson.fromJson(workexper, new TypeToken<List<StaticData>>() {
                        }.getType());
                        for (int i = 0; i < workExperinceList.size(); i++) {
                            workExperinceList.get(i).setTypese("experience");
                        }
                        experiencelist.clear();
                        experiencelist.addAll(workExperinceList);
//                        db.saveOrUpdateAll(workExperinceList);

                        String dgtime = data.getString("dgtime");
                        List<StaticData> dgTimeList = gson.fromJson(dgtime, new TypeToken<List<StaticData>>() {
                        }.getType());
                        for (int i = 0; i < dgTimeList.size(); i++) {
                            dgTimeList.get(i).setTypese("dgtime");
                        }
                        dgtimelist.clear();
                        dgtimelist.addAll(dgTimeList);
//                        db.saveOrUpdateAll(dgTimeList);

                        String status = data.getString("jobstatus");
                        List<StaticData> jobStatusList = gson.fromJson(status, new TypeToken<List<StaticData>>() {
                        }.getType());
                        for (int i = 0; i < jobStatusList.size(); i++) {
                            jobStatusList.get(i).setTypese("jobstatus");
                        }
                        jobstatuslist.clear();
                        jobstatuslist.addAll(jobStatusList);
//                        db.saveOrUpdateAll(jobStatusList);

                        String education = data.getString("education");
                        List<StaticData> educationList = gson.fromJson(education, new TypeToken<List<StaticData>>() {
                        }.getType());
                        for (int i = 0; i < educationList.size(); i++) {
                            educationList.get(i).setTypese("education");
                        }
                        educationlist.clear();
                        educationlist.addAll(educationList);
//                        db.saveOrUpdateAll(educationList);

                        String skill = data.getString("skill");//技能
                        List<StaticData> skillList = gson.fromJson(skill, new TypeToken<List<StaticData>>() {
                        }.getType());
                        for (int i = 0; i < skillList.size(); i++) {
                            skillList.get(i).setTypese("skill");
                        }
                        skilllist.clear();
                        skilllist.addAll(skillList);
//                        db.saveOrUpdateAll(skillList);

                        String ing = data.getString("ing");//熟练程度
                        List<StaticData> inglist = gson.fromJson(ing, new TypeToken<List<StaticData>>() {
                        }.getType());
                        for (int i = 0; i < inglist.size(); i++) {
                            inglist.get(i).setTypese("ing");
                        }
                        inglists.clear();
                        inglists.addAll(inglist);
//                        db.saveOrUpdateAll(inglist);

                        String pubtime = data.getString("fbtime");//发布时间
                        List<StaticData> publist = gson.fromJson(pubtime, new TypeToken<List<StaticData>>() {
                        }.getType());
                        for (int i = 0; i < publist.size(); i++) {
                            publist.get(i).setTypese("pubtime");
                        }
                        pubtimelist.clear();
                        pubtimelist.addAll(publist);

                        String citys = data.getString("three_cityid");//北京市
                        List<CityBJ> cityBJList = gson.fromJson(citys, new TypeToken<List<CityBJ>>() {
                        }.getType());
                        db.saveOrUpdateAll(cityBJList);

                        String job_classid = data.getString("job_classid");
                        List<StaticData> joblist = gson.fromJson(job_classid, new TypeToken<List<StaticData>>() {
                        }.getType());
                        for (int i = 0; i < joblist.size(); i++) {
                            joblist.get(i).setTypese("job");
                        }
                        db.saveOrUpdateAll(joblist);

                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                Log.v("TTT:",new String(volleyError.networkResponse.data));
//                ToastUtils.show(mContext, volleyError);
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        params.put("", "");

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_STATIC_DATA, false, params, true, listener, errorListener);

    }

    private void getJobStaticDataFromNet() {
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {

//                Log.v(TAG,(String)o);
                try {
                    JSONObject jsonObject = new JSONObject((String) o);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String hy = data.getString("hy");
                        Gson gson = new Gson();
                        List<StaticData> industryList = gson.fromJson(hy, new TypeToken<List<StaticData>>() {
                        }.getType());

                        for (int i = 0; i < industryList.size(); i++) {
                            industryList.get(i).setTypese("hy");
                        }
                        job_hylist.clear();
                        job_hylist.addAll(industryList);
//                        db.saveOrUpdateAll(industryList);

                        String pay = data.getString("pay");
                        List<StaticData> salaryList = gson.fromJson(pay, new TypeToken<List<StaticData>>() {
                        }.getType());
                        for (int i = 0; i < salaryList.size(); i++) {
                            salaryList.get(i).setTypese("pay");
                        }
                        job_paylist.clear();
                        job_paylist.addAll(salaryList);
//                        db.saveOrUpdateAll(salaryList);

                        String type = data.getString("type");
                        if (!type.equals("false")) {
                            List<StaticData> workTypeList = gson.fromJson(type, new TypeToken<List<StaticData>>() {
                            }.getType());
                            for (int i = 0; i < workTypeList.size(); i++) {
                                workTypeList.get(i).setTypese("types");
                            }
                            job_typelist.clear();
                            job_typelist.addAll(workTypeList);
//                            db.saveOrUpdateAll(workTypeList);
                        }

                        String workexper = data.getString("experience");
                        List<StaticData> workExperinceList = gson.fromJson(workexper, new TypeToken<List<StaticData>>() {
                        }.getType());
                        for (int i = 0; i < workExperinceList.size(); i++) {
                            workExperinceList.get(i).setTypese("experience");
                        }
                        job_experiencelist.clear();
                        job_experiencelist.addAll(workExperinceList);
//                        db.saveOrUpdateAll(workExperinceList);

                        String education = data.getString("education");
                        List<StaticData> educationList = gson.fromJson(education, new TypeToken<List<StaticData>>() {
                        }.getType());
                        for (int i = 0; i < educationList.size(); i++) {
                            educationList.get(i).setTypese("education");
                        }
                        job_educationlist.clear();
                        job_educationlist.addAll(educationList);
//                        db.saveOrUpdateAll(educationList);

                        String pubtime = data.getString("fbtime");//发布时间
                        List<StaticData> publist = gson.fromJson(pubtime, new TypeToken<List<StaticData>>() {
                        }.getType());
                        for (int i = 0; i < publist.size(); i++) {
                            publist.get(i).setTypese("pubtime");
                        }
                        job_pubtimelist.clear();
                        job_pubtimelist.addAll(publist);

                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                ToastUtils.show(mContext, volleyError);
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        params.put("", "");

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_STATIC_DATA_JOB, false, params, true, listener, errorListener);
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

        Intent intent = getIntent();
        String tags = intent.getStringExtra("Main");
        if (!TextUtils.isEmpty(tags) && tags.equals("MeFragment")) {
//            main_viewPager.setCurrentItem(3,false);
            radio_me.performClick();
        } else {
            //viewpager默认显示第一页
            main_viewPager.setCurrentItem(0, false);
        }

        main_viewPager.setScanSroll(true);
        main_viewPager.setOffscreenPageLimit(4);
        //ViewPager的页面改变监听器
//		main_viewPager.setOnPageChangeListener(new MyListner());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.id_radio_index:
                current = 0;
                break;
            case R.id.id_radio_social:
                current = 1;
                break;
            case R.id.id_radio_openclass:
                current = 2;
                break;
            case R.id.id_radio_me:
                current = 3;
                break;
        }

        if (main_viewPager.getCurrentItem() != current) {

            // viewpager直接从第一页切换到第五页，会从中间的几页过度过去，怎么取消这个效果
            // viewpagar.setCurrentItem(,);第二个参数设置为false
            getStaticDataFromNet();
            getJobStaticDataFromNet();
            main_viewPager.setCurrentItem(current, false);
            //设置没有登录状态下MeFragment不可见
//            if(current==3){
//                if(null!= EggshellApplication.getApplication().getUser()){
//                    main_viewPager.setCurrentItem(current);
//                }else{
//                    dialog.show();
//                }
//            }else{
//                main_viewPager.setCurrentItem(current);
//            }

        }
    }

    public class MyAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> list;

        public MyAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
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
        switch (v.getId()) {
        }
    }

    @Override
    public void changeViewPager(int position) {
        main_viewPager.setCurrentItem(position, false);
        if (position == 1) {
            radio_index.setChecked(false);
            radio_social.setChecked(true);
            radio_openclass.setChecked(false);
            radio_me.setChecked(false);
        } else if (position == 2) {
            radio_index.setChecked(false);
            radio_social.setChecked(false);
            radio_openclass.setChecked(true);
            radio_me.setChecked(false);
        }
    }

    private boolean isExit = false;

    @Override
    public void onBackPressed() {
        if (isExit) {
            finish();
        } else {
            isExit = true;
            ToastUtils.show(mContext, "再按一次退出应用");

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);

        }
    }


    //====================定位-----------==============
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;

    private String Longitude = "";
    private String Latitude = "";

    public Vibrator mVibrator;

    private void initLocation() {
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);


        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("gcj02");//返回的定位结果是百度经纬度，默认值gcj02
        int span = 60000;
        option.setScanSpan(span);//设置发起定位请求的间隔时间为 1min
        option.setIsNeedAddress(true);
        mLocationClient.start();
        mLocationClient.setLocOption(option);
    }


    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location

            Longitude = Double.toString(location.getLongitude());
            Latitude = Double.toString(location.getLatitude());

            PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "Longitude", Longitude);
            PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "Latitude", Latitude);

            Log.i("longitude", FormatUtils.getStringDate() + "==========longitude" + Longitude + "---------latitude:" + Latitude);

        }


    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        mLocationClient.stop();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
        Log.i(TAG, "onResume");
        initLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}
