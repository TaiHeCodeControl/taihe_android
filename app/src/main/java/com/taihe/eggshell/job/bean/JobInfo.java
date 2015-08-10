package com.taihe.eggshell.job.bean;

/**
 * 标示职位信息
 * Created by huan on 2015/8/10.
 */
public class JobInfo {

    private boolean isChecked;
    private int id;

    public JobInfo(boolean isChecked, int id) {
        this.isChecked = isChecked;
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
