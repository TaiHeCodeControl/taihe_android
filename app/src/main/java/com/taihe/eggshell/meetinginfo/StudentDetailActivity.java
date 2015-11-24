package com.taihe.eggshell.meetinginfo;

import android.content.Context;
import android.text.Html;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

import net.tsz.afinal.FinalBitmap;

import java.io.UnsupportedEncodingException;

/**
 * Created by wang on 2015/11/24.
 */
public class StudentDetailActivity extends BaseActivity{

    private Context mContext;

    private TextView titleView,usernameTextView,addressTextView,faithTextView,shielView;
    private ImageView imageView;

    @Override
    public void initView() {
        setContentView(R.layout.activity_student);
        super.initView();

        titleView = (TextView)findViewById(R.id.id_user_type_detail);
        imageView = (ImageView)findViewById(R.id.id_v);
        usernameTextView = (TextView)findViewById(R.id.id_user_name);
        addressTextView = (TextView)findViewById(R.id.id_user_city);
        faithTextView = (TextView)findViewById(R.id.id_user_faith);
        shielView = (TextView)findViewById(R.id.id_student_content);
    }

    @Override
    public void initData() {
        super.initData();

        initTitle("V达人");
        titleView.setText(getIntent().getStringExtra("type"));
        PersonModel personModel = getIntent().getParcelableExtra("student");
        FinalBitmap bitmap = FinalBitmap.create(mContext);
        bitmap.display(imageView, personModel.getStudentsphoto());
        usernameTextView.setText(personModel.getStudentsname());
        addressTextView.setText(personModel.getCityname());
        faithTextView.setText(personModel.getMotto());
        byte[] bytes = Base64.decode(personModel.getContent(), Base64.DEFAULT);
        String content = null;
        try {
            content = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        shielView.setText(Html.fromHtml(content));
    }
}
