package com.it.cn.zhihuibeijing.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.it.cn.zhihuibeijing.R;

/**
 * 自定义三级缓存图片加载工具
 * Created by Administrator on 2017/2/19.
 */

public class MyBitmapUtils {

    private final NetCacheUtils mNetCacheUtils;
    private LocalCacheUtils  mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public MyBitmapUtils(){
        mMemoryCacheUtils=new MemoryCacheUtils();
        mLocalCacheUtils=new LocalCacheUtils();
        mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils,mMemoryCacheUtils);

    }
    public void display(ImageView imageView, String url) {
        //设置一个默认图片
        imageView.setImageResource(R.mipmap.pic_item_list_default);
        //加载内存缓存
        Bitmap bitmap = mMemoryCacheUtils.getMemoryCache(url);
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
            System.out.println("从内存加载图片");
            return;
        }

        //加载本地缓存
      bitmap = mLocalCacheUtils.getLocalCache(url);
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
            System.out.println("从本地加载图片");
            //写内存
            mMemoryCacheUtils.setMemoryCache(url,bitmap);
            return;
        }
        //加载网络数据
        mNetCacheUtils.getBitmapFromNet(imageView,url);
//        System.out.println("从网络加载内存");

    }
}
