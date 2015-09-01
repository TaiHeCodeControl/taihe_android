package com.taihe.eggshell.base;

import com.taihe.eggshell.base.utils.PrefUtils;

public class Urls {

    public static final String BaseURL = "http://195.198.1.122:8066/eggker/phpyun/api/admin/index.php?";
    //测试
    public static final String BASE_HYR_MOBILE_URL = "http://195.198.1.197/eggker/interface";//测试服务器
//    public static final String BASE_HYR_MOBILE_URL = "http://195.198.1.83/eggker/interface";//测试服务器
    //正式
//    public static final String BASE_HYR_MOBILE_URL = "http://195.198.1.122:8066/eggker/phpyun/api/admin/index.php?";// 正式服务器

    public static final String getMopHostUrl() {
		return PrefUtils.getStringPreference(EggshellApplication.getApplication()
                        .getApplicationContext(), PrefUtils.CONFIG,
                PrefUtils.KEY_APP_REQUEST_URL, BASE_HYR_MOBILE_URL);
	}

	/*********************************************************************/

    public static final String METHOD_REPEAT_PHONE = "/auth/local/existed";//手机号是否重复注册

    public static final String METHOD_LOGIN = "/login/";//登录

    public static final String METHOD_DETAIL = "m=act&c=detail";//
    public static final String METHOD_GET_CODE = "/login/send_code";//获取验证码
    public static final String METHOD_RESET_PASSWORD = "/login/update_pwd";//重置密码
    public static final String METHOD_CHECK_CODE = "/login/next";//判断验证码
    public static final String METHOD_JOB_LIST = "/Position/loadMore";//职位列表

}
