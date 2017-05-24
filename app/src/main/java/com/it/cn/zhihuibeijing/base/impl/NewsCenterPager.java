package com.it.cn.zhihuibeijing.base.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.it.cn.zhihuibeijing.MainActivity;
import com.it.cn.zhihuibeijing.base.BaseMenuDetailPager;
import com.it.cn.zhihuibeijing.base.BasePager;
import com.it.cn.zhihuibeijing.base.impl.menu.InteractMenuDetailPager;
import com.it.cn.zhihuibeijing.base.impl.menu.NewsMenuDetailPager;
import com.it.cn.zhihuibeijing.base.impl.menu.PhotosMenuDetailPager;
import com.it.cn.zhihuibeijing.base.impl.menu.TopicMenuDetailPager;
import com.it.cn.zhihuibeijing.domain.NewsMenu;
import com.it.cn.zhihuibeijing.fragement.LeftMenuFragment;
import com.it.cn.zhihuibeijing.global.GlobalConstants;
import com.it.cn.zhihuibeijing.utils.CacheUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * 新闻
 * Created by Administrator on 2017/2/2.
 */

public class NewsCenterPager extends BasePager {
    private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;//菜单详情页集合
    private NewsMenu mData;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("新闻初始化了");
//        //要给帧布局填充布局对象
//        TextView view=new TextView(mActivity);
//        view.setText("新闻");
//        view.setTextColor(Color.RED);
//        view.setTextSize(22);
//        view.setGravity(Gravity.CENTER);
//        flContent.addView(view);
        tvTitle.setText("新闻中心");
        //显示菜单按钮
        btnMenu.setVisibility(View.VISIBLE);
        //先判断有没有缓存，如果有的话，就先加载缓存
        String cache = CacheUtils.getCache(GlobalConstants.CATEGORY_URL, mActivity);
        if(!TextUtils.isEmpty(cache)){
            System.out.println("发现缓存了...");
            processData(cache);

        }
            //请求服务器
            getDataFromServer();




    }


    /**
     * 从服务器获取数据
     */

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.CATEGORY_URL, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;//获取结果
                System.out.println("服务器结果"+result);
                //Gson
                processData(result);
                //开始写缓存
                CacheUtils.setCache(GlobalConstants.CATEGORY_URL,result,mActivity);


            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();


            }
        });

    }

    /**
     * 解析数据
     */

    private void processData(String json) {
        Gson gson= new Gson();
        mData = gson.fromJson(json, NewsMenu.class);
        System.out.println("解析结果"+ mData);
        MainActivity mainUI=(MainActivity) mActivity;
        LeftMenuFragment fragment = mainUI.getLeftMenuFragment();
        fragment.setMenuData(mData.data);
        //初始化四个菜单也
        mMenuDetailPagers=new ArrayList<>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity,mData.data.get(0).children));
        mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity,btnPhoto));
        mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));
        //将新闻菜单详情页设置为默认页面
       // setCurrentDetailPager(fragment.currentPosition);
        setCurrentDetailPager(0);

    }
    //设置菜单详情页
    public void setCurrentDetailPager(int position){
        //重新给frameLayout添加内容
        BaseMenuDetailPager pager = mMenuDetailPagers.get(position);//获取当前应该显示的页面
      View view=pager.mRootView;//当前页面的布局
        //清除之前那的布局
        flContent.removeAllViews();
        flContent.addView(view);//给帧布局添加布局
        //初始化页面数据
        pager.initData();
        //更新新闻标题页
        tvTitle.setText(mData.data.get(position).title);
        //如果是组图页面，需要显示 切换按钮
       if(pager instanceof PhotosMenuDetailPager){
           //显示切换按钮
           btnPhoto.setVisibility(View.VISIBLE);

       }else {
           //隐藏切换按钮
           btnPhoto.setVisibility(View.GONE);
       }



    }
}
