package com.taihe.eggshell.base.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.taihe.eggshell.base.EggshellApplication;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class APKUtils {


    /**
     * 获取版本的名称
     *
     * @param
     * @return
     */
    public static String getVersionName() {
        try {
            PackageInfo packageInfo = EggshellApplication.getApplication().getPackageManager()
                    .getPackageInfo(EggshellApplication.getApplication().getPackageName(),
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
            PackageInfo packageInfo = EggshellApplication.getApplication().getPackageManager().getPackageInfo(EggshellApplication.getApplication().getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return  packageInfo.versionCode;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return 1;
    }

}
