package com.taihe.eggshell.widget.cityselect;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Constants;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.personalCenter.entity.VisitedPerson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by huan on 2015/8/5.
 */

public class CitySelectActivity extends BaseActivity {
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    private CharacterParser characterParser;//汉字转换成拼音的类
    private List<SortModel> SourceDateList,lis;
    private PinyinComparator pinyinComparator;//根据拼音来排列ListView里面的数据类
    private List<SortModel> list;
    private AsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象
    private Map<Integer, SortModel> contactIdMap = null;
    private ArrayList<VisitedPerson> visitedPersons;

    private BroadcastReceiver sendMessage = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("TAG", "send。。" + intent.getAction() + "---" + getResultCode() + "---" + getResultData());
            //判断短信是否发送成功
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT).show();
                    sendVisitedPerson(intent.getStringExtra("phoneNum"));
                    break;
                default:
                    Toast.makeText(mContext, "发送失败", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("TAG", "receiver。。" + intent.getAction() + "---" + getResultCode() + "---" + getResultData());
            //表示对方成功收到短信
            Toast.makeText(mContext, "对方接收成功", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_place);
        super.initView();

        initViews();
    }

    @Override
    public void initData() {
        super.initData();

        initTitle("邀请手机好友");

        visitedPersons = (ArrayList<VisitedPerson>)getIntent().getExtras().getSerializable("visitedPerson");

        /* 自定义IntentFilter为SENT_SMS_ACTIOIN Receiver */
        IntentFilter mFilter = new IntentFilter(Constants.SENT_SMS_ACTION);
//        IntentFilter mFilter = new IntentFilter(Intent.ACTION_SENDTO);//系统界面
        registerReceiver(sendMessage, mFilter);

        /* 自定义IntentFilter为DELIVERED_SMS_ACTION Receiver */
        IntentFilter deliver = new IntentFilter(Constants.DELIVERED_SMS_ACTION);
        registerReceiver(receiver, deliver);

        // 实例化
        asyncQueryHandler = new MyAsyncQueryHandler(getContentResolver());
        init();
    }

    private void initViews() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
//                Toast.makeText(getApplication(), ((SortModel)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        //listview 数据获取
//        SourceDateList = filledData(getResources().getStringArray(R.array.date));

        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(sendMessage);
        unregisterReceiver(receiver);
    }

    private class MyAsyncQueryHandler extends AsyncQueryHandler {

        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                contactIdMap = new HashMap<Integer, SortModel>();
                SourceDateList = new ArrayList<SortModel>();
                cursor.moveToFirst(); // 游标移动到第一项
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    String name = cursor.getString(1);
                    String number = cursor.getString(2);
                    int contactId = cursor.getInt(4);

                    if (contactIdMap.containsKey(contactId)) {
                        // 无操作
                    } else {
                        // 创建联系人对象
                        SortModel contact = new SortModel();
                        contact.setName(name);
                        contact.setPhoneNum(number);
                        contact.setIsVisited("0");
                        SourceDateList.add(contact);
                        contactIdMap.put(contactId, contact);
                    }
                }
                if (SourceDateList.size() > 0) {
                    String[] namse = new String[SourceDateList.size()];
                    for(int i=0;i<SourceDateList.size();i++){
                            namse[i] = SourceDateList.get(i).getName();
                    }
                    lis = filledData(namse,SourceDateList);
                    // 根据a-z进行排序源数据
                    Collections.sort(lis, pinyinComparator);
                    adapter = new SortAdapter(mContext, lis);
                    sortListView.setAdapter(adapter);
                }
            }

            super.onQueryComplete(token, cookie, cursor);
        }

    }

    private void init() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
        // 查询的字段
        String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };
        // 按照sort_key升序查詢
        asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
                "sort_key COLLATE LOCALIZED asc");

    }

    /**
     * 为ListView填充数据
     * @param date
     * @return
     */
    private List<SortModel> filledData(String [] date,List<SortModel> list){
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0; i<date.length; i++){

            SortModel sortModel = new SortModel();
            for(int j=0;j<visitedPersons.size();j++){
                if(visitedPersons.get(j).getTelphone().equals(list.get(i).getPhoneNum())){
                    sortModel.setIsVisited("1");
                    break;
                }else{
                    sortModel.setIsVisited("0");
                }
            }

            sortModel.setName(date[i]);
            sortModel.setPhoneNum(list.get(i).getPhoneNum());

            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * @param filterStr
     */
    private void filterData(String filterStr){
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if(TextUtils.isEmpty(filterStr)){
            filterDateList = lis;
        }else{
            filterDateList.clear();
            for(SortModel sortModel : lis){
                String name = sortModel.getName();
                if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    private void sendVisitedPerson(String number){
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {

                   Log.v("PESON:",(String)o);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        };

        Map<String,String> params = new HashMap<String,String>();
        params.put("uid", EggshellApplication.getApplication().getUser().getId()+"");
        params.put("telphone",number);

        Log.v("PARAL:",params.toString());
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(),Urls.METHOD_SEND_VISITED_PERSON,true,params,true,listener,errorListener);
    }

}
