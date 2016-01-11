package com.taihe.eggshell.base;

import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;

import com.easefun.polyvsdk.PolyvSDKClient;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.main.entity.User;
import com.taihe.eggshell.videoplay.PolyvDemoService;
import com.umeng.socialize.PlatformConfig;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

public class EggshellApplication extends Application {

    public static EggshellApplication eggApplication;
    private User user;
    private File saveDir;

    //
    private String loginTag = "";

    public void onCreate() {
        eggApplication = this;
        super.onCreate();

        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "polyvSDK/Cache");
//        EggshellCrashHandler.getInstance().init(this);
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        try {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration
                    .Builder(getApplicationContext())
                    .memoryCacheExtraOptions(480, 800)
                    .threadPoolSize(2)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
//					    .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/
                    .memoryCache(new WeakMemoryCache())
                    .memoryCacheSize(2 * 1024 * 1024)
                    .discCacheSize(50 * 1024 * 1024)
//					    .discCacheFileNameGenerator(new Md5FileNameGenerator())//
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .discCacheFileCount(100)
                    .diskCache(new UnlimitedDiskCache(cacheDir))
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                    .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)
                    .writeDebugLogs() // Remove for release app
                    .build();
            // Initialize ImageLoader with configuration.

            //Initialize ImageLoader with configuration
            ImageLoader.getInstance().init(config);

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                saveDir = new File(Environment.getExternalStorageDirectory().getPath() + "/polyvdownload");
                if (!saveDir.exists()) saveDir.mkdir();
            }
            PolyvSDKClient client = PolyvSDKClient.getInstance();
            client.setReadtoken("AAiK2jiX0t-BAnX4n6CrX-xV0TfqPUML");
            client.setWritetoken("ZDYlp4fGF8g100D-TYug02Z14idkcelP");
            client.setPrivatekey("ylzOkbgQcn");
            client.setUserId("00018093b1");
            client.setSign(true);
            client.setDownloadDir(saveDir);
            client.startService(this, PolyvDemoService.class);



            //微信    wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
            PlatformConfig.setWeixin("wxdbe3c106ce5c4205", "d4624c36b6795d1d99dcf0547af5443d");
            //豆瓣RENREN平台目前只能在服务器端配置
            //新浪微博
            PlatformConfig.setSinaWeibo("223688747", "f450919c118dee6cee4396b4b6fa72f8");
//            PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
            PlatformConfig.setQQZone("1104792121", "fWiSdRHtrOfUg6ZI");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static EggshellApplication getApplication() {
        return eggApplication;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 返回用户信息
     *
     * @return
     */
    public User getUser() {
        String json = PrefUtils.getStringPreference(this, PrefUtils.CONFIG, PrefUtils.KEY_USER_JSON, "");
        if (TextUtils.isEmpty(json)) {
            return null;
        } else {
            if (user == null) {
                try {
                    User u = new Gson().fromJson(json, User.class);
                    return u;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return user;
            }
        }
    }


    public String getLoginTag() {
        return loginTag;
    }

    public void setLoginTag(String loginTag) {
        this.loginTag = loginTag;
    }
}
