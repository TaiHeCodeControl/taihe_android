package com.taihe.eggshell.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.main.entity.Professional;

import java.util.List;

/**
 * Created by wang on 2015/8/8.
 */
public class ProfessionalAdapter extends BaseAdapter{

    private Context context;
    private List<Professional> professionalList;

    public ProfessionalAdapter(Context mcontext,List<Professional> list){
        this.context = mcontext;
        this.professionalList = list;
    }

    @Override
    public int getCount() {
        return professionalList.size();
    }

    @Override
    public Object getItem(int position) {
        return professionalList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Professional professional = professionalList.get(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_recommend_professional,null);
        TextView textView = (TextView)convertView.findViewById(R.id.id_profession);
        textView.setText(professional.getName());

        return convertView;
    }
}
