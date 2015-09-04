package com.taihe.eggshell.personalCenter.activity;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.base.utils.UpdateUtils;
import com.taihe.eggshell.widget.ChoiceDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Thinkpad on 2015/7/15.
 */
public class FeedbackActivity extends BaseActivity{

    private static final String TAG = "FeedbackActivity";
    private Context mContext;

    private EditText et_feedcontext,et_qq,et_email;
    private TextView text_feedback_num;
    private Button btn_submit;
    private String content,qq,email;
    private int uid;
    private String version;
    private int txtnum=100;
    @Override
    public void initView() {
        setContentView(R.layout.activity_feedback);
        super.initView();
        mContext = this;


        et_email = (EditText) findViewById(R.id.et_feedback_feedemail);
        et_feedcontext = (EditText) findViewById(R.id.et_feedback_feedcontent);
        et_qq = (EditText) findViewById(R.id.et_feedback_feedqq);
        text_feedback_num = (TextView) findViewById(R.id.text_feedback_num);
        btn_submit = (Button) findViewById(R.id.btn_feedback_submit);

        et_feedcontext.addTextChangedListener(tw);
        text_feedback_num.setText(txtnum+"字");
        btn_submit.setOnClickListener(this);
        et_feedcontext.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        super.initTitle("意见反馈");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_feedback_submit:

                submitFeed();

                break;
        }
    }

    private void submitFeed() {

        content = et_feedcontext.getText().toString().trim();
        qq = et_qq.getText().toString().trim();
        email = et_email.toString().trim();
        version = UpdateUtils.getVersion(getApplicationContext());

        if (TextUtils.isEmpty(content)) {
            ToastUtils.show(mContext, "请输入反馈内容");
            return;
        }
        if(txtnum<=0){
            ToastUtils.show(mContext, "请输入100以内汉字");
            return;
        }
//        if(!TextUtils.isEmpty(email)){
//            if(!checkEmail(email)){
//                ToastUtils.show(mContext, "邮箱格式不正确");
//                return;
//            }
//        }
        toSubmit();

    }

    private void toSubmit() {

        Map<String,String> dataParams = new HashMap<String, String>();
        dataParams.put("uid","" + EggshellApplication.getApplication().getUser().getId());
//        dataParams.put("source","3");
//        dataParams.put("version",version);
        dataParams.put("opinion",content);
        dataParams.put("email",email);
        dataParams.put("qq",qq);


        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {

                try {
                    Log.v(TAG, (String) obj);
                    JSONObject jsonObject = new JSONObject((String) obj);
                    int code = jsonObject.getInt("code");
                    System.out.println("code=========" + code);
                    if (code == 0) {

                        String msg = jsonObject.getString("message");
                        ToastUtils.show(mContext, msg);
                        FeedbackActivity.this.finish();
                    } else {
                        String msg = jsonObject.getString("message");
                        ToastUtils.show(mContext, msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                try {
                    if (null != volleyError.networkResponse.data) {
                        Log.v("FEEDBACK:", new String(volleyError.networkResponse.data));
                    }
                    ToastUtils.show(mContext, volleyError.networkResponse.statusCode + "");
                } catch (Exception e) {
                    ToastUtils.show(mContext, "联网失败");
                }

            }
        };

        String method = "/feedback";
        RequestUtils.createRequest(mContext, "http://195.198.1.211/eggker/interface",method,true,dataParams,true,listener,errorListener);
    }

    TextWatcher tw = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // 显示实时输入内容
        }

        @Override
        public void afterTextChanged(Editable s) {
            String txt = s.toString();
            txtnum = 100-txt.length();
            text_feedback_num.setText(txtnum+"字");
        }
    };
    /**
     * 验证邮箱地址是否正确
     *
     * @param email
     * @return
     */
    public boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }
}
