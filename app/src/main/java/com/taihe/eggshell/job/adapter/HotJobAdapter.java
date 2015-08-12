package com.taihe.eggshell.job.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.main.entity.Professional;

import java.util.List;

/**
 * Created by huan on 2015/8/11.
 */
public class HotJobAdapter extends BaseAdapter {

    private Context context;
    private TextView tv_hotjob;
    private List<Professional> professionalList;

    public HotJobAdapter(Context context,List<Professional> list) {
        this.context = context;
        this.professionalList = list;
    }

    @Override
    public int getCount() {
        return professionalList.size();
    }

    @Override
    public Object getItem(int i) {
        return professionalList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final Professional professional = professionalList.get(i);
        HotViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new HotViewHolder();
            convertView = View.inflate(context,R.layout.gridview_hotjob, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_gridview_hotjob);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HotViewHolder)convertView.getTag();
        }

        viewHolder.textView.setText(professional.getName());

        return convertView;
    }

    class HotViewHolder{
        TextView textView;
    }

}
