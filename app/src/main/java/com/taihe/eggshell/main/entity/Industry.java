package com.taihe.eggshell.main.entity;

import java.util.List;

/**
 * Created by wang on 2015/8/8.
 */
public class Industry {

    private int id;
    private String name;
    private int imgsrc;
    private List<Professional> professionalList;

    public Industry(){}

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

    public List<Professional> getProfessionalList() {
        return professionalList;
    }

    public void setProfessionalList(List<Professional> professionalList) {
        this.professionalList = professionalList;
    }

    public int getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(int imgsrc) {
        this.imgsrc = imgsrc;
    }
}