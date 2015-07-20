package com.taihe.eggshell.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager{
	
	private boolean isCanScroll = true;  

	public CustomViewPager(Context context) {
		super(context);
	}

	public CustomViewPager(Context context,AttributeSet attrs) {
		super(context,attrs);
	}
	
	public void setScanSroll(boolean isScroll){
		this.isCanScroll = isScroll;
	}
	
	@Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }
 
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        /* return false;//super.onTouchEvent(arg0); */
        if (isCanScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }
 
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }
 
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }
 
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }
}
