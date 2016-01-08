package com.taihe.eggshell.main.mode;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/8/14.
 */
public class PlayInfoMode implements Parcelable{
    public String id;
    public String title;
    public String address;
    public String organizers;//主办方
    public String user;//联系人
    public String telphone;
    public String traffic_route;//路线
    public String logo;
    public String content;//介绍
    public String starttime;
    public String endtime;
    public String apply_count;//参加人数
    public String collect_count;//收藏人数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrganizers() {
        return organizers;
    }

    public void setOrganizers(String organizers) {
        this.organizers = organizers;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getTraffic_route() {
        return traffic_route;
    }

    public void setTraffic_route(String traffic_route) {
        this.traffic_route = traffic_route;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getApply_count() {
        return apply_count;
    }

    public void setApply_count(String apply_count) {
        this.apply_count = apply_count;
    }

    public String getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(String collect_count) {
        this.collect_count = collect_count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(organizers);
        parcel.writeString(address);
        parcel.writeString(starttime);
        parcel.writeString(apply_count);
        parcel.writeString(collect_count);
        parcel.writeString(endtime);
        parcel.writeString(user);
        parcel.writeString(telphone);
        parcel.writeString(traffic_route);
        parcel.writeString(content);
        parcel.writeString(logo);
    }

    public static final Creator<PlayInfoMode> CREATOR = new Creator<PlayInfoMode>() {
        @Override
        public PlayInfoMode createFromParcel(Parcel parcel) {
            PlayInfoMode playInfoMode = new PlayInfoMode();
            playInfoMode.id = parcel.readString();
            playInfoMode.title = parcel.readString();
            playInfoMode.organizers = parcel.readString();
            playInfoMode.address = parcel.readString();
            playInfoMode.starttime = parcel.readString();
            playInfoMode.apply_count = parcel.readString();
            playInfoMode.collect_count = parcel.readString();
            playInfoMode.endtime = parcel.readString();
            playInfoMode.user = parcel.readString();
            playInfoMode.telphone = parcel.readString();
            playInfoMode.traffic_route = parcel.readString();
            playInfoMode.content = parcel.readString();
            playInfoMode.logo = parcel.readString();
            return playInfoMode;
        }

        @Override
        public PlayInfoMode[] newArray(int i) {
            return new PlayInfoMode[i];
        }
    };
}
