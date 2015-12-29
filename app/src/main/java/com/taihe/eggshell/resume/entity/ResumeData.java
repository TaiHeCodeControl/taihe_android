package com.taihe.eggshell.resume.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wang on 2015/9/6.
 */
public class ResumeData implements Parcelable{

    private int id;
    private String uid;
    private String eid;
    private String name;
    private String sdate;
    private String edate;
    private String department;
    private String title;
    private String content;
    private String specialty;
    private String skill;
    private String ing;
    private String longtime;
    private String sys;

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getIng() {
        return ing;
    }

    public void setIng(String ing) {
        this.ing = ing;
    }

    public String getLongtime() {
        return longtime;
    }

    public void setLongtime(String longtime) {
        this.longtime = longtime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(uid);
        parcel.writeString(eid);
        parcel.writeString(name);
        parcel.writeString(sdate);
        parcel.writeString(edate);
        parcel.writeString(department);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(specialty);
        parcel.writeString(skill);
        parcel.writeString(ing);
        parcel.writeString(longtime);
        parcel.writeString(sys);

    }

    public static final Creator<ResumeData> CREATOR = new Creator<ResumeData>() {
        @Override
        public ResumeData createFromParcel(Parcel parcel) {

            ResumeData data = new ResumeData();
            data.id = parcel.readInt();
            data.uid = parcel.readString();
            data.eid = parcel.readString();
            data.name = parcel.readString();
            data.sdate = parcel.readString();
            data.edate = parcel.readString();
            data.department = parcel.readString();
            data.title = parcel.readString();
            data.content = parcel.readString();
            data.specialty = parcel.readString();
            data.skill = parcel.readString();
            data.ing = parcel.readString();
            data.longtime = parcel.readString();
            data.sys = parcel.readString();
            return data;
        }

        @Override
        public ResumeData[] newArray(int i) {
            return new ResumeData[i];
        }
    };
}
