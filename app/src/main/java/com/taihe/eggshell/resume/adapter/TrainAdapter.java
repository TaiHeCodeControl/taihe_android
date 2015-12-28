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
public class TrainAdapter extends BaseAdapter{

    private Context mContext;
    private List<ResumeData> worklists;

    public TrainAdapter(Context context,List<ResumeData> list){
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
        ResumeData train = worklists.get(position);
        TrainViewHolder viewHolder;
        if(null==convertView) {
            viewHolder = new TrainViewHolder();
            convertView = LinearLayout.inflate(mContext, R.layout.item_train, null);

            viewHolder.traintime = (TextView)convertView.findViewById(R.id.id_time_train);
            viewHolder.traindirection = (TextView)convertView.findViewById(R.id.id_train_direction);
            viewHolder.traincompnay = (TextView)convertView.findViewById(R.id.id_train_company);
            viewHolder.trainbrief = (TextView)convertView.findViewById(R.id.id_train_brief);
            viewHolder.delite = (TextView)convertView.findViewById(R.id.id_delite);
            viewHolder.edit = (TextView)convertView.findViewById(R.id.id_edit);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (TrainViewHolder)convertView.getTag();
        }

        viewHolder.traintime.setText(FormatUtils.timestampToDatetime(train.getSdate())+"——"+FormatUtils.timestampToDatetime(train.getEdate()));
        viewHolder.traincompnay.setText(train.getName());
        viewHolder.traindirection.setText(train.getTitle());
        viewHolder.trainbrief.setText(train.getContent());
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

    class TrainViewHolder{
        TextView traintime;
        TextView traincompnay;
        TextView traindirection;
        TextView trainbrief,delite,edit;
    }
}
