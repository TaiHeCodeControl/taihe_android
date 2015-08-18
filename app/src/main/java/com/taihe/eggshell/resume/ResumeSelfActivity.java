package com.taihe.eggshell.resume;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

/**
 * Created by wang on 2015/8/14.
 */
public class ResumeSelfActivity extends BaseActivity{

    private static final String TAG = "ResumeSelfActivity";

    private Context mContext;

    private TextView commitText,resetText;
    private EditText contextEdit;
    private String content;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resume_self);
        super.initView();

        mContext = this;

        commitText = (TextView)findViewById(R.id.id_commit);
        resetText = (TextView)findViewById(R.id.id_reset);
        contextEdit = (EditText)findViewById(R.id.id_context);

        commitText.setOnClickListener(this);
        resetText.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        initTitle("写简历");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_commit:
                content = contextEdit.getText().toString();

                break;
            case R.id.id_reset:
                contextEdit.setHint("2015-01-01");
                break;
        }
    }
}
