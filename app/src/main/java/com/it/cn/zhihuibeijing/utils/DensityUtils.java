package com.it.cn.zhihuibeijing.utils;

import android.content.Context;

/**
 * Created by Administrator on 2017/2/20.
 */

public class DensityUtils {
    public static int dip2px(float dip, Context ctx){
        float density = ctx.getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);
        return px;
    }
    public static float px2dip(int px ,Context ctx){
        float density = ctx.getResources().getDisplayMetrics().density;
        float dip = px / density;
        return dip;
    }
}
