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
public class EduAdapter extends BaseAdapter{

    private Context mContext;
    private List<ResumeData> worklists;

    public EduAdapter(Context context,List<ResumeData> list){
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
        EduViewHolder viewHolder;
        if(null==convertView) {
            viewHolder = new EduViewHolder();
            convertView = LinearLayout.inflate(mContext, R.layout.item_edu, null);

            viewHolder.edutime = (TextView)convertView.findViewById(R.id.id_time_edu);
            viewHolder.eduindusty = (TextView)convertView.findViewById(R.id.id_professional);
            viewHolder.eduschool = (TextView)convertView.findViewById(R.id.id_school_name);
            viewHolder.eduposition = (TextView)convertView.findViewById(R.id.id_school_posion);
            viewHolder.edubrief = (TextView)convertView.findViewById(R.id.id_prof_brief);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (EduViewHolder)convertView.getTag();
        }

        viewHolder.edutime.setText(FormatUtils.timestampToDatetime(work.getSdate())+"——"+FormatUtils.timestampToDatetime(work.getEdate()));
        viewHolder.eduindusty.setText(work.getSpecialty());
        viewHolder.eduposition.setText(work.getTitle());
        viewHolder.eduschool.setText(work.getName());
        viewHolder.edubrief.setText(work.getContent());

        return convertView;
    }

    class EduViewHolder{
        TextView edutime;
        TextView eduindusty;
        TextView eduschool;
        TextView eduposition;
        TextView edubrief;
    }
}
