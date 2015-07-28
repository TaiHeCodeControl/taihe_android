package com.taihe.eggshell.base.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by huan on 2015/7/28.
 */
public class UpdateUtils {

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 传入一个map返回一个json串
     * @param map
     * @return
     */
    public static String getJsonStr(Map<String, String> map) {

        StringBuffer buffer = new StringBuffer();
        buffer.append('"');
        buffer.append("{");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            buffer.append("\\");
            buffer.append('"');
            buffer.append(entry.getKey());
            buffer.append("\\");
            buffer.append('"');
            buffer.append(":");
            buffer.append("\\");
            buffer.append('"');
            buffer.append(entry.getValue());
            buffer.append("\\");
            buffer.append('"');
            buffer.append(",");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append("}");
        buffer.append('"');
        return buffer.toString();
    }


}
