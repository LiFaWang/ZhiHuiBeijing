package com.it.cn.zhihuibeijing.base;

import android.app.Activity;
import android.view.View;

/**
 * 菜单详情页的基类
 * Created by Administrator on 2017/2/5.
 */

public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    public  View mRootView;//菜单详情页艮布局

    public BaseMenuDetailPager(Activity activity){
        mActivity=activity;
        mRootView = initView();

    }
    public abstract View initView();
    public void initData(){


    }
}
