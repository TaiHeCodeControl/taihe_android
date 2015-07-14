package com.taihe.eggshell.base.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

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
                headers.put("Content-Type", "application/x-www-form-urlencoded");
//                headers.put("Accept-Encoding", "gzip,deflate");//判断是不是gzip返回类型
                headers.put("User-Agent","Android-"+getAppKey(context)+"-"+getVersion(context));
                headers.put("X-City", PrefUtils.getUserCityPreference(context, PrefUtils.KEY_USER_LOCATION, PrefUtils.KEY_USER_LOCATION_KEY, PrefUtils.KEY_USER_LOCATION_DEFAULTVALUE));
                if(null!= EggshellApplication.getApplication().getUser()){
//                    headers.put("X-Auth-Token", EggshellApplication.getApplication().getUser().getToken());
                }else if(!TextUtils.isEmpty(PrefUtils.getStringPreference(context,PrefUtils.USER_TOKEN,PrefUtils.KEY_USER_TOKEN,""))){
                    headers.put("X-Auth-Token", PrefUtils.getStringPreference(context,PrefUtils.USER_TOKEN,PrefUtils.KEY_USER_TOKEN,""));
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

    public static void createRequest_GET(final Context context, String url,final String method,
                                         final boolean islist,final String cursor,final String count,Listener<String> listener, ErrorListener errListener) {
        final StringRequest request = new StringRequest(Method.GET, url+method, listener,
                errListener) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, "UTF-8");
                    /*if(response.headers.containsKey("Accept-Encoding")){
                        String encoding = response.headers.get("Accept-Encoding");
                        if(encoding.contains("gzip")){
                        Log.v("HEADGET:", jsonString);

                        GZIPInputStream gStream = new GZIPInputStream(new ByteArrayInputStream(response.data));
                        InputStreamReader reader = new InputStreamReader(gStream);
                        BufferedReader in = new BufferedReader(reader);
                        String read;
                        String output = "";
                        while ((read = in.readLine()) != null) {
                            output += read;
                        }
                        reader.close();
                        in.close();
                        gStream.close();
                        return Response.success(output,HttpHeaderParser.parseCacheHeaders(response));
                        }
                    }*/

                    return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (Exception je) {
                    return Response.error(new ParseError(je));
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Charset", "UTF-8");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
//                headers.put("Accept-Encoding", "gzip,deflate");
                headers.put("User-Agent","Android-"+getAppKey(context)+"-"+getVersion(context));
                headers.put("X-City",PrefUtils.getUserCityPreference(context,PrefUtils.KEY_USER_LOCATION,PrefUtils.KEY_USER_LOCATION_KEY,PrefUtils.KEY_USER_LOCATION_DEFAULTVALUE));
                if(null!=EggshellApplication.getApplication().getUser()){
//                    headers.put("X-Auth-Token", EggshellApplication.getApplication().getUser().getToken());
                }else if(!TextUtils.isEmpty(PrefUtils.getStringPreference(context,PrefUtils.USER_TOKEN,PrefUtils.KEY_USER_TOKEN,""))){
                    headers.put("X-Auth-Token", PrefUtils.getStringPreference(context,PrefUtils.USER_TOKEN,PrefUtils.KEY_USER_TOKEN,""));
                }
                if(islist){
                    if(!TextUtils.isEmpty(cursor)){
                        headers.put("X-Page-Cursor",cursor);
                    }
                    if(!TextUtils.isEmpty(count)){
                        headers.put("X-Page-Count",count);
                    }
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

    public static void createRequest_DELETE(final Context context, String url,final String method,Listener<String> listener, ErrorListener errListener) {
        final StringRequest request = new StringRequest(Method.DELETE, url+method, listener,
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
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Charset", "UTF-8");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
//                headers.put("Accept-Encoding", "gzip,deflate");
                headers.put("User-Agent","Android-"+getAppKey(context)+"-"+getVersion(context));
                headers.put("X-City",PrefUtils.getUserCityPreference(context,PrefUtils.KEY_USER_LOCATION,PrefUtils.KEY_USER_LOCATION_KEY,PrefUtils.KEY_USER_LOCATION_DEFAULTVALUE));
                if(null!=EggshellApplication.getApplication().getUser()){
//                    headers.put("X-Auth-Token", EggshellApplication.getApplication().getUser().getToken());
                }else if(!TextUtils.isEmpty(PrefUtils.getStringPreference(context,PrefUtils.USER_TOKEN,PrefUtils.KEY_USER_TOKEN,""))){
                    headers.put("X-Auth-Token", PrefUtils.getStringPreference(context,PrefUtils.USER_TOKEN,PrefUtils.KEY_USER_TOKEN,""));
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

    public static String getAppKey(Context mContext) {
        Bundle metaData = null;
        String channel = null;
        try {
            ApplicationInfo ai = mContext.getPackageManager().getApplicationInfo(
                    mContext.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai)
                metaData = ai.metaData;
            if (null != metaData) {
                channel = metaData.getString("UMENG_CHANNEL");
                if (null == channel) {
                    channel = "nochannel";
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return channel;
    }

    public static String getVersion(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "Unknown";
        }
    }
}
