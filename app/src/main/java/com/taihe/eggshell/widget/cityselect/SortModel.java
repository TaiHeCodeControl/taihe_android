package com.taihe.eggshell.widget.cityselect;

public class SortModel {

	private String name;   //显示的数据
	private String sortLetters;  //显示数据拼音的首字母
    private String phoneNum;//手机号码
    private String isVisited;//是否被邀请
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getIsVisited() {
        return isVisited;
    }

    public void setIsVisited(String isVisited) {
        this.isVisited = isVisited;
    }
}
