package com.taihe.eggshell.personalCenter.entity;

/**
 * Created by tiahe on 2015/7/20.
 */
public class UpdateInfo {
    private String version;
    private String url;
    private String description;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
