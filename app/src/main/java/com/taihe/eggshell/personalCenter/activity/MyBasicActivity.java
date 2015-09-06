package com.taihe.eggshell.personalCenter.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.utils.GsonUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.bean.MyBasicInfo;
import com.taihe.eggshell.main.MainActivity;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.taihe.eggshell.widget.MyDialog;
import com.taihe.eggshell.widget.addressselect.AddressSelectActivity;
import com.taihe.eggshell.widget.datepicker.JudgeDate;
import com.taihe.eggshell.widget.datepicker.ScreenInfo;
import com.taihe.eggshell.widget.datepicker.WheelMain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 基本资料界面，个人基本信息的查看和编辑
 * Created by huan on 2015/8/11.
 */
public class MyBasicActivity extends Activity implements View.OnClickListener, TextWatcher {


    private static final int REQUEST_CODE_CITY = 10;
    private Context mContext;
    private TextView tv_phone,tv_birthdate, tv_mybasic_sex, tv_address, tv_mybasic_jianjie, tv_save;
    private EditText et_nickname, et_qq, et_email;
    private TextView tv_mybasic_registime;

    private String verTime, jianjie, jianjiehint;

    private Intent intent;
    private RelativeLayout iv_back;
    private RelativeLayout rl_date, rl_sex, rl_city, rl_jianjie;

    WheelMain wheelMain;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String oldphone,oldnickname,oldsex,oldaddress,oldjianjie,oldbirthday,oldemail,oldqq;
    private String newphone,newnickname,newsex,newaddress,newjianjie,newbirthday,newemail,newqq;
    private LoadingProgressDialog LoadingDialog;
    private int UserId;

