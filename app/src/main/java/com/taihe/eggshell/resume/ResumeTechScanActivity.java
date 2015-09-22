package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.resume.adapter.BookScanAdapter;
import com.taihe.eggshell.resume.entity.ResumeData;
import com.taihe.eggshell.resume.entity.Resumes;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2015/8/15.
 */
public class ResumeTechScanActivity extends BaseActivity{

    private static final String TAG = "ResumeTechScanActivity";

    private Context mContext;

    private TextView goonTextView,resume_name;
    private Resumes eid;
    private ListView myList;
    private BookScanAdapter bookScanAdapter;
    private List<ResumeData> itemlists = null;
    private LoadingProgressDialog dialog;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_item_scan);
        super.initView();

        mContext = this;

        resume_name = (TextView)findViewById(R.id.id_resume_num);
        goonTextView = (TextView)findViewById(R.id.id_go_on);
        myList = (ListView) findViewById(R.id.mylistview_resume_item);
        goonTextView.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("写简历");
        Intent intent = getIntent();
        eid = intent.getParcelableExtra("eid");
        resume_name.setText(eid.getName()+"-专业技能");
        dialog = new LoadingProgressDialog(mContext, getResources().getString(
                R.string.submitcertificate_string_wait_dialog));
        if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
            dialog.show();
            getListData();
        } else {
            ToastUtils.show(mContext, R.string.check_network);
        }

    }

    private void getListData() {

        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        JSONObject data = jsonObject.getJSONObject("data");

                        String booklist = data.getString("skill");//专业技能
                        if(!booklist.equals("[]")){
                            Gson gson = new Gson();
                            itemlists = gson.fromJson(booklist,new TypeToken<List<ResumeData>>(){}.getType());
                            bookScanAdapter = new BookScanAdapter(mContext,itemlists,"skill");
                            myList.setAdapter(bookScanAdapter);

                        } }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };



        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
//                Log.v(TAG,new String(volleyError.networkResponse.data));
                ToastUtils.show(mContext, volleyError);
            }
        };

        Map<String,String> map = new HashMap<String,String>();
        map.put("eid", eid.getRid() + "");

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_RESUME_SCAN, false, map, true, listener, errorListener);
    }



    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_go_on:
                Intent intent = new Intent(mContext,ResumeTechActivity.class);
                intent.putExtra("eid",eid);
                startActivity(intent);
                finish();
                break;
        }
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
