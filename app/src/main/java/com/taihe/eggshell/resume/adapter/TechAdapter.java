package com.taihe.eggshell.resume.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.resume.entity.ResumeData;

import java.util.List;

/**
 * Created by wang on 2015/9/9.
 */
public class TechAdapter extends BaseAdapter{

    private Context mContext;
    private List<ResumeData> worklists;

    public TechAdapter(Context context,List<ResumeData> list){
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
        ResumeData skill = worklists.get(position);
        TechViewHolder viewHolder;
        if(null==convertView) {
            viewHolder = new TechViewHolder();
            convertView = LinearLayout.inflate(mContext, R.layout.item_tech, null);

            viewHolder.techname = (TextView)convertView.findViewById(R.id.id_tech_name);
            viewHolder.techyears = (TextView)convertView.findViewById(R.id.id_contron_time);
            viewHolder.techlevel = (TextView)convertView.findViewById(R.id.id_hot_level);
            viewHolder.techn = (TextView)convertView.findViewById(R.id.id_tech);
            viewHolder.delite = (TextView)convertView.findViewById(R.id.id_delite);
            viewHolder.edit = (TextView)convertView.findViewById(R.id.id_edit);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (TechViewHolder)convertView.getTag();
        }

        viewHolder.techlevel.setText(skill.getIng());
        viewHolder.techyears.setText(skill.getLongtime()+"年");
        viewHolder.techname.setText(skill.getName());
        viewHolder.techn.setText(skill.getSkill());
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewHolder.delite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return convertView;
    }

    class TechViewHolder{
        TextView techname;
        TextView techyears;
        TextView techlevel;
        TextView techn,delite,edit;
    }
}
