package com.taihe.eggshell.job.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.GsonUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.adapter.AllJobAdapter;
import com.taihe.eggshell.job.bean.JobDetailInfo;
import com.taihe.eggshell.job.bean.JobInfo;
import com.taihe.eggshell.login.LoginActivity;
import com.taihe.eggshell.main.entity.User;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.taihe.eggshell.widget.MyListView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2015/8/10.
 */
public class JobDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "JobDetailActivity";
    private Context mContext;
    private TextView titleView, jobtitle, jobcompany, jobstart, jobend, jobtype, joblevel, jobyears, jobaddress, jobmoney, jobnum, updown, shouqi, company_jieshao,jobdaogang,jobsex,jobmarriage;
    private Button applyButton;
    private MyListView jobDescListView, moreJobListView;
    private ImageView collectionImg,id_jobinfo_logo,id_share;
    private LoadingProgressDialog dialog;
    private int jobId;
    private String com_id;
    private LinearLayout ll_moreposition;
    private TextView tv_allzhiwei, tv_shouqizhiwei;

    private ScrollView sc_detail;
    private List<JobInfo> jobInfos = null;
    private boolean isCollect = false;

    private Intent intent;

    private String address, cj_name, com_name, content, description, edu, exp, hy, id, lastupdate, linkmail, linktel, mun, name, provinceid, salary, type,number,daogang,sex,marriage,imgLogo;
    private String collect;

    private TextView tv_jobdetail_description;
    private TextView tv_companyAddress;
    GifView gf1;
    private int UserId;
    private User user;

    private Handler jobDetailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 201://收藏
                    String data = (String) msg.obj;
                    ToastUtils.show(mContext, data);
                    if (data.equals("收藏成功")) {//收藏
                        collectionImg.setImageResource(R.drawable.shoucang2);//已收藏图标
                    } else {// 取消收藏
                        collectionImg.setImageResource(R.drawable.shoucang);
                    }
                    break;
                case 101://职位详情
                    try {
                        JobDetailInfo jobDetaiInfo = (JobDetailInfo) msg.obj;
                        address = jobDetaiInfo.data.address;
                        if (address.length() > 2) {

                            address = address.substring(0, 2);
                        }
//                        address = jobDetaiInfo.data.address.split("区")[0].toString();
                        shareTitle = jobDetaiInfo.data.cj_name;
                        com_name = jobDetaiInfo.data.com_name;
                        shareContent = jobDetaiInfo.data.content;
                        description = jobDetaiInfo.data.cj_description;
                        edu = jobDetaiInfo.data.edu;
                        exp = jobDetaiInfo.data.exp;
                        hy = jobDetaiInfo.data.hy;
                        id = jobDetaiInfo.data.id;
                        lastupdate = jobDetaiInfo.data.lastupdate;
                        linkmail = jobDetaiInfo.data.linkmail;
                        linktel = jobDetaiInfo.data.linktel;
                        mun = jobDetaiInfo.data.mun;
                        name = jobDetaiInfo.data.name;
                        provinceid = jobDetaiInfo.data.provinceid;
                        salary = jobDetaiInfo.data.salary;
                        type = jobDetaiInfo.data.type;
                        number = jobDetaiInfo.data.number;
                        daogang = jobDetaiInfo.data.daogang;
                        sex = jobDetaiInfo.data.sex;
                        marriage = jobDetaiInfo.data.marriage;
                        sharePic = jobDetaiInfo.data.logo;
                        //54不限 55全职 56兼职
                        if (type.equals("55")) {
                            type = "全职";
                        } else if (type.equals("56")) {
                            type = "兼职";
                        } else if(type.equals("129")){
                            type = "实习";
                        } else {
                            type = "不限";
                        }

                        collect = jobDetaiInfo.data.iscollect;
                        if (collect.equals("1")) {//已收藏收藏

                            collectionImg.setImageResource(R.drawable.shoucang2);
                        } else {//未收藏
                            collectionImg.setImageResource(R.drawable.shoucang);
                        }
                        //职位详情信息填充
                        tv_companyAddress.setText(jobDetaiInfo.data.address);
                        jobtitle.setText(shareTitle);
                        shareTitle="招聘："+shareTitle;
                        jobcompany.setText(com_name);
                        jobstart.setText(lastupdate);//发布时间
                        jobend.setText(jobDetaiInfo.data.edate);//有效时间
                        jobtype.setText(type);//工作性质
                        joblevel.setText(edu);//学历要求
                        jobyears.setText(exp);//工作年限
                        jobaddress.setText(address);
                        jobmoney.setText(salary);
                        jobnum.setText(number);//招聘人数
                        jobdaogang.setText(daogang);//到岗时间
                        jobsex.setText(sex);//性别
                        jobmarriage.setText(marriage);//婚姻情况

                        byte[] bytes = Base64.decode(shareContent, Base64.DEFAULT);
                        shareContent = new String(bytes, "UTF-8");
                        company_jieshao.setText(Html.fromHtml(shareContent));//公司介绍

                        byte[] descriptionbytes = Base64.decode(description, Base64.DEFAULT);
                        description = new String(descriptionbytes, "UTF-8");
                        tv_jobdetail_description.setText(Html.fromHtml(description));//职责描述

                        finalBitmap.display(id_jobinfo_logo,sharePic,bitmap,bitmap);
                        int size = jobDetaiInfo.data.lists.size();
                        if (size < 1) {//如果没有更多职位，隐藏更多职位
                            ll_moreposition.setVisibility(View.GONE);
                        }
                        //该公司其他职位信息
                        for (int i = 0; i < size; i++) {
                            JobInfo jobInfo = new JobInfo();

                            jobInfo.setJob_Id(jobDetaiInfo.data.lists.get(i).id);
                            jobInfo.setCom_name(jobDetaiInfo.data.lists.get(i).com_name);
                            jobInfo.setEdu(jobDetaiInfo.data.lists.get(i).edu);
                            jobInfo.setName(jobDetaiInfo.data.lists.get(i).name);
                            jobInfo.setLastupdate(jobDetaiInfo.data.lists.get(i).lastupdate);
                            jobInfo.setProvinceid(jobDetaiInfo.data.lists.get(i).provinceid);
                            jobInfo.setSalary(jobDetaiInfo.data.lists.get(i).salary);
                            jobInfo.setUid(jobDetaiInfo.data.lists.get(i).uid);

                            jobInfos.add(jobInfo);
                        }


                        //填充listview
                        AllJobAdapter jobAdapter = new AllJobAdapter(mContext, jobInfos, false);
                        moreJobListView.setAdapter(jobAdapter);

                        new GetLinesAsyncTask().execute();
                    } catch (Exception e) {

                    }
                    break;
            }

        }
    };

    @Override
    public void initView() {

        setContentView(R.layout.activity_job_info);
        super.initView();
        mContext = this;
        jobInfos = new ArrayList<JobInfo>();

        user = EggshellApplication.getApplication().getUser();
        if (user != null) {
            UserId = EggshellApplication.getApplication().getUser().getId();
        }
        bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.no_logo_company);
        finalBitmap = FinalBitmap.create(mContext);
        dialog = new LoadingProgressDialog(mContext, getResources().getString(
                R.string.submitcertificate_string_wait_dialog));
        intent = getIntent();
        jobId = intent.getIntExtra("ID", 1);
        com_id = intent.getStringExtra("com_id");
        tv_companyAddress = (TextView) findViewById(R.id.tv_jobinfo_company_address);
        titleView = (TextView) findViewById(R.id.id_title);
        collectionImg = (ImageView) findViewById(R.id.id_other);
        id_share = (ImageView)findViewById(R.id.id_share);
        jobtitle = (TextView) findViewById(R.id.id_jobinfo_name);
        jobcompany = (TextView) findViewById(R.id.id_jobinfo_company);
        jobstart = (TextView) findViewById(R.id.id_jobinfo_start_time);
        jobend = (TextView) findViewById(R.id.id_jobinfo_end_time);
        jobtype = (TextView) findViewById(R.id.id_jobinfo_type);
        joblevel = (TextView) findViewById(R.id.id_jobinfo_school_leve);
        jobyears = (TextView) findViewById(R.id.id_jobinfo_work_time);
        jobaddress = (TextView) findViewById(R.id.id_jobinfo_addres);
        jobmoney = (TextView) findViewById(R.id.id_jobInfo_money);
        jobnum = (TextView) findViewById(R.id.id_jobinfo_pepple_num);
        updown = (TextView) findViewById(R.id.id_jobinfo_see_all);
        shouqi = (TextView) findViewById(R.id.id_jobinfo_see_shouqi);
        jobdaogang = (TextView) findViewById(R.id.id_jobinfo_daogang);
        jobsex = (TextView) findViewById(R.id.id_jobinfo_sex);
        jobmarriage = (TextView) findViewById(R.id.id_jobinfo_marriage);
        company_jieshao = (TextView) findViewById(R.id.id_jobinfo_company_jieshao);

        jobDescListView = (MyListView) findViewById(R.id.id_jobinfo_desc);
        moreJobListView = (MyListView) findViewById(R.id.id_jobinfo_other_position);
        moreJobListView.setFocusable(false);
        applyButton = (Button) findViewById(R.id.id_jobinfo_apply_button);
        id_jobinfo_logo = (ImageView) findViewById(R.id.id_jobinfo_logo);

