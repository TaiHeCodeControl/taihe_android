package com.taihe.eggshell.base.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.chinaway.framework.swordfish.network.http.VolleyError;

/**
 * Created by wangshaobin on 15/4/3.
 */
public class ToastUtils {

    /**
     *
     * @param context
     * @param str 字符串
     */
    public static void show(Context context,String str){
        if(!TextUtils.isEmpty(str)){
            Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     * @param context
     * @param id 字符串在String文件的ID
     */
    public static void show(Context context,int id){
        Toast.makeText(context,id,Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context,VolleyError volleyError){
        if(null!=volleyError.networkResponse){
            Toast.makeText(context,"服务器异常,请稍后再试",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"请检查网络",Toast.LENGTH_SHORT);
        }
    }
}
