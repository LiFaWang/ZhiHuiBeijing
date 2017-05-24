package com.it.cn.zhihuibeijing.fragement;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.it.cn.zhihuibeijing.MainActivity;
import com.it.cn.zhihuibeijing.R;
import com.it.cn.zhihuibeijing.base.BasePager;
import com.it.cn.zhihuibeijing.base.impl.GovAffairsPager;
import com.it.cn.zhihuibeijing.base.impl.HomePager;
import com.it.cn.zhihuibeijing.base.impl.NewsCenterPager;
import com.it.cn.zhihuibeijing.base.impl.SettingPager;
import com.it.cn.zhihuibeijing.base.impl.SmartServicePager;
import com.it.cn.zhihuibeijing.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.io.IOException;
import java.util.ArrayList;


/**
 * 主页面的fragment
 * Created by Administrator on 2017/2/2.
 */

public class ContentFragment extends BaseFragment {
    private NoScrollViewPager mViewPager;
    private ArrayList<BasePager> mPagers;//5个标签页的集合
    private RadioGroup rgGroup;
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        mViewPager= (NoScrollViewPager) view.findViewById(R.id.vp_content);
        rgGroup= (RadioGroup)view.findViewById(R.id.rg_group);
        return view ;
    }

    @Override
    public void initData()  {
        mPagers=new ArrayList<>();
        //添加5个页面
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new GovAffairsPager(mActivity));
        mPagers.add(new SettingPager(mActivity));
        mViewPager.setAdapter(new ContentAdapter());
        //给低栏标签切换标签
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rab_home:
                        //  首页
                        mViewPager.setCurrentItem(0,false);
                        break;
                    case R.id.rab_news:
                        //  新闻
                        mViewPager.setCurrentItem(1,false);
                        break;
                    case R.id.rab_smart:
                        //  智慧
                        mViewPager.setCurrentItem(2,false);
                        break;
                    case R.id.rab_gov:
                        //  政府
                        mViewPager.setCurrentItem(3,false);
                        break;
                    case R.id.rab_set:
                        //  设置
                        mViewPager.setCurrentItem(4,false);
                        break;

                }

            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePager pager = mPagers.get(position);

                try {
                    pager.initData();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(position==0||position==mPagers.size()-1){
                    //首页和设置禁用侧边栏
                    setSlidingMenuEnable(false);

                }else {
                    setSlidingMenuEnable(true);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //手动加载第一页
        try {
            mPagers.get(0).initData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setSlidingMenuEnable(false);

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

    class ContentAdapter extends PagerAdapter{

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
            BasePager pager = mPagers.get(position);
            View view = pager.mRootView;//获取当前页面布局
//            pager.initData();
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    public NewsCenterPager getNewsCenterPager(){
        NewsCenterPager pager = (NewsCenterPager) mPagers.get(1);
        return pager;
    }

}
