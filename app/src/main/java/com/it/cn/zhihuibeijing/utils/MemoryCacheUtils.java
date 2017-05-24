package com.it.cn.zhihuibeijing.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 内存缓存
 * 谷歌从（API 9），垃圾回收器倾向于回收持有软引用和弱引用的对象，谷歌建议使用LruCache
 * Created by Administrator on 2017/2/20.
 */

public class MemoryCacheUtils {
//    private HashMap<String,Bitmap> mMemoryCache=new HashMap<>();
//    private HashMap<String,SoftReference<Bitmap>> mMemoryCache=new HashMap<>();
    private LruCache <String,Bitmap> mMemoryCache;
    public MemoryCacheUtils(){
        //LruCache 可以将最近最少使用的对象回收掉，从而不会超出内存
        //Lrc : least recently used 最近最少使用算法
        long maxMemory = Runtime.getRuntime().maxMemory();//获取分配给app的内存大小
        System.out.println("maxMemory"+maxMemory);
        mMemoryCache=new LruCache<String, Bitmap>((int) (maxMemory/8)){
            //返回对象的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();//计算图片大小：每行字节数*高度
            }
        };

    }

    /**
     * 写缓存
     */
    public void setMemoryCache(String url,Bitmap bitmap){
//        mMemoryCache.put(url,bitmap);
//        SoftReference<Bitmap> soft=new SoftReference<>(bitmap);
//        mMemoryCache.put(url,soft);
        mMemoryCache.put(url,bitmap);
    }

    /**
     * 读缓存
     */
    public Bitmap getMemoryCache(String url){
//        return mMemoryCache.get(url);
//        SoftReference<Bitmap> softReference = mMemoryCache.get(url);
//
//        if(softReference!=null){
//            Bitmap bitmap = softReference.get();
//
//            return bitmap;
//
//        }
        return mMemoryCache.get(url);

    }
}
