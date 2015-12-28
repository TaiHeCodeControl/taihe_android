package com.taihe.eggshell.resume.adapter;

import android.content.Context;
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
public class ResumeCenterAdapter extends BaseAdapter{

    private Context mContext;
    private List<ResumeData> worklists;
    private String stateposition;
    public ResumeCenterAdapter(Context context, List<ResumeData> list, String stateposition){
        this.mContext = context;
        this.worklists = list;
        this.stateposition = stateposition;
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
            convertView = LinearLayout.inflate(mContext, R.layout.activity_resume_list_mb, null);

            viewHolder.tag1 = (TextView)convertView.findViewById(R.id.id_resume_list_mb_name);
            viewHolder.tag2 = (TextView)convertView.findViewById(R.id.id_resume_list_mb_content);
            viewHolder.tag3 = (TextView)convertView.findViewById(R.id.id_resume_list_mb_date);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (WorkViewHolder)convertView.getTag();
        }

        viewHolder.tag1.setText(work.getName());
        viewHolder.tag2.setText(work.getTitle());
        viewHolder.tag3.setText(FormatUtils.timestampToDatetime(work.getSdate())+"——"+FormatUtils.timestampToDatetime(work.getEdate()));

        return convertView;
    }

    class WorkViewHolder{
        TextView tag1,tag2,tag3;
    }
}
