package com.taihe.eggshell.personalCenter.entity;

/**
 * Created by wang on 2016/3/11.
 */
public class InvitedCompany {

    private String id;
    private String company_logo;
    private String company_name;
    private String company_id;
    private String time;
    private String company_job_id;
    private String company_job_name;
    private String salary;
    private String address;
    private String isread;//1未查看，2已查看

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCompany_job_id() {
        return company_job_id;
    }

    public void setCompany_job_id(String company_job_id) {
        this.company_job_id = company_job_id;
    }

    public String getCompany_job_name() {
        return company_job_name;
    }

    public void setCompany_job_name(String company_job_name) {
        this.company_job_name = company_job_name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }
}
