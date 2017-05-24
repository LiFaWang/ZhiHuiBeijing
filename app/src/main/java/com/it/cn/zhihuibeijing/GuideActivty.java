package com.it.cn.zhihuibeijing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.it.cn.zhihuibeijing.utils.DensityUtils;
import com.it.cn.zhihuibeijing.utils.PrefUtils;

import java.util.ArrayList;

/**
 * 新手引导页面
 * Created by Administrator on 2017/2/1.
 */

public class GuideActivty extends Activity {
    private ViewPager mViewPager;
    private Button btnStart;
    private ImageView ivRedPoint;//小红点
    //小红点移动距离
    private int mPointDis;
    private  LinearLayout llContainer;
    private ArrayList<ImageView> mImageViewList;//imageView集合
    //引导页图片 id数组
    private int[] mImageIds=new int[]{R.mipmap.guide_1,R.mipmap.guide_2,R.mipmap.guide_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题，必须在setContextView之前调用
        setContentView(R.layout.activity_guide);
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);

        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        ivRedPoint= (ImageView) findViewById(R.id.iv_redPoint);
        btnStart= (Button) findViewById(R.id.btn_start);
        initData();
        mViewPager.setAdapter(new GuideAdapter());//设置数据
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //当页面滑动过程中回调
                //更新小红点的距离
                int leftMargin = (int) (mPointDis * positionOffset)+position*mPointDis;//计算小红点当前的左边距
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
                params.leftMargin=leftMargin;//修改左边距
                ivRedPoint.setLayoutParams(params);//重新设置布局 参数

            }

            @Override
            public void onPageSelected(int position) {
                //某个页面被选中时回调
                if(position==mImageViewList.size()-1){
                    btnStart.setVisibility(View.VISIBLE);//最后一个页面开始显示体验按钮
                }else {
                    btnStart.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //当页面状态变化回调



            }
        });
        //计算两个原点的距离
        //移动距离=第二个圆点left-第一个圆点的left
        //measure-layout(确定位置)-draw(activity的onCreate方法走完后开始测量)

       // mPointDis = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
        //监听layout方法结束的事件，位置确定好之后再获取圆点间距
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivRedPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //layout方法执行结束的回调
                 mPointDis = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更新sp,已经不是第一次进入了
                PrefUtils.setBoolean(getApplicationContext(),"is_first_enter", false);
                //跳到主页面
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

    }

    //初始化数据
    private void initData() {
        mImageViewList=new ArrayList<>();
        for (int i=0;i<mImageIds.length;i++){
            ImageView view=new ImageView(this);
            view.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(view);
            //初始化小圆点
            ImageView point=new ImageView(this);
            point.setImageResource(R.drawable.shape_point_gray);//设置shape形状
            //初始化布局参数，宽高包裹内容父控件是谁就声明谁的布局参数
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if(i>0){
                //从第二个点起设置左边距
                params.leftMargin= DensityUtils.dip2px(10,this);
            }

            llContainer.addView(point);
            point.setLayoutParams(params);//设置布局参数



        }

    }

    class GuideAdapter extends PagerAdapter {
        //item个数
        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //初始化item布局
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view  = mImageViewList.get(position);
            container.addView(view);
            return view;
        }
        //销毁item

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //   super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
