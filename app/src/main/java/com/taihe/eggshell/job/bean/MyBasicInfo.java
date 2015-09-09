package com.taihe.eggshell.job.bean;

/**
 * 标示职位信息
 * Created by huan on 2015/8/10.
 */
public class MyBasicInfo {

    public String code;
    public String message;

    public BasicBean data;

        public class BasicBean{
            public String address;
            public String description;
            public String email;
            public String name;
            public String reg_date;
            public String sex;
            public String telphone;
            public String uid;
            public String birthday;
    }


}
