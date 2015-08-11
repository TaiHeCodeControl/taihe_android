package com.taihe.eggshell.job.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.job.bean.JobInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huan on 2015/8/10.
 */
public class AllJobAdapter extends BaseAdapter {

    // 填充数据的list
    private List<JobInfo> list;
    private boolean allChecked = false;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;



    // 构造器
    public AllJobAdapter(Context context,List<JobInfo> jobInfos) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = jobInfos;
    }



    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        View view;
        ViewHolder holder;
        if(convertView != null){
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }else{

            view = inflater.inflate(R.layout.list_job_all, null);
            holder = new ViewHolder();

            holder.cb_select = (CheckBox) view.findViewById(R.id.cb_listjob_select);
            holder.tv_jobName = (TextView) view.findViewById(R.id.tv_listjob_jobname);
            holder.tv_businessName = (TextView) view.findViewById(R.id.tv_listjob_businessname);
            holder.tv_city = (TextView) view.findViewById(R.id.tv_listjob_city);
            holder.tv_edu = (TextView) view.findViewById(R.id.tv_listjob_edu);
            holder.tv_pubTiem = (TextView) view.findViewById(R.id.tv_listjob_pubtime);
            holder.tv_salaryRange = (TextView) view.findViewById(R.id.tv_listjob_salaryrange);

            view.setTag(holder);
        }
        holder.tv_businessName.setText("太和天下");

        holder.cb_select.setChecked(allChecked);
        holder.cb_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                list.get(position).setIsChecked(b);
            }
        });


        return view;
    }

    public void notifyDataChanged(boolean allChecked){
        this.allChecked = allChecked;
        notifyDataSetChanged();
    }



    class ViewHolder{

        CheckBox cb_select;
        TextView tv_jobName, tv_businessName,tv_city,tv_edu,tv_pubTiem,tv_salaryRange;
    }



}



