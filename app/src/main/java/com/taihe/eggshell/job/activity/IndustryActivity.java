package com.taihe.eggshell.job.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.chinaway.framework.swordfish.db.sqlite.Selector;
import com.chinaway.framework.swordfish.exception.DbException;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.DbHelper;
import com.taihe.eggshell.main.entity.StaticData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huan on 2015/8/14.
 */
public class IndustryActivity extends BaseActivity {

    private String Tag = "IndustryActivity";
    private ListView list_industry;
    private List<StaticData> staticDataList = new ArrayList<StaticData>();
    private List<StaticData> selectedDataList = new ArrayList<StaticData>();
    private TextView tv_select;
    private Intent intent;
    private String filterString , title,selectString;
    private IndustrysAdapter industrysAdapter;

    private int page = 0;
    private int PAGE_SIZE = 100;

    @Override
    public void initView() {
        setContentView(R.layout.activity_industry_list);
        super.initView();

        intent = getIntent();
        filterString = intent.getStringExtra("Filter");

        tv_select = (TextView) findViewById(R.id.tv_industry_select);

        list_industry = (ListView) findViewById(R.id.list_industry_industry);
        list_industry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent();
                intent.putExtra("data", selectedDataList.get(position));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();

        selectedDataList.clear();
        getStaticDataFromDB();
        industrysAdapter = new IndustrysAdapter();
        list_industry.setAdapter(industrysAdapter);
    }

    private void initListView() {
        if(filterString.equals("industry")){
            title = "行业类别";
            selectString = "请选择行业类别";

            for(StaticData data:staticDataList){
                if(data.getType().equals("hy")){
                    selectedDataList.add(data);
                }
            }

        }else   if(filterString.equals("jobyears")){
            title = "工作经验";
            selectString = "请选择工作经验";

            for(StaticData data:staticDataList){
                if(data.getType().equals("experience")){
                    selectedDataList.add(data);
                }
            }

        }else if(filterString.equals("position")){
            title = "职位类别";
            selectString = "请选择您的职位";

            for(StaticData data:staticDataList){
                if(data.getType().equals("hy")){
                    selectedDataList.add(data);
                }
            }

        }else if(filterString.equals("salary")){
            title = "月薪范围";
            selectString = "请选择您期望的月薪范围";

            for(StaticData data:staticDataList){
                if(data.getType().equals("pay")){
                    selectedDataList.add(data);
                }
            }

        }else if(filterString.equals("pubtime")){
            title = "发布时间";
            selectString = "请选择职位发布时间";

            for(StaticData data:staticDataList){
                if(data.getType().equals("hy")){
                    selectedDataList.add(data);
                }
            }
        }else if(filterString.equals("edu")){
            title = "学历要求";
            selectString = "请选择学历要求";

            for(StaticData data:staticDataList){
                if(data.getType().equals("education")){
                    selectedDataList.add(data);
                }
            }

        }else if(filterString.equals("jobtype")){
            title = "工作性质";
            selectString = "请选择工作性质";

            for(StaticData data:staticDataList){
                if(data.getType().equals("type")){
                    selectedDataList.add(data);
                }
            }
        }else if(filterString.equals("status")){
            title = "求职状态";
            selectString = "请选择求职状态";
            for(StaticData data:staticDataList){
                if(data.getType().equals("jobstatus")){
                    selectedDataList.add(data);
                }
            }
        } else if(filterString.equals("techlevel")){
            title = "熟练程度";
            selectString = "请选择";
            for(StaticData data:staticDataList){
                if(data.getType().equals("ing")){
                    selectedDataList.add(data);
                }
            }
        } else if(filterString.equals("skill")){
            title = "技能类别";
            selectString = "请选择";
            for(StaticData data:staticDataList){
                if(data.getType().equals("skill")){
                    selectedDataList.add(data);
                }
            }
        }
        initTitle(title);
        tv_select.setText(selectString);
    }

    class IndustrysAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return selectedDataList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = View.inflate(IndustryActivity.this, R.layout.list_industry, null);
            TextView tv_industry = (TextView) view.findViewById(R.id.tv_industrylist_industry);
            tv_industry.setText(selectedDataList.get(position).getName());

            return view;
        }
    }

    private void getStaticDataFromDB() {
        final int offset = page * PAGE_SIZE;
        new AsyncTask<Void, Void, List<StaticData>>() {
            @Override
            protected List<StaticData> doInBackground(Void... params) {

                Selector selector = Selector.from(StaticData.class).limit(PAGE_SIZE).offset(offset);

                List<StaticData> list = null;
                try {
                    list = DbHelper.getDbUtils(DbHelper.DB_TYPE_USER).findAll(selector);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                return list;
            }

            protected void onPostExecute(List<StaticData> result) {
                if (result != null && result.size() > 0) {
                    staticDataList.clear();
                    staticDataList.addAll(result);
                    initListView();
                    industrysAdapter.notifyDataSetChanged();
                } else {
                    Log.v("搜索：", "空");
                }

                super.onPostExecute(result);
            }
        }.execute();
    }

}
