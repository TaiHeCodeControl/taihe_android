package com.taihe.eggshell.map;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Thinkpad on 2015/7/23.
 */
public class RootMarker implements Parcelable{

    private int id;
    private String areaAroud;//区域
    private int numberInArea;//区域里的数量
    private double latitude;//纬度
    private double longitude;//经度

    public RootMarker(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAreaAroud() {
        return areaAroud;
    }

    public void setAreaAroud(String areaAroud) {
        this.areaAroud = areaAroud;
    }

    public int getNumberInArea() {
        return numberInArea;
    }

    public void setNumberInArea(int numberInArea) {
        this.numberInArea = numberInArea;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeInt(id);
        parcel.writeString(areaAroud);
        parcel.writeInt(numberInArea);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }

    public static final Creator<RootMarker> CREATOR = new Creator<RootMarker>() {
        @Override
        public RootMarker createFromParcel(Parcel source) {
            RootMarker rootMarker = new RootMarker();
            rootMarker.id = source.readInt();
            rootMarker.areaAroud = source.readString();
            rootMarker.numberInArea = source.readInt();
            rootMarker.latitude = source.readDouble();
            rootMarker.longitude = source.readDouble();
            return rootMarker;
        }

        @Override
        public RootMarker[] newArray(int size) {
            return new RootMarker[size];
        }
    };
}
