package com.it.cn.zhihuibeijing.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/2/9.
 */

public class TopNewsViewPager extends ViewPager {

    private int startx;
    private int starty;

    public TopNewsViewPager(Context context) {
        super(context);
    }

    /**
     * 自定义ViewPager时，不能漏了这个构造方法否则会报错
     * --Caused by: java.lang.NoSuchMethodException:
     * <init> [class android.content.Context, interface android.util.AttributeSet]
     * @param context
     * @param attrs
     */
    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected boolean dispatchGenericFocusedEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startx = (int) event.getX();
                starty = (int) event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                int endx = (int) event.getX();
                int endy = (int) event.getY();
                int dx = endx - startx;
                int dy = endy - starty;
                if(Math.abs(dy)<Math.abs(dx)){
                    int currentItem = getCurrentItem();

                    //左右滑动
                    if(dx>0){
                        System.out.println("255");
                        //向右滑动，第一个页面拦截
                        if(currentItem==0){
                            System.out.println("253");
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }

                    }else {
                        //向左滑,最后一个页面拦截
                        System.out.println("200");
                        int count = getAdapter().getCount();
                        if(currentItem==count-1){
                            getParent().requestDisallowInterceptTouchEvent(false);

                        }
                    }

                }else {
                    //上下滑动
                    getParent().requestDisallowInterceptTouchEvent(false);

                }


                break;

            default:

                break;
        }
        return super.dispatchGenericFocusedEvent(event);
    }
}
