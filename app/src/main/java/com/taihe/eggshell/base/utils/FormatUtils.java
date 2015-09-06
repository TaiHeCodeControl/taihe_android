package com.taihe.eggshell.base.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.chinaway.framework.swordfish.util.MD5Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bei on 2015/7/15.
 */
public class FormatUtils {

    /**
     * phone
     */
    public static boolean isMobileNO(String mobiles) {
		/*
		 * ：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * ：130、131、132、152、155、156、185、186
		 * ：133、153、180、189、（1349）
		 *
		 */
        String telRegex = "[1][3458]\\d{9}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * eamil
     * @param post
     * @return
     */
    public static boolean isZip(String post) {
        if (post.matches("[1-9]\\d{5}(?!\\d)")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) {
            return true;
        } else {
            return false;
        }
    }

    public static void getMD5(String str){
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            byte[] m = digest.digest();
            StringBuilder sb = new StringBuilder();
            for(int i = 0;i<m.length;i++){
                sb.append(m[i]);
            }

            Log.v("MD5Utils:",MD5Utils.encode(str)+ "==" +sb.toString());


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String timestampToDatetime(String seconds){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = "";
        try {
            time = simpleDateFormat.format(new Date(Long.valueOf(seconds+"000")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

}
