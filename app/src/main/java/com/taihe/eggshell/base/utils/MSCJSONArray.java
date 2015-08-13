package com.taihe.eggshell.base.utils;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * @author 卡伦人
 * @email ka-lun-ren@foxmail.com
 * @创建时间 2014-9-1下午5:47:56
 */
public class MSCJSONArray extends JSONArray {

	public MSCJSONArray(JSONArray jsonArray) throws JSONException {
		super(jsonArray.toString());
	}

	public MSCJSONArray() {
		super();
	}

	public MSCJSONArray(String ver) throws JSONException {
		super(ver);
	}

	public MSCJSONArray optJSONArray(int i) {
		try {
			return new MSCJSONArray(super.optJSONArray(i));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new MSCJSONArray();
	}

	public MSCJSONObject getJSONObject(int i) throws JSONException {
		return new MSCJSONObject(super.getJSONObject(i));
	}

	@Override
	public MSCJSONObject optJSONObject(int index) {
		try {
			if (index >= length()) {
				return new MSCJSONObject();
			}
			return new MSCJSONObject(super.optJSONObject(index));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new MSCJSONObject();
	}

}
