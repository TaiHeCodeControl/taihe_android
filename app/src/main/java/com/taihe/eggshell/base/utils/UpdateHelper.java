package com.taihe.eggshell.base.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.chinaway.framework.swordfish.network.http.toolbox.Volley;
import com.taihe.eggshell.base.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by wang on 2015/8/17.
 */
public class UpdateHelper {

    private Context mContext;
    private DownloadProgress downloadProgress;

    public interface DownloadProgress{
        public void progress(int percent);
        public void complete();
        public void error();
    }

    public UpdateHelper(Context context,DownloadProgress progress){
        this.mContext= context;
        this.downloadProgress = progress;
    }

    public void downloadInBackground(String url){

        new AsyncTask<String,Integer,Boolean>(){

            @Override
            protected Boolean doInBackground(String... params) {
                String url = params[0];
                boolean result = false;
                HttpUriRequest get = new HttpGet(url);
                DefaultHttpClient client = new DefaultHttpClient();
                try {
                    HttpResponse response = client.execute(get);
                    if(response.getStatusLine().getStatusCode()==200){
                        HttpEntity entity = response.getEntity();
                        InputStream is = entity.getContent();
                        long total = entity.getContentLength();
                        if(is !=null) {
                            File file = new File(mContext.getCacheDir(), Constants.UPDATE_APK_NAME);
                            if (!file.exists()) {
                                file.createNewFile();
                            }
                            Runtime.getRuntime().exec("chmod 777 " + file.getAbsolutePath());
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            byte[] buf = new byte[1024];
                            int ch = -1;
                            int count = 0;
                            while ((ch = is.read(buf)) != -1) {
                                fileOutputStream.write(buf, 0, ch);
                                count += ch;
                                publishProgress((int) (count * 100 / total));
                            }
                            fileOutputStream.flush();
                            if(fileOutputStream!=null){
                                fileOutputStream.close();
                            }
                            result = true;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                downloadProgress.progress(values[0]);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(aBoolean){
                    downloadProgress.complete();
                    installNewAPK(mContext);
                }
                super.onPostExecute(aBoolean);
            }
        }.execute(url);

    }

    private void installNewAPK(Context context){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(context.getCacheDir(),Constants.UPDATE_APK_NAME)),"application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
