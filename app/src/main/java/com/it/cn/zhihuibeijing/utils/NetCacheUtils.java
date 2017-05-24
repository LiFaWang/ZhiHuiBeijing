package com.it.cn.zhihuibeijing.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络缓存工具类
 * Created by Administrator on 2017/2/19.
 */

public class NetCacheUtils {
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;
    public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        mLocalCacheUtils=localCacheUtils;
        mMemoryCacheUtils=memoryCacheUtils;

    }

    public void getBitmapFromNet(ImageView imageView, String url){
        //AsyncTask异步封装工具，可以实现异步请求（对线程池和handler封装）
        new BitmapTask().execute(imageView,url);//启动AsyncTask

    }

    /**
     * 三个泛型意义：
     * 第一个：doInBackground 里的参数
     * 第二个：onProgressUpdate 里的参数
     * 第三个：onPostExecute 里的参数类型及doInBackground里的返回类型
     */
    class BitmapTask extends AsyncTask<Object,Integer,Bitmap> {

        private ImageView imageView;
        private String mUrl;

        //预加载，运行在主线程
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("onPreExecute");
        }

        //正在加载，运行在子线程（核心方法）
        @Override
        protected Bitmap doInBackground(Object... params) {
            System.out.println("doInBackground");
            imageView = (ImageView) params[0];
            mUrl = (String) params[1];

            //开始下载图片
            Bitmap bitmap=download(mUrl);
            return bitmap;
        }

        //进度更新，运行在主线程
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        //加载结束，运行在主线程（核心方法）
        @Override
        protected void onPostExecute(Bitmap result) {
            System.out.println("onPostExecute");
            //给imageVIew打标记
            imageView.setTag(mUrl);
            if(result!=null){
                //给imageView设置图片
                //由于listVIew的重用机制导致imageview对象可能被多个item共用，从而可能将错误的图片设置给了imageview对象
                //所以要在此处校验
                String url = (String) imageView.getTag();
                if(url.equals(mUrl)){//判断图片绑定的url和请求来的图片url是否相等
                    imageView.setImageBitmap(result);
                    //写本地缓存
                    mLocalCacheUtils.setLocalCache(url,result);
                    //写内存缓存
                    mMemoryCacheUtils.setMemoryCache(url,result);

                }
            }

            super.onPostExecute(result);

        }
    }
//下载图像
    private Bitmap download(String url) {
        HttpURLConnection conn=null;
        try {
           conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);//连接超时
            conn.setReadTimeout(5000);//读取超时
            conn.connect();
            int code = conn.getResponseCode();
            if(code==200){
                InputStream inputStream = conn.getInputStream();
                //根据输入流生成bitmap 对象
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return  bitmap;
             }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

              if (conn!=null){
                  conn.disconnect();
            }
        }

        return null;
    }

}
