package com.it.cn.zhihuibeijing.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不允许滑动的viewpager
 * Created by Administrator on 2017/2/3.
 */

public class NoScrollViewPager extends ViewPager {


    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);
    }
//事件拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;//不拦截子控件
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //重写此方法，什么都不做，达到不能滑动的效果
        return true;
    }
}
