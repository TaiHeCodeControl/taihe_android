package com.taihe.eggshell.base;

import android.os.Environment;

/**
 * Created by tiahe on 2015/7/20.
 */
public class Constants {

    /**
     * 升级包缓存的名称
     */
    public static final String UPDATE_PACKAGE_NAME = "eggshel_upgrade.apk";
    public static final String APK_NAME = "xyb.apk";
    public static final int PAY_TYPE_CASH_DISCOUNT = 1;

    /**
     * 升级的下载路径
     */
    public static final String UPGRADE_DOWNLOAD_PATH = Environment.getExternalStorageDirectory() + "/"
            + "eggshell/download/";

}
