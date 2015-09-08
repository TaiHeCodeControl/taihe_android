package com.taihe.eggshell.videoplay;

import tv.danmaku.ijk.media.player.IMediaPlayer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.easefun.polyvsdk.ijk.IjkVideoView;
import com.easefun.polyvsdk.ijk.OnPreparedListener;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.main.adapter.VideoInfoAdapter;
import com.taihe.eggshell.videoplay.mode.VideoInfoMode;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thinkpad on 2015/7/24.
 */
public class VideoPlayActivity extends BaseActivity {
    private static final String TAG = "VideoPlayActivity";
    private LinearLayout lin_video_play_top;
    private IjkVideoView videoview;
    private MediaController mediaController;
    private VideoInfoAdapter videoAdapter;
    private ProgressBar progressBar;
    private String path,vid,c_id,plist;
    int w, h;
    private WindowManager wm;
    RelativeLayout rl;
    private boolean isLandscape = false;
    private int stopPosition = 0;
    private boolean encrypt = false;
    float ratio;
    private TextView playTitle;
    private List<VideoInfoMode> listInfo;
    private ListView lst_video_play;
    String[] arrPlist;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    try{
                        JSONArray j1 = new JSONArray(msg.obj.toString());
                        JSONObject j2;
                        for(int i=0;i<j1.length();i++){
                            VideoInfoMode vMode = new VideoInfoMode();
                            j2 = j1.getJSONObject(i);
                            vMode.setId(j2.optString("id").toString());
                            vMode.setC_id(j2.optString("c_id").toString());
                            vMode.setVideo_id(j2.optString("video_id").toString());
                            vMode.setVideo_name(j2.optString("video_name").toString());
                            vMode.setVideo_hour(j2.optString("video_hour").toString());
                            vMode.setVideo_teacher(j2.optString("video_teacher").toString());
                            vMode.setVideo_about(j2.optString("video_about").toString());
                            vMode.setVideo_obvious(j2.optString("video_obvious").toString());
                            vMode.setVimage(j2.optString("vimage").toString());
                            vMode.setStatus(j2.optString("status").toString());
                            listInfo.add(vMode);
                        }
                        videoAdapter.setVideoData(listInfo);
                        lst_video_play.setAdapter(videoAdapter);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    break;
            }
        }
    };
    @Override
    public void initView() {
        setContentView(R.layout.activity_video_play);
        playTitle = (TextView) findViewById(R.id.txt_video_play_title);
        lin_video_play_top = (LinearLayout) findViewById(R.id.lin_video_play_top);
        lst_video_play = (ListView) findViewById(R.id.lst_video_play);
        super.initView();
        super.initData();
    }

    @Override
    public void initData() {
        initTitle("课程播放");
        listInfo = new ArrayList<VideoInfoMode>();
        videoAdapter = new VideoInfoAdapter(getApplicationContext());
        lst_video_play.setDividerHeight(0);
        Bundle e = getIntent().getExtras();
        String title="";
        if (e != null) {
            path = e.getString("path");
            vid = e.getString("vid");
            plist = e.getString("plist");
            title = e.getString("title");
        }
        if (vid != null && vid.length() > 0) {
            encrypt = true;
        }
        arrPlist = plist.split(",");
        playTitle.setText(title);
        wm = this.getWindowManager();
        w = wm.getDefaultDisplay().getWidth();
        h = wm.getDefaultDisplay().getHeight();
        rl = (RelativeLayout) findViewById(R.id.rl);
        rl.setLayoutParams(new RelativeLayout.LayoutParams(w, h));
        progressBar = (ProgressBar) findViewById(R.id.loadingprogress);
        videoview = (IjkVideoView) findViewById(R.id.full_videoview);
        mediaController = new MediaController(this, false);
        videoview.setMediaController(mediaController);
        videoview.setMediaBufferingIndicator(progressBar);
        if (encrypt) {
            try{
                if (RequestUtils.GetWebType(getApplicationContext()) != 0) {
                    videoview.setVid(vid, 1);
                }else{
                    Toast.makeText(getApplicationContext(), "网络连接异常,请检查网络是否正常！", Toast.LENGTH_LONG).show();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }

        } else {

        }
        changeToPortrait();
//        videoview.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_STRETCH);
        videoview.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(IMediaPlayer mp) {
                
            }
        });

        mediaController.setOnBoardBackListener(new MediaController.OnBoardBackListener() {

            @Override
            public void onBack() {
                finish();
            }
        });
        // 设置切屏事件
        mediaController
                .setOnBoardChangeListener(new MediaController.OnBoardChangeListener() {

                    @Override
                    public void onPortrait() {
                        changeToLandscape();
                    }

                    @Override
                    public void onLandscape() {
                        changeToPortrait();
                    }
                });
        // 设置视频尺寸 ，在横屏下效果较明显
        mediaController.setOnVideoChangeListener(new MediaController.OnVideoChangeListener() {

            @Override
            public void onVideoChange(int layout) {
                // TODO Auto-generated method stub
                videoview.setVideoLayout(layout);
                switch (layout) {
                    case IjkVideoView.VIDEO_LAYOUT_ORIGIN:
//                        Log.e("err","err1=="+layout);
                        //Toast.makeText(IjkFullVideoActivity.this, "VIDEO_LAYOUT_ORIGIN", 1).show();
                        break;
                    case IjkVideoView.VIDEO_LAYOUT_SCALE:
//                       Log.e("err","err2=="+layout);
                        //Toast.makeText(IjkFullVideoActivity.this, "VIDEO_LAYOUT_SCALE", 1).show();
                        break;
                    case IjkVideoView.VIDEO_LAYOUT_STRETCH:
//                        Log.e("err","err3=="+layout);
                        //Toast.makeText(IjkFullVideoActivity.this, "VIDEO_LAYOUT_STRETCH", 1).show();
                        break;
                    case IjkVideoView.VIDEO_LAYOUT_ZOOM:
//                        Log.e("err","err4=="+layout);
                        //Toast.makeText(IjkFullVideoActivity.this, "VIDEO_LAYOUT_ZOOM", 1).show();
                        break;
                }
            }
        });
        getData();
        lst_video_play.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.bg_gray));
                videoAdapter.setSelectedPosition(i);
                videoAdapter.notifyDataSetInvalidated();
                if (RequestUtils.GetWebType(getApplicationContext()) != 0) {
                    videoview.setVid(listInfo.get(i).getVideo_id(), 1);
                }else{
                    Toast.makeText(getApplicationContext(), "网络连接异常,请检查网络是否正常！", Toast.LENGTH_LONG).show();
                }
                playTitle.setText(listInfo.get(i).getVideo_name());
            }
        });
    }

    void getData(){

        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    JSONObject jsonObject = new JSONObject((String) obj);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        String data = jsonObject.getString("data");
                        Message msg = new Message();
                        msg.what=100;
                        msg.obj=data;
                        mHandler.sendMessage(msg);
                        // Log.e("data",data);
                    } else {
                        String msg = jsonObject.getString("msg");
//                        ToastUtils.show(mContext, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {//返回值
//                    String err = new String(volleyError.networkResponse.data);
//                    volleyError.networkResponse.statusCode;
            }
        };

        Map<String,String> map = new HashMap<String,String>();
