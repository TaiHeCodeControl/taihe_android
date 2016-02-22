package com.taihe.eggshell.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.taihe.eggshell.base.utils.BitmapCache;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.activity.MyPostActivity;
import com.taihe.eggshell.login.LoginActivity;
import com.taihe.eggshell.main.entity.User;
import com.taihe.eggshell.meetinginfo.DiscussListActivity;
import com.taihe.eggshell.personalCenter.activity.MyBasicActivity;
import com.taihe.eggshell.personalCenter.activity.MyJoinActivity;
import com.taihe.eggshell.personalCenter.entity.VisitedPerson;
import com.taihe.eggshell.resume.ResumeManagerActivity;
import com.taihe.eggshell.widget.CircleImageView;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.taihe.eggshell.widget.cityselect.CitySelectActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的界面
 */
public class MeFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "MeFragment";
    private Context mContext;

    private View rootView;
    private RelativeLayout sign_up_activity,collection_activity, rl_editZiliao, rl_post, rl_collect, rl_jianli,discuss_activity,invisit_activity;
    private TextView tv_logintxt, tv_username, tv_qianming, tv_postNum, tv_collectNum, jianliNum,notificationNum;
    private LinearLayout ll_userinfo;
    private TextView tv_mine_postnum, tv_mine_collectnum, tv_mine_jianlinum;
    private ImageView settingImage;

    private CircleImageView circleiv_mine_icon;
    private Intent intent;
    private User user;
    private int UserId;
    private LoadingProgressDialog uploadImageDialog;
    private ArrayList<VisitedPerson> visitedPersonArrayList = new ArrayList<VisitedPerson>();

    // Image-Load配置
    private RequestQueue mQueue;
    private ImageLoader imageLoader;

    //expect 简历条数   favjob 投递职位条数  usejob收藏职位条数   resume_photo头像
    private String postNum = "";
    private String collectNum = "";
    private String resumeNun = "";
    private String nick = "";
    private String qianming = "";
    private String userImagePath = "";

    private int userId;
    private String token = "";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what==111){//消息个数变化
                if(0==(int)msg.obj){
                    notificationNum.setVisibility(View.GONE);
                }else{
                    notificationNum.setText(((int)msg.obj)+"");
                    notificationNum.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ((MainActivity)context).setUnReadHandler(handler);
    }

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

        rl_editZiliao = (RelativeLayout) rootView.findViewById(R.id.rl_mine_editziliao);
        rl_post = (RelativeLayout) rootView.findViewById(R.id.rl_mine_postposition);
        rl_collect = (RelativeLayout) rootView.findViewById(R.id.rl_mine_collectpostion);
        rl_jianli = (RelativeLayout) rootView.findViewById(R.id.rl_mine_jianliguanli);
        sign_up_activity = (RelativeLayout) rootView.findViewById(R.id.id_sign_up_activity);
        collection_activity = (RelativeLayout) rootView.findViewById(R.id.id_collection_activity);
        discuss_activity = (RelativeLayout) rootView.findViewById(R.id.id_discuss_activity);
        invisit_activity = (RelativeLayout) rootView.findViewById(R.id.id_invite_activity);

        ll_userinfo = (LinearLayout) rootView.findViewById(R.id.ll_mine_userinfo);
        tv_logintxt = (TextView) rootView.findViewById(R.id.tv_mine_logintxt);
        tv_username = (TextView) rootView.findViewById(R.id.tv_mine_username);
        tv_qianming = (TextView) rootView.findViewById(R.id.tv_mine_qianming);
        tv_mine_postnum = (TextView) rootView.findViewById(R.id.tv_mine_postnum);
        tv_mine_collectnum = (TextView) rootView.findViewById(R.id.tv_mine_collectnum);
        tv_mine_jianlinum = (TextView) rootView.findViewById(R.id.tv_mine_jianlinum);
        settingImage = (ImageView) rootView.findViewById(R.id.id_setting);
        notificationNum = (TextView) rootView.findViewById(R.id.id_discuss_num);

        settingImage.setOnClickListener(this);
        invisit_activity.setOnClickListener(this);
        discuss_activity.setOnClickListener(this);
        sign_up_activity.setOnClickListener(this);
        collection_activity.setOnClickListener(this);
        tv_logintxt.setOnClickListener(this);
        rl_editZiliao.setOnClickListener(this);
        rl_post.setOnClickListener(this);
        rl_collect.setOnClickListener(this);
        rl_jianli.setOnClickListener(this);

    }

    public void setHandles(String str){
        getBasic();
    }

    //初始化ImageLoad(volley)
    private void initImageLoad() {
        mQueue = Volley.newRequestQueue(mContext);
        imageLoader = new ImageLoader(mQueue, new BitmapCache());
    }

    private void initView() {

        //初始化选择图片popWindow
        initImageSelect();
        user = EggshellApplication.getApplication().getUser();

        if (null == user) {

            //退出登录，清空用户头像，职位个数等信息
            userImagePath = "";
            imageLoader.get(userImagePath, ImageLoader.getImageListener(
                    circleiv_mine_icon, R.drawable.touxiang,
                    R.drawable.touxiang));
            tv_mine_postnum.setText("(" + 0 + ")");
            tv_mine_collectnum.setText("(" + 0 + ")");
            tv_mine_jianlinum.setText("(" + 0 + ")");

            tv_logintxt.setVisibility(View.VISIBLE);
            ll_userinfo.setVisibility(View.GONE);

        } else {
            userId = user.getId();
            token = user.getToken();
            if (NetWorkDetectionUtils.checkNetworkAvailable(mContext) && null!=EggshellApplication.getApplication().getUser()) {
                getBasic();//获取用户基本信息，投递职位个数，简历个数，头像等
            } else {
                ToastUtils.show(mContext, R.string.check_network);
            }

            tv_logintxt.setVisibility(View.GONE);
            ll_userinfo.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser && getUserVisibleHint()){
            if (NetWorkDetectionUtils.checkNetworkAvailable(mContext) && null!=EggshellApplication.getApplication().getUser()) {
                getBasic();//获取用户基本信息，投递职位个数，简历个数，头像等
                getVisitedPerson();//获取邀请人列表
            }

            if(0==MainActivity.unnum){
                notificationNum.setVisibility(View.GONE);
            }else{
                notificationNum.setText(MainActivity.unnum+"");
                notificationNum.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    //获取用户基本信息，投递职位个数，简历个数，头像等
    private void getBasic() {

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                try {
//                    Log.v(TAG, (String) o);

                    JSONObject jsonObject = new JSONObject((String) o);
                    int code = jsonObject.getInt("code");

                    if (code == 0) {//
                        // expect 简历条数   favjob 投递职位条数  usejob收藏职位条数   resume_photo头像
                        JSONObject data = jsonObject.getJSONObject("data");
                        postNum = data.optString("favjob");
                        collectNum = data.optString("usejob");
                        resumeNun = data.optString("expect");
                        nick = data.optString("name");
                        qianming = data.optString("description");
                        userImagePath = data.optString("resume_photo");
                        // 加载头像
                        imageLoader.get(userImagePath, ImageLoader.getImageListener(
                                circleiv_mine_icon, R.drawable.touxiang,
                                R.drawable.touxiang));
                        //手机号
                        String phoneNum = user.getPhoneNumber();

                        if (!TextUtils.isEmpty(nick)) {//昵称
                            tv_username.setText(nick);
                        } else {
                            tv_username.setText(phoneNum);
                        }
                        if (TextUtils.isEmpty(qianming)) {
                            tv_qianming.setText("个性签名");
                        } else {
                            tv_qianming.setText(qianming);
                        }
                        if (TextUtils.isEmpty(postNum)) {
                            tv_mine_postnum.setText("(" + 0 + ")");
                        } else {
                            tv_mine_postnum.setText("(" + postNum + ")");
                        }

                        if (TextUtils.isEmpty(collectNum)) {
                            tv_mine_collectnum.setText("(" + 0 + ")");
                        } else {
                            tv_mine_collectnum.setText("(" + collectNum + ")");
                        }

                        if (TextUtils.isEmpty(resumeNun) || resumeNun.equals("0")) {
                            tv_mine_jianlinum.setText("(" + 0 + ")");
                        } else {
                            tv_mine_jianlinum.setText("(" + resumeNun + ")");
                        }

                    } else {
//                        ToastUtils.show(mContext, "访问失败");
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

        param.put("uid", userId + "");
        param.put("token", token);
        RequestUtils.createRequest(mContext, Urls.METHOD_MINE_BASIC, "", true, param, true, listener, errorListener);

    }

    private void getVisitedPerson(){
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {

//                Log.v("ssss:", (String) o);

                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    Gson gson = new Gson();
                    if(code == 0){
                        String data = jsonObject.getString("data");
                        if(!data.equals("[]")){
                            visitedPersonArrayList.clear();
                            List<VisitedPerson> list = gson.fromJson(data,new TypeToken<List<VisitedPerson>>(){}.getType());
                            visitedPersonArrayList.addAll(list);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        };

        Map<String,String> params = new HashMap<String,String>();
        params.put("uid", EggshellApplication.getApplication().getUser().getId()+"");

//        Log.v("PARALGET:",params.toString());
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(),Urls.METHOD_GET_VISITED_PERSON,true,params,true,listener,errorListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_setting:
                intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);
                break;
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
                    intent.putExtra("type",1);
                    startActivity(intent);
                } else {
                    intent = new Intent(mContext, LoginActivity.class);
                    EggshellApplication.getApplication().setLoginTag("myPost");
                    startActivity(intent);
                }
                break;
            case R.id.rl_mine_collectpostion://我的收藏
                if (null != EggshellApplication.getApplication().getUser()) {
                    intent = new Intent(mContext, MyPostActivity.class);
                    intent.putExtra("type",2);
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
                getActivity().startActivityForResult(intent, PHOTOHRAPH);

                camera_pop_window.dismiss();
                break;
            case R.id.tv_album:
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                getActivity().startActivityForResult(intent, PHOTOZOOM);
                camera_pop_window.dismiss();

            case R.id.tv_cancel:
                camera_pop_window.dismiss();
                break;
            case R.id.id_sign_up_activity:
                if (null != EggshellApplication.getApplication().getUser()) {
                    intent = new Intent(mContext, MyJoinActivity.class);
                    intent.putExtra("activity_type","已报名活动");
                    intent.putExtra("type","joined");
                    startActivity(intent);
                }else {
                    intent = new Intent(mContext, LoginActivity.class);
                    EggshellApplication.getApplication().setLoginTag("");
                    startActivity(intent);
                }

                break;
            case R.id.id_collection_activity:
                if (null != EggshellApplication.getApplication().getUser()) {
                    intent = new Intent(mContext, MyJoinActivity.class);
                    intent.putExtra("activity_type","已收藏活动");
                    intent.putExtra("type","collected");
                    startActivity(intent);
                }else {
                    intent = new Intent(mContext, LoginActivity.class);
                    EggshellApplication.getApplication().setLoginTag("");
                    startActivity(intent);
                }

                break;
            case R.id.id_discuss_activity://评论回复消息
                if (null != EggshellApplication.getApplication().getUser()) {
                    intent = new Intent(mContext, DiscussListActivity.class);
                    startActivity(intent);
                } else {
                    EggshellApplication.getApplication().setLoginTag("");
                    intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.id_invite_activity://邀请好友
                if (null != EggshellApplication.getApplication().getUser()) {
                    intent = new Intent(mContext, CitySelectActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("visitedPerson", visitedPersonArrayList);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    EggshellApplication.getApplication().setLoginTag("");
                    intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
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
    private static final int NONE = 0;
    private static final int PHOTOHRAPH = 1;// 拍照
    private static final int PHOTOZOOM = 2; // 缩放
    private static final int PHOTORESOULT = 3;// 结果
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private FileInputStream fStream;
    private String fString;

    /**
     * ********************* 从相机或者本地选图片到ImageView的method *********
     */
    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == NONE){
            return;
        }

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
//                circleiv_mine_icon.setImageBitmap(lastPhoto);

                if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
                    uploadImageDialog = new LoadingProgressDialog(mContext, "头像上传中...");
                    uploadImageDialog.show();
                    String ImageString = getPstr(filePath);
                    //上传用户图片
                    upLoadImage(ImageString);
                } else {
                    ToastUtils.show(mContext, R.string.check_network);
                }


            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }*/

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

                        userImagePath = imagePath;
                        imageLoader.get(imagePath, ImageLoader.getImageListener(
                                circleiv_mine_icon, R.drawable.touxiang,
                                R.drawable.touxiang));
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
        param.put("uid", UserId + "");
        param.put("photo", ImageString);
        param.put("token", token);
        //http://localhost/eggker/interface/basicdata/head  比传参数  uid =>uid   photo=>photo
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

}