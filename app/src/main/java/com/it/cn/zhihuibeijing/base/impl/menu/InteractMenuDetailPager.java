package com.it.cn.zhihuibeijing.base.impl.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.it.cn.zhihuibeijing.base.BaseMenuDetailPager;

/**
 * 新闻详情页-互动
 * Created by Administrator on 2017/2/5.
 */

public class InteractMenuDetailPager extends BaseMenuDetailPager {
    public InteractMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {

        TextView view=new TextView(mActivity);
        view.setText("新闻详情页-互动");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        return view;
    }
}
