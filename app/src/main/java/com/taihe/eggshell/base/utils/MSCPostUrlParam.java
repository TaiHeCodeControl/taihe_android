package com.taihe.eggshell.base.utils;

import java.io.Serializable;

import android.widget.EditText;
import android.widget.TextView;

/** @author kalunren,Post请求 **/
public class MSCPostUrlParam implements Serializable {

	private static final long serialVersionUID = 1L;

	public MSCPostUrlParam(String param, String value) {
		if (param == null || value == null) {
			return;
		}

		this.param = param;
		this.value = value;
	}

	public MSCPostUrlParam(String param, Object value) {
		if (param == null || value == null) {
			return;
		}

		this.param = param;
		this.value = value.toString();
	}
	public MSCPostUrlParam(String param, EditText value) {
		if (param == null || value == null) {
			return;
		}

		this.param = param;
		this.value = value.getText().toString();
	}

	public MSCPostUrlParam(String param, TextView value) {
		if (param == null || value == null) {
			return;
		}

		this.param = param;
		this.value = value.getText().toString();
	}

	/** 参数,值 **/
	public String param, value;

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String GetUrl() {

		return param + "=" + value + "&";
	}

	@Override
	public String toString() {
		return "MSCPostUrlParam [param=" + param + ", value=" + value + "]";
	}
	
}
