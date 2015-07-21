package com.taihe.eggshell.base;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by tiahe on 2015/7/20.
 */
public class UpdateService extends IntentService {

    private MyNotification notification = null;
    private NotificationManager manager = null;
    private int curPosition = 0;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    notification.changeProgressStatus(curPosition);
                    break;
                case 2:
                    break;
                case 3:

                    manager.cancel(1);
                    execInstall();
                    break;
            }
        };
    };
    private String updateUrl;

    public UpdateService() {
        super("下载服务");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            updateUrl = intent.getStringExtra("url");
            notification = new MyNotification(getApplicationContext());
            notification.createNotification();
            manager = notification.getManager();
            new LoadThread().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class LoadThread extends Thread {
        private InputStream is;
        private FileOutputStream fos;

        public void run() {
            // while(curPosition<=10){
            // try {
            // Thread.sleep(1000);
            //
            // } catch (InterruptedException e) {
            // e.printStackTrace();
            // }
            // handler.sendEmptyMessage(Constants.DOWNLOAD_OK);
            // if(curPosition==10){
            // handler.sendEmptyMessage(Constants.DOWNLOAD_COMPLETE);
            // }
            // curPosition++;
            // }
            URL url;
            try {
                url = new URL(updateUrl);
                URLConnection connection = url.openConnection();
                int filelenth = connection.getContentLength();
                is = connection.getInputStream();
                byte[] buff = new byte[102400];
                int len = 0, lenth = 0;
                fos = new FileOutputStream(
                        Environment.getExternalStorageDirectory() + "/xiyibang.apk");
                while ((len = is.read(buff)) != -1) {
                    sleep(1000);
                    fos.write(buff, 0, len);
                    lenth += len;
                    curPosition=lenth*100/filelenth;
                    handler.sendEmptyMessage(Constants.DOWNLOAD_OK);
                }
                handler.sendEmptyMessage(Constants.DOWNLOAD_COMPLETE);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void execInstall() {
        String fileName = Environment.getExternalStorageDirectory()
                + "/xiyibang.apk";
        Log.i("info", fileName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(fileName)),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

}