    private Handler basicHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 10://填充基本资料
                    MyBasicInfo.BasicBean basicBean = (MyBasicInfo.BasicBean) msg.obj;
                    tv_phone.setText(basicBean.telphone);
//                    tv_birthdate.setText(basicBean.);
                    tv_mybasic_sex.setText(basicBean.sex);
                    tv_address.setText(basicBean.address);
                    tv_mybasic_jianjie.setText(basicBean.description);
                    et_nickname.setText(basicBean.name);
//                    et_qq;
                    et_email.setText(basicBean.email);
                    tv_mybasic_registime.setText(basicBean.reg_date);


                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mybasic_edit);
        mContext = this;
        initView();
    }

    public void initView() {

        UserId = EggshellApplication.getApplication().getUser().getId();
        iv_back = (RelativeLayout) findViewById(R.id.iv_mybasic_back);
        tv_mybasic_jianjie = (TextView) findViewById(R.id.tv_mybasic_jianjie);
        tv_birthdate = (TextView) findViewById(R.id.tv_mybasic_birthdate);
        tv_address = (TextView) findViewById(R.id.tv_mybasic_city);
        tv_mybasic_sex = (TextView) findViewById(R.id.tv_mybasic_sex);
        tv_save = (TextView) findViewById(R.id.tv_mybasic_save);
        tv_save.setOnClickListener(this);
        tv_save.setClickable(false);
        tv_phone = (TextView) findViewById(R.id.tv_mybasic_userno);
        tv_mybasic_registime = (TextView) findViewById(R.id.tv_mybasic_registime);

        et_nickname = (EditText) findViewById(R.id.et_mybasic_nickname);
        et_qq = (EditText) findViewById(R.id.et_mybasic_qq);
        et_email = (EditText) findViewById(R.id.et_mybasic_email);


        rl_city = (RelativeLayout) findViewById(R.id.rl_mybasic_city);
        rl_city.setOnClickListener(this);
        rl_date = (RelativeLayout) findViewById(R.id.rl_mybasic_date);
        rl_date.setOnClickListener(this);
        rl_jianjie = (RelativeLayout) findViewById(R.id.rl_mybasic_jianjie);
        rl_jianjie.setOnClickListener(this);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_mybasic_sex);
        rl_sex.setOnClickListener(this);


        //初始化当前时间
        Calendar calendar = Calendar.getInstance();
        String CurrentTime = calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + "";
        //设置默认生日时间
        tv_birthdate.setText(CurrentTime);
        verTime = CurrentTime;


        iv_back.setOnClickListener(this);
        tv_birthdate.setOnClickListener(this);


        initData();


    }

    private void initData() {
        if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
            LoadingDialog = new LoadingProgressDialog(mContext, getResources().getString(
                    R.string.submitcertificate_string_wait_dialog));
            LoadingDialog.show();
            getViewDate();

            oldphone = tv_phone.getText().toString().trim();
            oldaddress = tv_address.getText().toString().trim();
            oldnickname = et_nickname.getText().toString().trim();
            oldqq = et_qq.getText().toString().trim();
            oldemail = et_email.getText().toString().trim();
            oldbirthday = tv_birthdate.getText().toString().trim();
            oldjianjie = tv_mybasic_jianjie.getText().toString().trim();
            oldsex = tv_mybasic_sex.getText().toString().trim();
            tv_address.addTextChangedListener(this);


            tv_birthdate.addTextChangedListener(this);

            tv_mybasic_sex.addTextChangedListener(this);

            tv_mybasic_jianjie.addTextChangedListener(this);
            et_email.addTextChangedListener(this);

            et_qq.addTextChangedListener(this);

            et_nickname.addTextChangedListener(this);
        } else {
            ToastUtils.show(mContext, R.string.check_network);
        }


    }

    //获取用户的基本资料
    private void getViewDate() {
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                LoadingDialog.dismiss();
                try {
                    Log.v("HHH:", (String) o);
                    MyBasicInfo myBasicInfo = GsonUtils
                            .changeGsonToBean(o.toString(),
                                    MyBasicInfo.class);

                    String code = myBasicInfo.code;
                    if (code.equals("0")) {
                        MyBasicInfo.BasicBean basicBean = myBasicInfo.data;
                        Message msg = new Message();
                        msg.what = 10;
                        msg.obj = basicBean;
                        basicHandler.sendMessage(msg);

                        ToastUtils.show(mContext, "获取基本资料成功");
                    } else {
                        ToastUtils.show(mContext, "获取失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LoadingDialog.dismiss();
                try {
                    if (null != volleyError.networkResponse.data) {
                        Log.v("Image:", new String(volleyError.networkResponse.data));
                    }
                    ToastUtils.show(mContext, volleyError.networkResponse.statusCode + "");

                } catch (Exception e) {
                    ToastUtils.show(mContext, "联网失败");
                }

            }
        };


        Map<String, String> param = new HashMap<String, String>();
        param.put("uid", UserId + "");

        RequestUtils.createRequest(mContext, "http://195.198.1.211/eggker/interface/basicdata", "", true, param, true, listener, errorListener);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mybasic_back://退出当前页面

                goBack();

                break;

            case R.id.rl_mybasic_city://选择城市

                intent = new Intent(MyBasicActivity.this, AddressSelectActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CITY);

                break;

            case R.id.rl_mybasic_sex://选择性别
                selectSex(b);
                break;
            case R.id.rl_mybasic_date://选择生日日期
                // 选择日期
                selectDate();
                break;
            case R.id.rl_mybasic_jianjie:
                // 请输入您的个人简介
                showJianjieDialog();
                break;

            case R.id.tv_mybasic_save://保存修改信息
                saveBasic();
                break;
        }
    }

    //提交用户基本资料的修改
    private void saveBasic() {

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                LoadingDialog.dismiss();
                try {
                    Log.v("HHH:", (String) o);

                    JSONObject jsonObject = new JSONObject((String) o);

                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if (code == 0) {
                        ToastUtils.show(mContext, "资料修改成功");
                    } else {
                        ToastUtils.show(mContext, "获取失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LoadingDialog.dismiss();
                try {
                    if (null != volleyError.networkResponse.data) {
                        Log.v("Image:", new String(volleyError.networkResponse.data));
                    }
                    ToastUtils.show(mContext, volleyError.networkResponse.statusCode + "");

                } catch (Exception e) {
                    ToastUtils.show(mContext, "联网失败");
                }

            }
        };

        //http://localhost/eggker/interface/basicdata/add_basicdata  基本资料添加
        // 参数必传项    用户id=>uid  ，  选传项    手机号=>telphone,昵称=>name,性别=>sex,地址=>address,
        // 简介=>description，生日=>birthday,邮箱=>email

        Map<String, String> param = new HashMap<String, String>();
        param.put("uid", UserId + "");
        param.put("telphone",oldphone);
        param.put("name",newnickname);
        param.put("sex",newsex);
        param.put("address",newaddress);
        param.put("description",newjianjie);
        param.put("birthday",newbirthday);
        param.put("email",newemail);
//
        RequestUtils.createRequest(mContext, "http://195.198.1.211/eggker/interface/basicdata/add_basicdata", "", true, param, true, listener, errorListener);

    }

    private void goBack() {
        Intent intent = new Intent(MyBasicActivity.this, MainActivity.class);
        intent.putExtra("MeFragment", "MeFragment");
        startActivity(intent);

        MyBasicActivity.this.finish();
    }

    //-----------------------输入个人简介 start---------------------------------------

    private MyDialog jianjieDialog = null;

    public void showJianjieDialog() {

        View view = View.inflate(mContext, R.layout.dialog_jianjie,
                null);

        RelativeLayout rl_cancel = (RelativeLayout) view.findViewById(R.id.rl_dialogsex_cancel);

        final EditText et_jianjie = (EditText) view.findViewById(R.id.et_jianjie_jianjie);

        jianjiehint = tv_mybasic_jianjie.getText().toString().trim();
        et_jianjie.setHint(jianjiehint);
        Button btn_ok = (Button) view.findViewById(R.id.btn_jianjie_confirm);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                jianjie = et_jianjie.getText().toString().trim();
                if (!TextUtils.isEmpty(jianjie)) {

                    tv_mybasic_jianjie.setText(jianjie);
                }
                jianjieDialog.dismiss();

            }
        });

        rl_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                jianjieDialog.dismiss();
            }
        });

        jianjieDialog = new MyDialog(mContext, view, R.style.mydialog_style);
        jianjieDialog.show();
    }

    //-----------------------选择性别 start---------------------------------------

    private MyDialog sexSelectDialog = null;
    private boolean b = false;

    public void selectSex(boolean a) {

        View view = View.inflate(mContext, R.layout.dialog_sexselect,
                null);

        RelativeLayout rl_cancel = (RelativeLayout) view.findViewById(R.id.rl_dialogsex_cancel);


        RelativeLayout rl_man = (RelativeLayout) view.findViewById(R.id.rl_dialogsex_man);
        RelativeLayout rl_woman = (RelativeLayout) view.findViewById(R.id.rl_dialogsex_woman);

        final ImageView iv_man = (ImageView) view.findViewById(R.id.iv_dialogsex_man);
        final ImageView iv_woman = (ImageView) view.findViewById(R.id.iv_dialogsex_woman);

        if (a) {
            iv_woman.setVisibility(View.VISIBLE);
            iv_man.setVisibility(View.INVISIBLE);
        } else {
            iv_man.setVisibility(View.VISIBLE);
            iv_woman.setVisibility(View.INVISIBLE);
        }


        rl_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sexSelectDialog.dismiss();
            }
        });

        //选择女
        rl_woman.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tv_mybasic_sex.setText("女");
                b = true;
                sexSelectDialog.dismiss();
            }
        });

        //选择男
        rl_man.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tv_mybasic_sex.setText("男");
                b = false;
                sexSelectDialog.dismiss();
            }
        });
        sexSelectDialog = new MyDialog(mContext, view, R.style.mydialog_style);
        sexSelectDialog.show();

    }


    //-----------------------选择日期 start---------------------------------------
    //日期对话框
    private AlertDialog timeDialog;

    /**
     * 选择日期
     */
    private void selectDate() {

        LayoutInflater inflater = LayoutInflater.from(MyBasicActivity.this);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(MyBasicActivity.this);
        wheelMain = new WheelMain(timepickerview);
        wheelMain.screenheight = screenInfo.getHeight();
        String time = tv_birthdate.getText().toString();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
            try {
                calendar.setTime(dateFormat.parse(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year, month, day);

        Button btn_ok = (Button) timepickerview.findViewById(R.id.btn_timepicker_ok);
        Button btn_cancel = (Button) timepickerview.findViewById(R.id.btn_timepicker_cancel);

        timeDialog = new AlertDialog.Builder(MyBasicActivity.this)
                .setView(timepickerview).show();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDialog.dismiss();
                verTime = wheelMain.getTime();
                tv_birthdate.setText(verTime);

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDialog.dismiss();
            }
        });
    }

    //--------------------------------------------------------------

    //监听返回按钮
    @Override
    public void onBackPressed() {
        goBack();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String address = "";
        if (data != null) {
            address = data.getStringExtra("Address");
        }
        if (address == null || TextUtils.isEmpty(address)) {
            return;
        }
        if (requestCode == REQUEST_CODE_CITY && resultCode == AddressSelectActivity.RESULT_CODE_ADDRESS) {

            tv_address.setText(address);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        setButtonState();

    }

    /**
     * 设置编辑&完成按钮的点击状态
     */
    private void setButtonState() {
        newaddress = tv_address.getText().toString().trim();
        newnickname = et_nickname.getText().toString().trim();
        newqq = et_qq.getText().toString().trim();
        newemail = et_email.getText().toString().trim();
        newbirthday = tv_birthdate.getText().toString().trim();
        newjianjie = tv_mybasic_jianjie.getText().toString().trim();
        newsex = tv_mybasic_sex.getText().toString().trim();

        if (!newaddress.equals(oldaddress) || !newnickname.equals(oldnickname) || !newqq.equals(oldqq) ||!newemail.equals(oldemail) || !newbirthday.equals(oldbirthday) || !newjianjie.equals(oldjianjie) || !newsex.equals(oldsex)) {
            tv_save.setClickable(true);
            tv_save.setTextColor(getResources().getColor(R.color.font_color_red));
        } else {
            tv_save.setClickable(false);
            tv_save.setTextColor(getResources().getColor(R.color.font_color_gray));
        }

    }
}
