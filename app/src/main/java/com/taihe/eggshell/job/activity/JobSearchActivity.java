package com.taihe.eggshell.job.activity;

import android.content.Context;
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
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.DbHelper;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.adapter.HotJobAdapter;
import com.taihe.eggshell.job.adapter.SearchHistoryAdapter;
import com.taihe.eggshell.job.bean.SearchHistory;
import com.taihe.eggshell.main.entity.Industry;
import com.taihe.eggshell.main.entity.Professional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huan on 2015/8/11.
 */
public class JobSearchActivity extends BaseActivity implements View.OnClickListener{

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

    private int page = 0;
    private int PAGE_SIZE = 10;

    private DbUtils db;

    @Override
    public void initView() {
        setContentView(R.layout.activity_job_search);
        super.initView();
        mContext = this;

        jobTextView = (TextView)findViewById(R.id.tv_jobsearch_city);
        searchWork = (EditText)findViewById(R.id.et_jobsearch_position);
        searchButton = (Button)findViewById(R.id.btn_jobsearch_search);
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
                ToastUtils.show(mContext,prolist.get(position).getName());
            }
        });

        getDataFromDatabase();
        historyAdapter = new SearchHistoryAdapter(mContext,historyList);
        lv_searchHistory.setAdapter(historyAdapter);
        lv_searchHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.show(mContext,historyList.get(position).getName());
            }
        });



    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_jobsearch_city:
                   ToastUtils.show(mContext,"地址");
                break;
            case R.id.btn_jobsearch_search:
                String word = searchWork.getText().toString();
                if(!TextUtils.isEmpty(word)){
                    ToastUtils.show(mContext,"搜索");
                    SearchHistory history = new SearchHistory();
                    history.setName(word);
                    history.setTime(new Date().toString());
                    try {
                        db.saveOrUpdate(history);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }else{
                    ToastUtils.show(mContext,"请输入内容");
                }

                break;
        }
    }

    private void getHotSearch(){
        String[] media = new String[]{"客户专员哈哈哈","创意专员","企业策划水电费","规划设计","地产销售水电费","测绘测量","清算哥哥员","操盘手","会计","出纳员"};
        for(int j=0;j<media.length;j++){
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

                Selector selector = Selector.from(SearchHistory.class).limit(PAGE_SIZE).offset(offset);

                List<SearchHistory> list = null;
                try {
                    list = DbHelper.getDbUtils(DbHelper.DB_TYPE_USER).findAll(selector);
                } catch (DbException e) {
                    e.printStackTrace();
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

}