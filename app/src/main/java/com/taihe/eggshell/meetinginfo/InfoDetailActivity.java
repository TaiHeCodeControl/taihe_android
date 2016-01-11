package com.taihe.eggshell.meetinginfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.login.LoginActivity;
import com.taihe.eggshell.main.entity.User;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.social.UMSocialService;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang on 2015/8/12.
 */
public class InfoDetailActivity extends BaseActivity{

    private static final String TAG = "InforDetailActivity";
    private Context mContext;
    private LoadingProgressDialog loading;
    private User user;
    String collect_count,apply_count;//1=已收藏,2=未收藏   apply_count   1=已报名,2=未报名

    private TextView mainPlat,startTime,address,telPhone,callPerson,comeWay,jobBrief,id_collect_count,id_apply_count;
    private TextView id_txt_info_bm,id_txt_info_sc;
    private ImageView imgLog,id_share,id_img_info_sc;
    private PullToRefreshListView id_info_listview;
    private LinearLayout id_lin_info_sc,id_lin_info_pl,id_lin_info_bm;
    private String actid,applyNum;
    private int UserId;
    int limit=8,page=1,type=2;
    @Override
    public void initView() {
        setContentView(R.layout.activity_info_detail);
        super.initView();

        mainPlat = (TextView)findViewById(R.id.id_info_company);
        startTime = (TextView)findViewById(R.id.id_info_time);
        address = (TextView)findViewById(R.id.id_info_addre);
        telPhone = (TextView)findViewById(R.id.id_info_phone);
        callPerson = (TextView)findViewById(R.id.id_person);
        comeWay = (TextView)findViewById(R.id.id_info_way);
        jobBrief = (TextView)findViewById(R.id.id_company_brief);
        imgLog = (ImageView)findViewById(R.id.id_info_logo);
        id_share = (ImageView)findViewById(R.id.id_share);
        id_collect_count = (TextView)findViewById(R.id.id_collect_count);
        id_apply_count = (TextView)findViewById(R.id.id_apply_count);
        id_info_listview = (PullToRefreshListView) findViewById(R.id.id_info_listview);
        id_lin_info_sc = (LinearLayout) findViewById(R.id.id_lin_info_sc);
        id_lin_info_pl = (LinearLayout) findViewById(R.id.id_lin_info_pl);
        id_lin_info_bm = (LinearLayout) findViewById(R.id.id_lin_info_bm);
        id_txt_info_sc = (TextView) findViewById(R.id.id_txt_info_sc);
        id_img_info_sc = (ImageView) findViewById(R.id.id_img_info_sc);
        id_txt_info_bm = (TextView) findViewById(R.id.id_txt_info_bm);
        id_lin_info_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user == null) {
                    EggshellApplication.getApplication().setLoginTag("InfoDetail");
                    Intent intent = new Intent(InfoDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    getCollectData();
                }
            }
        });
        id_lin_info_pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user == null) {
                    EggshellApplication.getApplication().setLoginTag("InfoDetail");
                    Intent intent = new Intent(InfoDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtils.show(mContext,"我要评论啦");
                }
            }
        });
        id_lin_info_bm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user == null) {
                    EggshellApplication.getApplication().setLoginTag("InfoDetail");
                    Intent intent = new Intent(InfoDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if("1".equals(apply_count)){
                        showCancelDialog();
                    }else {
                        getApplyData();
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        mContext = this;
        Intent intent = getIntent();
        initTitle("详情");
        loading = new LoadingProgressDialog(mContext,"正在请求...");
        actid = intent.getStringExtra("playId");
        if(TextUtils.isEmpty(intent.getStringExtra("outTime"))){
            apply_count="";
        }else{
            apply_count = intent.getStringExtra("outTime");//outTime 3 报名结束
        }

        id_share.setVisibility(View.VISIBLE);
        id_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShareDialog();
            }
        });
