package com.taihe.eggshell.base.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.chinaway.framework.swordfish.network.http.AuthFailureError;
import com.chinaway.framework.swordfish.network.http.DefaultRetryPolicy;
import com.chinaway.framework.swordfish.network.http.NetworkResponse;
import com.chinaway.framework.swordfish.network.http.ParseError;
import com.chinaway.framework.swordfish.network.http.Request.Method;
import com.chinaway.framework.swordfish.network.http.RequestQueue;
import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.Response.ErrorListener;
import com.chinaway.framework.swordfish.network.http.Response.Listener;
import com.chinaway.framework.swordfish.network.http.toolbox.HttpHeaderParser;
import com.chinaway.framework.swordfish.network.http.toolbox.StringRequest;
import com.chinaway.framework.swordfish.network.http.toolbox.Volley;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.utils.PrefUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RequestUtils {
	private static final int DEFAULT_REQUEST_TIMEOUT = 20 * 1000;
	private static final int DEFAULT_REQUEST_RETRY_COUNT = 0;

	private static RequestQueue mRequestQueue;

	public static RequestQueue getRequestQueue(Context context) {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(context);
		}
		return mRequestQueue;
	}

	public static void createRequest(final Context context, String url,
			final String method, final boolean beforeLogin,
			final Map<String, String> dataParams, final boolean jsonFormat,
			Listener<String> listener, ErrorListener errListener) {
		final StringRequest request = new StringRequest(Method.POST, url+method, listener,
				errListener) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, "UTF-8");
                    return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (Exception je) {
                    return Response.error(new ParseError(je));
                }
            }

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				if (dataParams == null) {
					throw new AuthFailureError("缺少参数！");
				} else {
					if (jsonFormat) {
                        if(null!= EggshellApplication.getApplication().getUser() && null!= EggshellApplication.getApplication().getUser().getToken()){
                            dataParams.put("token",EggshellApplication.getApplication().getUser().getToken());
                        }
                        Log.v("DD:",dataParams.toString());
                        return dataParams;
					} else {
						dataParams.put("method", method);
						return dataParams;
					}
				}
			}

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Charset", "UTF-8");
                if(null!= EggshellApplication.getApplication().getUser()){
//                    headers.put("X-Auth-Token", EggshellApplication.getApplication().getUser().getToken());
                }else if(!TextUtils.isEmpty(PrefUtils.getStringPreference(context,PrefUtils.USER_TOKEN,PrefUtils.KEY_USER_TOKEN,""))){
//                    headers.put("X-Auth-Token", PrefUtils.getStringPreference(context,PrefUtils.USER_TOKEN,PrefUtils.KEY_USER_TOKEN,""));
                }
//                Log.v("Head:",headers.toString());
                return headers;
            }
        };
		request.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_REQUEST_TIMEOUT,
				DEFAULT_REQUEST_RETRY_COUNT,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		request.setShouldCache(false);

		getRequestQueue(context).add(request);
	}

	public static void cancelRequest(Object tag) {
		mRequestQueue.cancelAll(tag);
	}

    /**
     * 判断用户的网络类型
     *
     * @return 无网络:0 <br>
     *         Wifl:1 <br>
     *         3G:3 <br>
     *         2G:2<br>
     *         default:4
     * **/
    public static int GetWebType(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cManager.getActiveNetworkInfo();
        if (null == networkInfo) {
            return 0;
        } else {
            switch (networkInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI: // wifi
                    return 1;
                case ConnectivityManager.TYPE_MOBILE:// 手机网络
                    // * NETWORK_TYPE_CDMA 网络类型为CDMA
                    // * NETWORK_TYPE_EDGE 网络类型为EDGE
                    // * NETWORK_TYPE_EVDO_0 网络类型为EVDO0
                    // * NETWORK_TYPE_EVDO_A 网络类型为EVDOA
                    // * NETWORK_TYPE_GPRS 网络类型为GPRS
                    // * NETWORK_TYPE_HSDPA 网络类型为HSDPA
                    // * NETWORK_TYPE_HSPA 网络类型为HSPA
                    // * NETWORK_TYPE_HSUPA 网络类型为HSUPA
                    // * NETWORK_TYPE_UMTS 网络类型为UMTS
                    // 联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EDGE，电信的2G为CDMA，电信的3G为EVDO
                    switch (networkInfo.getSubtype()) {
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                            return 3;
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                            return 2;
                    }
                    break;
            }
            return 4;
        }
    }
}
