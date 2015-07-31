package com.taihe.eggshell.videoplay;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Thinkpad on 2015/7/24.
 */
public class VideoPlayActivity extends BaseActivity{

    private static final String TAG = "VideoPlayActivity";

    private final static int NETWORK_PARSE_ERROR = 0;
    private final static int VIDEO_FILE_ERROR = 1;
    private final static int VIDEO_STATE_BEGIN = 2;
    private final static int VIDEO_CACHE_FINISH = 3;
    private final static int VIDEO_UPDATE_SEEKBAR = 5;

    private Button playBtn,backBtn,changeDirection;  //用于开始和暂停的按钮
    private SurfaceView videoSurfaceView;   //绘图容器对象，用于把视频显示在屏幕上
    private String url;   //视频播放地址
    private MediaPlayer mediaPlayer;    //播放器控件
    private int postSize=0;    //保存已播视频大小
    private SeekBar seekbar;   //进度条控件
    private boolean flag = true;   //用于判断视频是否在播放中
    private RelativeLayout opLy;
    private boolean display;   //用于是否显示其他按钮
    private ProgressBar loadingVideoV;
    private TextView cacheT;
    private UpdateSeekBarR updateSeekBarR;   //更新进度条用

    private long mediaLength = 0 , readSize = 0;
    private HttpURLConnection httpConnection;
    private FileOutputStream out = null;
    private InputStream is = null;

