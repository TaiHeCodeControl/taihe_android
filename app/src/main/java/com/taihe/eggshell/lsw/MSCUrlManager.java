package com.taihe.eggshell.lsw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.taihe.eggshell.lsw.MSCJSONArray;
import com.taihe.eggshell.lsw.MSCJSONObject;

/**
 * 2014/7/16 更新日志,从写initUrl使得更新链接成为了可能！ 2014/4/26
 * 更新日志,从写initUrl使得多次调用initUrl成为了可能
 * 
 * @author kalunren
 * **/
public class MSCUrlManager implements Serializable {
	public static String httpmain = "http://www.gmtxw06.com";
	public static String http = "http://195.198.1.122:8066/eggker/phpv/API.php";
	// public static String http = "http://192.168.1.109/gzxd/index.php?";

	// private String http = "http://test.api.ziyuanjia.com/";

	private static final long serialVersionUID = 1L;

	// 15510240020

	public MSCUrlManager(String state,String apitype, String urlapi) {
		this.state = "g=" + state;
		this.apitype = "&m=" + apitype;
		this.urlapi = "&a=" + urlapi;

		listMscUrlStrings = new HashMap<String, MSCUrlParam>();
	}
    public MSCUrlManager( String urlapi) {
        listMscUrlStrings = new HashMap<String, MSCUrlParam>();
    }
	public MSCUrlManager(String apitype, String urlapi) {
		this.apitype = "/m=" + apitype;
		this.urlapi = "/a=" + urlapi;

		listMscUrlStrings = new HashMap<String, MSCUrlParam>();
	}

	public MSCUrlManager() {
		listMscUrlStrings = new HashMap<String, MSCUrlParam>();
	}

	// public MSCUrlManager(MSCImgUrl imgUrl) {
	// http = imgUrl.GetImgUrl();
	// }

	public void initUrl(MSCUrlParam... yuanmsc) {

		Set<String> strings = listMscUrlStrings.keySet();

		for (String key : strings) {

			MSCUrlParam mscUrlString = listMscUrlStrings.get(key);
			for (int i = 0; i < yuanmsc.length; i++) {
				if (mscUrlString.param.equalsIgnoreCase(yuanmsc[i].param)) {
					listMscUrlStrings.remove(key);
					initUrl(yuanmsc);
					return;
				}
			}
		}

		for (MSCUrlParam mscUrlString : yuanmsc) {
			listMscUrlStrings.put(mscUrlString.param, mscUrlString);
		}

	}

	HashMap<String, MSCUrlParam> listMscUrlStrings;

	private String state = "";
	private String apitype = "";
	private String urlapi = "";

	public URL GetUrl() throws MalformedURLException {

		return new URL(geturlstr());
	}

	private String geturlstr() {
		String url = http + state + apitype + urlapi;

		if (listMscUrlStrings != null) {

			Set<String> strings = listMscUrlStrings.keySet();

			for (String key : strings) {

				MSCUrlParam mscUrlString = listMscUrlStrings.get(key);

				url += mscUrlString.GetUrl();
			}
			if (url.substring(url.length() - 1, url.length()).equalsIgnoreCase(
					"=")) {
				return url.substring(0, url.length() - 1);
			}

		}

		return url;
	}

	// 返回String类型的url
	public String toString() {
		return geturlstr();
	}

	/** 更换域名 **/
	public void setUrlTitle(String http) {
		MSCUrlManager.http = http;
	}

	public static String getPostUrl(MSCPostUrlParam[] posturlparams) {
		String string = "";
		for (int i = 0; i < posturlparams.length; i++) {
			if (posturlparams[i] != null) {
				string += posturlparams[i].GetUrl();
			}
		}
		if (string.length() > 1) {
			return string.substring(0, string.length() - 1);
		}
		return string;
	}

	// 组成json
	public static String getArgvMode(List<MSCPostUrlParam> mode) {
		JSONObject jsonObject = new JSONObject();
		for (int i = 0; i < mode.size(); i++) {

			try {
				jsonObject.put(mode.get(i).getParam(), mode.get(i).getValue());
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return jsonObject.toString();
	}

	// get
	public static String openUrlreString(URL url) {
		String resultData = "";
		try {
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();

			urlConn.setReadTimeout(10000);

			// urlConn.setRequestProperty("cookie", user.cookie_forString);
			urlConn.connect();
			InputStreamReader in = new InputStreamReader(
					urlConn.getInputStream());

			BufferedReader buffer = new BufferedReader(in);
			while (buffer.ready()) {
				resultData = buffer.readLine();
			}

			buffer.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultData;
	}

	public static MSCJSONObject NewopenpostUrlreString(URL getUrl,
			List<MSCPostUrlParam> urlparams) {

		String resultData = "";
		try {
			HttpURLConnection urlConn = (HttpURLConnection) getUrl
					.openConnection();

			// urlConn.setRequestProperty("cookie", user.cookie_forString);

			urlConn.setDoOutput(true);
			urlConn.setRequestMethod("POST");
			urlConn.setReadTimeout(10000);

			urlConn.connect();

			OutputStream outputStream = urlConn.getOutputStream();

			StringBuffer params = new StringBuffer();

			for (int i = 0; i < urlparams.size(); i++) {
				params.append(urlparams.get(i).GetUrl());
				Log.e("textpost", urlparams.get(i).GetUrl());
			}

			byte[] bypes = params.toString().getBytes();

			outputStream.write(bypes);

			outputStream.close();

			Log.e("textpost",
					"urlConn.getResponseCode():" + urlConn.getResponseCode());

			InputStreamReader in = new InputStreamReader(
					urlConn.getInputStream());

			BufferedReader reader = new BufferedReader(in);
			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			resultData = sb.toString();
			in.close();
			
//			resultData = MSCTool.getTrueString(resultData);
//			resultData = resultData.replace("align=\"center\"", "align=center");
//			resultData = resultData.replace("	", "");
//			resultData = resultData.replace(" ", "");
			Log.d("textpost", "yuanshitextpost:" + resultData);
			
			
			if (resultData.length() == 4) {
				try {
					int errcode;
					if ((errcode = Integer.decode(resultData)) > 0) {
						MSCJSONObject jsonObject = new MSCJSONObject();
						jsonObject.put("errcode", errcode);
						Log.d("textpost", "errcode：" + errcode);
						return jsonObject;
					}
				} catch (Exception e) {
					Log.d("textpost", "转换errcode异常：" + resultData);
				}
			}
			Log.d("textpost", "textpost:" + resultData);

			// 将array 转换 为 object格式
			if (resultData.length() > 0
					&& resultData.substring(0, 1).equalsIgnoreCase("[")) {
				MSCJSONArray array = new MSCJSONArray(resultData);

				MSCJSONObject jsonObject = new MSCJSONObject();

				jsonObject.put("array", array);

				return jsonObject;
			}

			return new MSCJSONObject(resultData);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("textpost", "Exception:联网方法异常");
			MSCJSONObject yichangjsonObject = new MSCJSONObject();
			try {
				yichangjsonObject.put("errcode", 8888);
			} catch (JSONException e1) {
				Log.e("textpost", "生成错误码异常");
				return yichangjsonObject;
			}
			Log.e("textpost", "正常生成错误码" + yichangjsonObject.toString());
			return yichangjsonObject;
		}
	}

}
