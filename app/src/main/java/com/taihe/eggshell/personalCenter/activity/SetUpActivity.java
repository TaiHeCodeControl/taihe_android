package com.taihe.eggshell.personalCenter.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.utils.DataCleanManager;
import com.taihe.eggshell.base.utils.HttpsUtils;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.base.utils.UpdateUtils;
import com.taihe.eggshell.login.LoginActivity;
import com.taihe.eggshell.widget.ChoiceDialog;
import com.taihe.eggshell.widget.CustomProgressDialog;
import com.taihe.eggshell.widget.HorizontalProgressBarWithNumber;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thinkpad on 2015/7/14.
 */
public class SetUpActivity extends BaseActivity {

    private static final String TAG = "UserInfoActivity";
    private AlertDialog updateDialog = null;
    private Context mContext;
    private String url;
    private int updateCode;

    private ChoiceDialog dialog;

    private RelativeLayout cacheclear,aboutLayout, helpLayout, feedBackLayout, updateLayout, changePwd;
    private Button btn_logout;

    @Override
    public void initView() {
        setContentView(R.layout.activity_system_setup);
        super.initView();
        overridePendingTransition(R.anim.activity_right_to_center, R.anim.activity_center_to_left);

        mContext = this;
        cacheclear = (RelativeLayout) findViewById(R.id.rl_set_cacheclear);
        aboutLayout = (RelativeLayout) findViewById(R.id.rl_set_about);
        helpLayout = (RelativeLayout) findViewById(R.id.rl_set_help);
        feedBackLayout = (RelativeLayout) findViewById(R.id.rl_set_feedback);
        updateLayout = (RelativeLayout) findViewById(R.id.rl_set_update);

        changePwd = (RelativeLayout) findViewById(R.id.rl_set_changepwd);
        btn_logout = (Button) findViewById(R.id.btn_set_logout);

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
                EggshellApplication.getApplication().setUser(null);
                PrefUtils.saveStringPreferences(getApplicationContext(), PrefUtils.CONFIG, PrefUtils.KEY_USER_JSON, "");

                Intent intent = new Intent(SetUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        dialog.getTitleText().setText("确定退出当前账号吗？");
        dialog.getLeftButton().setText("以后再说");
        dialog.getRightButton().setText("确认退出");
    }

    @Override
    public void initData() {
        super.initData();
        super.initTitle("个人中心");

        cacheclear.setOnClickListener(this);
        aboutLayout.setOnClickListener(this);
        helpLayout.setOnClickListener(this);
        feedBackLayout.setOnClickListener(this);
        updateLayout.setOnClickListener(this);
        changePwd.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.rl_set_cacheclear://清除缓存
                cleanCache();
                //调用系统方法清除缓存
//                Intent intent = new Intent();
//                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//                intent.addCategory(Intent.CATEGORY_DEFAULT);// 有无没影响
//                intent.setData(Uri.parse("package:" + getPackageName()));
//                startActivity(intent);
                break;
            case R.id.rl_set_about://关于我们
                Intent aboutIntent = new Intent(mContext, AboutActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.rl_set_help://使用帮助
                Intent HelpIntent = new Intent(mContext, HelpActivity.class);
                startActivity(HelpIntent);
                break;
            case R.id.rl_set_feedback://意见反馈

                break;
            case R.id.rl_set_update://检查更新
                checkUpdate();
                break;
            case R.id.rl_set_changepwd://更改密码

                break;
            case R.id.btn_set_logout:
                dialog.show();
                break;
        }
    }

    private String mCacheSize;

    /**
     * 获取缓存大小
     */
    private void getCacheSize() {

        try {
           mCacheSize = DataCleanManager.getCacheSize(SetUpActivity.this
                   .getCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 清理缓存
     */
    private void cleanCache() {
        getCacheSize();
        Log.i(TAG,mCacheSize);
        if(mCacheSize.equals("0.0Byte")){
            ToastUtils.show(SetUpActivity.this,"当前缓存为0，无需清理");
        }else{

            DataCleanManager.deleteFilesByDirectory(SetUpActivity.this.getCacheDir());
            ToastUtils.show(SetUpActivity.this,"缓存清理成功");
        }
    }

    // ======================================版本更新-====================================

    private String version_new;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            dismissLoading();
            switch (msg.what) {
                case 0:// 有新版本
                    String url = (String) msg.obj;
                    processUpdate(url);
                    break;
                case 1:// 无新版本
                    String noINfo = (String) msg.obj;
                    ToastUtils.show(getApplicationContext(), noINfo);
                    break;
                case 2:// 访问网络失败
                    ToastUtils.show(getApplicationContext(), "访问网络失败");
                    break;

                default:
                    break;
            }
        };
    };

    /**
     * 检查更新
     */
    private void checkUpdate() {
        // 加载动画
        initLoading();
        new Thread() {

            public void run() {
                String version_old = UpdateUtils
                        .getVersion(getApplicationContext());
                String system = "android";
                Map<String, String> map = new HashMap<String, String>();
                map.put("version", version_old);
                map.put("system", system);

//                Gson gson = new Gson();
//                // 将对象转换为JSON数据
//                String params = gson.toJson(map);

                String params = UpdateUtils.getJsonStr(map);

                String result = HttpsUtils.sendPost("http://app.zhikubao.net:8080/api/VersionApi/VersionPost", params);

                if (TextUtils.isEmpty(result)) {
                    handler.sendEmptyMessage(2);
                } else {
                    try {
                        JSONObject obj = new JSONObject(result);
                        String status = obj.optString("status");

                        if ("0".equals(status)) {
                            version_new = obj.optString("version");
                            String url = obj.optString("url");
                            Log.i(TAG,url);
                            Log.i(TAG,status);
                            Log.i(TAG,result);
                            Message msg = Message.obtain();
                            msg.what = 0;
                            msg.obj = url;
                            handler.sendMessage(msg);
                        } else {
                            // 访问失败
                            String fInfo = obj.optString("msg");
                            Message msg = Message.obtain();
                            msg.what = 1;
                            msg.obj = fInfo;
                            handler.sendMessage(msg);
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, "", e);
                    }
                }
            };
        }.start();

    }



    /**
     * 解析更新数据
     *
     * @param
     */
    private void processUpdate(final String downloadUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetUpActivity.this);

        View view = View.inflate(SetUpActivity.this, R.layout.dialog_custom,
                null);

        final TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        final HorizontalProgressBarWithNumber pb_update = (HorizontalProgressBarWithNumber) view.findViewById(R.id.pb_dialog_update);
        final TextView tv_qx = (TextView) view.findViewById(R.id.tv_cancel);
        final TextView tv_qd = (TextView) view.findViewById(R.id.tv_ok);

        tv_msg.setText("发现新版本, 当前版本V"
                + UpdateUtils.getVersion(SetUpActivity.this) + ", 新版本V"
                + version_new + ", 是否立即更新？");

        tv_qx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateDialog.dismiss();
            }
        });
        tv_qd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 下载新版本