    @Override
    public void initView() {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 应用运行时，保持屏幕高亮，不锁屏

        setContentView(R.layout.activity_video_play);
        url = "http://zqgbzx.cn:6060/zwapi/videos/ZQ0005.mp4";//视频播放地址
//		url =Environment.getExternalStorageDirectory().getAbsolutePath()+"/9533522808.f4v.mp4";

        mediaPlayer = new MediaPlayer();
        updateSeekBarR = new UpdateSeekBarR();//创建更新进度条对象

        seekbar = (SeekBar) findViewById(R.id.seekBar);
        opLy = (RelativeLayout) findViewById(R.id.opLy);
        loadingVideoV = (ProgressBar) findViewById(R.id.loadingVideo);
        cacheT = (TextView) findViewById(R.id.cacheT);
        backBtn = (Button) findViewById(R.id.backBtn);
        changeDirection = (Button)findViewById(R.id.id_change_direction);
        playBtn = (Button) findViewById(R.id.playBtn);
        playBtn.setEnabled(false); //刚进来，设置其不可点击

        videoSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        videoSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);   //不缓冲
        videoSurfaceView.getHolder().setKeepScreenOn(true);   //保持屏幕高亮
        videoSurfaceView.getHolder().addCallback(new VideoSurfaceView());//设置监听事件,从这里监听surface创建完成开始播放视频

    }

    @Override
    public void initData() {

        videoSurfaceView.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        changeDirection.setOnClickListener(this);

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {//缓冲去更新
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
            }
        });
        //播放完毕监听
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { //视频播放完成
            @Override
            public void onCompletion(MediaPlayer mp) {
                flag = false;
                playBtn.setBackgroundResource(R.drawable.btn_play);
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = seekbar.getProgress() * mediaPlayer.getDuration() / seekbar.getMax();  //计算进度条需要前进的位置数据大小
                mediaPlayer.seekTo(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.backBtn:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                mediaPlayer = null;
                finish();
                break;
            case R.id.playBtn:
                if (mediaPlayer.isPlaying()) {
                    playBtn.setBackgroundResource(R.drawable.btn_play);
                    mediaPlayer.pause();
                    postSize = mediaPlayer.getCurrentPosition();
                } else {
                    if (flag == false) {
                        flag = true;
                        new Thread(updateSeekBarR).start();
                    }
                    mediaPlayer.start();
                    playBtn.setBackgroundResource(R.drawable.btn_pause);
                }
                break;
            case R.id.surfaceView:
                if (display) {
                    backBtn.setVisibility(View.GONE);
                    changeDirection.setVisibility(View.GONE);
                    opLy.setVisibility(View.GONE);
                    display = false;
                } else {
                    backBtn.setVisibility(View.VISIBLE);
                    changeDirection.setVisibility(View.VISIBLE);
                    opLy.setVisibility(View.VISIBLE);
                    videoSurfaceView.setVisibility(View.VISIBLE);

                    //设置播放为全屏
					/*ViewGroup.LayoutParams lp = videoSurfaceView.getLayoutParams();
					lp.height = LayoutParams.FILL_PARENT;
					lp.width = LayoutParams.FILL_PARENT;
					videoSurfaceView.setLayoutParams(lp);*/
                    display = true;
                }
                break;
            case R.id.id_change_direction:
                if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }else{
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
        }
    }

    //视频播放视图回调事件//上面绑定的监听的事件
    private class VideoSurfaceView implements SurfaceHolder.Callback {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,int height){}

        @Override
        public void surfaceCreated(SurfaceHolder holder) {//创建完成后调用
            //不是网络视频,直接播放
            if(URLUtil.isNetworkUrl(url)) {
                try {
//                    new Thread(new CacheNetFileR()).start();
                    new CacheVideoFile().execute();
                }catch (Exception e) {
                    mHandler.sendEmptyMessage(NETWORK_PARSE_ERROR);
                    e.printStackTrace();
                }
            }else{
                File videoFile=new File(url);
                if(videoFile.exists()){
                    readSize = mediaLength = videoFile.length();
                    mHandler.sendEmptyMessage(VIDEO_STATE_BEGIN);
                }
            }
        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) { //activity调用过pause方法，保存当前播放位置
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                postSize = mediaPlayer.getCurrentPosition();
                mediaPlayer.stop();
                flag = false;
                loadingVideoV.setVisibility(View.VISIBLE);
            }
        }
    }

    //播放视频的方法
    private void playMediaMethod(){
        if (postSize > 0 && url!= null) {    //说明，停止过 activity调用过pause方法，跳到停止位置播放
            new PlayMovieT(postSize).start();//从postSize位置开始放
            flag = true;
            int sMax = seekbar.getMax();
            int mMax = mediaPlayer.getDuration();
            seekbar.setProgress(postSize * sMax / mMax);

            loadingVideoV.setVisibility(View.GONE);
        }else {
            new PlayMovieT(0).start();//表明是第一次开始播放
        }
    }

    //播放视频的线程
    class PlayMovieT extends Thread {
        int post = 0;
        public PlayMovieT(int post) {
            this.post = post;
        }
        @Override
        public void run() {
            try {
                mediaPlayer.reset();    //回复播放器默认
                mediaPlayer.setDataSource(url);   //设置播放路径
                mediaPlayer.setDisplay(videoSurfaceView.getHolder());  //把视频显示在SurfaceView上
                mediaPlayer.setOnPreparedListener(new VideoPreparedL(post));  //设置监听事件
                mediaPlayer.prepare();  //准备播放
            } catch (Exception e) {
                mHandler.sendEmptyMessage(VIDEO_FILE_ERROR);
            }
            super.run();
        }
    }

    //视频播放视图准备事件监听器
    class VideoPreparedL implements MediaPlayer.OnPreparedListener {
        int postSize;

        public VideoPreparedL(int postSize) {
            this.postSize = postSize;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {//准备完成
            loadingVideoV.setVisibility(View.GONE);  //准备完成后，隐藏控件
            backBtn.setVisibility(View.GONE);
            changeDirection.setVisibility(View.GONE);
            opLy.setVisibility(View.GONE);

            display = false;
            if (mediaPlayer != null) {
                mediaPlayer.start();  //开始播放视频
            } else {
                return;
            }
            if (postSize > 0) {  //说明中途停止过（activity调用过pause方法，不是用户点击停止按钮），跳到停止时候位置开始播放
                mediaPlayer.seekTo(postSize);   //跳到postSize大小位置处进行播放
            }
            new Thread(updateSeekBarR).start();   //启动线程，更新进度条
        }
    }

    //每隔1秒更新一下进度条线程
    class UpdateSeekBarR implements Runnable {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(VIDEO_UPDATE_SEEKBAR);
            if (flag) {
                mHandler.postDelayed(updateSeekBarR, 1000);
            }
        }
    }

    @Override
    protected void onDestroy() {   //activity销毁后，释放资源
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (out != null && is != null) {
            try {
                is.close();
                out.flush();
                out.close();
            } catch (IOException e){
            }
        }

        if(null!=httpConnection){
            httpConnection.disconnect();
        }

//        System.gc();
    }

    private class CacheVideoFile extends AsyncTask<String,Double,String>{

        @Override
        protected String doInBackground(String... params) {

            try {
                HttpURLConnection httpConnectionLength = (HttpURLConnection)new URL(url).openConnection();
                long contentLength = httpConnectionLength.getContentLength();
                httpConnectionLength.disconnect();

                httpConnection = (HttpURLConnection)new URL(url).openConnection();

                String cacheUrl = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Cache/"+url.substring(url.lastIndexOf("/")+1);
                File cacheFile = new File(cacheUrl);
                boolean isNeedCache=false;
                if (cacheFile.exists()) {
                    readSize = mediaLength = cacheFile.length();
                    Log.v(TAG,"SDC长度："+readSize+";"+contentLength);
                    if(contentLength == readSize){//视频已经成功缓存完成
                        url=cacheUrl;
                        isNeedCache=false;
                    }else{
                        //先讲属性设置好,不然getContentLength之后已经打开连接了,不能再设置了
                        httpConnection.setRequestProperty("User-Agent", "NetFox");
                        httpConnection.setRequestProperty("Range", "bytes=" + readSize + "-");//从断点处请求读取文件
                        isNeedCache=true;
                    }
                }else{
                    cacheFile.getParentFile().mkdirs();
                    cacheFile.createNewFile();
                    isNeedCache=true;
                }

                mHandler.sendEmptyMessage(VIDEO_STATE_BEGIN);//开始播放视频

                if(isNeedCache){//需要缓存视频
                    out = new FileOutputStream(cacheFile, true);

                    is = httpConnection.getInputStream();
                    mediaLength = httpConnection.getContentLength();

                    if(-1 == mediaLength) {
//                        System.out.println("-------------视频文件缓存失败,不能成功获取文件大小!");
                        return null;
                    }
                    mediaLength += readSize;

                    byte buf[] = new byte[4 * 1024];
                    int size = 0;

                    while(is!=null && (size = is.read(buf)) != -1){//缓存文件
//                        System.out.println("--------------缓存文件部分:"+size);
                        try {
                            out.write(buf, 0, size);
                            readSize += size;
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    url=cacheUrl;//将url替换成本地
                    mHandler.sendEmptyMessage(VIDEO_CACHE_FINISH);//视频缓存结束,将当前视频切换成播放本地的文件
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                if (out != null && is != null) {
                    try {
                        is.close();
                        out.flush();
                        out.close();

                    } catch (IOException e){
                    }
                }
            }
            return null;
        }
    }

    //在线播放时后台缓存文件,方便下次直接播放
    private class CacheNetFileR implements Runnable{
        @Override
        public void run() {

            try {
                HttpURLConnection httpConnectionLength = (HttpURLConnection)new URL(url).openConnection();
                long contentLength = httpConnectionLength.getContentLength();
                httpConnectionLength.disconnect();

                httpConnection = (HttpURLConnection)new URL(url).openConnection();

                String cacheUrl = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Cache/"+url.substring(url.lastIndexOf("/")+1);
                File cacheFile = new File(cacheUrl);
                boolean isNeedCache=false;
                if (cacheFile.exists()) {
                    readSize = mediaLength = cacheFile.length();
                    Log.v(TAG,"SDC长度："+readSize+";"+contentLength);
                    if(contentLength == readSize){//视频已经成功缓存完成
                        url=cacheUrl;
                        isNeedCache=false;
                    }else{
                        //先讲属性设置好,不然getContentLength之后已经打开连接了,不能再设置了
                        httpConnection.setRequestProperty("User-Agent", "NetFox");
                        httpConnection.setRequestProperty("Range", "bytes=" + readSize + "-");//从断点处请求读取文件
                        isNeedCache=true;
                    }
                }else{
                    cacheFile.getParentFile().mkdirs();
                    cacheFile.createNewFile();
                    isNeedCache=true;
                }

                mHandler.sendEmptyMessage(VIDEO_STATE_BEGIN);//开始播放视频

                if(isNeedCache){//需要缓存视频
                    out = new FileOutputStream(cacheFile, true);

                    is = httpConnection.getInputStream();
                    mediaLength = httpConnection.getContentLength();

                    if(-1 == mediaLength) {
                        System.out.println("-------------视频文件缓存失败,不能成功获取文件大小!");
                        return;
                    }
                    mediaLength += readSize;

                    byte buf[] = new byte[4 * 1024];
                    int size = 0;

                    while((size = is.read(buf)) != -1){//缓存文件
                        System.out.println("--------------缓存文件部分:"+size);
                        try {
                            out.write(buf, 0, size);
                            readSize += size;
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    url=cacheUrl;//将url替换成本地
                    mHandler.sendEmptyMessage(VIDEO_CACHE_FINISH);//视频缓存结束,将当前视频切换成播放本地的文件
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (is != null && out != null) {
                    try {
                        is.close();
                        out.flush();
                        out.close();
                    } catch (IOException e){
                    }
                }
            }
        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NETWORK_PARSE_ERROR://错误信息
                    Toast.makeText(getApplicationContext(), "网络连接错误,不能完成播放!", Toast.LENGTH_LONG).show();
                    loadingVideoV.setVisibility(View.GONE);
                    break;
                case VIDEO_FILE_ERROR://错误信息
                    Toast.makeText(getApplicationContext(), "视频文件错误,不能完成播放!",Toast.LENGTH_LONG).show();
                    loadingVideoV.setVisibility(View.GONE);
                    break;
                case VIDEO_STATE_BEGIN://开始播放视频
                    playMediaMethod();
                    playBtn.setEnabled(true);
                    playBtn.setBackgroundResource(R.drawable.btn_pause);
                    break;
                case VIDEO_CACHE_FINISH://视频缓存完成,使用本地文件播放
                    Toast.makeText(getApplicationContext(), "视频已经缓存完毕,正在切换成本地文件播放!",Toast.LENGTH_LONG).show();
                    postSize = mediaPlayer.getCurrentPosition();
                    playMediaMethod();
                    break;
                case VIDEO_UPDATE_SEEKBAR:// 更新进度条
                    if (mediaPlayer == null) {
                        flag = false;
                    }else{
                        double cachepercent = readSize * 100.00 / mediaLength * 1.0;
                        String s = String.format("缓存:[%.2f%%]", cachepercent);
                        seekbar.setSecondaryProgress((int)cachepercent);
                        if(mediaPlayer.isPlaying()) {
                            flag = true;
                            int position = mediaPlayer.getCurrentPosition();
                            int mMax = mediaPlayer.getDuration();
                            int sMax = seekbar.getMax();
                            seekbar.setProgress(position * sMax / mMax);

                            mMax = 0 == mMax ? 1 : mMax;
                            double playpercent = position * 100.00 / mMax * 1.0;

                            int i = position / 1000;
                            int hour = i / (60 * 60);
                            int minute = i / 60 % 60;
                            int second = i % 60;

                            s += String.format(" 当前:%02d:%02d:%02d [%.2f%%]", hour, minute, second, playpercent);
                        }else{
                            s += "";
                        }
                        cacheT.setText(s);
                    }
                    break;
                default:
                    break;
            }
        };
    };

}
