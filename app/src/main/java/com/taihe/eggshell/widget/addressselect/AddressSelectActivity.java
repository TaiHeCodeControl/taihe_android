package com.taihe.eggshell.widget.addressselect;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

import com.taihe.eggshell.R;
import com.taihe.eggshell.personalCenter.activity.MyBasicActivity;


/**
 * 选择城市
 */
public class AddressSelectActivity extends Activity implements View.OnClickListener {

    public static final int RESULT_CODE_ADDRESS = 101;
    private DBManager dbm;
    private SQLiteDatabase db;
    private Spinner spinner1 = null;
    private Spinner spinner2 = null;
    //	private Spinner spinner3=null;
    private String province = null;
    private String city = null;
    private String district = null;
    private LinearLayout lin_back;
    private TextView tv_title;
    private EditText et_jiedao;
    private Button btn_confirm,btn_reset;

    private String addressString;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_address);

        et_jiedao = (EditText) findViewById(R.id.et_address_jiedao);
        btn_confirm = (Button) findViewById(R.id.btn_address_comfirm);
        btn_reset = (Button) findViewById(R.id.btn_address_reset);
        btn_reset.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        lin_back = (LinearLayout) findViewById(R.id.lin_back);
        tv_title = (TextView) findViewById(R.id.id_title);
        tv_title.setText("所在地");
        lin_back.setOnClickListener(this);


        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
//        spinner3=(Spinner)findViewById(R.id.spinner3);
        spinner1.setPrompt("省");
//		spinner1.setDropDownWidth(20);
//		spinner1.setDropDownHorizontalOffset(20);

        spinner2.setPrompt("城市");
//		spinner3.setPrompt("地区");

        initSpinner1();
    }

    public void initSpinner1() {
        dbm = new DBManager(this);
        dbm.openDatabase();
        db = dbm.getDatabase();
        List<MyListItem> list = new ArrayList<MyListItem>();

        try {
            String sql = "select * from province";
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            while (!cursor.isLast()) {
                String code = cursor.getString(cursor.getColumnIndex("code"));
                byte bytes[] = cursor.getBlob(2);
                String name = new String(bytes, "gbk");
                MyListItem myListItem = new MyListItem();
                myListItem.setName(name);
                myListItem.setPcode(code);
                list.add(myListItem);
                cursor.moveToNext();
            }
            String code = cursor.getString(cursor.getColumnIndex("code"));
            byte bytes[] = cursor.getBlob(2);
            String name = new String(bytes, "gbk");
            MyListItem myListItem = new MyListItem();
            myListItem.setName(name);
            myListItem.setPcode(code);
            list.add(myListItem);

        } catch (Exception e) {
        }
        dbm.closeDatabase();
        db.close();

        MyAdapter myAdapter = new MyAdapter(this, list);
        spinner1.setAdapter(myAdapter);
        spinner1.setOnItemSelectedListener(new SpinnerOnSelectedListener1());
    }

    public void initSpinner2(String pcode) {
        dbm = new DBManager(this);
        dbm.openDatabase();
        db = dbm.getDatabase();
        List<MyListItem> list = new ArrayList<MyListItem>();

        try {
            String sql = "select * from city where pcode='" + pcode + "'";
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            while (!cursor.isLast()) {
                String code = cursor.getString(cursor.getColumnIndex("code"));
                byte bytes[] = cursor.getBlob(2);
                String name = new String(bytes, "gbk");
                MyListItem myListItem = new MyListItem();
                myListItem.setName(name);
                myListItem.setPcode(code);
                list.add(myListItem);
                cursor.moveToNext();
            }
            String code = cursor.getString(cursor.getColumnIndex("code"));
            byte bytes[] = cursor.getBlob(2);
            String name = new String(bytes, "gbk");
            MyListItem myListItem = new MyListItem();
            myListItem.setName(name);
            myListItem.setPcode(code);
            list.add(myListItem);

        } catch (Exception e) {
        }
        dbm.closeDatabase();
        db.close();

        MyAdapter myAdapter = new MyAdapter(this, list);
        spinner2.setAdapter(myAdapter);
        spinner2.setOnItemSelectedListener(new SpinnerOnSelectedListener2());
    }



    /*
      *  public void initSpinner3(String pcode){
            dbm = new DBManager(this);
             dbm.openDatabase();
             db = dbm.getDatabase();
             List<MyListItem> list = new ArrayList<MyListItem>();

             try {
                String sql = "select * from district where pcode='"+pcode+"'";
                Cursor cursor = db.rawQuery(sql,null);
                cursor.moveToFirst();
                while (!cursor.isLast()){
                    String code=cursor.getString(cursor.getColumnIndex("code"));
                    byte bytes[]=cursor.getBlob(2);
                    String name=new String(bytes,"gbk");
                    MyListItem myListItem=new MyListItem();
                    myListItem.setName(name);
                    myListItem.setPcode(code);
                    list.add(myListItem);
                    cursor.moveToNext();
                }
                String code=cursor.getString(cursor.getColumnIndex("code"));
                byte bytes[]=cursor.getBlob(2);
                String name=new String(bytes,"gbk");
                MyListItem myListItem=new MyListItem();
                myListItem.setName(name);
                myListItem.setPcode(code);
                list.add(myListItem);

            } catch (Exception e) {
            }
             dbm.closeDatabase();
             db.close();

             MyAdapter myAdapter = new MyAdapter(this,list);
             spinner3.setAdapter(myAdapter);
            spinner3.setOnItemSelectedListener(new SpinnerOnSelectedListener3());
        }
        */
    class SpinnerOnSelectedListener1 implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapterView, View view, int position,
                                   long id) {
            province = ((MyListItem) adapterView.getItemAtPosition(position)).getName();
            String pcode = ((MyListItem) adapterView.getItemAtPosition(position)).getPcode();

            initSpinner2(pcode);
//			initSpinner3(pcode);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            // TODO Auto-generated method stub
        }
    }

    class SpinnerOnSelectedListener2 implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapterView, View view, int position,
                                   long id) {
            city = ((MyListItem) adapterView.getItemAtPosition(position)).getName();
            String pcode = ((MyListItem) adapterView.getItemAtPosition(position)).getPcode();
//            Toast.makeText(AddressSelectActivity.this, province + " " + city, Toast.LENGTH_LONG).show();

            addressString = province + city ;
//			initSpinner3(pcode);

        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            // TODO Auto-generated method stub
        }
    }

    class SpinnerOnSelectedListener3 implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapterView, View view, int position,
                                   long id) {
            district = ((MyListItem) adapterView.getItemAtPosition(position)).getName();
            Toast.makeText(AddressSelectActivity.this, province + " " + city + " " + district, Toast.LENGTH_LONG).show();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            // TODO Auto-generated method stub
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                AddressSelectActivity.this.finish();
                break;
            case R.id.btn_address_comfirm://确认地址

                Intent intent = new Intent();
                intent.putExtra("Address",addressString + et_jiedao.getText().toString());
                setResult(RESULT_CODE_ADDRESS, intent);
                AddressSelectActivity.this.finish();
                //                //当前界面向右退出
//                overridePendingTransition(R.anim.activity_left_to_center, R.anim.activity_center_to_right);
                break;

            case R.id.btn_address_reset:
                et_jiedao.setText("");
                spinner1.setSelection(0);
                spinner2.setSelection(0);
                break;
        }
    }
}