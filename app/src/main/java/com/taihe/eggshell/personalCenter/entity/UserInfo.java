package com.taihe.eggshell.personalCenter.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Thinkpad on 2015/7/14.
 */
public class UserInfo implements Parcelable{

    private int id;
    private String userName;
    private String gender;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(userName);
        parcel.writeString(gender);
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            UserInfo userInfo = new UserInfo();
            userInfo.id = source.readInt();
            userInfo.userName = source.readString();
            userInfo.gender = source.readString();
            return userInfo;
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
