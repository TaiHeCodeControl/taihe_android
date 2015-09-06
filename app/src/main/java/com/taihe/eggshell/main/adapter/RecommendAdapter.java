package com.taihe.eggshell.main.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.taihe.eggshell.main.CompanyDetailActivity;
import com.taihe.eggshell.main.entity.RecommendCompany;
import com.taihe.eggshell.videoplay.mode.VideoInfoMode;
import com.taihe.eggshell.widget.MyGridView;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * Created by wang on 2015/8/8.
 */
public class RecommendAdapter extends BaseAdapter{

    private Context context;
    private List<VideoInfoMode> companyList;

    public RecommendAdapter(Context mcontext,List<VideoInfoMode> list){
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        VideoInfoMode company = companyList.get(position);
        LogoViewHolder holder;
        if(convertView == null){
            holder = new LogoViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recommend_company,parent,false);
            holder.imageView = (ImageView)convertView.findViewById(R.id.id_company_logo);
            convertView.setTag(holder);
        }else{
            holder = (LogoViewHolder)convertView.getTag();
        }
        String img=companyList.get(position).getVimage().toString();
        if(!img.contains("http://")){
            img="http://test2.tiahel.com//data/upload/hotpic/20150825/14459328941.PNG";
        }
        FinalBitmap bitmap = FinalBitmap.create(context);
        bitmap.display(holder.imageView,img);
//            ImageLoader.getInstance().displayImage("http://img10.3lian.com/c1/newpic/05/32/52.jpg",companylogo);
//            holder.imageView.setImageResource(company.getImgsrc());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,CompanyDetailActivity.class);
                intent.putExtra("id",companyList.get(position).getId().toString());
                intent.putExtra("uid",companyList.get(position).getC_id().toString());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class LogoViewHolder{
        ImageView imageView;
    }

}
