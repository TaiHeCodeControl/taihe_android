package com.taihe.eggshell.meetinginfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.main.mode.PlayInfoMode;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
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

    private TextView mainPlat,startTime,address,telPhone,callPerson,comeWay,jobBrief;
    private ImageView imgLog,id_share;
    private PullToRefreshListView id_info_listview;
    private String actid,uid;
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
        id_info_listview = (PullToRefreshListView) findViewById(R.id.id_info_listview);
    }

    @Override
    public void initData() {
        super.initData();
        mContext = this;
        Intent intent = getIntent();
        initTitle("详情");
        uid = EggshellApplication.getApplication().getUser().getId()+"";
        loading = new LoadingProgressDialog(mContext,"正在请求...");
        actid = intent.getStringExtra("playId");

        id_share.setVisibility(View.VISIBLE);
        id_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShareDialog();
            }
        });
        getListData();
    }
    private void getListData() {
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    loading.dismiss();
                    JSONObject jsonObject = new JSONObject((String) obj);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        String data = jsonObject.getString("data");
                        try{
//                            JSONArray j1 = new JSONArray(data);
//                            JSONObject j2;
//                            for(int i=0;i<j1.length();i++){
//                                PlayInfoMode vMode = new PlayInfoMode();
//                                j2 = j1.getJSONObject(i);
//
//                            }
//                            mainPlat.setText(intent.getStringExtra("organizers"));
//                            startTime.setText(intent.getStringExtra("starttime") + "至" + intent.getStringExtra("endtime"));
//                            address.setText(intent.getStringExtra("address"));
//                            telPhone.setText(intent.getStringExtra("telphone"));
//                            callPerson.setText(intent.getStringExtra("user"));
//                            comeWay.setText(intent.getStringExtra("traffic_route"));
//                            jobBrief.setText(Html.fromHtml(intent.getStringExtra("content")));
//                            FinalBitmap bitmap = FinalBitmap.create(mContext);
//                            bitmap.display(imgLog,intent.getStringExtra("logo"));
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
        map.put("id",""+type);
        map.put("uid",""+uid);
        String url = Urls.ACTDETAIL_LIST_URL;
        RequestUtils.createRequest(mContext, url, "", true, map, true, listener, errorListener);
    }
    public void showShareDialog() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_share, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mystyle);
        // 在底部显示
        window.showAtLocation(InfoDetailActivity.this.findViewById(R.id.id_share),
                Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.7f);
        window.setOnDismissListener(new poponDismissListener());
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
