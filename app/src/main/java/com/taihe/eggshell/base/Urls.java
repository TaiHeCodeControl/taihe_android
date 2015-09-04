package com.taihe.eggshell.base;

import com.taihe.eggshell.base.utils.PrefUtils;

public class Urls {

    public static final String BaseURL = "http://195.198.1.122:8066/eggker/phpyun/api/admin/index.php?";
    //测试
    public static final String BASE_HYR_MOBILE_URL = "http://195.198.1.120/eggker/interface";//测试服务器
    //正式
//    public static final String BASE_HYR_MOBILE_URL = "http://195.198.1.122:8066/eggker/phpyun/api/admin/index.php?";// 正式服务器

    public static final String getMopHostUrl() {
		return PrefUtils.getStringPreference(EggshellApplication.getApplication()
                        .getApplicationContext(), PrefUtils.CONFIG,
                PrefUtils.KEY_APP_REQUEST_URL, BASE_HYR_MOBILE_URL);
	}

	/*********************************************************************/

    public static final String METHOD_LOGIN = "/login";//登录
    public static final String METHOD_REGIST = "/register";//注册
    public static final String METHOD_REGIST_GETCODE = "/register/chTelphone";//注册获取验证码

    public static final String METHOD_GET_CODE = "/login/send_code";//获取验证码
    public static final String METHOD_RESET_PASSWORD = "/login/update_pwd";//重置密码
    public static final String METHOD_CHECK_CODE = "/login/next";//判断验证码
    public static final String METHOD_JOB_LIST = "/Position/loadMore";//职位列表
    public static final String METHOD_JOB_LIST_COLLECT = "/Position/collectlist";//收藏职位列表
    public static final String METHOD_JOB_LIST_COLLECT_DELETE = "/Position/delcollectlist";//收藏职位列表
    public static final String METHOD_JOB_DETAIL = "/Position/details";//职位详情
    public static final String METHOD_JOB_COLLECT = "/Position/collect";//职位收藏
    public static final String METHOD_STATIC_DATA = "/Except/except_select";//静态数据
    public static final String METHOD_CREATE_RESUME = "/Except/except";//创建简历
    public static final String METHOD_UPDATE = "/update";//版本更新
    public static final String METHOD_RESUME_TECH = "/Except/resume_skill";//简历--技能
    public static final String METHOD_GET_RESUME = "/Except/resume_manager";//简历管理
    public static final String METHOD_DELETE_RESUME = "/Except/resume_del";//删除简历
    public static final String METHOD_RESUME_SCAN = "/Except/resume_preview";//简历预览

    public static final String NEARBY_URL = "http://195.198.1.83/eggker/interface/Activity/index?";//玩出范
    public static final String RESUME_WORK_URL = "http://195.198.1.120/eggker/interface/Except/resume_work";//工作经历
    public static final String RESUME_EDU_URL = "http://195.198.1.120/eggker/interface/Except/resume_edu";//教育经历
    public static final String RESUME_TRAIN_URL = "http://195.198.1.120/eggker/interface/Except/resume_training";//培训预览
    public static final String RESUME_PROJECT_URL = "http://195.198.1.120/eggker/interface/Except/resume_project";//项目经验
    public static final String RESUME_BOOK_URL = "http://195.198.1.120/eggker/interface/Except/resume_cert";//证书
    public static final String RESUME_OTHER_URL = "http://195.198.1.120/eggker/interface/Except/resume_other";//自我评价
    public static final String VIDEO_LIST_URL = "http://195.198.1.211/eggker/phpv/api.php/video/getPageList";//公开课

}
