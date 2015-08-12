package com.taihe.eggshell.videoplay;

import tv.danmaku.ijk.media.player.IMediaPlayer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easefun.polyvsdk.PolyvSDKClient;
import com.easefun.polyvsdk.ijk.IjkVideoView;
import com.easefun.polyvsdk.ijk.OnPreparedListener;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by Thinkpad on 2015/7/24.
 */
public class VideoPlayActivity extends BaseActivity {
    private static final String TAG = "VideoPlayActivity";

    private IjkVideoView videoview;
    private MediaController mediaController;
    private ProgressBar progressBar;
    private String path;
    private String vid;
    int w, h;
    private WindowManager wm;
    RelativeLayout rl;
    private boolean isLandscape = false;
    private int stopPosition = 0;
    private boolean encrypt = false;
    float ratio;
    private TextView playTitle;
    @Override
    public void initView() {
        setContentView(R.layout.activity_video_play);
        playTitle = (TextView) findViewById(R.id.txt_video_play_title);
        //initTitle("课程播放");
    }

    @Override
    public void initData() {

        Bundle e = getIntent().getExtras();
        String title="";
        if (e != null) {
            path = e.getString("path");
            vid = e.getString("vid");
            title = e.getString("title");
        }
        if (vid != null && vid.length() > 0) {
            encrypt = true;
        }
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
                videoview.setVid(vid, 1);
            }catch (Exception ex){
                ex.printStackTrace();
            }

        } else {

        }
        changeToPortrait();
        videoview.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(IMediaPlayer mp) {
                // TODO Auto-generated method stub

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
                        //Toast.makeText(IjkFullVideoActivity.this, "VIDEO_LAYOUT_ORIGIN", 1).show();
                        break;
                    case IjkVideoView.VIDEO_LAYOUT_SCALE:
                        //Toast.makeText(IjkFullVideoActivity.this, "VIDEO_LAYOUT_SCALE", 1).show();
                        break;
                    case IjkVideoView.VIDEO_LAYOUT_STRETCH:
                        //Toast.makeText(IjkFullVideoActivity.this, "VIDEO_LAYOUT_STRETCH", 1).show();
                        break;
                    case IjkVideoView.VIDEO_LAYOUT_ZOOM:
                        //Toast.makeText(IjkFullVideoActivity.this, "VIDEO_LAYOUT_ZOOM", 1).show();
                        break;
                }
            }
        });
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
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w, w);
        rl.setLayoutParams(p);
        stopPosition = videoview.getCurrentPosition();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        isLandscape = !isLandscape;
    }

    // 切换到竖屏
    public void changeToPortrait() {
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w,h);
        rl.setLayoutParams(p);
        stopPosition = videoview.getCurrentPosition();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        isLandscape = !isLandscape;
    }
}