//        map.put("limit",""+limit);
//        map.put("page",""+page);

//        String url = "http://195.198.1.122:8066/eggker/phpv/API.php/video/getGroup?id="+arrPlist[arrPlist.length-1];
        RequestUtils.createRequest(getApplicationContext(), Urls.VIDEO_DETAIL_LIST_URL+arrPlist[arrPlist.length-1], "", true, map, true, listener, errorListener);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.backBtn:
                finish();
                break;

        }
    }

    // 切换到横屏
    public void changeToLandscape() {
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(h, w);
        rl.setLayoutParams(p);
        stopPosition = videoview.getCurrentPosition();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        isLandscape = !isLandscape;
        lin_video_play_top.setVisibility(View.GONE);

    }

    // 切换到竖屏
    public void changeToPortrait() {
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w,w);
        rl.setLayoutParams(p);
        stopPosition = videoview.getCurrentPosition();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        isLandscape = !isLandscape;
        lin_video_play_top.setVisibility(View.VISIBLE);
    }
    // 配置文件设置congfigchange 切屏调用一次该方法，hide()之后再次show才会出现在正确位置
    @Override
    public void onConfigurationChanged(Configuration arg0) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(arg0);
        videoview.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
        mediaController.hide();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        if(videoview.getMediaPlayer()!=null) videoview.getMediaPlayer().release();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(getApplication());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getApplication());
    }

}
