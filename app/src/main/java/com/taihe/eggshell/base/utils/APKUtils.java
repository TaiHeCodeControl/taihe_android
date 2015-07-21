package com.taihe.eggshell.base.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.taihe.eggshell.base.EggshellApplication;

public class APKUtils {
	
	
	public static PackageInfo getPackageInfo(String fullFileName){
		PackageManager packageManager = EggshellApplication.getApplication().getPackageManager();
		return packageManager.getPackageArchiveInfo(fullFileName, PackageManager.GET_ACTIVITIES);
	}
	
	public static int getVersionCode(String fullFileName){
		return getPackageInfo(fullFileName).versionCode;
	}
	
	public static String getVerisonName(String fullFileName){
		return getPackageInfo(fullFileName).versionName;
	}
	
	public static String getUID(String fullFileName){
		return getPackageInfo(fullFileName).sharedUserId;
	}
}
