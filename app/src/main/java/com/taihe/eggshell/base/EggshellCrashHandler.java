package com.taihe.eggshell.base;

import android.content.Context;
import android.os.Environment;

import com.chinaway.framework.swordfish.logstatistics.UserdataCollect;

import org.w3c.dom.UserDataHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Thinkpad on 2015/7/14.
 */
public class EggshellCrashHandler implements Thread.UncaughtExceptionHandler{

     private static EggshellCrashHandler crashHandler = null;
     private Context mContext;

     private EggshellCrashHandler(){}

     public static EggshellCrashHandler getInstance(){
         if(crashHandler==null){
             crashHandler = new EggshellCrashHandler();
         }
         return crashHandler;
     }

    public void init(Context context){
        mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        UserdataCollect.logExceptionInfo(ex);
        UserdataCollect.uninitLogUtils();
        handleException(ex);
    }

    private void handleException(Throwable ex){
        if(null!=ex && null!= mContext){
            saveCrashInfo2File(ex);
        }
    }

    private void saveCrashInfo2File(Throwable ex){
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while(null!=cause){
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        try {
        String time = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String fileName = "crash-"+time+".log";
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/eggshell/crash/";
            File dir = new File(path);
            if(!dir.exists()){
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(path+fileName);
            fos.write(result.getBytes());
            fos.close();
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
