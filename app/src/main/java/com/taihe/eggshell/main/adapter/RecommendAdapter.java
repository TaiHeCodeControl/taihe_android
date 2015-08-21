package com.taihe.eggshell.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.main.entity.RecommendCompany;
import com.taihe.eggshell.widget.MyGridView;

import java.util.List;

/**
 * Created by wang on 2015/8/8.
 */
public class RecommendAdapter extends BaseAdapter{

    private Context context;
    private List<RecommendCompany> companyList;

    public RecommendAdapter(Context mcontext,List<RecommendCompany> list){
        this.context = mcontext;
        this.companyList = list;
    }

    @Override
    public int getCount() {
        return companyList.size();
    }

    @Override
    public Object getItem(int position) {
        return companyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            RecommendCompany company = companyList.get(position);
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recommend_company,parent,false);
            ImageView companylogo = (ImageView)convertView.findViewById(R.id.id_company_logo);
            companylogo.setImageResource(company.getImgsrc());

        return convertView;
    }

}
