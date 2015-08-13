package com.taihe.eggshell.base.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;

/**
 * @author 卡伦人
 * @email ka-lun-ren@foxmail.com
 * @创建时间 2014-8-27上午10:26:01
 * @see 该实体类为系统原生的子类，主要作用有，根据需求再返回值时过滤了一些null数据，新增了转换方正时间的方法
 */
public class MSCJSONObject extends JSONObject {

	public MSCJSONObject(String jsonObject) throws JSONException {
		super(jsonObject.toString());
	}

	public MSCJSONObject(JSONObject jsonObject) throws JSONException {
		super(jsonObject.toString());
	}

	public MSCJSONObject() {
		super();
	}

	public MSCJSONArray getJSONArray(String name) throws JSONException {
		return new MSCJSONArray(super.getJSONArray(name));
	}

	public MSCJSONObject optJSONObject(String name) {
		try {
			JSONObject str = super.optJSONObject(name);
			return new MSCJSONObject(str);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new MSCJSONObject();
	}

	@Override
	public MSCJSONArray optJSONArray(String name) {
		try {
			return new MSCJSONArray(super.optJSONArray(name));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new MSCJSONArray();
	}

	public MSCJSONObject getJSONObject(String name) throws JSONException {
		return new MSCJSONObject(super.getJSONObject(name));
	}

	@Override
	public String getString(String name) throws JSONException {
		String string = super.getString(name);
		if (string == null || string.equalsIgnoreCase("null")) {
			return "";
		}
		return Html.fromHtml(string).toString();
	}

	@Override
	public String optString(String name) {
		String string = super.optString(name);
		if (string == null || string.equalsIgnoreCase("null")) {
			return "";
		}
		return Html.fromHtml(string).toString();
	}

	/*** 解析方正的时间 ***/
	public String optStringTodata(String name) {

		String string = super.optString(name);
		if (string.equalsIgnoreCase("null")) {
			return "";
		}
		String info = string.replaceAll("T", " ");

		try {
			int po = info.length();
			for (int i = 0; i < info.length() - 1; i++) {
				if (info.substring(i, i + 1).equalsIgnoreCase(".")) {
					po = i;
				}
			}
			return info.substring(0, po);
		} catch (Exception e) {
		}

		return info;
	}

	/** 返回玖信贷的性别 0女 1男 **/
	public String optjxdxingbie(String string) {
		return optInt(string) > 0 ? "男" : "女";
	}

}
