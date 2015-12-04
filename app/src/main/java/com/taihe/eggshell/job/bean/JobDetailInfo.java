package com.taihe.eggshell.job.bean;

import java.util.List;

/**
 * Created by huan on 2015/9/2.
 */
public class JobDetailInfo {

    public int code;
    public JobDetails data;


    public class JobDetails {

        public String address;
        public String cj_description;
        public String cj_id;
        public String cj_name;
        public String com_id;
        public String com_name;
        public String content;
        public String datetime;
        public String edate;
        public String edu;
        public String exp;
        public String hy;
        public String iscollect;
        public String id;
        public String job_id;
        public String job_name;
        public String lastupdate;
        public String linkmail;
        public String linktel;

        public String mun;
        public String name;
        public String provinceid;
        public String salary;
        public String type;
        public String uid;
        public String number;

        public List<otherPositions> lists;

        public class otherPositions {
            public String com_name;
            public String edu;
            public int id;
            public String lastupdate;
            public String name;
            public String provinceid;
            public String salary;
            public String uid;
        }
    }
}