//                downloadAPK(downloadUrl, pb_update,tv_msg, tv_qd, version_new);

                //显示进度条

                pb_update.setVisibility(View.VISIBLE);
                int pross = pb_update.getProgress();

                    pb_update.setProgress(pross++);
                    tv_msg.setText("当前下载进度："
                            + pross);

            }
        });

        builder.setView(view);
        updateDialog = builder.create();
        updateDialog.show();

    }

    /**
     * 下载更新包
     * @param
     * @param tv_msg
     * @param tv_qd
     * @param nVersion 服务器返回的新版本的版本号
     */
    private void downloadAPK(final String url,final ProgressBar pb_update, final TextView tv_msg,
                             final TextView tv_qd, final String nVersion) {

        new Thread() {
            public void run() {
                com.lidroid.xutils.HttpUtils downutils = new com.lidroid.xutils.HttpUtils();
                String apkname = "eggker_" + nVersion;
                final String apkpath = Environment
                        .getExternalStorageDirectory() + "/" + apkname;
                downutils.download(url, apkpath, new RequestCallBack<File>() {

                    public void onStart() {
                        // 开始下载
                        tv_qd.setVisibility(View.INVISIBLE);
                    };

                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                        //显示进度条
                        pb_update.setVisibility(View.VISIBLE);
                        Long progress = current * 100 / total;
                        String prs = progress.toString();
                        int pross = Integer.parseInt(prs);
                        pb_update.setProgress(pross);
                        // 下载进度：current/total
                        tv_msg.setText("当前下载进度："
                                + DataCleanManager
                                .getFormatSize((double) current)
                                + "/"
                                + DataCleanManager
                                .getFormatSize((double) total));
                    };

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        // 下载完成
                        tv_qd.setVisibility(View.VISIBLE);
                        tv_qd.setText("立即安装");
                        tv_qd.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // 安装APK
                                installAPK(apkpath);
                            }
                        });
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        // 下载失败
                        handler.sendEmptyMessage(2);
                    }
                });
            };
        }.start();

    }

    /**
     * 安装下载的更新包
     */
    private void installAPK(String apkpatch) {
        if (updateDialog.isShowing()) {
            updateDialog.dismiss();
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(apkpatch);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    // =========================加载动画========================================
    private CustomProgressDialog loading;

    /**
     * 加载动画
     */
    private void initLoading() {
        if (loading == null) {
            loading = new CustomProgressDialog(this, getResources().getString(
                    R.string.submitcertificate_string_wait_dialog));
        }
        loading.show();
    }

    /**
     * 取消加载
     */
    private void dismissLoading() {
        if (loading != null) {
            loading.dismiss();
            loading = null;
        }
    }
}
