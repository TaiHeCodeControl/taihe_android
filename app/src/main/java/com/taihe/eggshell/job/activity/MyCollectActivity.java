package com.taihe.eggshell.job.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.adapter.AllJobAdapter;
import com.taihe.eggshell.job.bean.JobInfo;
import com.taihe.eggshell.main.MainActivity;
import com.taihe.eggshell.main.entity.User;
import com.taihe.eggshell.widget.JobApplyDialogUtil;
import com.taihe.eggshell.widget.LoadingProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 收藏职位列表
 * Created by huan on 2015/8/14.
 */
public class MyCollectActivity extends BaseActivity {


    private AllJobAdapter adapter;
    private CheckBox cb_selectAll;

    private LoadingProgressDialog dialog;
    private List<JobInfo> jobInfos = null;
    private JobInfo jobInfo;

    private PullToRefreshGridView list_job_all;
    private Context mContext;

    private View footerView;
    private static final String TAG = "MyCollectActivity";

    private int page = 1;
    private int pageSize = 2;
    //选中条数的统计
    private int selectSize = 0;
    private int postednum = 0;
    private TextView tv_collect_num;//收藏个数
    private StringBuilder sb = new StringBuilder();

    private User user;
    private String userId = "";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 101://删除职位成功
                    adapter.notifyDataSetChanged();
                    cb_selectAll.setChecked(false);
                    ToastUtils.show(mContext, "删除成功");
                    break;
                case 201://收藏职位列表


