package com.taihe.eggshell.job.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.chinaway.framework.swordfish.DbUtils;
import com.chinaway.framework.swordfish.db.sqlite.Selector;
import com.chinaway.framework.swordfish.db.sqlite.WhereBuilder;
import com.chinaway.framework.swordfish.exception.DbException;
import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.DbHelper;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.adapter.HotJobAdapter;
import com.taihe.eggshell.job.adapter.SearchHistoryAdapter;
import com.taihe.eggshell.job.bean.SearchHistory;
import com.taihe.eggshell.main.entity.Industry;
import com.taihe.eggshell.main.entity.Professional;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by huan on 2015/8/11.
 */
public class JobSearchActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "JobSearchActivity";
    private Context mContext;

    private TextView jobTextView;
    private EditText searchWork;
    private Button searchButton;
    private GridView gv_hotjob;
    private ListView lv_searchHistory;
    private List<Professional> prolist = new ArrayList<Professional>();
    private List<SearchHistory> historyList = new ArrayList<SearchHistory>();
    private SearchHistoryAdapter historyAdapter;
    private LoadingProgressDialog LoadingDialog;
    private int page = 0;
    private int PAGE_SIZE = 10;
    private String Longitude, Latitude;
    private DbUtils db;
    private Intent intents;
    private String fromTags;

    @Override
    public void initView() {
        setContentView(R.layout.activity_job_search);
        super.initView();
        mContext = this;

        intents = getIntent();
        fromTags = intents.getStringExtra("From");
        Longitude = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "Longitude", "");
        Latitude = PrefUtils.getStringPreference(mContext, PrefUtils.CONFIG, "Latitude", "");
        Log.i("job--Longitude ", Longitude + "Latitude====" + Latitude);
        jobTextView = (TextView) findViewById(R.id.tv_jobsearch_city);
        searchWork = (EditText) findViewById(R.id.et_jobsearch_position);
        searchButton = (Button) findViewById(R.id.btn_jobsearch_search);
        gv_hotjob = (GridView) findViewById(R.id.gv_jobsearch_hotjob);
        lv_searchHistory = (ListView) findViewById(R.id.lv_jobsearch_searchhistory);

        jobTextView.setOnClickListener(this);
        searchButton.setOnClickListener(this);

    }

    @Override
    public void initData() {
        super.initData();

        initTitle("职位搜索");
        db = DbHelper.getDbUtils(DbHelper.DB_TYPE_USER);

        getHotSearch();
        gv_hotjob.setAdapter(new HotJobAdapter(mContext, prolist));
        gv_hotjob.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (prolist.get(position).getName().equals("银行柜员")) {
                    PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "keyword", "柜员");
                } else {
                    PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "keyword", prolist.get(position).getName());
                }

                if (fromTags.equals("Index")) {
                    Intent intent = new Intent(JobSearchActivity.this, FindJobActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    setResult(201, intent);
                }
                PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "titleString", "搜索结果");

                JobSearchActivity.this.finish();
            }
        });

        getDataFromDatabase();


        historyAdapter = new SearchHistoryAdapter(mContext, historyList);
        lv_searchHistory.setAdapter(historyAdapter);
        lv_searchHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "keyword", historyList.get(position).getName());
                if (fromTags.equals("Index")) {
                    Intent intent = new Intent(JobSearchActivity.this, FindJobActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    setResult(201, intent);
                }
                PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "titleString", "搜索结果");

                JobSearchActivity.this.finish();
            }
        });


    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_jobsearch_city:
//                ToastUtils.show(mContext, "地址");
                break;
            case R.id.btn_jobsearch_search:
                if (NetWorkDetectionUtils.checkNetworkAvailable(mContext)) {
                    String word = searchWork.getText().toString();
                    if (!TextUtils.isEmpty(word)) {
                        SearchHistory history = new SearchHistory();
                        history.setName(word);
                        history.setTime(new Date().toString());
                        try {
                            db.saveOrUpdate(history);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        getDataFromDatabase();
                        //搜索职位

                        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "keyword", word);

                        if (fromTags.equals("Index")) {
                            Intent intent = new Intent(JobSearchActivity.this, FindJobActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent();
                            setResult(201, intent);
                        }
                        PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "titleString", "搜索结果");
                        this.finish();
                    } else {
                        ToastUtils.show(mContext, "请输入内容");
                    }
                } else {
                    ToastUtils.show(mContext, R.string.check_network);
                }
                break;
        }
    }


    private void getHotSearch() {
        String[] media = new String[]{"网站编辑", "运营专员", "美工", "银行柜员", "会计", "出纳员", "文案策划", "媒介专员"};
        for (int j = 0; j < media.length; j++) {
            Professional professional = new Professional();
            professional.setId(j);
            professional.setName(media[j]);
            prolist.add(professional);
        }
    }

    /**
     * 从数据库获取数据
     */
    private void getDataFromDatabase() {
        final int offset = page * PAGE_SIZE;
        new AsyncTask<Void, Void, List<SearchHistory>>() {
            @Override
            protected List<SearchHistory> doInBackground(Void... params) {

//                Selector selector = Selector.from(SearchHistory.class).limit(PAGE_SIZE).offset(offset);
                Selector selector = Selector.from(SearchHistory.class);

                List<SearchHistory> list = null;
                try {
                    list = DbHelper.getDbUtils(DbHelper.DB_TYPE_USER).findAll(selector);

                } catch (DbException e) {
                    e.printStackTrace();
                }


                if (list != null) {
                    int size = list.size();
                    for (int i = 0; i < size - 1; i++) {
                        for (int j = i + 1; j < size; j++) {
                            if (list.get(i).getName().equalsIgnoreCase(list.get(j).getName())) {
                                list.remove(i);
                                i--;
                                size--;
                                break;
                            }
                        }

                    }
                    if (size > 10) {
                        for (int i = 0; i < size - 10; i++) {
                            list.remove(i);
                        }
                    }
                    Collections.reverse(list);
                }


                return list;
            }

            protected void onPostExecute(List<SearchHistory> result) {
                if (result != null && result.size() > 0) {
                    historyList.clear();
                    historyList.addAll(result);
                    historyAdapter.notifyDataSetChanged();
                } else {
                    Log.v("搜索：", "空");
                }

                super.onPostExecute(result);
            }
        }.execute();
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
