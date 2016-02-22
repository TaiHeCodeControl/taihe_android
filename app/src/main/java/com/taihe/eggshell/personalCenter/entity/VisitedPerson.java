package com.taihe.eggshell.personalCenter.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Thinkpad on 2015/7/14.
 */
public class VisitedPerson implements Parcelable{

    private int id;
    private String userName;
    private String telphone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(userName);
        parcel.writeString(telphone);
    }

    public static final Creator<VisitedPerson> CREATOR = new Creator<VisitedPerson>() {
        @Override
        public VisitedPerson createFromParcel(Parcel source) {
            VisitedPerson visitedPerson = new VisitedPerson();
            visitedPerson.id = source.readInt();
            visitedPerson.userName = source.readString();
            visitedPerson.telphone = source.readString();
            return visitedPerson;
        }

        @Override
        public VisitedPerson[] newArray(int size) {
            return new VisitedPerson[size];
        }
    };
}
