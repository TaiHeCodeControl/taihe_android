package com.taihe.eggshell.main.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.main.entity.Industry;
import com.taihe.eggshell.main.entity.Professional;

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

        Industry industry = industryList.get(position);
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
        viewHolder.textView.setCompoundDrawables(null,imgDrawable,null,null);

        viewHolder.gridView.setAdapter(new ProfessionalAdapter(context,industry.getProfessionalList()));

        return convertView;
    }

    class IndustryViewHolder{
        TextView textView;
        GridView gridView;
    }

}
