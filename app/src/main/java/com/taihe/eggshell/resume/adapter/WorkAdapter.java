package com.taihe.eggshell.resume.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.utils.FormatUtils;
import com.taihe.eggshell.resume.entity.ResumeData;

import java.util.List;

/**
 * Created by wang on 2015/9/9.
 */
public class WorkAdapter extends BaseAdapter{

    private Context mContext;
    private List<ResumeData> worklists;
    public WorkAdapter(Context context,List<ResumeData> list){
        this.mContext = context;
        this.worklists = list;
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
            convertView = LinearLayout.inflate(mContext, R.layout.item_work, null);

            viewHolder.worktime = (TextView)convertView.findViewById(R.id.id_time_work);
            viewHolder.workposition = (TextView)convertView.findViewById(R.id.id_position);
            viewHolder.workcompany = (TextView)convertView.findViewById(R.id.id_company_name);
            viewHolder.workcontent = (TextView)convertView.findViewById(R.id.id_work_content);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (WorkViewHolder)convertView.getTag();
        }

        viewHolder.worktime.setText(FormatUtils.timestampToDatetime(work.getSdate())+"——"+FormatUtils.timestampToDatetime(work.getEdate()));
        viewHolder.workposition.setText(work.getName());
        viewHolder.workcompany.setText(work.getDepartment());
        viewHolder.workcontent.setText(work.getContent());

        return convertView;
    }

    class WorkViewHolder{
        TextView worktime;
        TextView workposition;
        TextView workcompany;
        TextView workcontent;
    }
}
