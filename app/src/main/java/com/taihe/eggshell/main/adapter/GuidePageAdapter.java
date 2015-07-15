package com.taihe.eggshell.main.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.main.MainActivity;

import java.util.ArrayList;

/**
 * Created by Thinkpad on 2015/7/15.
 */
public class GuidePageAdapter extends PagerAdapter{

    private Context mContext;
    private ArrayList<View> imgList;

    public GuidePageAdapter(Context context,ArrayList<View> imagelist){
        this.mContext = context;
        this.imgList = imagelist;
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView(imgList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager)container).addView(imgList.get(position),0);
        ToastUtils.show(mContext,position+":"+imgList.size());
        if(position == imgList.size()-1){
            Button button = (Button)container.findViewById(R.id.id_go);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrefUtils.saveBooleanData(mContext, PrefUtils.KEY_FIRST_USE, false);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(intent);
                    ((Activity)mContext).finish();
                }
            });
        }

        return imgList.get(position);
    }

}
