package com.taihe.eggshell.resume.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.FormatUtils;
import com.taihe.eggshell.resume.entity.ResumeData;

import java.util.List;

/**
 * Created by wang on 2015/9/9.
 */
public class ResumeAdapter extends BaseAdapter{

    private Context mContext;
    private List<ResumeData> worklists;
    private int stateposition;
    private String[] title;
    public ResumeAdapter(Context context, List<ResumeData> list,int stateposition,String[] title){
        this.mContext = context;
        this.worklists = list;
        this.stateposition = stateposition;
        this.title = title;
    }
    @Override
    public int getCount() {
        return worklists.size();
    }

    @Override
    public Object getItem(int position) {
        return worklists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResumeData work = worklists.get(position);
        WorkViewHolder viewHolder;
        if(null==convertView) {
            viewHolder = new WorkViewHolder();
            convertView = LinearLayout.inflate(mContext, R.layout.activity_resume_scan_mb, null);

            viewHolder.tag1 = (TextView)convertView.findViewById(R.id.id_company_name_mb1);
            viewHolder.tag2 = (TextView)convertView.findViewById(R.id.id_start_time_mb_1);
            viewHolder.tag3 = (TextView)convertView.findViewById(R.id.id_department_mb_1);
            viewHolder.tag4 = (TextView)convertView.findViewById(R.id.id_position_mb_1);
            viewHolder.tag5 = (TextView)convertView.findViewById(R.id.id_context_mb_1);

            viewHolder.value1 = (TextView)convertView.findViewById(R.id.id_company_name_mb2);
            viewHolder.value2 = (TextView)convertView.findViewById(R.id.id_start_time_mb_2);
            viewHolder.value3 = (TextView)convertView.findViewById(R.id.id_department_mb_2);
            viewHolder.value4 = (TextView)convertView.findViewById(R.id.id_position_mb_2);
            viewHolder.value5 = (TextView)convertView.findViewById(R.id.id_context_mb_2);

            viewHolder.linear1 = (LinearLayout)convertView.findViewById(R.id.id_linear_scan_mb1);
            viewHolder.linear2 = (LinearLayout)convertView.findViewById(R.id.id_linear_scan_mb2);
            viewHolder.linear3 = (LinearLayout)convertView.findViewById(R.id.id_linear_scan_mb3);
            viewHolder.linear4 = (LinearLayout)convertView.findViewById(R.id.id_linear_scan_mb4);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (WorkViewHolder)convertView.getTag();
        }
        viewHolder.tag1.setText(title[0]);
        viewHolder.tag2.setText(title[1]);
        viewHolder.tag3.setText(title[2]);
        viewHolder.tag4.setText(title[3]);
        viewHolder.tag5.setText(title[4]);

        viewHolder.value1.setText(work.getName());
        viewHolder.value2.setText(FormatUtils.timestampToDatetime(work.getSdate())+"——"+FormatUtils.timestampToDatetime(work.getEdate()));
        if(stateposition==2){
            viewHolder.value3.setText(work.getSpecialty());
        }else {
            viewHolder.value3.setText(work.getDepartment());
        }
        viewHolder.value4.setText(work.getTitle());
        viewHolder.value5.setText(work.getContent());

        if(stateposition==3){
            viewHolder.linear3.setVisibility(View.GONE);
        }
        return convertView;
    }

    class WorkViewHolder{
        TextView tag1,tag2,tag3,tag4,tag5;
        TextView value1,value2,value3,value4,value5;
        LinearLayout linear1,linear2,linear3,linear4;
    }
}
