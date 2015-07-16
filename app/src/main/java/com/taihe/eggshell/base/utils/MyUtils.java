package com.taihe.eggshell.base.utils;

import android.text.TextUtils;

/**
 * Created by mh on 2015/7/15.
 */
public class MyUtils {

    /**
     * Verify the phone
     */
    public static boolean isMobileNO(String mobiles) {
		/*
		 * CMCC ：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * China Unicom：130、131、132、152、155、156、185、186
		 * China Telecom：133、153、180、189、（1349）
		 *
		 */
        String telRegex = "[1][3458]\\d{9}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * Verify the zip code
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
     * Verify the email
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
}
