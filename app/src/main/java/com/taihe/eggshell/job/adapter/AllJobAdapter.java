package com.taihe.eggshell.job.adapter;

import android.content.Context;
import android.location.GpsStatus;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.job.bean.JobInfo;
import com.taihe.eggshell.job.fragment.AllJobFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huan on 2015/8/10.
 */
public class AllJobAdapter extends BaseAdapter {

    // 填充数据的list
    private List<JobInfo> list;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;
    private boolean isHaveCheckBox;

    private checkedListener listener;


    // 构造器
    public AllJobAdapter(Context context, List<JobInfo> jobInfos, boolean isHaveCheckBox) {
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

        JobInfo jobInfo = list.get(position);
        View view;
        final ViewHolder holder;
        if (convertView != null) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {

            view = inflater.inflate(R.layout.list_job_all, null);
            holder = new ViewHolder();

//            holder.cb_select = (CheckBox) view.findViewById(R.id.cb_listjob_select);
            holder.tv_jobName = (TextView) view.findViewById(R.id.tv_listjob_jobname);
            holder.tv_businessName = (TextView) view.findViewById(R.id.tv_listjob_businessname);
            holder.tv_city = (TextView) view.findViewById(R.id.tv_listjob_city);
            holder.tv_edu = (TextView) view.findViewById(R.id.tv_listjob_edu);
            holder.tv_pubTiem = (TextView) view.findViewById(R.id.tv_listjob_pubtime);
            holder.tv_salaryRange = (TextView) view.findViewById(R.id.tv_listjob_salaryrange);
//            holder.rl_listjob_select = (RelativeLayout) view.findViewById(R.id.rl_listjob_select);
            holder.view_marginLeft = view.findViewById(R.id.view_marginLeft);
            holder.iv_xuanze = (ImageView) view.findViewById(R.id.iv_joblist_xuanze);


            //判断listview是否带有checkBox
            if (!isHaveCheckBox) {
//                holder.cb_select.setVisibility(View.GONE);
                holder.iv_xuanze.setVisibility(View.GONE);
                holder.view_marginLeft.setVisibility(View.VISIBLE);
            }

            view.setTag(holder);
        }
        holder.tv_jobName.setText(jobInfo.getName());
        holder.tv_businessName.setText(jobInfo.getCom_name());
        holder.tv_city.setText(jobInfo.getProvinceid());
        holder.tv_edu.setText(jobInfo.getEdu());

        holder.tv_pubTiem.setText(jobInfo.getLastupdate());

        holder.tv_salaryRange.setText(jobInfo.getSalary());

        if (list.get(position).isChecked()) {
            holder.iv_xuanze.setImageResource(R.drawable.xuankuang_red);
        } else {
            holder.iv_xuanze.setImageResource(R.drawable.xuankuang);
        }

        holder.iv_xuanze.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (list.get(position).isChecked()) {
                    holder.iv_xuanze.setImageResource(R.drawable.xuankuang);
                    list.get(position).setIsChecked(false);
                    listener.checkedPosition(position, false);
                } else {
                    holder.iv_xuanze.setImageResource(R.drawable.xuankuang_red);
                    list.get(position).setIsChecked(true);
                    listener.checkedPosition(position, true);
                }


            }
        });

        return view;
    }


    public void setCheckedListener(checkedListener listener) {
        this.listener = listener;
    }

    public interface checkedListener {
        public void checkedPosition(int position, boolean isChecked);
    }


    class ViewHolder {

        TextView tv_jobName, tv_businessName, tv_city, tv_edu, tv_pubTiem, tv_salaryRange;
        View view_marginLeft;
        ImageView iv_xuanze;
    }


}



