package com.taihe.eggshell.resume.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wang on 2015/9/4.
 */
public class Resumes implements Parcelable{

    private int rid;
    private String name;
    private String ctime;

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rid);
        dest.writeString(name);
        dest.writeString(ctime);
    }

    public static final Creator<Resumes> CREATOR = new Creator<Resumes>() {
        @Override
        public Resumes createFromParcel(Parcel source) {
            Resumes resumes = new Resumes();
            resumes.rid = source.readInt();
            resumes.name = source.readString();
            resumes.ctime = source.readString();
            return resumes;
        }

        @Override
        public Resumes[] newArray(int size) {
            return new Resumes[size];
        }
    };
}
