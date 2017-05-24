package com.it.cn.zhihuibeijing;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.it.cn.zhihuibeijing.fragement.ContentFragment;
import com.it.cn.zhihuibeijing.fragement.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 主页面
 * Created by Administrator on 2017/2/1.
 */

public class MainActivity extends SlidingFragmentActivity {
    private static final String TAG_LEFT_MENU="TAG_LEFT_MENU";
    private static final String TAG_CONTENT = "TAG_CONTENT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题，必须在setContextView之前调用
        setContentView(R.layout.activity_main);
        //设置侧边栏
        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏触摸
        int width = getWindowManager().getDefaultDisplay().getWidth();
        slidingMenu.setBehindOffset(width*200/320);//屏幕预留
        initFragment();


    }
    /**
     * 初始化fragment
     */
    private void initFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();//开始事物
        transaction.replace(R.id.fl_left_menu,new LeftMenuFragment(),TAG_LEFT_MENU);//用fragment替换帧布局，参数一：帧布局的id；参数二：要替换多大的帧布局
        transaction.replace(R.id.fl_main,new ContentFragment(),TAG_CONTENT);
        transaction.commit();
    }
    //获取侧边栏fragment对象
    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fm =getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);//根据标记找到对应的fragment
        return fragment;
    }
    //获取主页fragment对象
    public ContentFragment getContentFragment(){
        FragmentManager fm =getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);//根据标记找到对应的fragment
        return fragment;
    }
}
