package com.taihe.eggshell.job.bean;

import android.content.Context;

import com.taihe.eggshell.base.utils.PrefUtils;

/**
 * Created by huan on 2015/9/21.
 */
public class JobFilterUtils {

    //hy=>工作行业 职位类别=>job_post 月薪范围=>salary 学历要求=>edu 工作年限=>exp
    // 工作性质=>type
    public static void filterJob(Context mContext,String keyword,String type,String hy,String job1,String job_post,
                          String salary,String edu,String exp,String city,String pubtime,String titleString){
        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "keyword", keyword);
        PrefUtils.saveStringPreferences(mContext,PrefUtils.CONFIG,"type",type);
        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "hy", hy);//工作行业
        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "job1", job1);//工作行业
        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "job_post",job_post);//职位类别
        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "salary", salary);
        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "edu", edu);
        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "exp", exp);//工作年限
        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "cityid", city);//工作城市
        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "fbtime", pubtime);//发布时间

        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "titleString", titleString);//搜索职位
    }
}
