package com.it.cn.zhihuibeijing.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.it.cn.zhihuibeijing.MainActivity;
import com.it.cn.zhihuibeijing.R;

import java.io.IOException;

/**
 * 五个标签页的基类
 * Created by Administrator on 2017/2/2.
 */

public class BasePager {
    public Activity mActivity;
    public TextView tvTitle;
    public ImageButton btnMenu;
    public FrameLayout flContent;
    public View mRootView;//当前页面的布局文件对象
    public  ImageButton btnPhoto;//组图切换按钮
    public BasePager(Activity activity){
        mActivity=activity;
        mRootView=initView();

    }
    //初始化布局
    public View initView(){
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        tvTitle= (TextView) view.findViewById(R.id.tv_title);
        btnMenu= (ImageButton) view.findViewById(R.id.btn_menu);
        flContent= (FrameLayout) view.findViewById(R.id.fl_content);
        btnPhoto= (ImageButton) view.findViewById(R.id.btn_photo);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mActivity).getSlidingMenu().toggle();
            }
        });

        return view;
    }
    //初始化数据
    public void initData() throws IOException {

    }
}
