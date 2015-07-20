package com.taihe.eggshell.base;

import android.app.Application;

import com.google.gson.Gson;
import com.taihe.eggshell.base.utils.PrefUtils;

public class EggshellApplication extends Application {
	public static EggshellApplication hyrApplication;
	private User user;
    public static boolean isCop = true;

	public void onCreate() {
		hyrApplication = this;
        super.onCreate();
//        EggshellCrashHandler.getInstance().init(this);
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
