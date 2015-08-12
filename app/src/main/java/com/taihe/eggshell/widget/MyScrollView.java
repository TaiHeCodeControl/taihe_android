package com.taihe.eggshell.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by wang on 2015/8/10.
 */
public class MyScrollView extends ScrollView{

    private OnScrollChangeListener onScrollChangeListener;

    public MyScrollView(Context context){
        super(context);
    }

    public MyScrollView(Context context,AttributeSet attrs){
        super(context,attrs);
    }

    public MyScrollView(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(null!=onScrollChangeListener){
            onScrollChangeListener.onScrollChange(l,t,oldl,oldt);
        }
    }

    public void setOnScrollChangeListener(OnScrollChangeListener listener){
        this.onScrollChangeListener = listener;
    }

    public interface OnScrollChangeListener{
        void onScrollChange(int x, int y, int oldxX, int oldY);
    }
}
