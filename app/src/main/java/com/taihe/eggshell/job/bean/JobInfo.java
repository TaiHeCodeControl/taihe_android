package com.taihe.eggshell.job.bean;

/**
 * 标示职位信息
 * Created by huan on 2015/8/10.
 */
public class JobInfo {

    private boolean isChecked;

    private String com_id;
    private int job_id;
    private String job_name;
    private String provinceid;
    private String salary;
    private String edu;
    private String lastupdate;
    private String com_name;
    private String uid;
    private String type;
    private String number;
    private String exp;
    private String report;
    private String sex;
    private String marriage;
    private String com_logo;


    public JobInfo() {
    }

    public JobInfo(boolean isChecked, int job_id) {
        this.isChecked = isChecked;
        this.job_id = job_id;
    }

    public String getCom_id() {
        return com_id;
    }

    public void setCom_id(String com_id) {
        this.com_id = com_id;
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

    public int getJob_id() {
        return job_id;
    }

    public void setJob_id(int job_id) {
        this.job_id = job_id;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getCom_logo() {
        return com_logo;
    }

    public void setCom_logo(String com_logo) {
        this.com_logo = com_logo;
    }
}
