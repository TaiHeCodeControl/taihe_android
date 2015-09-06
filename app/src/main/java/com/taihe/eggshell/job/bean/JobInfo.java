package com.taihe.eggshell.job.bean;

/**
 * 标示职位信息
 * Created by huan on 2015/8/10.
 */
public class JobInfo {

    private boolean isChecked;

    private int job_id;
    private String job_name;
    private String provinceid;
    private String salary;
    private String edu;
    private String lastupdate;
    private String com_name;
    private String uid;

    public JobInfo() {
    }

    public JobInfo(boolean isChecked, int job_id) {
        this.isChecked = isChecked;
        this.job_id = job_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getJob_Id() {
        return job_id;
    }

    public void setJob_Id(int job_id) {
        this.job_id = job_id;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getName() {
        return job_name;
    }

    public void setName(String name) {
        this.job_name = name;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getEdu() {
        return edu;
    }

    public void setEdu(String edu) {
        this.edu = edu;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getCom_name() {
        return com_name;
    }

    public void setCom_name(String com_name) {
        this.com_name = com_name;
    }
}
