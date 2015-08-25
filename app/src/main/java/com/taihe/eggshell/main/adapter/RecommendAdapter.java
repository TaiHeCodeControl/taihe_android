package com.taihe.eggshell.main.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.network.http.toolbox.ImageLoader;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.RequestUtils;
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
//            ImageLoader.getInstance().displayImage("http://img10.3lian.com/c1/newpic/05/32/52.jpg",companylogo);
            companylogo.setImageResource(company.getImgsrc());

        return convertView;
    }

}
