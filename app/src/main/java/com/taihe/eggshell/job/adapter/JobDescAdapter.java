package com.taihe.eggshell.job.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taihe.eggshell.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by wang on 2015/8/10.
 */
public class JobDescAdapter extends BaseAdapter{

    private Context context;
    private List<String> desclist;

    public JobDescAdapter(Context mcontext,List<String> list){
        this.context = mcontext;
        this.desclist = list;
    }

    @Override
    public int getCount() {
        return desclist.size();
    }

    @Override
    public Object getItem(int position) {
        return desclist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.item_job_desc,null);
        TextView textView = (TextView)convertView.findViewById(R.id.id_job_desc_num);
        textView.setText(desclist.get(position));
        return convertView;
    }
}
