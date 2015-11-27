package com.taihe.eggshell.company.mode;

/**
 * Created by Administrator on 2015/8/14.
 */
public class ComResumeMode {
    /**
     * id : 2105
     * uid : 1785
     * job_name : 董事长
     * eid : 1675
     * salary : 10K以上
     * exp : 10年以上
     * name : lili
     * com_id : 1323
     * datetime : 2015.11.05
     */
    private String id;
    private String uid;
    private String job_id;
    private String job_name;
    private String eid;
    private String salary;
    private String exp;
    private String name;
    private String com_id;
    private String datetime;
    private boolean isChecked;
//    public ComResumeMode(boolean isChecked) {
//        this.isChecked = isChecked;
//    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCom_id() {
        return com_id;
    }

    public void setCom_id(String com_id) {
        this.com_id = com_id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
