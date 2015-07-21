package com.taihe.eggshell.base;

import android.os.Environment;

public final class Config {
	public static final int FLAG_SPECIAL = 1;
	public static int USER_ID;
	public static long ADDRESS_ID;
	
	/**
	 * 升级的下载路径
	 */
	public static final String UPGRADE_DOWNLOAD_PATH = Environment.getExternalStorageDirectory() + "/"
			+ "eggshell/download/";
	
	/**
	 * 
	 */
	public static final String APK_NAME = "xyb.apk";
}
