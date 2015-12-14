package com.taihe.eggshell.job.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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

public class SwipecardsActivity extends Activity {

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
    private String job1 = "";
    private boolean islogin = false;
    public List<JobInfo> jobInfos = new ArrayList<JobInfo>();
    private JobInfo jobInfo;
    private User user;
    private SwipeFlingAdapterView flingContainer;

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
        flingContainer = (SwipeFlingAdapterView)findViewById(R.id.frame);
        final FrameLayout frameLayout = (FrameLayout)findViewById(R.id.id_monogo);
        if(!PrefUtils.getBooleanData(mContext,PrefUtils.CONFIG,true)){
            frameLayout.setVisibility(View.GONE);
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout.setVisibility(View.GONE);
                PrefUtils.saveBooleanData(mContext,PrefUtils.CONFIG,false);
            }
        });
    }

    private void initData(){

        getList();
        user = EggshellApplication.getApplication().getUser();
        arrayAdapter =  new CardsDataAdapter(mContext,al);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                if(islogin){
                    jobInfo = al.get(0);
                    al.remove(0);
                }else{
                   Intent intent = new Intent(mContext, LoginActivity.class);
                   startActivity(intent);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                if(islogin){
                    collectPosition();
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

                LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.id_transprent_card);
                linearLayout.setVisibility(View.VISIBLE);
                if(scrollProgressPercent < 0){//left
                    linearLayout.setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                    islogin = true;
                }else{//right
                    linearLayout.setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
                    if (null == user) {//登录
                        EggshellApplication.getApplication().setLoginTag("findJobCard");
                        islogin = false;
                    } else {
                        islogin = true;
                        int userId = EggshellApplication.getApplication().getUser().getId();
                    }
                }

                TextView rightView = (TextView)view.findViewById(R.id.item_swipe_right_indicator);
                rightView.setVisibility(View.VISIBLE);
                rightView.setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);

                TextView leftView = (TextView)view.findViewById(R.id.item_swipe_left_indicator);
                leftView.setVisibility(View.VISIBLE);
                leftView.setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);

            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Intent intent = new Intent(mContext, JobInfoActivity.class);
                intent.putExtra("ID", al.get(0).getJob_Id());
                intent.putExtra("com_id", al.get(0).getUid());
                startActivity(intent);
            }
        });
    }

    //收藏职位
    private void collectPosition() {
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                dialog.dismiss();
                try {
//                    Log.v(TAG, (String) o);
                    JSONObject jsonObject = new JSONObject((String) o);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        ToastUtils.show(mContext, "收藏成功");
                    } else {
                        ToastUtils.show(mContext, "收藏失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                ToastUtils.show(mContext, "网络异常");
            }
        };

        Map<String, String> param = new HashMap<String, String>();
        param.put("uid", user.getId() + "");
        param.put("job_id", jobInfo.getJob_Id() + "");

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
                                ToastUtils.show(mContext, "没有了");
                            }
                        } else {
                            Gson gson = new Gson();
                            List<JobInfo> joblist = gson.fromJson(data, new TypeToken<List<JobInfo>>() {
                            }.getType());
                            al.addAll(joblist);
                            arrayAdapter.notifyDataSetChanged();

                        }
                    } else if (code == 4001) {

                    } else {
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

        Log.v(TAG, param.toString());
        RequestUtils.createRequest(mContext, "", Urls.METHOD_JOB_LIST, false, param, true, listener, errorListener);
    }
}
