package com.taihe.eggshell.job.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.job.activity.JobDetailActivity;
import com.taihe.eggshell.job.bean.JobInfo;

import java.util.List;

/**
 * Created by huan on 2015/8/10.
 */
public class CompanyDetailAdapter extends BaseAdapter {

    // 填充数据的list
    private List<JobInfo> list;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;


    // 构造器
    public CompanyDetailAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }
    public void  setComData(List<JobInfo> jobInfos){
        this.list = jobInfos;
        notifyDataSetChanged();
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

        final JobInfo jobInfo = list.get(position);
        View view;
        final ViewHolder holder;
        if (convertView != null) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.company_detail_job_list, null);
            holder = new ViewHolder();
            holder.tv_jobName = (TextView) view.findViewById(R.id.COMDETtv_listjob_jobname);
            holder.tv_comName = (TextView) view.findViewById(R.id.COMDETtv_listjob_comname);
            holder.tv_city = (TextView) view.findViewById(R.id.COMDETtv_listjob_city);
            holder.tv_edu = (TextView) view.findViewById(R.id.COMDETtv_listjob_edu);
            holder.tv_pubTiem = (TextView) view.findViewById(R.id.COMDETtv_listjob_pubtime);
            holder.tv_salaryRange = (TextView) view.findViewById(R.id.COMDETtv_listjob_salaryrange);
            view.setTag(holder);
        }

        String jobName = jobInfo.getName();
        if(!TextUtils.isEmpty(jobName)){
            holder.tv_jobName.setText(jobName);
        }
        if(!TextUtils.isEmpty(jobInfo.getName())){
            holder.tv_comName.setText(jobInfo.getCom_name());
        }
        if(!TextUtils.isEmpty(jobInfo.getProvinceid())){
            holder.tv_city.setText(jobInfo.getProvinceid());
        }
        if(!TextUtils.isEmpty(jobInfo.getEdu())){
            holder.tv_edu.setText(jobInfo.getEdu());
        }
        if(!TextUtils.isEmpty(jobInfo.getLastupdate())){
          holder.tv_pubTiem.setText(jobInfo.getLastupdate().substring(5));
        }
        if(!TextUtils.isEmpty(jobInfo.getSalary())){
            holder.tv_salaryRange.setText(jobInfo.getSalary());
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, JobDetailActivity.class);
                intent.putExtra("ID",jobInfo.getJob_Id());
                intent.putExtra("com_id",jobInfo.getUid().toString()+jobInfo.getJob_Id()+">"+jobInfo.getUid());
                context.startActivity(intent);
            }
        });
        return view;
    }

    class ViewHolder {
        TextView tv_jobName, tv_comName,tv_city, tv_edu, tv_pubTiem, tv_salaryRange;
    }


}



