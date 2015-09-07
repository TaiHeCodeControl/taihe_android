package com.taihe.eggshell.main.entity;

/**
 * Created by Thinkpad on 2015/7/1.
 */
public class User {

    private int id;
    private String name;
    private String phoneNumber;
    private String token;
    private String resumeid;
    private String description;
     //expect 简历条数   favjob 投递职位条数  usejob收藏职位条数   resume_photo头像
    private String expect;
    private String favjob;
    private String resume_photo;//用户头像
    private String usejob;

    public User() {
    }

    public User(int id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public String getFavjob() {
        return favjob;
    }

    public void setFavjob(String favjob) {
        this.favjob = favjob;
    }

    public String getResume_photo() {
        return resume_photo;
    }

    public void setResume_photo(String resume_photo) {
        this.resume_photo = resume_photo;
    }

    public String getUsejob() {
        return usejob;
    }

    public void setUsejob(String usejob) {
        this.usejob = usejob;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getResumeid() {
        return resumeid;
    }

    public void setResumeid(String resumeid) {
        this.resumeid = resumeid;
    }
}
