package com.taihe.eggshell.meetinginfo.entity;

/**
 * Created by wang on 2016/2/17.
 */
public class DiscussInfo {

    private String id;
    private String nick_name;
    private String username;
    private String coment;
    private String aid;//活动id
    private String isread;//1未阅读，2已阅读
    private String type;//reply代表回复，二级，discuss 代表评论，一级
    private String addtime;//消息时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComent() {
        return coment;
    }

    public void setComent(String coment) {
        this.coment = coment;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
