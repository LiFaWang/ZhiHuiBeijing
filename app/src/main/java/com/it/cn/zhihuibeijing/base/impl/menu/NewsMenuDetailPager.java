package com.it.cn.zhihuibeijing.base.impl.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.it.cn.zhihuibeijing.MainActivity;
import com.it.cn.zhihuibeijing.R;
import com.it.cn.zhihuibeijing.base.BaseMenuDetailPager;
import com.it.cn.zhihuibeijing.domain.NewsMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * 新闻详情页-新闻
 * Created by Administrator on 2017/2/5.
 */

public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener{
    @ViewInject(R.id.vp_news_menu_detail)
    private ViewPager mViewPager;
    @ViewInject(R.id.indicator)
    private TabPageIndicator mIndicator;
    private ArrayList<NewsMenu.NewsTabData> mTabData;//页签网络数据
    private ArrayList<TebDetailPager> mPagers;//页签页面集合
    public NewsMenuDetailPager(Activity activity, ArrayList<NewsMenu.NewsTabData> children) {
        super(activity);
        mTabData=children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
        ViewUtils.inject(this,view);

        return view;
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<>();
        //初始化页签
        for (int i = 0; i <mTabData.size() ; i++) {
            TebDetailPager pager = new TebDetailPager(mActivity,mTabData.get(i));
            mPagers.add(pager);
        }
        mViewPager.setAdapter(new NewsMenuDetailAdapter());
        mIndicator.setViewPager(mViewPager);//将viewpager和指示器绑定在一起，注意必须在viewpager设置数据后
        //设置滑动监听
//        mViewPager.setOnPageChangeListener(this);
        mIndicator.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        System.out.println("当前位置"+position);
        if(position==0){
            //开启侧边栏
            setSlidingMenuEnable(true);
        }else {
            //禁用侧边栏
            setSlidingMenuEnable(false);

        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {


    }
    /**
     * 开启或者禁用侧边栏
     * @param enable
     */

    private void setSlidingMenuEnable(boolean enable) {
        //获取侧边栏对象
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        if(enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

        }

    }
    @OnClick(R.id.btn_next)
    public void nextPage(View view){
        //跳到下一个页面
        int currentItem = mViewPager.getCurrentItem();
        currentItem ++;
        mViewPager.setCurrentItem(currentItem);

    }


    class NewsMenuDetailAdapter extends PagerAdapter{
        @Override
        public CharSequence getPageTitle(int position) {
            NewsMenu.NewsTabData data = mTabData.get(position);

            return data.title;
        }

        @Override
        public int getCount() {
            return mPagers.size();
            }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TebDetailPager pager = mPagers.get(position);
            View view = pager.mRootView;
            container.addView(view);
            pager.initData();
            return view;

        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}







