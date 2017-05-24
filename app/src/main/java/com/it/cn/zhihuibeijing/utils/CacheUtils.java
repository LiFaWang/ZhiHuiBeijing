package com.it.cn.zhihuibeijing.utils;

import android.content.Context;

/**
 * 网络缓存的工具类
 * Created by Administrator on 2017/2/4.
 */

public class CacheUtils {
    /**
     *
     * @param url
     * @param json
     * @param ctx
     */
    public static void setCache(String url, String json, Context ctx){
        PrefUtils.setString(ctx,url,json);

    }

    /**
     * 获取缓存
     * @param url
     * @param ctx
     * @return
     */

    public static String getCache(String url,Context ctx){
        return PrefUtils.getString(ctx,url,null);

    }

}
