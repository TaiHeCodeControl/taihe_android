package com.taihe.eggshell.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaway.framework.swordfish.DbUtils;
import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.DbHelper;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.activity.JobFilterActivity;
import com.taihe.eggshell.job.activity.JobSearchActivity;
import com.taihe.eggshell.main.entity.CityBJ;
import com.taihe.eggshell.main.entity.StaticData;
import com.taihe.eggshell.widget.CustomViewPager;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, IndexFragment.ChangeViewPagerListener,InternshipFragment.RefreshJobListListener {

    private static final String TAG = "MainActivity";
    private Context mContext;

    private CustomViewPager main_viewPager;
    private RadioGroup main_tab_RadioGroup;
    public RadioButton radio_index;
    private RadioButton radio_social, radio_openclass, radio_me;
    private TextView notifiNum;
    private ArrayList<Fragment> fragmentList;
    private DbUtils db;
    private int current = 0;
    private Handler refreshHandle;
    private Handler unReadHandler;
    public static int unnum = 0;

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

    public static boolean isForeground = false;
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private static final int REQUEST_CODE_KEYWORDSEARCH = 1001;
    private static final int REQUEST_CODE_FILTER = 1002;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    Log.d(TAG, "Set tags in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mContext = this;

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        initView();
        initViewPager();
        initData();

        // used for receive msg  推送
        JPushInterface.init(getApplicationContext());
        registerMessageReceiver();
    }

    public void initView() {

        main_tab_RadioGroup = (RadioGroup) findViewById(R.id.main_tab_RadioGroup);
        radio_index = (RadioButton) findViewById(R.id.id_radio_index);
        radio_social = (RadioButton) findViewById(R.id.id_radio_social);
        radio_openclass = (RadioButton) findViewById(R.id.id_radio_openclass);
        radio_me = (RadioButton) findViewById(R.id.id_radio_me);
        notifiNum = (TextView) findViewById(R.id.id_notification_numss);
        main_tab_RadioGroup.setOnCheckedChangeListener(this);
    }

    public void initData() {

        db = DbHelper.getDbUtils(DbHelper.DB_TYPE_USER);

        // ","隔开的多个 转换成 Set
        String[] sArray = "企业".split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            if (!isValidTagAndAlias(sTagItme)) {
                Toast.makeText(mContext, R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            tagSet.add(sTagItme);
        }
        //调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

        getStaticDataFromNet();
        getJobStaticDataFromNet();
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    private void getStaticDataFromNet() {
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {

//                Log.v(TAG,(String)o);
                try {
                    JSONObject jsonObject = new JSONObject((String) o);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String hy = data.getString("hy");//行业
                        Gson gson = new Gson();
                        List<StaticData> industryList = gson.fromJson(hy, new TypeToken<List<StaticData>>() {
                        }.getType());

                        for (int i = 0; i < industryList.size(); i++) {
                            industryList.get(i).setTypese("hy");
                        }
                        hylist.clear();
                        hylist.addAll(industryList);
//                        db.saveOrUpdateAll(industryList);

                        String pay = data.getString("pay");//薪资
                        List<StaticData> salaryList = gson.fromJson(pay, new TypeToken<List<StaticData>>() {
                        }.getType());
                        for (int i = 0; i < salaryList.size(); i++) {
                            salaryList.get(i).setTypese("pay");
                        }
                        paylist.clear();
                        paylist.addAll(salaryList);
//                        db.saveOrUpdateAll(salaryList);

                        String type = data.getString("type");//工作类型，兼职，全职
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

                        String workexper = data.getString("experience");//工作经验
                        List<StaticData> workExperinceList = gson.fromJson(workexper, new TypeToken<List<StaticData>>() {
                        }.getType());
                        for (int i = 0; i < workExperinceList.size(); i++) {
                            workExperinceList.get(i).setTypese("experience");
                        }
                        experiencelist.clear();
                        experiencelist.addAll(workExperinceList);
//                        db.saveOrUpdateAll(workExperinceList);

                        String dgtime = data.getString("dgtime");//到岗时间
                        List<StaticData> dgTimeList = gson.fromJson(dgtime, new TypeToken<List<StaticData>>() {
                        }.getType());
                        for (int i = 0; i < dgTimeList.size(); i++) {
                            dgTimeList.get(i).setTypese("dgtime");
                        }
                        dgtimelist.clear();
                        dgtimelist.addAll(dgTimeList);
//                        db.saveOrUpdateAll(dgTimeList);

                        String status = data.getString("jobstatus");//工作状态，离职，在职
                        List<StaticData> jobStatusList = gson.fromJson(status, new TypeToken<List<StaticData>>() {
                        }.getType());
                        for (int i = 0; i < jobStatusList.size(); i++) {
                            jobStatusList.get(i).setTypese("jobstatus");
                        }
                        jobstatuslist.clear();
                        jobstatuslist.addAll(jobStatusList);
//                        db.saveOrUpdateAll(jobStatusList);

                        String education = data.getString("education");//学历
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

                        String job_classid = data.getString("job_classid");//职位类别
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

                        String type = data.getString("type");//工作类型，全职，兼职
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

    //实现职位界面监听跳转界面
    @Override
    public void refreshJobList(int i) {
        if(1==i){
            Intent intent = new Intent(mContext, JobSearchActivity.class);
            intent.putExtra("From", "findjob");
            startActivityForResult(intent, REQUEST_CODE_KEYWORDSEARCH);
        }else if(2==i){
            Intent intent = new Intent(mContext, JobFilterActivity.class);
            startActivityForResult(intent, REQUEST_CODE_FILTER);
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

    //MainAtivity实现IndexFragment监听事件
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

    private PopupWindow pw;
    private Bitmap lastPhoto = null;
    private static final int NONE = 0;
    private static final int PHOTOHRAPH = 1;// 拍照
    private static final int PHOTOZOOM = 2; // 缩放
    private static final int PHOTORESOULT = 3;// 结果
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private LoadingProgressDialog uploadImageDialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == 101 || resultCode == 201) {
            if(main_viewPager.getCurrentItem()==2){
                if(null!=getSupportFragmentManager().getFragments().get(2)){
                    InternshipFragment fragemnt = (InternshipFragment)(fragmentList.get(2));
                    fragemnt.setHandles("");//data.getStringExtra("jobtype")
                }
            }
        }/*else if(resultCode == 101){
            if(main_viewPager.getCurrentItem()==2){
                if(null!=getSupportFragmentManager().getFragments().get(2)){
                    InternshipFragment fragemnt = (InternshipFragment)(fragmentList.get(2));
                    fragemnt.setHandles("");
//                    refreshHandle.sendEmptyMessage(InternshipFragment.MSG_FIND_JOB_REFRESH);
                }
            }
        }*/

        // 拍照
        if (requestCode == PHOTOHRAPH) {

            // 设置文件保存路径这里放在跟目录下

            File picture = new File(Environment.getExternalStorageDirectory()
                    + "/temp.jpg");
            startPhotoZoom(Uri.fromFile(picture));

        }

        if (data == null)

            return;

        // 读取相册缩放图片

        if (requestCode == PHOTOZOOM) {
            startPhotoZoom(data.getData());
        }

        // 处理结果

        if (requestCode == PHOTORESOULT) {

            Bundle extras = data.getExtras();

            if (extras != null) {

                lastPhoto = extras.getParcelable("data");
                extras.getParcelable("data");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();// 字节数组输出
                lastPhoto.compress(Bitmap.CompressFormat.JPEG, 75, stream);//
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;

                // **************将截取后的图片保存到SD卡的temp.jpg文件
                byte[] byteArray = stream.toByteArray();// 字节数组输出流转换成字节数组

                File file = new File(Environment.getExternalStorageDirectory()
                        + "/eggkerImage.JPEG");
                String filePath = Environment.getExternalStorageDirectory() + "/eggkerImage.JPEG";

                // 将字节数组写入到刚创建的图片文件
                try {
                    fos = new FileOutputStream(file);
                    bos = new BufferedOutputStream(fos);
                    bos.write(byteArray);

                    if (stream != null)
                        stream.close();
                    if (bos != null)
                        bos.close();
                    if (fos != null)
                        fos.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //用户头像图片显示==================================================
                if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
                    uploadImageDialog = new LoadingProgressDialog(mContext, "头像上传中...");
                    uploadImageDialog.show();
                    String ImageString = getPstr(filePath);
//                    上传用户图片
                    upLoadImage(ImageString);
                } else {
                    ToastUtils.show(mContext, R.string.check_network);
                }
            }
        }
    }

    //图片裁剪
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);

        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例

        intent.putExtra("aspectX", 1);

        intent.putExtra("aspectY", 1);

        // outputX outputY 是裁剪图片宽

        intent.putExtra("outputX", 200);

        intent.putExtra("outputY", 200);

        intent.putExtra("return-data", true);

        startActivityForResult(intent, PHOTORESOULT);

    }

    //上传头像
    private void upLoadImage(String ImageString) {

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                uploadImageDialog.dismiss();
                try {
//                    Log.v("upLoadImage:", (String) o);

                    JSONObject jsonObject = new JSONObject((String) o);

                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if (code == 0) {//图片上传成功
                        JSONObject data = jsonObject.getJSONObject("data");
                        String imagePath = data.getString("resume_photo");

                        ToastUtils.show(mContext, "头像上传成功");

                        if(main_viewPager.getCurrentItem()==3){
                            if(null!=getSupportFragmentManager().getFragments().get(3)){
                                MeFragment fragemnt = (MeFragment)(fragmentList.get(3));
                                fragemnt.setHandles("");
                            }
                        }
                    } else {
                        ToastUtils.show(mContext, "上传失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                uploadImageDialog.dismiss();
                ToastUtils.show(mContext, "网络异常");
            }
        };

        Map<String, String> param = new HashMap<String, String>();
        param.put("uid", EggshellApplication.getApplication().getUser().getId()+"");
        param.put("photo", ImageString);
        param.put("token", EggshellApplication.getApplication().getUser().getToken());
        RequestUtils.createRequest(mContext, Urls.METHOD_UPLOAD_IMAGE, "", true, param, true, listener, errorListener);

    }

    //将头像转换成Base64编码
    public String getPstr(String pathname) {

        try {
            FileInputStream fileInputStream = new FileInputStream(pathname);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int i;
            // 转化为字节数组流
            while ((i = fileInputStream.read()) != -1) {
                byteArrayOutputStream.write(i);
            }
            fileInputStream.close();
            // 把文件存在一个字节数组中
            byte[] buff = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            // 将图片的字节数组进行BASE64编码

            String pstr = new String(Base64.encodeToString(buff, Base64.DEFAULT));
            return pstr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void setRefreshHandle(Handler handle){
            this.refreshHandle = handle;
    }

    public void setUnReadHandler(Handler handler){
        this.unReadHandler = handler;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        isForeground = true;
        super.onResume();
        MobclickAgent.onResume(mContext);
        startService(new Intent(LocationService.ACTION_NAME));
        if(null != EggshellApplication.getApplication().getUser()){
            sendPhoneInfo();
            getUnReadNotification();
        }

    }

    @Override
    public void onPause() {
        isForeground = false;
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        startService(new Intent(LocationService.ACTION_NAME));
        super.onDestroy();
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.taihe.message.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!TextUtils.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                Log.v(TAG+"Receiver:",showMsg.toString());
            }
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

    private void sendPhoneInfo(){

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
//                Log.v("SING;",(String)o);
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
//                        Log.v("sing:","OK");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                Log.v("EEE:",new String(volleyError.networkResponse.data));
            }
        };

        Map<String,String> params = new HashMap<String,String>();
        params.put("uid", EggshellApplication.getApplication().getUser().getId()+"");
        params.put("sign", PrefUtils.getStringPreference(mContext,PrefUtils.CONFIG,PrefUtils.KEY_PHONE_INFO,""));
//        Log.v("SING:",params.toString());
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_PHONE_SIGN, false, params, true, listener, errorListener);

    }

    private void getUnReadNotification(){
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                Log.v("UNREAD;",(String)o);
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        unnum = jsonObject.getInt("data");
                        if(0==unnum){
                            notifiNum.setVisibility(View.GONE);
                        }else{
                            notifiNum.setText(unnum+"");
                            notifiNum.setVisibility(View.VISIBLE);
                        }

                        Message message = Message.obtain();
                        message.what = 111;
                        message.obj = unnum;
                        unReadHandler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                Log.v("EEE:",new String(volleyError.networkResponse.data));
            }
        };

        Map<String,String> params = new HashMap<String,String>();
        params.put("uid", EggshellApplication.getApplication().getUser().getId()+"");
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(),Urls.METHOD_UNREAD_NUM,false,params,true,listener,errorListener);

    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };
}