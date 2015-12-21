package com.taihe.eggshell.personalCenter.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Thinkpad on 2015/7/14.
 */
public class AboutActivity extends BaseActivity{

    private static final String TAG = "AboutActivity";
    private TextView txt_about_tell;
    private Context mContext;

    @Override
    public void initView() {
        setContentView(R.layout.activity_about);
        super.initView();
        mContext = this;

        txt_about_tell = (TextView)findViewById(R.id.txt_about_tell);
        txt_about_tell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4000084111" ));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        super.initTitle("关于蛋壳儿");
//        WebView webView = (WebView)findViewById(R.id.id_webview);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setAllowFileAccessFromFileURLs(true);
//        webView.loadUrl("file:///android_asset/asdd.htm");
//        webView.loadUrl("http://www.baidu.com");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

}
