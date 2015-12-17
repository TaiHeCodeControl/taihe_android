package com.taihe.eggshell.personalCenter.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.FormatUtils;
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
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 基本资料界面，个人基本信息的查看和编辑
 * Created by huan on 2015/8/11.
 */
public class MyBasicActivity extends Activity implements View.OnClickListener {


    private static final int REQUEST_CODE_CITY = 10;
    private Context mContext;
    private TextView tv_phone, tv_birthdate, tv_mybasic_sex, tv_address, tv_mybasic_jianjie, tv_save;
    private EditText et_nickname, et_qq, et_email;
    private TextView tv_mybasic_registime;

    private String verTime, jianjie, jianjiehint;

    private Intent intent;
    private RelativeLayout iv_back;
    private RelativeLayout rl_date, rl_sex, rl_city, rl_jianjie;

    WheelMain wheelMain;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String oldphone, oldnickname, oldsex, oldaddress, oldjianjie, oldbirthday, oldemail, oldqq;
    private String newnickname, newsex, newaddress, newjianjie, newbirthday, newemail, newqq;
    private LoadingProgressDialog LoadingDialog;
    private int UserId;
    private String token;

    private TextView text_jianjie_size;
    private int txtsize;

    private Handler basicHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10://填充基本资料
                    MyBasicInfo.BasicBean basicBean = (MyBasicInfo.BasicBean) msg.obj;
                    tv_phone.setText(basicBean.telphone);
//                    tv_birthdate.setText(basicBean.);
                    if (basicBean.sex.equals("0") || basicBean.sex.equals("6")) {
                        oldsex = "男";
                    } else {
                        oldsex = "女";
                    }

                    oldphone = basicBean.telphone;
                    oldaddress = basicBean.address;
                    oldnickname = basicBean.name;
//                    oldqq = et_qq.getText().toString().trim();
                    oldemail = basicBean.email;
                    oldbirthday = basicBean.birthday;
                    oldjianjie = basicBean.description;

                    if (TextUtils.isEmpty(oldjianjie)) {
                        tv_mybasic_jianjie.setHint("个性签名");
                    } else {
                        tv_mybasic_jianjie.setText(oldjianjie);
                    }
                    tv_mybasic_sex.setText(oldsex);
                    tv_address.setText("北京");
                    tv_birthdate.setText(oldbirthday);
                    et_nickname.setText(oldnickname);
//                    et_qq;
                    et_email.setText(oldemail);
                    tv_mybasic_registime.setText(basicBean.reg_date);

                    tv_address.addTextChangedListener(new MyTextWhatch());
                    tv_birthdate.addTextChangedListener(new MyTextWhatch());
                    tv_mybasic_sex.addTextChangedListener(new MyTextWhatch());
                    tv_mybasic_jianjie.addTextChangedListener(new MyTextWhatch());
                    et_email.addTextChangedListener(new MyTextWhatch());
//                    et_qq.addTextChangedListener(new MyTextWhatch());
                    et_nickname.addTextChangedListener(new MyTextWhatch());

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
        token = EggshellApplication.getApplication().getUser().getToken();
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
        LoadingDialog = new LoadingProgressDialog(mContext, getResources().getString(
                R.string.submitcertificate_string_wait_dialog));
        if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
            LoadingDialog.show();
            getViewDate();
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
                    Log.v("MyBasicActivity:", (String) o);
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
                    } else {
//                        ToastUtils.show(mContext, "获取失败");
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
                ToastUtils.show(mContext, "网络异常");

            }
        };

        Map<String, String> param = new HashMap<String, String>();
        param.put("uid", UserId + "");
        param.put("token", token);

        RequestUtils.createRequest(mContext, "", Urls.METHOD_BASIC, true, param, true, listener, errorListener);

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
                if (!FormatUtils.isEmail(newemail) && !TextUtils.isEmpty(newemail)) {
                    ToastUtils.show(mContext, "请填写正确的邮箱");
                    return;
                }

                if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
                    LoadingDialog.show();
                    saveBasic();
                } else {
                    ToastUtils.show(mContext, R.string.check_network);
                }
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
//                    Log.v("SAVEBASIC:", (String) o);

                    JSONObject jsonObject = new JSONObject((String) o);

                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if (code == 0) {
                        ToastUtils.show(mContext, "资料修改成功");
                        MyBasicActivity.this.finish();
                    } else {
//                        ToastUtils.show(mContext, "获取失败");
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
                ToastUtils.show(mContext, "网络异常");
            }
        };

        //http://localhost/eggker/interface/basicdata/add_basicdata  基本资料添加
        // 参数必传项    用户id=>uid  ，  选传项    手机号=>telphone,昵称=>name,性别=>sex,地址=>address,
        // 简介=>description，生日=>birthday,邮箱=>email

        Map<String, String> param = new HashMap<String, String>();
        param.put("uid", UserId + "");
        param.put("telphone", oldphone);
        param.put("name", newnickname);
        if (newsex.equals("男")) {
            newsex = "6";
        } else {
            newsex = "7";
        }
        param.put("sex", newsex);
        param.put("address", newaddress);
        param.put("description", newjianjie);
        param.put("birthday", newbirthday);
        param.put("email", newemail);
        param.put("token", token);

        RequestUtils.createRequest(mContext, Urls.METHOD_BASIC_SAVE, "", true, param, true, listener, errorListener);

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

        text_jianjie_size = (TextView) view.findViewById(R.id.tv_mine_jianjie_size);
        jianjiehint = tv_mybasic_jianjie.getText().toString().trim();
        et_jianjie.setText(jianjiehint);

        text_jianjie_size.setText(15 - jianjiehint.length() + "字");
        et_jianjie.addTextChangedListener(tw);
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
            txtsize = 15 - txt.length();
            text_jianjie_size.setText(txtsize + "字");
        }
    };

    class MyTextWhatch implements TextWatcher {
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

        if (!newaddress.equals(oldaddress) || !newnickname.equals(oldnickname) || !newqq.equals(oldqq) || !newemail.equals(oldemail) || !newbirthday.equals(oldbirthday) || !newjianjie.equals(oldjianjie) || !newsex.equals(oldsex)) {
            tv_save.setClickable(true);
            tv_save.setTextColor(getResources().getColor(R.color.font_color_red));
        } else {
            tv_save.setClickable(false);
            tv_save.setTextColor(getResources().getColor(R.color.font_color_gray));
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
