package com.taihe.eggshell.base;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.taihe.eggshell.base.utils.PrefUtils;

public class EggshellApplication extends Application {
	public static EggshellApplication hyrApplication;
	private User user;

	public void onCreate() {
		hyrApplication = this;
        super.onCreate();
//        EggshellCrashHandler.getInstance().init(this);
    }

	/**
	 * 获取版本的名称
	 *
	 * @param
	 * @return
	 */
	public static String getVersionName() {
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
	public static int getVersionCode(){
		try{
			PackageInfo packageInfo = hyrApplication.getPackageManager().getPackageInfo(hyrApplication.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			return  packageInfo.versionCode;
		}catch (PackageManager.NameNotFoundException e){
			e.printStackTrace();
		}
		return 1;
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
		if (user == null) {
			String json = PrefUtils.getStringPreference(this, PrefUtils.CONFIG,
                    PrefUtils.KEY_USER_JSON, "");
			try {
				User u = new Gson().fromJson(json, User.class);
				return u;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return user;
	}
}
