package com.it.cn.zhihuibeijing.utils;

import android.app.Application;

import com.lzy.okgo.OkGo;

/**
 * Created by Administrator on 2017/2/26.
 */

public class OKApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //必须调用初始化
        OkGo.init(this);
    }
}
