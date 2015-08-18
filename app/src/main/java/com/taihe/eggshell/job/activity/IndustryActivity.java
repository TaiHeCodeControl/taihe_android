package com.taihe.eggshell.job.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Switch;
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

    public static final int RESULT_CODE_INDUSTRYTYPE = 10;
    public static final int RESULT_CODE_JOBYEAR = 11;
    public static final int RESULT_CODE_EDU = 12;
    public static final int RESULT_CODE_POSITION = 13;
    public static final int RESULT_CODE_JOBCITY = 14;
    public static final int RESULT_CODE_SALARY = 15;
    public static final int RESULT_CODE_JOBTYPE = 16;
    public static final int RESULT_CODE_PUBTIME = 17;

    private int RESULT_CODE = 0;

    @Override
    public void initView() {
        setContentView(R.layout.activity_industry_list);
        super.initView();

        industLists = new ArrayList<String>();

        intent = getIntent();
        filterString = intent.getStringExtra("Filter");

        initListView();

        tv_select = (TextView) findViewById(R.id.tv_industry_select);
        tv_select.setText(selectString);
        list_industry = (ListView) findViewById(R.id.list_industry_industry);
        list_industry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent();
                intent.putExtra("data", industLists.get(position));

                    setResult(RESULT_CODE,intent);

                finish();

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



    private void initListView() {

        if(filterString.equals("industry")){
            title = "行业类别";
            selectString = "请选择行业类别";

            for (int i = 0; i < 10; i++) {
                industLists.add("计算机/互联网"+i);
            }

            RESULT_CODE = RESULT_CODE_INDUSTRYTYPE;

        }else   if(filterString.equals("jobyears")){
            title = "工作经验";
            selectString = "请选择工作经验";

            for (int i = 0; i < 10; i++) {
                industLists.add(i+"年");
            }

            RESULT_CODE = RESULT_CODE_JOBYEAR;
        }else if(filterString.equals("position")){
            title = "职位类别";
            selectString = "请选择您的职位";

            for(int i = 0; i < 10 ; i++){

                industLists.add("工程师" + i);
            }

            RESULT_CODE = RESULT_CODE_POSITION;
        }else if(filterString.equals("salary")){
            title = "月薪范围";
            selectString = "请选择您期望的月薪范围";

            for(int i = 0; i < 10 ; i++){

                industLists.add( i + "000-" + i + 1 + "999");
            }

            RESULT_CODE = RESULT_CODE_SALARY;
        }else if(filterString.equals("pubtime")){
            title = "发布时间";
            selectString = "请选择职位发布时间";

            for(int i = 0; i < 10 ; i++){

                industLists.add( i + "周");
            }
            RESULT_CODE = RESULT_CODE_PUBTIME;
        }else if(filterString.equals("edu")){
            title = "学历要求";
            selectString = "请选择学历要求";

            for(int i = 0; i < 10 ; i++){

                industLists.add(i + "本");
            }

            RESULT_CODE = RESULT_CODE_EDU;
        }else if(filterString.equals("jobtype")){
            title = "工作性质";
            selectString = "请选择工作性质";

            for(int i = 0; i < 10 ; i++){

                industLists.add(i + "全职");
            }
            RESULT_CODE = RESULT_CODE_JOBTYPE;
        }

    }

}
