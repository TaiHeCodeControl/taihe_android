package com.taihe.eggshell.base;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.main.entity.User;

public class EggshellApplication extends Application {

	public static EggshellApplication hyrApplication;
	private User user;

	public void onCreate() {
		hyrApplication = this;
        super.onCreate();
//        EggshellCrashHandler.getInstance().init(this);

		//在使用SDK各组件之前初始化context信息，传入ApplicationContext
		//注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
    }

	public static EggshellApplication getApplication() {
		return hyrApplication;
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
        String json = PrefUtils.getStringPreference(this, PrefUtils.CONFIG,PrefUtils.KEY_USER_JSON, "");
        if(TextUtils.isEmpty(json)){
            return null;
        }else{
            if (user == null) {
                try {
                    User u = new Gson().fromJson(json, User.class);
                    return u;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }else{
                return user;
            }
        }
	}

    /**
     * 获取版本的名称
     *
     * @param
     * @return
     */
    public String getVersionName() {
        try {
            PackageInfo packageInfo = hyrApplication.getPackageManager()
                    .getPackageInfo(hyrApplication.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return "";
    }

    /**
     * 获取应用的版本号
     *
     * @param
     * @return
     */
    public int getVersionCode(){
        try{
            PackageInfo packageInfo = hyrApplication.getPackageManager().getPackageInfo(hyrApplication.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return  packageInfo.versionCode;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return 1;
    }
}
