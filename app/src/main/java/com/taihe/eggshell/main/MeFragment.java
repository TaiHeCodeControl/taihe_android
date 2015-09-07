package com.taihe.eggshell.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.RequestQueue;
import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.network.http.toolbox.ImageLoader;
import com.chinaway.framework.swordfish.network.http.toolbox.Volley;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.APKUtils;
import com.taihe.eggshell.base.utils.BitmapCache;
import com.taihe.eggshell.base.utils.GsonUtils;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.base.utils.UpdateHelper;
import com.taihe.eggshell.base.utils.UpdateUtils;
import com.taihe.eggshell.job.activity.MyCollectActivity;
import com.taihe.eggshell.job.bean.JobDetailInfo;
import com.taihe.eggshell.job.bean.JobInfo;
import com.taihe.eggshell.login.LoginActivity;
import com.taihe.eggshell.main.entity.User;
import com.taihe.eggshell.personalCenter.activity.TeamActivity;
import com.taihe.eggshell.personalCenter.activity.AboutActivity;
import com.taihe.eggshell.job.activity.MyPostActivity;
import com.taihe.eggshell.personalCenter.activity.FeedbackActivity;
import com.taihe.eggshell.personalCenter.activity.MyBasicActivity;
import com.taihe.eggshell.resume.ResumeManagerActivity;
import com.taihe.eggshell.widget.ChoiceDialog;
import com.taihe.eggshell.widget.CircleImageView;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.taihe.eggshell.widget.ProgressDialog;
import com.taihe.eggshell.widget.UpdateDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的界面
 */
