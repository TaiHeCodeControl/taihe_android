package com.taihe.eggshell.base;

import android.app.Application;

import android.os.Environment;
import android.text.TextUtils;


import com.easefun.polyvsdk.PolyvSDKClient;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.main.entity.User;
import com.taihe.eggshell.videoplay.PolyvDemoService;

import java.io.File;

public class EggshellApplication extends Application {

    public static EggshellApplication eggApplication;
    private User user;
    private File saveDir;


    public void onCreate() {
        eggApplication = this;
        super.onCreate();


        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "polyvSDK/Cache");
//        EggshellCrashHandler.getInstance().init(this);

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
                    .discCache(new UnlimitedDiscCache(cacheDir))
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                    .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)��ʱʱ��
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



}
