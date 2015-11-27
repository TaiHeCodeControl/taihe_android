package com.taihe.eggshell.company.mode;

/**
 * Created by Administrator on 2015/8/14.
 */
public class PersonResumeMode {
    /**
     * uid : 1785
     * job_name : 董事长
     * eid : 1675
     * salary : 10K以上
     * exp : 10年以上
     * name : lili
     * com_id : 1323
     * datetime : 2015.11.05
     */
    private String uid;
    private String hopeprofess;
    private String eid;
    private String salary;
    private String exp;
    private String name;
    private String com_id;
    private String ctime;
    private boolean isChecked;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getHopeprofess() {
        return hopeprofess;
    }

    public void setHopeprofess(String hopeprofess) {
        this.hopeprofess = hopeprofess;
    }
}
