package com.taihe.eggshell.company;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wang on 2015/11/25.
 */
public class CompanyJob implements Parcelable{

    private String cj_id;
    private String cj_name;
    private Boolean isSelected = false;
    private String jobhits;//点击数
    private String count;//收到个数
    private int status;//状态
    private String lastupdate;//最近刷新时间
    private String edate;//创建时间

    public String getCj_id() {
        return cj_id;
    }

    public void setCj_id(String cj_id) {
        this.cj_id = cj_id;
    }

    public String getCj_name() {
        return cj_name;
    }

    public void setCj_name(String cj_name) {
        this.cj_name = cj_name;
    }

    public String getJobhits() {
        return jobhits;
    }

    public void setJobhits(String jobhits) {
        this.jobhits = jobhits;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
