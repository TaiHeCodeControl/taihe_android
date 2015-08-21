package com.taihe.eggshell.job.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
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
    private checkedListener listener;
    private boolean isHaveCheckBox;


    // 构造器
    public AllJobAdapter(Context context,List<JobInfo> jobInfos,boolean isHaveCheckBox) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = jobInfos;
        this.isHaveCheckBox = isHaveCheckBox;
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
        final ViewHolder holder;
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
            holder.rl_listjob_select = (RelativeLayout) view.findViewById(R.id.rl_listjob_select);

            if(!isHaveCheckBox){
                holder.cb_select.setVisibility(View.GONE);
            }
            view.setTag(holder);
        }
        holder.cb_select.setChecked(false);
        holder.tv_businessName.setText("太和天下");
        holder.rl_listjob_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                holder.cb_select.setChecked(true);
                holder.cb_select.performClick();
            }
        });

        holder.cb_select.setChecked(list.get(position).isChecked());
        holder.cb_select.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                list.get(position).setIsChecked(!list.get(position).isChecked());
                listener.checkedPosition(position,list.get(position).isChecked());
            }
        });


        return view;
    }

    public void notifyDataChanged(boolean allChecked){
        for(JobInfo info:list){
            info.setIsChecked(allChecked);
        }
        notifyDataSetChanged();
    }

    public void setCheckedListener(checkedListener listener){
        this.listener = listener;
    }
    public interface checkedListener{
        public void checkedPosition(int position,boolean isChecked);
    }
    class ViewHolder{

        CheckBox cb_select;
        TextView tv_jobName, tv_businessName,tv_city,tv_edu,tv_pubTiem,tv_salaryRange;
        RelativeLayout rl_listjob_select;
    }



}



