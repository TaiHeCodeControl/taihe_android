package com.taihe.eggshell.main.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.taihe.eggshell.base.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by Thinkpad on 2015/7/29.
 */
public class ChangImageAdapter extends PagerAdapter{

    private Context mContext;
    private ArrayList<ImageView> mlist;

    public ChangImageAdapter(Context context,ArrayList<ImageView> list){
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public int getCount() {
        if(mlist.size()==1){
            return mlist.size();
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        if(((ViewPager)container).getChildCount()==mlist.size()){
            ((ViewPager)container).removeView(mlist.get(position%mlist.size()));
        }
        ((ViewPager)container).addView(mlist.get(position%mlist.size()), 0);

        mlist.get(position%mlist.size()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(mContext,position%mlist.size()+"");
            }
        });

        return mlist.get(position%mlist.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        ((ViewPager)container).removeView(mlist.get(position%mlist.size()));//循环去掉
    }
}
