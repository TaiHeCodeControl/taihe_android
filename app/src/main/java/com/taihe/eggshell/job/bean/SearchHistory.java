package com.taihe.eggshell.job.bean;

import com.chinaway.framework.swordfish.db.annotation.Id;
import com.chinaway.framework.swordfish.db.annotation.Table;

/**
 * Created by wang on 2015/8/11.
 */
@Table (name = "table_history")
public class SearchHistory {
    @Id(column = "id")
    private int id;
    private String name;
    private String time;

    public SearchHistory(){}

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
