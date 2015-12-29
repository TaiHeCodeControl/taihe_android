package com.taihe.eggshell.resume;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.resume.adapter.ResumeCenterAdapter;
import com.taihe.eggshell.resume.entity.ResumeData;
import com.taihe.eggshell.resume.entity.Resumes;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2015/8/13.
 */
public class ResumeListActivity extends BaseActivity implements Serializable {

    private static final String TAG = "ResumeMultiActivity";

    private Context mContext;
    private TextView resumeName,id_resume_list_add;
    private ListView id_resume_list;
    private Intent intent;
    private String strTypeTitle,strType,strUrl;
    private Resumes resume;
    private LoadingProgressDialog loading;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_list);
        super.initView();
        mContext = this;

        resumeName = (TextView)findViewById(R.id.id_resume_list_title);
        id_resume_list = (ListView)findViewById(R.id.id_resume_list);
        id_resume_list.setDividerHeight(0);
        id_resume_list_add = (TextView)findViewById(R.id.id_resume_list_add);
        id_resume_list_add.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("写简历");
        resume = getIntent().getParcelableExtra("eid");
        strType = getIntent().getStringExtra("type");
        strUrl = getIntent().getStringExtra("url");
        strTypeTitle = getIntent().getStringExtra("title");
        id_resume_list_add.setText("+添加"+strTypeTitle);
        resumeName.setText(resume.getName()+"-"+strTypeTitle);
        loading = new LoadingProgressDialog(mContext,"正在请求...");
        if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
            loading.show();
            getData();
        }else{
            ToastUtils.show(mContext, R.string.check_network);
        }
        id_resume_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posion, long l) {
                switch (strType){
                    case "1":
                        intent = new Intent(mContext,ResumeWorkActivity.class);
                        intent.putExtra("eid",resume);
                        intent.putExtra("type","1");
                        intent.putExtra("posion",posion);
//                        intent.putExtra("listobj", worklists.get(posion));
                        intent.putExtra("title","工作经历");
                        startActivity(intent);
                        break;
                    case "2":
                        intent = new Intent(mContext,ResumeEduActivity.class);
                        intent.putExtra("eid",resume);
                        intent.putExtra("type","2");
                        intent.putExtra("posion",posion);
                        intent.putExtra("listobj", (Serializable) worklists);
                        intent.putExtra("title","教育经历");
                        startActivity(intent);
                        break;
                    case "3":
                        intent = new Intent(mContext,ResumeTrainActivity.class);
                        intent.putExtra("eid",resume);
                        intent.putExtra("type","3");
                        intent.putExtra("posion",posion);
                        intent.putExtra("listobj", (Serializable) worklists);
                        intent.putExtra("title","培训经历");
                        startActivity(intent);
                        break;
                    case "4":
                        intent = new Intent(mContext,ResumeTechActivity.class);
                        intent.putExtra("eid",resume);
                        intent.putExtra("type","4");
                        intent.putExtra("posion",posion);
                        intent.putExtra("listobj", (Serializable) worklists);
                        intent.putExtra("title","专业技能");
                        startActivity(intent);
                        break;
                    case "5":
                        intent = new Intent(mContext,ResumeProjectActivity.class);
                        intent.putExtra("eid",resume);
                        intent.putExtra("type","5");
                        intent.putExtra("posion",posion);
                        intent.putExtra("listobj", (Serializable) worklists);
                        intent.putExtra("title","项目经验");
                        startActivity(intent);
                        break;
                    case "6":
                        intent = new Intent(mContext,ResumeBookActivity.class);
                        intent.putExtra("eid",resume);
                        intent.putExtra("type","6");
                        intent.putExtra("posion",posion);
                        intent.putExtra("listobj", (Serializable) worklists);
                        intent.putExtra("title","证书");
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_resume_list_add:
                switch (strType){
                    case "1":
                        intent = new Intent(mContext,ResumeWorkActivity.class);
                        intent.putExtra("eid",resume);
                        intent.putExtra("type","");
                        intent.putExtra("posion",0);
                        intent.putExtra("strJson","");
                        intent.putExtra("title","工作经历");
                        startActivity(intent);
                        break;
                    case "2":
                        intent = new Intent(mContext,ResumeEduActivity.class);
                        intent.putExtra("eid",resume);
                        intent.putExtra("type","");
                        intent.putExtra("posion",0);
                        intent.putExtra("strJson","");
                        intent.putExtra("title","教育经历");
                        startActivity(intent);
                        break;
                    case "3":
                        intent = new Intent(mContext,ResumeTrainActivity.class);
                        intent.putExtra("eid",resume);
                        intent.putExtra("type","");
                        intent.putExtra("posion",0);
                        intent.putExtra("strJson","");
                        intent.putExtra("title","培训经历");
                        startActivity(intent);
                        break;
                    case "4":
                        intent = new Intent(mContext,ResumeTechActivity.class);
                        intent.putExtra("eid",resume);
                        intent.putExtra("type","");
                        intent.putExtra("posion",0);
                        intent.putExtra("strJson","");
                        intent.putExtra("title","专业技能");
                        startActivity(intent);
                        break;
                    case "5":
                        intent = new Intent(mContext,ResumeProjectActivity.class);
                        intent.putExtra("eid",resume);
                        intent.putExtra("type","");
                        intent.putExtra("posion",0);
                        intent.putExtra("strJson","");
                        intent.putExtra("title","项目经验");
                        startActivity(intent);
                        break;
                    case "6":
                        intent = new Intent(mContext,ResumeBookActivity.class);
                        intent.putExtra("eid",resume);
                        intent.putExtra("type","");
                        intent.putExtra("posion",0);
                        intent.putExtra("strJson","");
                        intent.putExtra("title","证书");
                        startActivity(intent);
                        break;
                }
                break;
        }
    }
    List<ResumeData> worklists;
    private void getData() {
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject((String) obj);
                    Log.d("edu", jsonObject.toString());
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        try{
                            Gson gson = new Gson();
                            String worklist = jsonObject.getString("data");
                            worklists = gson.fromJson(worklist,new TypeToken<List<ResumeData>>(){}.getType());

                            id_resume_list.setAdapter(new ResumeCenterAdapter(mContext,worklists,strType));

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(mContext, msg.toString(), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {//返回值
                loading.dismiss();
                ToastUtils.show(mContext, "网络异常");
            }
        };

        Map<String,String> map = new HashMap<String,String>();
        map.put("uid", EggshellApplication.getApplication().getUser().getId()+"");
        map.put("eid",resume.getRid()+"");

        RequestUtils.createRequest(mContext, strUrl, "", true, map, true, listener, errorListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}
