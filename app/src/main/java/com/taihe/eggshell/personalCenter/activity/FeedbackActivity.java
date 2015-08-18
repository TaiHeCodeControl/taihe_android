package com.taihe.eggshell.personalCenter.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.base.utils.UpdateUtils;


/**
 * Created by Thinkpad on 2015/7/15.
 */
public class FeedbackActivity extends BaseActivity{

    private static final String TAG = "FeedbackActivity";
    private Context mContext;

    private EditText et_feedcontext,et_qq,et_email;
    private Button btn_submit;
    private String content,qq,email;
    private int uid;
    private String version;

    @Override
    public void initView() {
        setContentView(R.layout.activity_feedback);
        super.initView();
        mContext = this;


        et_email = (EditText) findViewById(R.id.et_feedback_feedemail);
        et_feedcontext = (EditText) findViewById(R.id.et_feedback_feedcontent);
        et_qq = (EditText) findViewById(R.id.et_feedback_feedqq);

        btn_submit = (Button) findViewById(R.id.btn_feedback_submit);
        btn_submit.setOnClickListener(this);
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
        ToastUtils.show(mContext,"意见提交成功，我们将尽快跟你联系");
    }
}