//        getListData();
    }
    private void getListData() {
        user = EggshellApplication.getApplication().getUser();
        if (user != null) {
            UserId = EggshellApplication.getApplication().getUser().getId();
        }
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    loading.dismiss();
                    JSONObject jsonObject = new JSONObject((String) obj);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        try{
                            JSONObject j2 = new JSONObject(jsonObject.getString("data"));
                            mainPlat.setText(j2.optString("organizers"));
                            startTime.setText(j2.optString("starttime") + "至" + j2.optString("endtime"));
                            address.setText(j2.optString("address"));
                            telPhone.setText(j2.optString("telphone"));
                            callPerson.setText(j2.optString("user"));
                            comeWay.setText(j2.optString("traffic_route"));
                            jobBrief.setText(Html.fromHtml(j2.optString("content")));
                            FinalBitmap bitmap = FinalBitmap.create(mContext);
                            bitmap.display(imgLog,j2.optString("logo"));
                            collect_count=j2.optString("is_collect_count");
                            applyNum=j2.optString("num");
                            showPersonNum(j2.optString("apply_count"),applyNum,0);
                            id_collect_count.setText(j2.optString("collect_count"));

                            if("1".equals(collect_count)){
                                id_txt_info_sc.setText("已收藏");
                                id_lin_info_sc.setBackgroundColor(getResources().getColor(R.color.origin));
                            }else {
                                id_txt_info_sc.setText("收藏");
                                id_lin_info_sc.setBackgroundColor(getResources().getColor(R.color.next_step_color));
                            }
                            if("3".equals(apply_count)){
                                id_txt_info_bm.setText("报名已结束");
                                id_lin_info_bm.setEnabled(false);
                                id_lin_info_bm.setBackgroundColor(getResources().getColor(R.color.bg_gray));
                            }else {
                                apply_count=j2.optString("is_appty_count");
                                if ("1".equals(apply_count)) {
                                    id_txt_info_bm.setText("已报名");
                                    id_lin_info_bm.setBackgroundColor(getResources().getColor(R.color.origin));
                                } else {
                                    id_txt_info_bm.setText("我要报名");
                                    id_lin_info_bm.setBackgroundColor(getResources().getColor(R.color.next_step_color));
                                }
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {//返回值
                loading.dismiss();
                ToastUtils.show(mContext, "网络异常");
            }
        };

        loading.show();
        Map<String,String> map = new HashMap<String,String>();
        map.put("id",""+actid);
        if(UserId!=0) {
            map.put("uid", "" + UserId);
        }else{
            map.put("uid", "" );
        }
        String url = Urls.ACTDETAIL_LIST_URL;
        RequestUtils.createRequest(mContext, url, "", true, map, true, listener, errorListener);
    }
    private void showPersonNum(String nowNum,String sumNum,int num){
        String temp =Integer.parseInt(nowNum)+num+"/"+sumNum;
        int i = temp.indexOf("/");
        SpannableString mspk = new SpannableString(temp);
        int k = (temp).length();
        mspk.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.origin)),0, i,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        id_apply_count.setText(mspk);
    }
    private void getCollectData() {
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    loading.dismiss();
                    JSONObject jsonObject = new JSONObject((String) obj);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        try{
                            if ("收藏成功".equals(jsonObject.optString("data"))){
                                ToastUtils.show(mContext,jsonObject.optString("data"));
                                collect_count="1";
                                id_txt_info_sc.setText("已收藏");
                                id_lin_info_sc.setBackgroundColor(getResources().getColor(R.color.origin));
                                id_collect_count.setText(Integer.parseInt(id_collect_count.getText().toString())+1+"");
                            }else if ("取消收藏成功".equals(jsonObject.optString("data"))){
                                ToastUtils.show(mContext,jsonObject.optString("data"));
                                collect_count="2";
                                id_txt_info_sc.setText("收藏");
                                id_collect_count.setText(Integer.parseInt(id_collect_count.getText().toString())-1+"");
                                id_lin_info_sc.setBackgroundColor(getResources().getColor(R.color.next_step_color));
                            }else{
                                ToastUtils.show(mContext,"收藏失败");
                            }
                        }catch (Exception ex){
                            ToastUtils.show(mContext,"收藏失败");
                            ex.printStackTrace();
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {//返回值
                loading.dismiss();
                ToastUtils.show(mContext, "网络异常");
            }
        };

        loading.show();
        Map<String,String> map = new HashMap<String,String>();
        map.put("id",""+actid);
        map.put("uid", "" + UserId);
        String url = Urls.ACT_COLLECT_LIST_URL;
        RequestUtils.createRequest(mContext, url, "", true, map, true, listener, errorListener);
    }
    private void getApplyData() {
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    loading.dismiss();
                    JSONObject jsonObject = new JSONObject((String) obj);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        try{
                            if ("报名成功".equals(jsonObject.optString("data"))){
                                ToastUtils.show(mContext,jsonObject.optString("data"));
                                apply_count="1";
                                id_txt_info_bm.setText("已报名");
                                id_lin_info_bm.setBackgroundColor(getResources().getColor(R.color.origin));
                                showPersonNum(id_apply_count.getText().toString(),applyNum,1);
                            }else if ("取消报名成功".equals(jsonObject.optString("data"))){
                                ToastUtils.show(mContext,jsonObject.optString("data"));
                                apply_count="2";
                                id_txt_info_bm.setText("我要报名");
                                id_lin_info_bm.setBackgroundColor(getResources().getColor(R.color.next_step_color));
                                showPersonNum(id_apply_count.getText().toString(),applyNum,-1);
                            }else{
                                ToastUtils.show(mContext,"报名失败");
                            }
                        }catch (Exception ex){
                            ToastUtils.show(mContext,"报名失败");
                            ex.printStackTrace();
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {//返回值
                loading.dismiss();
                ToastUtils.show(mContext, "网络异常");
            }
        };

        loading.show();
        Map<String,String> map = new HashMap<String,String>();
        map.put("id",""+actid);
        map.put("uid", "" + UserId);
        String url = Urls.ACT_APPLY_LIST_URL;
        RequestUtils.createRequest(mContext, url, "", true, map, true, listener, errorListener);
    }
    PopupWindow shareWindow;
    public void showShareDialog() {
        final UMImage image = new UMImage(mContext, "http://www.umeng.com/images/pic/social/integrated_3.png");
        final UMusic music = new UMusic("http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
        music.setTitle("sdasdasd");
        music.setThumb(new UMImage(mContext,"http://www.umeng.com/images/pic/social/chart_1.png"));
        final UMVideo video = new UMVideo("http://video.sina.com.cn/p/sports/cba/v/2013-10-22/144463050817.html");
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_share, null);
        LinearLayout linQQ = (LinearLayout) view.findViewById(R.id.shareLinQQ);
        LinearLayout linWeiBo = (LinearLayout) view.findViewById(R.id.shareLinWeiBo);
        LinearLayout linQzone = (LinearLayout) view.findViewById(R.id.shareLinQzone);
        LinearLayout linWeiXin = (LinearLayout) view.findViewById(R.id.shareLinWeiXin);
        LinearLayout linWeiXinFrind = (LinearLayout) view.findViewById(R.id.shareLinWeiXinFrind);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        shareWindow = new PopupWindow(view,WindowManager.LayoutParams.FILL_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        shareWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        shareWindow.setBackgroundDrawable(dw);
        // 设置popWindow的显示和消失动画
        shareWindow.setAnimationStyle(R.style.mystyle);
        // 在底部显示
        shareWindow.showAtLocation(InfoDetailActivity.this.findViewById(R.id.id_share),Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.7f);
        shareWindow.setOnDismissListener(new poponDismissListener());
        linQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(InfoDetailActivity.this).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                        .withText("hello umeng")
                        .withMedia(music)
                        .withTitle("qqshare")
                        .withTargetUrl("http://dev.umeng.com")
                        .share();
            }
        });
        linWeiBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(InfoDetailActivity.this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
                        .withText("hello umeng video")
                        .withTargetUrl("http://www.baidu.com")
                        .withMedia(image)
                        .share();
            }
        });
        linQzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(InfoDetailActivity.this).setPlatform(SHARE_MEDIA.QZONE).setCallback(umShareListener)
                        .withText("hello umeng")
                        .withMedia(image)
                        .share();
            }
        });
        linWeiXin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(InfoDetailActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                        .withText("hello wx")
//                        .withMedia(video)
                        .share();
            }
        });
        linWeiXinFrind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(InfoDetailActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                        .withText("hello umeng")
                        .withMedia(music)
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
    public void showCancelDialog() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_cancel, null);
        TextView cancelOK = (TextView) view.findViewById(R.id.tvCancelOK);
        TextView cancel = (TextView) view.findViewById(R.id.tvCancel);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        final PopupWindow window = new PopupWindow(view,WindowManager.LayoutParams.FILL_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mystyle);
        // 在底部显示
        window.showAtLocation(InfoDetailActivity.this.findViewById(R.id.id_share),Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.7f);
        window.setOnDismissListener(new poponDismissListener());
        cancelOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getApplyData();
                window.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
                backgroundAlpha(1f);
            }
        });
    }
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    class poponDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }
    @Override
    public void onResume() {
        super.onResume();
        getListData();
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}
