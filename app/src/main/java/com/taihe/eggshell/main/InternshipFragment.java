package com.taihe.eggshell.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.taihe.eggshell.job.activity.JobDetailActivity;
import com.taihe.eggshell.job.activity.JobFilterActivity;
import com.taihe.eggshell.job.activity.JobSearchActivity;
import com.taihe.eggshell.job.adapter.CardsDataAdapter;
import com.taihe.eggshell.job.bean.JobFilterUtils;
import com.taihe.eggshell.job.bean.JobInfo;
import com.taihe.eggshell.login.LoginActivity;
import com.taihe.eggshell.main.entity.User;
import com.taihe.eggshell.widget.swipecard.SwipeFlingAdapterView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InternshipFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "FindJobFragemnt";
    public static final int MSG_FIND_JOB_REFRESH = 1010;

    private Context mContext;
    private Intent intent;

    private View rootView;
    private TextView findjob_title;
    private ImageView iv_search,iv_filter;
    private RelativeLayout backLayout;

    private int page = 1;
    private String Longitude = "";
    private String Latitude = "";
    private String keyword = "";
    private String hy = "", job_post = "", salary = "", edu = "", exp = "", type = "", cityid = "", fbtime = "";
    private String job1 = "",job1_son;
    private boolean islogin = false;
    private User user;
    private JobInfo jobInfo;
    private ArrayList<JobInfo> al = new ArrayList<JobInfo>();
    private CardsDataAdapter arrayAdapter;
    public List<JobInfo> jobInfos = new ArrayList<JobInfo>();
    private SwipeFlingAdapterView flingContainer;

    private RefreshJobListListener jobListListener;

    public interface RefreshJobListListener{
        public void refreshJobList(int i);
    }

    public Handler refreshHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_FIND_JOB_REFRESH:
                    jobInfos.clear();
                    page = 1;
                    initView();
                    initData();
                    break;
            }
        }
    };

    public void setHandles(String title){
        page = 1;
        initData();
//        tv_findjob_title.setText(title);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)context).setRefreshHandle(refreshHandle);
        try{
            jobListListener = (RefreshJobListListener)context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser && getUserVisibleHint()){
            hy = "";
            job_post = "";
            salary = "";
            edu = "";
            exp = "";
            type = "";
            cityid = "0";
            fbtime = "";
            job1 = "";
            keyword = "";
            page = 1;
            Longitude = "";
            Latitude = "";
            job1_son = "";
            JobFilterUtils.filterJob(mContext, keyword, type, hy, "", job_post, salary, edu, exp, cityid, fbtime, "找工作",job1_son);
            setHandles("");
        }
    }

    @Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){
		rootView = inflater.inflate(R.layout.activity_swipecard, null) ;
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        initView();
        initData();
    }

    private void initView(){

        backLayout = (RelativeLayout) rootView.findViewById(R.id.iv_findjob_back);
        findjob_title = (TextView) rootView.findViewById(R.id.tv_findjob_title);
        iv_search = (ImageView) rootView.findViewById(R.id.iv_findjob_search);
        iv_filter = (ImageView) rootView.findViewById(R.id.iv_findjob_filter);
        flingContainer = (SwipeFlingAdapterView)rootView.findViewById(R.id.frame);

        iv_search.setOnClickListener(this);
        iv_filter.setOnClickListener(this);
        backLayout.setVisibility(View.GONE);

        //蒙层，提示用，只显示一次
        final FrameLayout frameLayout = (FrameLayout) rootView.findViewById(R.id.id_monogo);
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

        /*keyword = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "keyword", "");
        hy = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "hy", "");
        job_post = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "job_post", "");
        salary = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "salary", "");
        edu = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "edu", "");
        exp = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "exp", "");
        type = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "type", "");
        cityid = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "three_cityid", "");
        fbtime = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "fbtime", "");
        job1 = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "job1", "");
        job1_son = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "job1_son", "");

        cb_selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cb_selectAll.isChecked()) {
                    cb_selectAll.setChecked(false);
                    for (JobInfo info : jobInfos) {
                        info.setIsChecked(false);
                    }
                } else {
                    cb_selectAll.setChecked(true);
                    selectSize = jobInfos.size();
                    for (JobInfo info : jobInfos) {
                        info.setIsChecked(true);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        list_job_all.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        list_job_all.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                page++;
                getList();
                list_job_all.onRefreshComplete();
            }
        });

        list_job_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //listviewItem点击事件
                if (position < jobInfos.size()) {
                    JobInfo job = jobInfos.get(position);
                    Intent intent = new Intent(mContext, JobDetailActivity.class);
                    int jobId = job.getJob_Id();
                    String com_id = job.getUid();
                    intent.putExtra("ID", jobId);
                    intent.putExtra("com_id", com_id);
                    startActivity(intent);
                }
            }
        });*/

    }

    private void initData(){
        findjob_title.setText("找工作");

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

        /*dialog = new LoadingProgressDialog(mContext, getResources().getString(R.string.submitcertificate_string_wait_dialog));
        user = EggshellApplication.getApplication().getUser();

        if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
            getList();
        } else {
            ToastUtils.show(mContext, R.string.check_network);
        }*/
    }

    private class ColllectionAsynTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            collectPosition();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
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

        RequestUtils.createRequest(mContext, "", Urls.METHOD_JOB_COLLECT, false, param, true, listener, errorListener);

    }

    /**
     * 全城职位列表//这里修改了，记得修改FindJobActivity类，这两套代码是一样的
     * 这里修改了，记得修改FindJobActivity类，这两套代码是一样的
     */
    //全城职位列表
    private void getList() {
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
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

                        }
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
//                Log.v(TAG,new String(volleyError.networkResponse.data));
//                ToastUtils.show(mContext, volleyError);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_findjob_search://关键字搜索
                intent = new Intent(mContext, JobSearchActivity.class);
                intent.putExtra("From", "findjob");
                startActivity(intent);
                break;
            case R.id.iv_findjob_filter://职位筛选
                intent = new Intent(mContext, JobFilterActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }
}