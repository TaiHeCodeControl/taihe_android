package com.taihe.eggshell.job.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.adapter.CardsDataAdapter;
import com.taihe.eggshell.job.bean.JobInfo;
import com.taihe.eggshell.login.LoginActivity;
import com.taihe.eggshell.main.entity.User;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.taihe.eggshell.widget.swipecard.SwipeFlingAdapterView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwipecardsActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "SwipecardsActivity";
    private Context mContext;

    private ArrayList<JobInfo> al = new ArrayList<JobInfo>();
    private CardsDataAdapter arrayAdapter;
    private LoadingProgressDialog dialog;

    private int page = 1;
    private String Longitude = "";
    private String Latitude = "";
    private String keyword = "";
    private String hy = "", job_post = "", salary = "", edu = "", exp = "", type = "", cityid = "", fbtime = "";
    private String job1 = "",job1_son = "";
    private boolean islogin = false;
    public List<JobInfo> jobInfos = new ArrayList<JobInfo>();
    private JobInfo jobInfo;
    private User user;
    private SwipeFlingAdapterView flingContainer;
    private TextView rightView,leftView;
    private ImageView iv_search,iv_filter,iv_no_data;
    private RelativeLayout backLayout;
    private LinearLayout linearLayout;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipecard);
        mContext = this;

        initView();
        initData();
    }

    private void initView(){

        dialog = new LoadingProgressDialog(mContext, getResources().getString(R.string.submitcertificate_string_wait_dialog));
        backLayout = (RelativeLayout) findViewById(R.id.iv_findjob_back);
        iv_search = (ImageView) findViewById(R.id.iv_findjob_search);
        iv_filter = (ImageView) findViewById(R.id.iv_findjob_filter);
        flingContainer = (SwipeFlingAdapterView)findViewById(R.id.frame);
        iv_no_data = (ImageView) findViewById(R.id.id_no_data);

        iv_search.setOnClickListener(this);
        iv_filter.setOnClickListener(this);
        backLayout.setOnClickListener(this);

        //蒙层，提示用，只显示一次
        final FrameLayout frameLayout = (FrameLayout)findViewById(R.id.id_monogo);
        if(!PrefUtils.getBooleanData(mContext,PrefUtils.KEY_FIRST_IN,true)){
            frameLayout.setVisibility(View.GONE);
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout.setVisibility(View.GONE);
                PrefUtils.saveBooleanData(mContext,PrefUtils.KEY_FIRST_IN,false);
            }
        });
    }

    private void initData(){
        //首页传的type类型
        type = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "type", "");
        dialog.show();
        getList();
        user = EggshellApplication.getApplication().getUser();
        arrayAdapter =  new CardsDataAdapter(mContext,al);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {//先执行
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                if(islogin){//如果登录了删除否则跳转到登录页，且不删除最前边的卡
                    jobInfo = al.get(0);
                    al.remove(0);
                    if(al.size()==0){
                        flingContainer.setBackgroundResource(R.color.white);
                        iv_no_data.setVisibility(View.VISIBLE);
                    }
                }else{
                   Intent intent = new Intent(mContext, LoginActivity.class);
                   startActivity(intent);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {//后执行
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                if(islogin){//如果登录了直接收藏
                    new ColllectionAsynTask().execute();
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                page +=1;
                getList();
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();

                if(null!=view){
                    linearLayout = (LinearLayout)view.findViewById(R.id.id_transprent_card);
                    linearLayout.setVisibility(View.VISIBLE);
                    if(scrollProgressPercent < 0){//left
                        if(scrollProgressPercent < -0.6){
                            linearLayout.setAlpha(0.6f);
                        }else {
                            linearLayout.setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                        }
                        islogin = true;
                    }else{//right
                        if(scrollProgressPercent > 0.6){
                            linearLayout.setAlpha(0.6f);
                        }else {
                            linearLayout.setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
                        }
                        if (null == user) {//登录
                            EggshellApplication.getApplication().setLoginTag("findJobCard");
                            islogin = false;
                        } else {
                            islogin = true;
//                            int userId = EggshellApplication.getApplication().getUser().getId();
                        }
                    }

                    rightView = (TextView)view.findViewById(R.id.item_swipe_right_indicator);
                    rightView.setVisibility(View.VISIBLE);
                    rightView.setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);

                    leftView = (TextView)view.findViewById(R.id.item_swipe_left_indicator);
                    leftView.setVisibility(View.VISIBLE);
                    leftView.setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
                }
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Intent intent = new Intent(mContext, JobDetailActivity.class);
                intent.putExtra("ID", al.get(0).getJob_Id());
                intent.putExtra("com_id", al.get(0).getUid());
                startActivity(intent);
            }
        });
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.iv_findjob_search://关键字搜索
                intent = new Intent(mContext, JobSearchActivity.class);
                intent.putExtra("From", "findjob");
                startActivity(intent);
                break;
            case R.id.iv_findjob_filter://职位筛选
                intent = new Intent(mContext, JobFilterActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_findjob_back:
                finish();
                break;
        }
    }

    private class ColllectionAsynTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            collectPosition();
            return null;
        }
    }

    //收藏职位
    private void collectPosition() {
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                try {
//                    Log.v(TAG, (String) o);
                    JSONObject jsonObject = new JSONObject((String) o);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
//                        ToastUtils.show(mContext, "收藏成功");
                    } else {
//                        ToastUtils.show(mContext, "收藏失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.show(mContext, "网络异常");
            }
        };

        Map<String, String> param = new HashMap<String, String>();
        param.put("uid", user.getId() + "");
        param.put("job_id", jobInfo.getJob_Id() + "");
        param.put("card","1");//收藏、取消收藏

        RequestUtils.createRequest(mContext, "", Urls.METHOD_JOB_COLLECT, false, param, true, listener, errorListener);

    }

    //全城职位列表
    private void getList() {
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                dialog.dismiss();
                try {
//                    Log.v("JOB:", (String) o);
                    JSONObject jsonObject = new JSONObject((String) o);
                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if (code == 0) {
                        String data = jsonObject.getString("data");
                        if ("[]".equals(data)) {
                            if (page == 1) {
                                jobInfos.clear();
//                                adapter = new AllJobAdapter(mContext, jobInfos, true);
//                                list_job_all.setAdapter(adapter);
//                                adapter.notifyDataSetChanged();
//                                ToastUtils.show(mContext, "没有了");
                            } else {
//                                ToastUtils.show(mContext, "没有了");
                            }
                        } else {
                            Gson gson = new Gson();
                            List<JobInfo> joblist = gson.fromJson(data, new TypeToken<List<JobInfo>>() {
                            }.getType());
                            al.addAll(joblist);
                            arrayAdapter.notifyDataSetChanged();
                            if(al.size()>0){
                                flingContainer.setBackgroundResource(R.drawable.card_stack_background);
                                iv_no_data.setVisibility(View.GONE);
                            }
                        }
                    }else {
                        ToastUtils.show(mContext, "获取失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
//                Log.v(TAG,new String(volleyError.networkResponse.data));
                ToastUtils.show(mContext, volleyError);
            }
        };

        // longitude=116.404916&dimensionality=39.927471&keyword=工程师&page=1
        //    传值项： longitude=>经度  dimensionality=>纬度 keyword=>关键字
        //    page=>页数 hy=>工作行业 职位类别=>job_post 月薪范围=>salary 学历要求=>edu 工作年限=>exp
        // 工作性质=>type

        Map<String, String> param = new HashMap<String, String>();
        param.put("longitude", Longitude);
        param.put("dimensionality", Latitude);
        param.put("keyword", keyword);//关键字
        param.put("page", page + "");
        param.put("hy", hy);//工作行业
        param.put("job_post", job_post);//职位类别
        param.put("salary", salary);
        param.put("edu", edu);
        param.put("exp", exp);//工作年限
        param.put("type", type);//工作性质
        param.put("fbtime", fbtime);//
        if("0".equals(cityid)){
            param.put("provinceid", "2");
        }else{
            param.put("three_cityid", cityid);//
        }
        param.put("job1", job1);
        param.put("job1_son",job1_son);

//        Log.v(TAG, param.toString());
        RequestUtils.createRequest(mContext, "", Urls.METHOD_JOB_LIST, false, param, true, listener, errorListener);
    }
}
