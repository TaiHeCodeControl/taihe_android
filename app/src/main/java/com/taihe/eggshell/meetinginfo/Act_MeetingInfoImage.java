package com.taihe.eggshell.meetinginfo;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.main.mode.PlayInfoMode;
import com.taihe.eggshell.meetinginfo.adapter.MeetingAdapter;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/12.
 */
public class Act_MeetingInfoImage extends BaseActivity {
    private ImageView meetinginfo_img;
    private Context mContext;
    private String title,pic,content;
    private TextView meetinginfo_content;

    @Override
    public void initView() {
        setContentView(R.layout.activity_meetinginfo_info);
        super.initView();
        mContext = this;
        meetinginfo_img = (ImageView)findViewById(R.id.meetinginfo_img);
        meetinginfo_content = (TextView)findViewById(R.id.meetinginfo_content);
    }

    public void initData(){
        super.initData();

        title = getIntent().getStringExtra("title");
        pic = getIntent().getStringExtra("pic");
        content = getIntent().getStringExtra("content");
        initTitle(title);
        FinalBitmap bitmap = FinalBitmap.create(mContext);
        bitmap.display(meetinginfo_img,pic);
        meetinginfo_content.setText(Html.fromHtml(content));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}
