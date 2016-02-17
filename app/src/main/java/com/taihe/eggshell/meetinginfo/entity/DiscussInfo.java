package com.taihe.eggshell.meetinginfo.entity;

/**
 * Created by wang on 2016/2/17.
 */
public class DiscussInfo {

    private String id;
    private String name;
    private String discuss;
    private String answerTime;

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

    public String getDiscuss() {
        return discuss;
    }

    public String getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }

    public void setDiscuss(String discuss) {
        this.discuss = discuss;
    }


}
