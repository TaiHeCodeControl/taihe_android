package com.taihe.eggshell.base;

import com.taihe.eggshell.base.utils.PrefUtils;

public class Urls {

    //正式
    public static final String BASE_HYR_MOBILE_URL = "http://ceshi.tiahel.com/eggker/interface/index.php";

    //测试
//    public static final String BASE_HYR_MOBILE_URL = "http://195.198.1.120/eggker/interface";

    public static final String getMopHostUrl() {
        return PrefUtils.getStringPreference(EggshellApplication.getApplication().getApplicationContext(), PrefUtils.CONFIG,
                PrefUtils.KEY_APP_REQUEST_URL, BASE_HYR_MOBILE_URL);
    }

    /**
     * *****************************************************************
     */
//
    public static final String METHOD_REGIST = BASE_HYR_MOBILE_URL+"/register";//注册
    public static final String FEEDBACK_URL = "/Feedback/index";//意见反馈
    public static final String METHOD_REGIST_GETCODE = BASE_HYR_MOBILE_URL+"/register/chTelphone";//注册获取验证码

//    public static final String METHOD_REGIST_LOGOUT = "http://195.198.1.211/eggker/interface/login/loginout";//退出登录
//    public static final String METHOD_BASIC = "http://195.198.1.211/eggker/interface/basicdata";//基本资料
//    public static final String METHOD_UPLOAD_IMAGE = "http://195.198.1.211/eggker/interface/basicdata/head";//上传头像

    public static final String METHOD_LOGIN = BASE_HYR_MOBILE_URL + "/login";//登录
    public static final String METHOD_REGIST_LOGOUT = BASE_HYR_MOBILE_URL + "/login/loginout";//退出登录

    public static final String METHOD_BASIC = BASE_HYR_MOBILE_URL+"/basicdata";//基本资料
    public static final String METHOD_BASIC_SAVE = BASE_HYR_MOBILE_URL+"/basicdata/add_basicdata";//保存修改的基本资料
    public static final String METHOD_UPLOAD_IMAGE = BASE_HYR_MOBILE_URL+"/basicdata/head";//上传头像

    public static final String METHOD_GET_CODE = "/login/send_code";//获取验证码
    public static final String METHOD_RESET_PASSWORD = "/login/update_pwd";//重置密码
    public static final String METHOD_CHECK_CODE = "/login/next";//判断验证码


    public static final String METHOD_JOB_LIST = BASE_HYR_MOBILE_URL + "/Position/nearbycompany";//职位列表
    public static final String METHOD_JOB_LIST_COLLECT = BASE_HYR_MOBILE_URL + "/Position/collectlist";//收藏职位列表
    public static final String METHOD_JOB_LIST_POST = BASE_HYR_MOBILE_URL + "/Position/getPositionlist";//申请职位列表
    public static final String METHOD_JOB_LIST_COLLECT_DELETE = BASE_HYR_MOBILE_URL + "/Position/delcollectlist";//删除收藏职位
    public static final String METHOD_JOB_LIST_POST_DELETE = BASE_HYR_MOBILE_URL + "/Position/delgetPosition";//删除申请职位


    public static final String METHOD_JOB_DETAIL = BASE_HYR_MOBILE_URL+"/Position/details";//职位详情
    public static final String METHOD_JOB_COLLECT = BASE_HYR_MOBILE_URL+"/Position/collect";//职位收藏
    public static final String METHOD_JOB_POST = BASE_HYR_MOBILE_URL+"/Position/getPosition";//申请职位

    public static final String METHOD_STATIC_DATA_JOB = "/Position/except_select";//职位列表筛选
    public static final String METHOD_STATIC_DATA = "/Except/except_select";//静态数据
    public static final String METHOD_CREATE_RESUME = "/Except/except";//创建简历
    public static final String METHOD_UPDATE = "/update";//版本更新
    public static final String METHOD_RESUME_TECH = "/Except/resume_skill";//简历--技能
    public static final String METHOD_GET_RESUME = "/Except/resume_manager";//简历管理
    public static final String METHOD_DELETE_RESUME = "/Except/resume_del";//删除简历
    public static final String METHOD_RESUME_SCAN = "/Except/resume_preview";//简历预览

    public static final String NEARBY_URL = BASE_HYR_MOBILE_URL + "/Activity/index?";//玩出范
    public static final String RESUME_WORK_URL = BASE_HYR_MOBILE_URL + "/Except/resume_work";//工作经历
    public static final String RESUME_EDU_URL = BASE_HYR_MOBILE_URL + "/Except/resume_edu";//教育经历
    public static final String RESUME_TRAIN_URL = BASE_HYR_MOBILE_URL + "/Except/resume_training";//培训预览
    public static final String RESUME_PROJECT_URL = BASE_HYR_MOBILE_URL + "/Except/resume_project";//项目经验
    public static final String RESUME_BOOK_URL = BASE_HYR_MOBILE_URL + "/Except/resume_cert";//证书
    public static final String RESUME_OTHER_URL = BASE_HYR_MOBILE_URL + "/Except/resume_other";//自我评价
    public static final String VIDEO_LIST_URL = "http://195.198.1.211/eggker/phpv/api.php/video/getPageList";//公开课
    public static final String MEETING_LIST_URL = BASE_HYR_MOBILE_URL + "/Infos/index";//信息台
    public static final String COMPY_LIST_URL = BASE_HYR_MOBILE_URL + "/Position/recommend_company";//名企推荐
    public static final String COMPY_DETAIL_URL = BASE_HYR_MOBILE_URL + "/Position/company_detail";//名企详情

}
