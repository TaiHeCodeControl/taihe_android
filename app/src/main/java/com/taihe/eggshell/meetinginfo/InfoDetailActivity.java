package com.taihe.eggshell.meetinginfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.login.LoginActivity;
import com.taihe.eggshell.main.entity.User;
import com.taihe.eggshell.meetinginfo.adapter.InfoDetailAdapter;
import com.taihe.eggshell.meetinginfo.entity.InfoDetailMode;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.taihe.eggshell.widget.MyListView;
import com.taihe.eggshell.widget.MyScrollView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2015/8/12.
 */
public class InfoDetailActivity extends BaseActivity{
    static InputMethodManager keyinput ;
    private static final String TAG = "InforDetailActivity";
    private Context mContext;
    private LoadingProgressDialog loading;
    private User user;
    String collect_count,apply_count;//1=已收藏,2=未收藏   apply_count   1=已报名,2=未报名3报名结束
    String applyed;//已经报名人数
    private TextView mainPlat,startTime,address,telPhone,callPerson,comeWay,id_collect_count,id_apply_count;
    private TextView id_txt_info_bm,id_txt_info_sc,id_send;
    private WebView jobBrief;
    public static EditText id_edit_chat;
    private ImageView imgLog,id_share,id_img_info_sc;
    private MyListView id_info_listview;
    private LinearLayout id_lin_info_sc,id_lin_info_pl,id_lin_info_bm,id_lin_info_logo;
    public static LinearLayout id_lin_info_chat,id_lin_info_button;
    private String actid,applyNum;
    public static MyScrollView id_scroll_info;
    private int UserId;
    int limit=8,page=1,type=2;
    List<InfoDetailMode> listInfoDetail;
    InfoDetailAdapter infoDetailAdapter;
    private View footview;
    public static String d_id,ruid;
    private String shareTitle,shareContent,sharePic;
    @Override
    public void initView() {
        setContentView(R.layout.activity_info_detail);
        super.initView();
        keyinput = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mainPlat = (TextView)findViewById(R.id.id_info_company);
        startTime = (TextView)findViewById(R.id.id_info_time);
        address = (TextView)findViewById(R.id.id_info_addre);
        telPhone = (TextView)findViewById(R.id.id_info_phone);
        callPerson = (TextView)findViewById(R.id.id_person);
        comeWay = (TextView)findViewById(R.id.id_info_way);
        jobBrief = (WebView)findViewById(R.id.id_company_brief);
        id_scroll_info = (MyScrollView)findViewById(R.id.id_scroll_info);
        id_edit_chat = (EditText)findViewById(R.id.id_edit_chat);
        id_lin_info_chat = (LinearLayout)findViewById(R.id.id_lin_info_chat);
        id_lin_info_button = (LinearLayout)findViewById(R.id.id_lin_info_button);
        id_send = (TextView)findViewById(R.id.id_send);
        imgLog = (ImageView)findViewById(R.id.id_info_logo);
        id_lin_info_logo = (LinearLayout) findViewById(R.id.id_lin_info_logo);
        id_share = (ImageView)findViewById(R.id.id_share);
        id_collect_count = (TextView)findViewById(R.id.id_collect_count);
        id_apply_count = (TextView)findViewById(R.id.id_apply_count);
        id_info_listview = (MyListView) findViewById(R.id.id_info_listview);
        id_lin_info_sc = (LinearLayout) findViewById(R.id.id_lin_info_sc);
        id_lin_info_pl = (LinearLayout) findViewById(R.id.id_lin_info_pl);
        id_lin_info_bm = (LinearLayout) findViewById(R.id.id_lin_info_bm);
        id_txt_info_sc = (TextView) findViewById(R.id.id_txt_info_sc);
        id_img_info_sc = (ImageView) findViewById(R.id.id_img_info_sc);
        id_txt_info_bm = (TextView) findViewById(R.id.id_txt_info_bm);

        //scrollview的touch事件
        id_scroll_info.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InfoDetailActivity.ShowChatSend(false,"","","","");
                return false;
            }
        });
        //收藏
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
        //评论
        id_lin_info_pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user == null) {
                    EggshellApplication.getApplication().setLoginTag("InfoDetail");
                    Intent intent = new Intent(InfoDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    InfoDetailActivity.ShowChatSend(true,"","","","");
                }
            }
        });
        //报名
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
        //发送评论
        id_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!"".equals(id_edit_chat.getText().toString())) {
                    getAddChat(id_edit_chat.getText().toString());
                }else{
                    ToastUtils.show(mContext,"评论不能为空！");
                }
            }
        });
    }

    /**
     * 发送评论框
     * @param isShow
     * @param name
     * @param username
     * @param did
     * @param ruid
     */
    public static void ShowChatSend(boolean isShow,String name,String username,String did,String ruid){

        InfoDetailActivity.d_id=did;
        InfoDetailActivity.ruid=ruid;
        if(!isShow) {
            id_lin_info_chat.setVisibility(View.GONE);
            id_lin_info_button.setVisibility(View.VISIBLE);
            id_edit_chat.setText("");
            id_edit_chat.setHint("评论:");
            id_edit_chat.setFocusable(true);
            id_edit_chat.requestFocus();
            keyinput.hideSoftInputFromWindow(id_edit_chat.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }else{
            id_lin_info_chat.setVisibility(View.VISIBLE);
            id_lin_info_button.setVisibility(View.GONE);
            if(!"".equals(name)){
                id_edit_chat.setHint("回复　"+name+":");
                id_edit_chat.setFocusable(true);
                id_edit_chat.requestFocus();
                keyinput.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }else if(!"".equals(username)){
                id_edit_chat.setHint("回复　" + username + ":");
                id_edit_chat.setFocusable(true);
                id_edit_chat.requestFocus();
                keyinput.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }else {
                id_edit_chat.setFocusable(true);
                id_edit_chat.requestFocus();
                id_edit_chat.setHint("评论:");
                keyinput.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void initData() {
        super.initData();
        mContext = this;
        Intent intent = getIntent();
        initTitle("详情");
        infoDetailAdapter = new InfoDetailAdapter(mContext);
        loading = new LoadingProgressDialog(mContext,"正在请求...");
        actid = intent.getStringExtra("playId");
        listInfoDetail = new ArrayList<InfoDetailMode>();
        id_share.setVisibility(View.VISIBLE);
        id_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShareDialog();
            }
        });
        getChatList();

        footview = LayoutInflater.from(mContext).inflate(R.layout.item_foot_bottom,null);
        id_info_listview.addFooterView(footview);
        footview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page += 1;
                getChatList();
            }
        });
    }

    private void getAddChat(String content) {
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
                            InfoDetailActivity.ShowChatSend(false,"","","","");
                            listInfoDetail.clear();
                            page = 1;
                            getChatList();
                        }catch (Exception ex){
                            loading.dismiss();
                            ex.printStackTrace();
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    loading.dismiss();
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
        String url;
        if(!"".equals(InfoDetailActivity.d_id) && !"".equals(InfoDetailActivity.ruid)){//二级回复
            map.put("d_id",""+InfoDetailActivity.d_id);
            map.put("uid", UserId+"" );
            map.put("ruid", InfoDetailActivity.ruid+"" );
            map.put("r_coment", content);
            url = Urls.ACT_ADDREPLY_URL;
        }else{//一级回复
            map.put("aid",""+actid);
            map.put("uid", UserId+"" );
            map.put("d_coment", content);
            url = Urls.ACT_ADDDISCUSS_URL;
        }

        RequestUtils.createRequest(mContext, url, "", true, map, true, listener, errorListener);
    }

    /**
     * 评论列表
     */
    private void getChatList() {
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    loading.dismiss();
                    JSONObject jsonObject = new JSONObject((String) obj);
//                    Log.d("aaa",jsonObject.toString());
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        try{
                            InfoDetailMode infoMode;
                            JSONArray ja = jsonObject.optJSONArray("data");
                            JSONArray childArr;
                            JSONObject j1,j2;
                            List<InfoDetailMode.ChildEntity> childList;
                            InfoDetailMode.ChildEntity childMode;
                            if(ja.length()!=0){
                                for(int i=0;i<ja.length();i++) {
                                    j1 = ja.getJSONObject(i);
                                    infoMode = new InfoDetailMode();
                                    infoMode.setUid(j1.optString("uid"));
                                    infoMode.setD_id(j1.optString("d_id"));
                                    infoMode.setUsername(j1.optString("username"));
                                    infoMode.setAddtime(j1.optString("addtime"));
                                    infoMode.setUname(j1.optString("uname"));
                                    infoMode.setUphoto(j1.optString("uphoto"));
                                    infoMode.setAid(j1.optString("aid"));
                                    infoMode.setD_coment(j1.optString("d_coment"));
                                    childArr = new JSONArray(j1.optString("child"));
                                    childList = new ArrayList<InfoDetailMode.ChildEntity>();
                                    if(childArr.length()!=0) {
                                        for (int k = 0; k < childArr.length(); k++) {
                                            childMode = new InfoDetailMode.ChildEntity();
                                            j2 = childArr.getJSONObject(k);
                                            childMode.setUid(j2.optString("uid"));
                                            childMode.setRphoto(j2.optString("rphoto"));
                                            childMode.setUsername(j2.optString("username"));
                                            childMode.setAddtime(j2.optString("addtime"));
                                            childMode.setRuid(j2.optString("ruid"));
                                            childMode.setUname(j2.optString("uname"));
                                            childMode.setR_coment(j2.optString("r_coment"));
                                            childMode.setRname(j2.optString("rname"));
                                            childMode.setRusername(j2.optString("rusername"));
                                            childMode.setUphoto(j2.optString("uphoto"));
                                            childList.add(childMode);
                                        }
                                        infoMode.setChild(childList);
                                    }else{
                                        infoMode.setChild(childList);
                                    }
                                    listInfoDetail.add(infoMode);
                                }
                            }else{
                                if(page!=1){
                                    ToastUtils.show(mContext,"没有了");
                                }
                                id_info_listview.removeFooterView(footview);
                            }

                            infoDetailAdapter.setPlayData(listInfoDetail,2);
                            id_info_listview.setAdapter(infoDetailAdapter);

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
        map.put("aid",""+actid);
        map.put("page", page+"" );

        String url = Urls.ACT_GETREPLY_LIST_URL;
        RequestUtils.createRequest(mContext, url, "", true, map, true, listener, errorListener);
    }

    /**
     * 详情
     */
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
//                    Log.d("ddd",jsonObject.toString());
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
                            jobBrief.loadDataWithBaseURL("about:blank",j2.optString("content"),"text/html", "utf-8",null);
                            sharePic = j2.optString("logo");
                            FinalBitmap bitmap = FinalBitmap.create(mContext);
                            bitmap.display(imgLog,sharePic);
                            shareTitle =j2.optString("title");
                            shareContent =j2.optString("content");
                            WindowManager wm = getWindowManager();
                            int width = wm.getDefaultDisplay().getWidth();
                            int height=9*width/16;
                            ViewGroup.LayoutParams lp = imgLog.getLayoutParams();
                            lp.width = width;
                            lp.height = height;
                            imgLog.setLayoutParams(lp);
                            applyed = j2.optString("apply_count");
                            collect_count=j2.optString("is_collect_count");
                            applyNum=j2.optString("num");

                            showPersonNum(applyed,applyNum,0);
                            id_collect_count.setText(j2.optString("collect_count"));
                            apply_count=j2.optString("is_appty_count");

                            if("1".equals(collect_count)){
                                id_txt_info_sc.setText("已收藏");
                                id_img_info_sc.setBackgroundResource(R.drawable.scstar);
//                                id_lin_info_sc.setBackgroundColor(getResources().getColor(R.color.origin));
                            }else {
                                id_txt_info_sc.setText("收藏");
                                id_img_info_sc.setBackgroundResource(R.drawable.scstark);
//                                id_lin_info_sc.setBackgroundColor(getResources().getColor(R.color.next_step_color));
                            }

                            if ("1".equals(apply_count)) {
                                id_txt_info_bm.setText("已报名");
                                id_lin_info_bm.setBackgroundColor(getResources().getColor(R.color.origin));
                            } else if("3".equals(apply_count)){
                                id_txt_info_bm.setText("报名已结束");
                                id_lin_info_bm.setEnabled(false);
                                id_lin_info_bm.setBackgroundColor(getResources().getColor(R.color.font_color_gray_jobsearch));
                            }else {
                                if(!applyed.equals(applyNum)) {
                                    id_txt_info_bm.setText("我要报名");
                                    id_lin_info_bm.setBackgroundColor(getResources().getColor(R.color.next_step_color));
                                }else{
                                    id_txt_info_bm.setText("报名已满");
                                    id_lin_info_bm.setEnabled(false);
                                    id_lin_info_bm.setBackgroundColor(getResources().getColor(R.color.font_color_gray_jobsearch));
                                }
                            }
                            /*id_scroll_info.post(new Runnable() {
                                public void run() {
                                    id_scroll_info.fullScroll(ScrollView.SCROLL_INDICATOR_TOP);
                                }
                            });*/
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
        applyed=Integer.parseInt(nowNum)+num+"";
        String temp =applyed+"/"+sumNum;
        int i = temp.indexOf("/");
        SpannableString mspk = new SpannableString(temp);
        int k = (temp).length();
        mspk.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.origin)),0, i,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        id_apply_count.setText(mspk);
    }

    /**
     * 收藏
     */
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
//                                id_lin_info_sc.setBackgroundColor(getResources().getColor(R.color.origin));
                                id_img_info_sc.setBackgroundResource(R.drawable.scstar);
                                id_collect_count.setText(Integer.parseInt(id_collect_count.getText().toString())+1+"");
                            }else if ("取消收藏成功".equals(jsonObject.optString("data"))){
                                ToastUtils.show(mContext,jsonObject.optString("data"));
                                collect_count="2";
                                id_txt_info_sc.setText("收藏");
                                id_img_info_sc.setBackgroundResource(R.drawable.scstark);
                                id_collect_count.setText(Integer.parseInt(id_collect_count.getText().toString())-1+"");
//                                id_lin_info_sc.setBackgroundColor(getResources().getColor(R.color.next_step_color));
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
    /**
     * 报名
     */
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
                                showPersonNum(applyed,applyNum,1);
                            }else if ("取消报名成功".equals(jsonObject.optString("data"))){
                                ToastUtils.show(mContext,jsonObject.optString("data"));
                                apply_count="2";
                                id_txt_info_bm.setText("我要报名");
                                id_lin_info_bm.setBackgroundColor(getResources().getColor(R.color.next_step_color));
                                showPersonNum(applyed,applyNum,-1);
                            }else{
                                ToastUtils.show(mContext,"操作失败");
                            }
                        }catch (Exception ex){
                            ToastUtils.show(mContext,"操作失败");
                            ex.printStackTrace();
                        }
                    } else if (code == 2 || code ==3) {
                        ToastUtils.show(mContext,jsonObject.optString("data"));
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

    /**
     * 分享
     */
    PopupWindow shareWindow;
    public void showShareDialog() {
        final UMImage image = new UMImage(mContext, sharePic);
//        final UMusic music = new UMusic("http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
//        music.setTitle(shareTitle);
//        music.setThumb(new UMImage(mContext,"http://www.umeng.com/images/pic/social/chart_1.png"));
//        final UMVideo video = new UMVideo("http://video.sina.com.cn/p/sports/cba/v/2013-10-22/144463050817.html");
        final String shareURL=Urls.SHARE_URL+actid;
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
        shareContent = String.valueOf(Html.fromHtml(shareContent));
        linQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(InfoDetailActivity.this).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                        .withTitle(shareTitle)
                        .withText(shareContent)
                        .withTargetUrl(shareURL)
                        .withMedia(image)
//                        .withMedia(music)
                        .share();
            }
        });
        linWeiBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(InfoDetailActivity.this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
                        .withTitle(shareTitle)
                        .withText(shareContent.substring(0,100))
                        .withTargetUrl(shareURL)
                        .withMedia(image)
                        .share();
            }
        });
        linQzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(InfoDetailActivity.this).setPlatform(SHARE_MEDIA.QZONE).setCallback(umShareListener)
                        .withTitle(shareTitle)
                        .withText(shareContent)
                        .withTargetUrl(shareURL)
                        .withMedia(image)
                        .share();
            }
        });
        linWeiXin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(InfoDetailActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                        .withTitle(shareTitle)
                        .withText(shareContent)
                        .withTargetUrl(shareURL)
                        .withMedia(image)
//                        .withMedia(video)
                        .share();
            }
        });
        linWeiXinFrind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(InfoDetailActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                        .withTitle(shareTitle)
                        .withText(shareContent)
                        .withTargetUrl(shareURL)
                        .withMedia(image)
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
