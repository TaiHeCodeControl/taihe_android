package com.taihe.eggshell.main;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.APKUtils;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.base.utils.UpdateHelper;
import com.taihe.eggshell.main.entity.User;
import com.taihe.eggshell.personalCenter.activity.AboutActivity;
import com.taihe.eggshell.personalCenter.activity.FeedbackActivity;
import com.taihe.eggshell.personalCenter.activity.TeamActivity;
import com.taihe.eggshell.widget.ChoiceDialog;
import com.taihe.eggshell.widget.ProgressDialog;
import com.taihe.eggshell.widget.SlideSwitch;
import com.taihe.eggshell.widget.UpdateDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2016/2/17.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout rl_mine_checkupdate, rl_mine_feedback,rl_about, rl_hezuo, rl_logout;
    private TextView tv_version;
    private UpdateDialog updateDialog;
    private ChoiceDialog dialog;
    private SlideSwitch slideSwitch;

    private User user;

    @Override
    public void initView() {

        setContentView(R.layout.activity_setting);
        super.initView();

        slideSwitch = (SlideSwitch) findViewById(R.id.id_is_open);

        rl_about = (RelativeLayout) findViewById(R.id.rl_mine_about);
        rl_hezuo = (RelativeLayout) findViewById(R.id.rl_mine_hezuoqudao);
        rl_logout = (RelativeLayout) findViewById(R.id.rl_mine_logout);
        rl_mine_feedback = (RelativeLayout) findViewById(R.id.rl_mine_feedback);
        rl_mine_checkupdate = (RelativeLayout) findViewById(R.id.rl_mine_checkupdate);
        tv_version = (TextView) findViewById(R.id.tv_mine_version);
        tv_version.setText("当前版本V" + APKUtils.getVersionName());
    }

    @Override
    public void initData() {
        super.initData();

        initTitle("设置");
        user = EggshellApplication.getApplication().getUser();

        if(null == user){
            rl_logout.setVisibility(View.GONE);
        }else{
            rl_logout.setVisibility(View.VISIBLE);
        }

        rl_about.setOnClickListener(this);
        rl_hezuo.setOnClickListener(this);
        rl_logout.setOnClickListener(this);
        rl_mine_checkupdate.setOnClickListener(this);
        rl_mine_feedback.setOnClickListener(this);

        dialog = new ChoiceDialog(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ToastUtils.show(mContext, "取消");
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                logout();//退出登录
            }
        });

        dialog.getTitleText().setText("确定退出当前账号吗？");
        dialog.getLeftButton().setText("以后再说");
        dialog.getRightButton().setText("确认退出");

        if(null!=user){
            slideSwitch.setSlideListener(new SlideSwitch.SlideListener() {
                @Override
                public void open() {
                    ToastUtils.show(mContext,"已开启");
                    openOrCloseMsgPush(1);
                }

                @Override
                public void close() {
                    ToastUtils.show(mContext,"已关闭");
                    openOrCloseMsgPush(2);
                }
            });
        }else{
            slideSwitch.setSlideable(false);
            slideSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtils.show(mContext,"请先登录");
                }
            });
        }

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()){
            case R.id.rl_mine_about://关于蛋壳儿
                intent = new Intent(mContext, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_mine_hezuoqudao://合作渠道
                intent = new Intent(mContext, TeamActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_mine_logout://退出登录
                dialog.show();
                break;
            case R.id.rl_mine_feedback:
                intent = new Intent(mContext, FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_mine_checkupdate://检查更新
                getVersionCode();
                break;
        }
    }

    private void openOrCloseMsgPush(int status){

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                Log.v("MSG:",(String)o);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        };

        Map<String,String> params = new HashMap<String,String>();
        params.put("uid",user.getId()+"");
        params.put("status",status+"");

        RequestUtils.createRequest(mContext,Urls.getMopHostUrl(),Urls.METHOD_OPEN_CLOSE_MSG,true,params,true,listener,errorListener);

    }

    //获取新版本
    private void getVersionCode() {
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {

//                Log.v(TAG, (String) o);
                try {
                    JSONObject jsonObject = new JSONObject((String) o);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        final String url = jsonObject.getString("data");
                        String title = jsonObject.getString("title");
                        String message = jsonObject.getString("message");
                        Gson gson = new Gson();
                        List<String> meglist = gson.fromJson(message, new TypeToken<List<String>>() {
                        }.getType());

                        updateDialog = new UpdateDialog(mContext, title, meglist, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateDialog.dismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                ToastUtils.show(mContext, "更新");
                                updateDialog.dismiss();
                                updateAPK(url);
                            }
                        });

                        updateDialog.getTitleText().setText("发现新版本");
                        updateDialog.show();
                    } else {
                        ToastUtils.show(mContext, "已是最新版本");
                    }
                } catch (JSONException e) {
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

        Map<String, String> params = new HashMap<String, String>();
        params.put("version", APKUtils.getVersionCode() + "");

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_UPDATE, true, params, true, listener, errorListener);
    }

    //更新APK
    private void updateAPK(String url) {

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.show();

        new UpdateHelper(mContext, new UpdateHelper.DownloadProgress() {
            @Override
            public void progress(int percent) {
                progressDialog.getProgressBar().setProgress(percent);
            }

            @Override
            public void complete() {
                progressDialog.dismiss();
            }

            @Override
            public void error() {
                ToastUtils.show(mContext, "错误");
            }
        }).downloadInBackground(url);
    }


    //退出登录
    private void logout() {

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
//                LoadingDialog.dismiss();
                try {
//                    Log.v(TAG, (String) o);

                    JSONObject jsonObject = new JSONObject((String) o);
                    int code = jsonObject.getInt("code");

                    if (code == 0) {//退出成功

                        ToastUtils.show(mContext, "成功退出");
                        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, PrefUtils.KEY_USER_JSON, "");
                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
//                        ((MainActivity) getActivity()).radio_index.performClick();
                    } else {
                        ToastUtils.show(mContext, "退出失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                LoadingDialog.dismiss();
                ToastUtils.show(mContext, "网络异常");

            }
        };

        Map<String, String> param = new HashMap<String, String>();
        int userId = user.getId();
        param.put("uid", userId + "");
        RequestUtils.createRequest(mContext, Urls.METHOD_REGIST_LOGOUT, "", true, param, true, listener, errorListener);

    }
}
