package com.taihe.eggshell.base;

import com.taihe.eggshell.base.utils.PrefUtils;

public class Urls {

    //测试
//    public static final String BASE_HYR_MOBILE_URL = "http://api.dev.yuguo.cn";//测试服务器
//    public static final String METHOD_SHARE_DOCTOR = "http://dev.yuguo.cn:1338/mobile/doctor/";//分享医生
//    public static final String METHOD_SHARE_HOSPITAL = "http://dev.yuguo.cn:1338/mobile/hostpial/";//分享医院

    //正式
    public static final String BASE_HYR_MOBILE_URL = "http://api.yuguo.cn";// 正式服务器192.168.1.14:1337
    public static final String METHOD_SHARE_DOCTOR = "http://m.yuguo.cn/doctor/";//分享医生
    public static final String METHOD_SHARE_HOSPITAL = "http://m.yuguo.cn/hostpial/";//分享医院

	public static final String getMopHostUrl() {
		return PrefUtils.getStringPreference(EggshellApplication.getApplication()
                        .getApplicationContext(), PrefUtils.CONFIG,
                PrefUtils.KEY_APP_REQUEST_URL, BASE_HYR_MOBILE_URL);
	}

	/*********************************************************************/

    public static final String METHOD_REPEAT_PHONE = "/auth/local/existed";//手机号是否重复注册

}
