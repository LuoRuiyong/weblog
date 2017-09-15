package com.luoruiyong.weblog.myview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**自定义viewpager控件，静止左右滑动
 * Created by Administrator on 2017/9/15.
 */

public class MyViewPager extends ViewPager {

    private boolean isScroll = false;

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(isScroll){
            return super.onInterceptTouchEvent(ev);
        }else{
            return false;
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(isScroll){
            return super.onTouchEvent(ev);
        }else{
            return true;
        }

    }
}