//        // 从xml中得到GifView的句柄
//        gf1 = (GifView) findViewById(R.id.gif1);
//        // 设置Gif图片源
//        gf1.setGifImage(R.drawable.gif1);
//        // 添加监听器
////        gf1.setOnClickListener(this);
//        // 设置显示的大小，拉伸或者压缩
////        gf1.setShowDimension(300, 300);
//        // 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
//        gf1.setGifImageType(GifView.GifImageType.COVER);



        applyButton.setOnClickListener(this);
        collectionImg.setOnClickListener(this);
        id_share.setOnClickListener(this);
        updown.setOnClickListener(this);
        shouqi.setOnClickListener(this);

        tv_jobdetail_description = (TextView) findViewById(R.id.tv_jobinfo_description);
        tv_allzhiwei = (TextView) findViewById(R.id.id_jobinfo_see_allzhize);
        tv_allzhiwei.setOnClickListener(this);

        tv_shouqizhiwei = (TextView) findViewById(R.id.id_jobinfo_see_shouqizhize);
        tv_shouqizhiwei.setOnClickListener(this);
        ll_moreposition = (LinearLayout) findViewById(R.id.ll_jobinfo_moreposition);

        sc_detail = (ScrollView) findViewById(R.id.sv_jobinfo);
        sc_detail.smoothScrollTo(0, 20);
    }

    @Override
    public void initData() {
        super.initData();
        titleView.setText("职位详情");
        collectionImg.setVisibility(View.VISIBLE);
        id_share.setVisibility(View.VISIBLE);
        //该公司其他职位的点击事件
        moreJobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //listviewItem点击事件

                if (position < jobInfos.size()) {
                    Intent intent = new Intent(mContext, JobDetailActivity.class);
                    intent.putExtra("ID", jobInfos.get(position).getJob_Id());
                    intent.putExtra("com_id", jobInfos.get(position).getUid());
                    startActivity(intent);
                    JobDetailActivity.this.finish();
                }

            }
        });


        if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
            dialog.show();

            getDetail();

        } else {
            ToastUtils.show(mContext, R.string.check_network);
        }

    }


    //申请职位
    public void postJob() {

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                dialog.dismiss();
                try {
//                    Log.v(TAG, (String) o);

                    JSONObject jsonObject = new JSONObject((String) o);

                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if (code == 0) {//申请成功

                        ToastUtils.show(mContext, "申请成功");

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
                ToastUtils.show(mContext, "网络异常");
            }
        };

        Map<String, String> param = new HashMap<String, String>();
        param.put("uid", UserId + "");//UserID       userId
        param.put("job_id", jobId + "");
        RequestUtils.createRequest(mContext, "", Urls.METHOD_JOB_POST, false, param, true, listener, errorListener);

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_jobinfo_apply_button://申请职位

                if (user == null) {
                    EggshellApplication.getApplication().setLoginTag("jobDetail");
                    Intent intent = new Intent(JobDetailActivity.this, LoginActivity.class);
                    intent.putExtra("ID", jobId);
                    intent.putExtra("com_id", com_id);
                    startActivity(intent);
                } else {
                    //申请职位
                    if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {

                        dialog.show();
                        postJob();
                    } else {
                        ToastUtils.show(mContext, R.string.check_network);
                    }

                }

                break;
            case R.id.id_jobinfo_see_all:
                company_jieshao.setMaxLines(Integer.MAX_VALUE);
                updown.setVisibility(View.GONE);
                shouqi.setVisibility(View.VISIBLE);
                break;
            case R.id.id_jobinfo_see_shouqi:
                company_jieshao.setMaxLines(3);
                updown.setVisibility(View.VISIBLE);
                shouqi.setVisibility(View.GONE);
                break;
            case R.id.id_jobinfo_see_allzhize:
                tv_jobdetail_description.setMaxLines(Integer.MAX_VALUE);
                tv_allzhiwei.setVisibility(View.GONE);
                tv_shouqizhiwei.setVisibility(View.VISIBLE);
                break;
            case R.id.id_jobinfo_see_shouqizhize:
                tv_jobdetail_description.setMaxLines(3);
                tv_allzhiwei.setVisibility(View.VISIBLE);
                tv_shouqizhiwei.setVisibility(View.GONE);
                break;
            case R.id.id_share:
                showShareDialog();
                break;
            case R.id.id_other:

                if (user == null) {
                    EggshellApplication.getApplication().setLoginTag("jobDetail");
                    Intent intent = new Intent(JobDetailActivity.this, LoginActivity.class);
                    intent.putExtra("ID", jobId);
                    intent.putExtra("com_id", com_id);
                    startActivity(intent);
                } else {
                    //收藏&取消收藏
                    if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {

                        dialog.show();
                        collectPosition();
                    } else {
                        ToastUtils.show(mContext, R.string.check_network);
                    }

                }
                break;
        }
    }
    /**
     * 分享
     */
    PopupWindow shareWindow;
    private String shareTitle,shareContent,sharePic;
    private Bitmap bitmap;
    private FinalBitmap finalBitmap;
    public void showShareDialog() {
        final UMImage image = new UMImage(mContext, sharePic);
        final String shareURL=Urls.SHARE_JOB_URL+com_id+"&pid="+jobId;
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_share, null);
        LinearLayout linQQ = (LinearLayout) view.findViewById(R.id.shareLinQQ);
        LinearLayout linWeiBo = (LinearLayout) view.findViewById(R.id.shareLinWeiBo);
        LinearLayout linQzone = (LinearLayout) view.findViewById(R.id.shareLinQzone);
        LinearLayout linWeiXin = (LinearLayout) view.findViewById(R.id.shareLinWeiXin);
        LinearLayout linWeiXinFrind = (LinearLayout) view.findViewById(R.id.shareLinWeiXinFrind);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        shareWindow = new PopupWindow(view, WindowManager.LayoutParams.FILL_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        shareWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        shareWindow.setBackgroundDrawable(dw);
        // 设置popWindow的显示和消失动画
        shareWindow.setAnimationStyle(R.style.mystyle);
        // 在底部显示
        shareWindow.showAtLocation(JobDetailActivity.this.findViewById(R.id.id_share), Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.7f);
        shareWindow.setOnDismissListener(new poponDismissListener());
        shareContent = String.valueOf(Html.fromHtml(shareContent));

        Drawable drawable = id_jobinfo_logo.getDrawable();
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w,h,config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        final UMImage im = new UMImage(mContext,getImageSize(bitmap));

        linQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(JobDetailActivity.this).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                        .withTitle(shareTitle)
                        .withText(shareContent)
                        .withTargetUrl(shareURL)
                        .withMedia(im)
//                        .withMedia(music)
                        .share();
            }
        });
        linWeiBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int contentL=0;
                if(!"".equals(String.valueOf(Html.fromHtml(shareContent)))) {
                    contentL = String.valueOf(Html.fromHtml(shareContent)).length();
                }
                if(contentL>=70){
                    contentL=70;
                }
                String strSina = String.valueOf(Html.fromHtml(shareContent)).substring(0,contentL);

                new ShareAction(JobDetailActivity.this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
                        .withTitle("　"+shareTitle)
                        .withText(strSina)
                        .withTargetUrl(shareURL)
                        .withMedia(im)
                        .share();
            }
        });
        linQzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(JobDetailActivity.this).setPlatform(SHARE_MEDIA.QZONE).setCallback(umShareListener)
                        .withTitle(shareTitle)
                        .withText(shareContent)
                        .withTargetUrl(shareURL)
                        .withMedia(im)
                        .share();
            }
        });
        linWeiXin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(JobDetailActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                        .withTitle(shareTitle)
                        .withText(shareContent)
                        .withTargetUrl(shareURL)
                        .withMedia(im)
//                        .withMedia(video)
                        .share();
            }
        });
        linWeiXinFrind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(JobDetailActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                        .withTitle(shareTitle)
                        .withText(shareContent)
                        .withTargetUrl(shareURL)
                        .withMedia(im)
                        .share();
            }
        });
    }
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(mContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            shareWindow.dismiss();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mContext,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mContext,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    class poponDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }
    private Bitmap getImageSize(Bitmap image){
        int size = 32;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 85, out);
