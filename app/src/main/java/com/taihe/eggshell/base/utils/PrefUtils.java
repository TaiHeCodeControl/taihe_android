package com.taihe.eggshell.base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences工具类
 * 
 * @author wangshaobin
 * 
 */
public class PrefUtils {
    private static SharedPreferences sharedPreferences;
	/**
	 * SharedPreferences配置文件：config
	 */
	public static final String CONFIG = "yg_config";

	/**
	 * APP配置：md5
	 */
	public static final String KEY_APP_MD5 = "key_app_md5";
	/**
	 * APP配置：请求URL
	 */
	public static final String KEY_APP_REQUEST_URL = "key_app_requesturl";
	/**
	 * APP配置：offline
	 */
	public static final String KEY_APP_OFFLINE = "key_app_offline";
	/**
	 * APP配置：deleted
	 */
	public static final String KEY_APP_DELETED = "key_app_deleted";
	/**
	 * APP配置：md5
	 */
	public static final String KEY_APP_DEBUG = "key_app_debug";

	/**
	 * 第一次登录
	 */
	public static final String KEY_FIRST_LOGIN = "key_first_login";

    /**
     * 第一次使用应用
     */
    public static final String KEY_FIRST_USE = "key_first_use";


    /**
	 * 上次登录手机号码
	 */
	public static final String KEY_PHONE_NUMBER = "phone_number";
	/**
	 * 上次登录手机序列号
	 */
	public static final String KEY_SIM_SERIAL_NUMBER = "sim_serial_number";

	public static final String KEY_ID = "id";
	public static final String KEY_TOKEN = "token";
	/**
	 * 登录返回用户信息
	 */
	public static final String KEY_USER_JSON = "key_user_json";

    public static final String KEY_USER_LOCATION = "user_location";
    public static final String KEY_USER_LOCATION_KEY = "userlocation";
    public static final String KEY_USER_LOCATION_DEFAULTVALUE = "1";
    public static final String KEY_QINIU = "qiniu";
    public static final String KEY_QINIU_KEY = "qiniu_key";
    public static final String KEY_QINIU_DEFAULT = "7xikx2.com2.z0.glb.qiniucdn.com";
    public static final String KEY_USER_TOKEN = "token";
    public static final String USER_TOKEN = "user_token";

	public static void saveStringPreferences(Context context, String name,
			String key, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				name, Context.MODE_MULTI_PROCESS);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getStringPreference(Context context, String name,
			String key, String defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				name, Context.MODE_MULTI_PROCESS);
		return sharedPreferences.getString(key, defaultValue);
	}

    public static void setUserCityPreference(Context context,String prename,String key,String value){
        SharedPreferences sp = context.getSharedPreferences(prename, Context.MODE_MULTI_PROCESS);
        Editor editor = sp.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String getUserCityPreference(Context context,String prename,String key,String defaultValue){
        return context.getSharedPreferences(prename,Context.MODE_MULTI_PROCESS).getString(key,defaultValue);
    }

    public static void setQiNiuCDN(Context context,String prename,String key,String value){
        SharedPreferences sp = context.getSharedPreferences(prename, Context.MODE_MULTI_PROCESS);
        Editor editor = sp.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String getQiNiuCDN(Context context,String prename,String key,String defaultValue){
        return context.getSharedPreferences(prename,Context.MODE_MULTI_PROCESS).getString(key,defaultValue);
    }
    public static void saveBooleanData(Context context,String key,boolean value){
        if(sharedPreferences==null){
            sharedPreferences=context.getSharedPreferences(CONFIG,Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putBoolean(key,value).commit();
    }
    public static boolean getBooleanData(Context context,String key,boolean defValue){
        if(sharedPreferences==null){
            sharedPreferences=context.getSharedPreferences(CONFIG,Context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(key,defValue);
    }

}
