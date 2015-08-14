package com.taihe.eggshell.job.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huan on 2015/8/14.
 */
public class IndustryActivity extends BaseActivity {

    private String Tag = "IndustryActivity";

    private ListView list_industry;
    private List<String> industLists = null;

    private TextView tv_select;

    private Intent intent;
    private String filterString , title,selectString;

    @Override
    public void initView() {
        setContentView(R.layout.activity_industry_list);
        super.initView();

        industLists = new ArrayList<String>();



        intent = getIntent();
        filterString = intent.getStringExtra("Filter");
        if(filterString.equals("industry")){
            title = "行业类别";
            selectString = "请选择行业类别";

            for (int i = 0; i < 10; i++) {
                industLists.add("计算机/互联网");
            }
        }else   if(filterString.equals("jobyears")){
            title = "工作经验";
            selectString = "请选择工作经验";

            for (int i = 0; i < 10; i++) {
                industLists.add("1年");
            }
        }


        tv_select = (TextView) findViewById(R.id.tv_industry_select);
        tv_select.setText(selectString);
        list_industry = (ListView) findViewById(R.id.list_industry_industry);
        list_industry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String filter = industLists.get(position);
                Log.i(Tag, filter);

                intent = new Intent(IndustryActivity.this, JobFilterActivity.class);
                startActivity(intent);
            }
        });
        list_industry.setAdapter(new IndustryAdapter());


    }

    @Override
    public void initData() {
        super.initData();
        initTitle(title);
    }

    class IndustryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return industLists.size();
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
            tv_industry.setText(industLists.get(position));

            return view;
        }
    }

}