//        Log.v(TAG,"图片大小："+(out.toByteArray().length/1024));
        float zoom = (float)Math.sqrt(size * 1024 / (float)out.toByteArray().length);

        Matrix matrix = new Matrix();
        matrix.setScale(zoom, zoom);

        Bitmap result = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);

        out.reset();
        result.compress(Bitmap.CompressFormat.JPEG, 85, out);
        while(out.toByteArray().length > size * 1024){
//            System.out.println(out.toByteArray().length);
            matrix.setScale(0.9f, 0.9f);
            result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
            out.reset();
            result.compress(Bitmap.CompressFormat.JPEG, 85, out);
        }
//        Log.v(TAG,"大小："+(out.toByteArray().length/1024));
        return result;
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
                        String data = jsonObject.getString("data");
                        Message msg = new Message();
                        msg.obj = data;
                        msg.what = 201;
                        jobDetailHandler.sendMessage(msg);

                    } else {
                        ToastUtils.show(mContext, "获取失败");
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
        param.put("uid", UserId + "");
        param.put("job_id", jobId + "");

        RequestUtils.createRequest(mContext, "", Urls.METHOD_JOB_COLLECT, false, param, true, listener, errorListener);

    }

    //获取职位详情
    private void getDetail() {
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                dialog.dismiss();
                try {
//                    Log.v(TAG, (String) o);

                    JobDetailInfo jobDetaiInfo = GsonUtils
                            .changeGsonToBean(o.toString(),
                                    JobDetailInfo.class);
                    int code = jobDetaiInfo.code;
                    if (code == 0) {

                        Message msg = new Message();
                        msg.obj = jobDetaiInfo;
                        msg.what = 101;
                        jobDetailHandler.sendMessage(msg);

                    } else {
                        ToastUtils.show(mContext, "获取详情信息失败");
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
        param.put("id", com_id + "");//职位列表中的uid
        param.put("pid", jobId + "");
        param.put("uid", UserId + "");
        RequestUtils.createRequest(mContext, "", Urls.METHOD_JOB_DETAIL, false, param, true, listener, errorListener);

    }

    //获取职位职责和公司介绍的总行数
    private class GetLinesAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            int lineCount = company_jieshao.getLineCount();
            int zhiweiline = tv_jobdetail_description.getLineCount();
            if (lineCount <= 3) {
                updown.setVisibility(View.GONE);
                shouqi.setVisibility(View.GONE);
            } else {
                company_jieshao.setMaxLines(3);
                updown.setVisibility(View.VISIBLE);
                shouqi.setVisibility(View.GONE);
            }
            if (zhiweiline <= 3) {
                tv_shouqizhiwei.setVisibility(View.GONE);
                tv_allzhiwei.setVisibility(View.GONE);
            } else {
                tv_jobdetail_description.setMaxLines(3);
                tv_shouqizhiwei.setVisibility(View.GONE);
                tv_allzhiwei.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

}