                    try {
                        JSONObject jsonObject = (JSONObject) msg.obj;
                        String count = jsonObject.getString("count");
                        String data = jsonObject.getString("data");


                        Gson gson = new Gson();
                        List<JobInfo> joblist = gson.fromJson(data, new TypeToken<List<JobInfo>>() {
                        }.getType());


                        jobInfos.addAll(joblist);

                        tv_collect_num.setText(count + "条记录");
                        adapter = new AllJobAdapter(mContext, jobInfos, true);
                        adapter.setCheckedListener(new AllJobAdapter.checkedListener() {
                            @Override
                            public void checkedPosition(int position, boolean isChecked) {
                                jobInfos.get(position).setIsChecked(isChecked);
                                //如果有listview没有被选中，全选按钮状态为false
                                if (jobInfos.get(position).isChecked()) {
                                    selectSize += 1;
                                    if (selectSize == jobInfos.size()) {
                                        cb_selectAll.setChecked(true);
                                    }
                                } else {
                                    selectSize -= 1;
                                    cb_selectAll.setChecked(false);
                                }


                            }
                        });

                        list_job_all.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {

                    }
                    break;
            }
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_job_list);
        super.initView();
        mContext = this;
    }

    @Override
    public void initData() {
        super.initData();
        super.initTitle("收藏职位");
        initListView();
        initListData();
    }


    public void initListView() {
        user = EggshellApplication.getApplication().getUser();
        if (user != null) {
            userId = user.getId() + "";
        }
        jobInfos = new ArrayList<JobInfo>();
        tv_collect_num = (TextView) findViewById(R.id.tv_collect_num);//收藏职位记录
        list_job_all = (PullToRefreshGridView) findViewById(R.id.list_job);

        list_job_all.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        //  职位详情
        list_job_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //listviewItem点击事件

                JobInfo job = jobInfos.get(position);
                Intent intent = new Intent(mContext, JobDetailActivity.class);
                intent.putExtra("ID", job.getJob_Id());
                intent.putExtra("UID", job.getCom_id());
                Log.i("ID", job.getJob_Id() + "");
                startActivity(intent);
            }
        });

        list_job_all.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                cb_selectAll.setChecked(false);
                page++;
                getList();
                list_job_all.onRefreshComplete();
            }
        });


        Button btn_shenqing = (Button) findViewById(R.id.btn_alljob_shenqing);
        Button btn_delete = (Button) findViewById(R.id.btn_collectjob_delete);
        btn_delete.setOnClickListener(this);
        btn_shenqing.setOnClickListener(this);
        cb_selectAll = (CheckBox) findViewById(R.id.cb_findjob_selectall);


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
    }


    private void initListData() {

        if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
            dialog = new LoadingProgressDialog(mContext, getResources().getString(
                    R.string.submitcertificate_string_wait_dialog));
            dialog.show();
            getList();
        } else {
            ToastUtils.show(mContext, R.string.check_network);
        }


    }

    //我的收藏职位列表
    private void getList() {

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                dialog.dismiss();
                try {
                    Log.v("MyCollect:", (String) o);

                    JSONObject jsonObject = new JSONObject((String) o);

                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if (code == 0) {

                        Message msg = Message.obtain();
                        msg.what = 201;
                        msg.obj = jsonObject;
                        mHandler.sendMessage(msg);


                    } else if (code == 1) {
                        ToastUtils.show(mContext, "没有收藏的职位");
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
                try {
                    if (null != volleyError.networkResponse.data) {
                        Log.v("MYCOLLECT:", new String(volleyError.networkResponse.data));
                    }
                    ToastUtils.show(mContext, volleyError.networkResponse.statusCode + "");

                } catch (Exception e) {
                    ToastUtils.show(mContext, "联网失败");
                }

            }
        };

        Map<String, String> param = new HashMap<String, String>();
        param.put("page", page + "");
        param.put("limit", pageSize + "");
//        param.put("uid", 141 + "");//UserID
        param.put("uid", userId);
        RequestUtils.createRequest(mContext, "", Urls.METHOD_JOB_LIST_COLLECT, false, param, true, listener, errorListener);

    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.lin_back:
                goBack();

                break;
            case R.id.btn_collectjob_delete://删除职位

                Iterator<JobInfo> it = jobInfos.iterator();
                while (it.hasNext()) {
                    JobInfo jobinfo = it.next();
                    if (jobinfo.isChecked()) {
                        sb.append(jobinfo.getJob_Id());
                        sb.append(",");
                        it.remove();
                    }
                }
                deletePositin();

                break;
            case R.id.btn_alljob_shenqing://投递selectSize条职位，其中已投递条数需要从服务器获取
                //申请职位
//                JobApplyDialogUtil.isApplyJob(mContext, selectSize, 2);
                postJob();
                break;
        }
    }

    //删除职位
    private void deletePositin() {
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                dialog.dismiss();
                try {
                    Log.v("HHH:", (String) o);

                    JSONObject jsonObject = new JSONObject((String) o);

                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if (code == 0) {
                        mHandler.sendEmptyMessage(101);

                    } else {
                        ToastUtils.show(mContext, "删除失败");
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
                try {
                    if (null != volleyError.networkResponse.data) {
                        Log.v("MYCOLLECTDELETE:", new String(volleyError.networkResponse.data));
                    }
                    ToastUtils.show(mContext, volleyError.networkResponse.statusCode + "");

                } catch (Exception e) {
                    ToastUtils.show(mContext, "联网失败");
                }

            }
        };

        Map<String, String> param = new HashMap<String, String>();

        String ss = sb.toString();

        param.put("id", ss);
        param.put("uid", 141 + "");
        RequestUtils.createRequest(mContext, "", Urls.METHOD_JOB_LIST_COLLECT_DELETE, false, param, true, listener, errorListener);

    }


    private void goBack() {
        Intent intent = new Intent(MyCollectActivity.this, MainActivity.class);
        intent.putExtra("MeFragment", "MeFragment");
        startActivity(intent);
        MyCollectActivity.this.finish();
    }


    //申请职位
    public void postJob() {


        StringBuilder sb = new StringBuilder();//选择的职位
        for (JobInfo jobInfo : jobInfos) {
            System.out.println(jobInfo.getJob_Id() + "======" + jobInfo.isChecked());
            if (jobInfo.isChecked()) {
                sb.append(jobInfo.getJob_Id());
                sb.append(",");
            }
        }

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                dialog.dismiss();
                try {
                    Log.v(TAG, (String) o);

                    JSONObject jsonObject = new JSONObject((String) o);

                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if (code == 0) {//申请成功

                        int sucNum = Integer.valueOf(jsonObject.getString("data"));
                        postednum = selectSize - sucNum;

                        JobApplyDialogUtil.isApplyJob(mContext, selectSize, postednum);

                    } else if (code == 1) {//请先创建简历
                        ToastUtils.show(mContext, "请先创建简历");

                    } else if (code == 2) {//不能重复申请

                        ToastUtils.show(mContext, "你选的职位已申请过，一周内不能重复申请");
                    } else {
                        ToastUtils.show(mContext, "申请失败");
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
                try {
                    if (null != volleyError.networkResponse.data) {
                        Log.v("jobPost:", new String(volleyError.networkResponse.data));
                    }
                    ToastUtils.show(mContext, volleyError.networkResponse.statusCode + "");
                } catch (Exception e) {
                    ToastUtils.show(mContext, "联网失败");
                }

            }
        };

        String jobIds = sb.toString();
        Map<String, String> param = new HashMap<String, String>();
//        param.put("uid", 6 + "");//UserID       userId
        param.put("uid", userId + "");//UserID       userId
        param.put("job_id", jobIds);
        RequestUtils.createRequest(mContext, "", Urls.METHOD_JOB_POST, false, param, true, listener, errorListener);

    }


    //监听返回按钮
    @Override
    public void onBackPressed() {
        goBack();
    }
}