public class MeFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "MeFragment";
    private Context mContext;

    private ChoiceDialog dialog;
    private UpdateDialog updateDialog;

    private View rootView;
    private RelativeLayout rl_mine_checkupdate, rl_mine_feedback, rl_setting, rl_editZiliao, rl_post, rl_collect, rl_jianli, rl_about, rl_hezuo, rl_logout;
    private TextView tv_logintxt, tv_version, tv_username, tv_qianming, tv_postNum, tv_collectNum, jianliNum;
    private LinearLayout ll_userinfo;

    private CircleImageView circleiv_mine_icon;
    private Intent intent;
    private User user;
    private int UserId;
    private LoadingProgressDialog LoadingDialog;

    // Image-Load配置
    private RequestQueue mQueue;
    private ImageLoader imageLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();
        rootView = inflater.inflate(R.layout.fragment_me, null);
        // 初始化Image-load
        initImageLoad();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        circleiv_mine_icon = (CircleImageView) rootView.findViewById(R.id.circleiv_mine_icon);
        circleiv_mine_icon.setOnClickListener(this);

        rl_setting = (RelativeLayout) rootView.findViewById(R.id.rl_mine_setting);
        rl_editZiliao = (RelativeLayout) rootView.findViewById(R.id.rl_mine_editziliao);
        rl_post = (RelativeLayout) rootView.findViewById(R.id.rl_mine_postposition);
        rl_collect = (RelativeLayout) rootView.findViewById(R.id.rl_mine_collectpostion);
        rl_jianli = (RelativeLayout) rootView.findViewById(R.id.rl_mine_jianliguanli);
        rl_about = (RelativeLayout) rootView.findViewById(R.id.rl_mine_about);
        rl_hezuo = (RelativeLayout) rootView.findViewById(R.id.rl_mine_hezuoqudao);
        rl_logout = (RelativeLayout) rootView.findViewById(R.id.rl_mine_logout);
        rl_mine_feedback = (RelativeLayout) rootView.findViewById(R.id.rl_mine_feedback);
        rl_mine_checkupdate = (RelativeLayout) rootView.findViewById(R.id.rl_mine_checkupdate);

        tv_version = (TextView) rootView.findViewById(R.id.tv_mine_version);
        tv_version.setText("当前版本V" + APKUtils.getVersionName());

        ll_userinfo = (LinearLayout) rootView.findViewById(R.id.ll_mine_userinfo);
        tv_logintxt = (TextView) rootView.findViewById(R.id.tv_mine_logintxt);

        tv_username = (TextView) rootView.findViewById(R.id.tv_mine_username);


        rl_mine_checkupdate.setOnClickListener(this);
        rl_mine_feedback.setOnClickListener(this);
        tv_logintxt.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        rl_editZiliao.setOnClickListener(this);
        rl_post.setOnClickListener(this);
        rl_collect.setOnClickListener(this);
        rl_jianli.setOnClickListener(this);
        rl_about.setOnClickListener(this);
        rl_hezuo.setOnClickListener(this);
        rl_logout.setOnClickListener(this);


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
    }





    private void initImageLoad() {
        mQueue = Volley.newRequestQueue(mContext);
        imageLoader = new ImageLoader(mQueue, new BitmapCache());
    }

    private void initView() {

        //初始化选择图片popWindow
        initImageSelect();
        user = EggshellApplication.getApplication().getUser();

        if (null == user) {

            tv_logintxt.setVisibility(View.VISIBLE);
            ll_userinfo.setVisibility(View.GONE);

            rl_logout.setVisibility(View.GONE);
        } else {

            // 加载头像
            if (user.getImage() != null) {
                imageLoader.get(user.getImage(), ImageLoader.getImageListener(
                        circleiv_mine_icon, R.drawable.touxiang,
                        R.drawable.touxiang));
            }

            LoadingDialog = new LoadingProgressDialog(mContext, getResources().getString(
                    R.string.submitcertificate_string_wait_dialog));


            String phoneNum = user.getPhoneNumber();
            Log.i("PHONeNUM", phoneNum);
            String nick = user.getName();
            tv_logintxt.setVisibility(View.GONE);
            ll_userinfo.setVisibility(View.VISIBLE);

            rl_logout.setVisibility(View.VISIBLE);
            tv_username.setText(phoneNum);
            //
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mine_logintxt://登录

                EggshellApplication.getApplication().setLoginTag("meFragment");
                intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_mine_editziliao://基本资料
//                LoginConfirmDialog.dialogShow(mContext);
                if (null != EggshellApplication.getApplication().getUser()) {
                    intent = new Intent(mContext, MyBasicActivity.class);
                    startActivity(intent);
                } else {
                    EggshellApplication.getApplication().setLoginTag("myBasic");
                    intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.rl_mine_postposition://我的投递
                if (null != EggshellApplication.getApplication().getUser()) {
                    intent = new Intent(mContext, MyPostActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(mContext, LoginActivity.class);
                    EggshellApplication.getApplication().setLoginTag("myPost");
                    startActivity(intent);
                }

                break;

            case R.id.rl_mine_collectpostion://我的收藏
                if (null != EggshellApplication.getApplication().getUser()) {
                    intent = new Intent(mContext, MyCollectActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(mContext, LoginActivity.class);
                    EggshellApplication.getApplication().setLoginTag("myCollect");
                    startActivity(intent);
                }

                break;

            case R.id.rl_mine_jianliguanli://简历管理
                if (null != EggshellApplication.getApplication().getUser()) {
                    intent = new Intent(mContext, ResumeManagerActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(mContext, LoginActivity.class);
                    EggshellApplication.getApplication().setLoginTag("myResume");
                    startActivity(intent);
                }
                break;
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
//            case R.id.rl_mine_setting:
//                intent = new Intent(mContext, SetUpActivity.class);
//                startActivity(intent);
//                break;

            case R.id.rl_mine_feedback:
                intent = new Intent(mContext, FeedbackActivity.class);
                startActivity(intent);
                break;

            case R.id.rl_mine_checkupdate://检查更新
//                getVersionCode();
                ToastUtils.show(mContext, "当前已是最新版本");
                break;

            case R.id.circleiv_mine_icon:
                //判断登录状态，
                if (null == user) {//登录
                    intent = new Intent(mContext, LoginActivity.class);
                    intent.putExtra("LoginTag", "meFragment");
                    startActivity(intent);
                } else {
                    UserId = EggshellApplication.getApplication().getUser().getId();
                    showCameraPopWindow();
                }


                break;

            // 以下是修改头像中的点击事件
            case R.id.tv_camera:

                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                        Environment.getExternalStorageDirectory(), "temp.jpg")));
                startActivityForResult(intent, PHOTOHRAPH);

                camera_pop_window.dismiss();
                break;

            case R.id.tv_album:
                intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_UNSPECIFIED);

                startActivityForResult(intent, PHOTOZOOM);

                camera_pop_window.dismiss();

            case R.id.tv_cancel:
                camera_pop_window.dismiss();
                break;
        }
    }

    private void getVersionCode() {
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {

                updateDialog = new UpdateDialog(mContext, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.show(mContext, "更新");
                        updateDialog.dismiss();
//                        updateAPK();
                    }
                });

                updateDialog.getTitleText().setText("发现新版本" + APKUtils.getVersionName());
                updateDialog.show();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        };

        Map<String, String> params = new HashMap<String, String>();
        params.put("vercode", APKUtils.getVersionCode() + "");

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), "method", true, params, true, listener, errorListener);
    }

    private void updateAPK() {

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
        }).downloadInBackground("");
    }


    @Override
    public void onResume() {
        super.onResume();
        initView();
    }


    // ================================以下是修改头像代码============================

    // 修改头像所需参数数据
    // -----选择图片-------
    private PopupWindow camera_pop_window;
    private View camera_pop_view;
    private LayoutInflater mInflater;
    private Context context;
    private TextView tv_camera, tv_album, tv_cancel;

    private void initImageSelect() {
        // -选择图片------
        mInflater = LayoutInflater.from(mContext);
        camera_pop_view = mInflater.inflate(R.layout.camera_option_pop, null);
        tv_camera = (TextView) camera_pop_view.findViewById(R.id.tv_camera);
        tv_album = (TextView) camera_pop_view.findViewById(R.id.tv_album);
        tv_cancel = (TextView) camera_pop_view.findViewById(R.id.tv_cancel);
        tv_camera.setOnClickListener(this);
        tv_album.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

    }

    // -------------------弹出选择图片对话框--------------------------------

    private void showCameraPopWindow() {
        if (camera_pop_window == null) {
            initCameraPopWindow();
        }
        if (camera_pop_window.isShowing()) {
            camera_pop_window.dismiss();
        } else {
            camera_pop_window.showAtLocation(camera_pop_view, Gravity.BOTTOM,
                    0, 0);
            camera_pop_window.update();
        }
        backgroundAlpha(0.5f);

        //添加pop窗口关闭事件
        camera_pop_window.setOnDismissListener(new poponDismissListener());
    }

    //选择图片Pop
    private void initCameraPopWindow() {
        camera_pop_window = new PopupWindow(camera_pop_view,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        camera_pop_window.setAnimationStyle(R.style.pop_ani);
        ColorDrawable c = new ColorDrawable();
        camera_pop_window.setBackgroundDrawable(c);
        camera_pop_window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

            }
        });


    }


    /**
     * 弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            //Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    //
    private String uploadFile = Environment.getDataDirectory()
            + "/data/com.taihe.eggshell/temp.jpg";
    private PopupWindow pw;
    private Bitmap lastPhoto = null;
    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private FileInputStream fStream;
    private String fString;

    /**
     * ********************* 从相机或者本地选图片到ImageView的method *********
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == NONE)

            return;

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

                //门店图片显示==================================================
                circleiv_mine_icon.setImageBitmap(lastPhoto);
                if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
                    LoadingDialog = new LoadingProgressDialog(mContext, getResources().getString(
                            R.string.submitcertificate_string_wait_dialog));
                    dialog.show();
                    String ImageString = getPstr(filePath);
                    //上传门店图片
                    upLoadImage(ImageString);
                } else {
                    ToastUtils.show(mContext, R.string.check_network);
                }


            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //上传头像
    private void upLoadImage(String ImageString) {

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                LoadingDialog.dismiss();
                try {
                    Log.v("HHH:", (String) o);

                    JSONObject jsonObject = new JSONObject((String) o);

                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if (code == 0) {//图片上传成功
                        ToastUtils.show(mContext, "头像上传成功");
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
                LoadingDialog.dismiss();
                try {
                    if (null != volleyError.networkResponse.data) {
                        Log.v("Image:", new String(volleyError.networkResponse.data));
                    }
                    ToastUtils.show(mContext, volleyError.networkResponse.statusCode + "");

                } catch (Exception e) {
                    ToastUtils.show(mContext, "联网失败");
                }

            }
        };

        Map<String, String> param = new HashMap<String, String>();
        param.put("uid", UserId + "");
        param.put("photo", ImageString);
        //http://localhost/eggker/interface/basicdata/head  比传参数  uid =>uid   photo=>photo
        RequestUtils.createRequest(mContext, Urls.METHOD_UPLOAD_IMAGE, "", true, param, true, listener, errorListener);

    }

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




    //退出登录
    private void logout() {

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
//                LoadingDialog.dismiss();
                try {
                    Log.v(TAG, (String) o);

                    JSONObject jsonObject = new JSONObject((String) o);
                    int code = jsonObject.getInt("code");
                    System.out.println("code=========" + code);

                    if (code == 0) {//退出成功

                        ToastUtils.show(mContext,"成功退出");
                        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, PrefUtils.KEY_USER_JSON, "");
                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        ((MainActivity) getActivity()).radio_index.performClick();
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
                try {
                    if (null != volleyError.networkResponse.data) {
                        Log.v("LogOut:", new String(volleyError.networkResponse.data));
                    }
                    ToastUtils.show(mContext, volleyError.networkResponse.statusCode + "");
                } catch (Exception e) {
                    ToastUtils.show(mContext, "联网失败");
                }

            }
        };

        Map<String, String> param = new HashMap<String, String>();
        int userId = user.getId();
        param.put("uid", userId + "");
        RequestUtils.createRequest(mContext, Urls.METHOD_REGIST_LOGOUT, "", true, param, true, listener, errorListener);

    }
}