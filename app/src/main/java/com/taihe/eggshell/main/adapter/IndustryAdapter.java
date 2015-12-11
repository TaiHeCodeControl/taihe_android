package com.taihe.eggshell.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.job.activity.FindJobActivity;
import com.taihe.eggshell.job.bean.JobFilterUtils;
import com.taihe.eggshell.main.entity.Industry;

import java.util.List;

/**
 * Created by wang on 2015/8/8.
 */
public class IndustryAdapter extends BaseAdapter {

    private Context context;
    private List<Industry> industryList;

    public IndustryAdapter(Context mcontext, List<Industry> list){
        this.context = mcontext;
        this.industryList = list;
    }

    @Override
    public int getCount() {
        return industryList.size();
    }

    @Override
    public Object getItem(int position) {
        return industryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Industry industry = industryList.get(position);
        IndustryViewHolder viewHolder;
        if(null==convertView){
            viewHolder = new IndustryViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recommend_industry,null);

            viewHolder.textView = (TextView)convertView.findViewById(R.id.id_industry);
            viewHolder.gridView = (GridView)convertView.findViewById(R.id.id_gridview_profession);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (IndustryViewHolder)convertView.getTag();
        }

        viewHolder.textView.setText(industry.getName());
        Drawable imgDrawable = context.getResources().getDrawable(industry.getImgsrc());
        imgDrawable.setBounds(0,0,imgDrawable.getMinimumWidth(),imgDrawable.getMinimumHeight());
        viewHolder.textView.setCompoundDrawables(null, imgDrawable, null, null);
        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String job1 = industry.getId() + "";
                //搜索职位
                if("842".equals(job1)){
                    JobFilterUtils.filterJob(context, "", "", job1,"" , "", "", "", "", "", "", "","");
                } else if("1048".equals(job1)){
                    JobFilterUtils.filterJob(context, "", "", "",job1 , "", "", "", "", "", "", "","");
                }

                Intent intent = new Intent(context,FindJobActivity.class);
                intent.putExtra("jobtype",industry.getName());
                context.startActivity(intent);
            }
        });
        viewHolder.gridView.setAdapter(new ProfessionalAdapter(context,industry.getProfessionalList()));
        viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String job_post = industry.getProfessionalList().get(position).getId() + "";
                //搜索职位
                JobFilterUtils.filterJob(context, "", "", "", "", "", "", "", "", "", "","",job_post);
                Intent intent = new Intent(context,FindJobActivity.class);
                intent.putExtra("jobtype",industry.getProfessionalList().get(position).getName());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class IndustryViewHolder{
        TextView textView;
        GridView gridView;
    }

}
