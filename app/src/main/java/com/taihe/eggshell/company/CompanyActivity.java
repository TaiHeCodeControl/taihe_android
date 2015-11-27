package com.taihe.eggshell.company;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.company.mode.ComResumeMode;
import com.taihe.eggshell.main.MainActivity;
import com.taihe.eggshell.widget.LoadingProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang on 2015/11/23.
 */
public class CompanyActivity extends BaseActivity{

    private Context mContext;
    private TextView toPerson;
    private LinearLayout com_lin_topl,com_lin_topr,com_lin_footl,com_lin_footc,com_lin_footr;
    private LoadingProgressDialog loading;
    @Override
    public void initView() {
        setContentView(R.layout.activity_company_main);
        super.initView();

        mContext = this;
        toPerson = (TextView)findViewById(R.id.id_to_person);
        com_lin_topl = (LinearLayout)findViewById(R.id.com_lin_topl);
        com_lin_topr = (LinearLayout)findViewById(R.id.com_lin_topr);
        com_lin_footl = (LinearLayout)findViewById(R.id.com_lin_footl);
        com_lin_footc = (LinearLayout)findViewById(R.id.com_lin_footc);
        com_lin_footr = (LinearLayout)findViewById(R.id.com_lin_footr);

        toPerson.setOnClickListener(this);
        com_lin_topl.setOnClickListener(this);
        com_lin_topr.setOnClickListener(this);
        com_lin_footl.setOnClickListener(this);
        com_lin_footc.setOnClickListener(this);
        com_lin_footr.setOnClickListener(this);
    }

    @Override
    public void initData() {
//        super.initData();
        loading = new LoadingProgressDialog(mContext,"正在请求...");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()){
            case R.id.id_to_person:
                intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.com_lin_topl:
                intent = new Intent(mContext, CompanyResumeGetActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
                break;
            case R.id.com_lin_topr:
                intent = new Intent(mContext, CompanyPersonResumeActivity.class);
                startActivity(intent);
                break;
            case R.id.com_lin_footl:
                intent = new Intent(mContext, CompanyJobListActivity.class);
                intent.putExtra("type",0);
                startActivity(intent);
                break;
            case R.id.com_lin_footc:
                intent = new Intent(mContext, CompanyJobListActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
                break;
            case R.id.com_lin_footr:
                intent = new Intent(mContext, CompanyJobListActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
                break;
        }
    }
    private void getListData() {
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    loading.dismiss();
                    JSONObject jsonObject = new JSONObject((String) obj);

                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        String data = jsonObject.getString("data");
                        try{

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    } else {

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

        loading.show();
        Map<String,String> map = new HashMap<String,String>();
        map.put("uid", "116");
        String url = Urls.COM_GET_URL;
        RequestUtils.createRequest(mContext, url, "", true, map, true, listener, errorListener);
    }
}
