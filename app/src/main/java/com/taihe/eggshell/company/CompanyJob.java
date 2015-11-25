package com.taihe.eggshell.company;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wang on 2015/11/25.
 */
public class CompanyJob implements Parcelable{

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
