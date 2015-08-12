package com.taihe.eggshell.lsw;

import java.io.Serializable;

import android.widget.EditText;

/** @author kalunren **/
public class MSCUrlParam implements Serializable {

	public String key = "&";

	public String ver = "=";

	private static final long serialVersionUID = 1L;

	public MSCUrlParam(String param, String value) {
		this.param = key + param;
		this.value = ver + value;
	}

	public MSCUrlParam(String param, EditText value) {

		this.param = key + param;
		this.value = ver + value.getText().toString();

	}

	public MSCUrlParam(String param, Object value) {
		this.param = key + param;
		this.value = ver + value.toString();
	}

	public MSCUrlParam(String param, int value) {
		this.param = key + param;
		this.value = ver + value;
	}

	/** 参数,值 **/
	public String param, value;

	public String GetUrl() {
		return param + value;
	}

	public String toString() {
		return param + value;
	}

}
