package com.it.cn.zhihuibeijing.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.it.cn.zhihuibeijing.base.BasePager;

/**
 * 政务
 * Created by Administrator on 2017/2/2.
 */

public class GovAffairsPager extends BasePager {
    public GovAffairsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("政务初始化了");
        //要给帧布局填充布局对象
        TextView view=new TextView(mActivity);
        view.setText("政务");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        flContent.addView(view);
        tvTitle.setText("政务信息");
        //显示菜单
        btnMenu.setVisibility(View.VISIBLE);


    }
}
