package com.taihe.eggshell.main.entity;

/**
 * Created by wang on 2015/8/8.
 */
public class Professional {

    private int id;
    private String name;
    private int parentid;

    public Professional(){}

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

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }
}
