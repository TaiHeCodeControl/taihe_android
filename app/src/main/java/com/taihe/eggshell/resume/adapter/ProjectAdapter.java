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
 * Created by wang on 2015/9/10.
 */
public class ProjectAdapter extends BaseAdapter{

    private Context mContext;
    private List<ResumeData> worklists;

    public ProjectAdapter(Context context,List<ResumeData> list){
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
        ResumeData project = worklists.get(position);
        ProjectViewHolder viewHolder;
        if(null==convertView) {
            viewHolder = new ProjectViewHolder();
            convertView = LinearLayout.inflate(mContext, R.layout.item_project, null);

            viewHolder.projecttime = (TextView)convertView.findViewById(R.id.id_time_project);
            viewHolder.projectpostion = (TextView)convertView.findViewById(R.id.id_own_posion);
            viewHolder.projectname = (TextView)convertView.findViewById(R.id.id_project_name);
            viewHolder.projectbrief = (TextView)convertView.findViewById(R.id.id_content);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ProjectViewHolder)convertView.getTag();
        }

        viewHolder.projecttime.setText(FormatUtils.timestampToDatetime(project.getSdate())+"——"+FormatUtils.timestampToDatetime(project.getEdate()));
        viewHolder.projectname.setText(project.getName());
        viewHolder.projectbrief.setText(project.getContent());
        viewHolder.projectpostion.setText(project.getTitle());

        return convertView;
    }

    class ProjectViewHolder{
        TextView projecttime;
        TextView projectname;
        TextView projectbrief;
        TextView projectpostion;
    }
}
