package com.it.cn.zhihuibeijing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.it.cn.zhihuibeijing.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 下拉刷新的LIstView
 * Created by Administrator on 2017/2/10.
 */

public class PullTORefreshListView extends ListView implements AbsListView.OnScrollListener {
    private static final int STATE_PULL_TO_REFRESH=1;
    private static final int STATE_RELEASE_TO_REFRESH=2;
    private static final int STATE_REFRESHING=3;
    private  int mCurrentState=STATE_PULL_TO_REFRESH;

    private View mHeaderView;
    private int mHeaderViewHeight;
    private int mStartY=-1;
    private TextView mTvTitle;
    private TextView mTvTime;
    private ImageView mIvArrow;
    private RotateAnimation mAnimUp;
    private RotateAnimation mAnimDown;
    private ProgressBar mPbProgress;
    private View mFooterView;
    private int mFooterViewHeight;

    public PullTORefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public PullTORefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();

    }

    public PullTORefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();

    }

    /**
     * 初始化头布局
     */
    private void initHeaderView(){
        mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh_header,null);
        addHeaderView(mHeaderView);
        mTvTitle = (TextView)mHeaderView.findViewById(R.id.tv_title);
        mTvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        mIvArrow = (ImageView)mHeaderView.findViewById(R.id.iv_arrow);
        mPbProgress = (ProgressBar)mHeaderView.findViewById(R.id.pb_loading);
        //隐藏头布局
        mHeaderView.measure(0,0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);
        initAnim();
        setCurrentTime();
    }

    /**
     * 初始化脚布局
     */
    private void initFooterView(){
        mFooterView = View.inflate(getContext(), R.layout.pull_to_refresh_footer,null);
        addFooterView(mFooterView);
        mFooterView.measure(0,0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0,-mFooterViewHeight,0,0);
        this.setOnScrollListener(this);//滑动监听


    }
    //设置刷新时间
    private void setCurrentTime(){

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());
        mTvTime.setText(time);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartY =   (int) ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                if(mStartY==-1){
                    mStartY =   (int) ev.getY();//当用户按到新闻头条的Viewpager进行下拉时，ACTION_DOWN会被VIewPager消费掉吗，此处重新获取
                }
                if(mCurrentState==STATE_REFRESHING){
                    //如果正在刷新就跳出循环
//                    System.out.println("23");
                  break;
                }
                int endY= (int) ev.getY();
                int dy =endY-mStartY;
                int firstVisiblePosition = getFirstVisiblePosition();
                //必须下拉，并且当前显示的是第一个item
                if (dy>0&&firstVisiblePosition==0){
                    int padding=dy-mHeaderViewHeight;
                    mHeaderView.setPadding(0,padding,0,0);
                    if(padding>0 && mCurrentState!=STATE_RELEASE_TO_REFRESH){
                        //改为松开刷新
                        mCurrentState=STATE_RELEASE_TO_REFRESH;
                        refreshState();

                    }else if(padding<0 && mCurrentState!=STATE_PULL_TO_REFRESH) {
                        //改为下拉刷新
                        mCurrentState=STATE_PULL_TO_REFRESH;
                        refreshState();


                    }
                    return  true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mStartY=-1;
                if(mCurrentState==STATE_RELEASE_TO_REFRESH){
                    mCurrentState=STATE_REFRESHING;
                    refreshState();
                    //完整显示头布局
                    mHeaderView.setPadding(0,0,0,0);
                    //4.通知接收
                    if(mListener!=null){
                        mListener.onRefresh();
                    }

                }else if (mCurrentState==STATE_PULL_TO_REFRESH){
                    //隐藏头布局
                    mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);
                    refreshState();
                }

                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 初始化箭头动画
     */
    private void initAnim(){
        mAnimUp = new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        mAnimUp.setDuration(200);
        mAnimUp.setFillAfter(true);
        mAnimDown = new RotateAnimation(-180,0, Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        mAnimDown.setDuration(200);
        mAnimDown.setFillAfter(true);

    }

    /**
     * 根据当前刷新界面
     */

    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_TO_REFRESH:
                mTvTitle.setText("下拉刷新...");
                mIvArrow.startAnimation(mAnimDown);
                mPbProgress.setVisibility(INVISIBLE);
                mIvArrow.setVisibility(VISIBLE);

                break;
            case  STATE_RELEASE_TO_REFRESH:
                mTvTitle.setText("松开刷新...");
                mIvArrow.startAnimation(mAnimUp);
                mPbProgress.setVisibility(INVISIBLE);
                mIvArrow.setVisibility(VISIBLE);

                break;
            case STATE_REFRESHING:
                mIvArrow.clearAnimation();//清楚箭头动画，否则不能隐藏掉
                mTvTitle.setText("正在刷新...");
                mPbProgress.setVisibility(VISIBLE);
                mIvArrow.setVisibility(INVISIBLE);

                break;

            default:
                break;
        }
    }

    /**
     * 刷新结束收起控件
     */
    public void onRefreshComplete(boolean success){
        if(!isLoadMore){
            mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);
//        refreshState();
            mCurrentState=STATE_PULL_TO_REFRESH;
            mPbProgress.setVisibility(INVISIBLE);
            mIvArrow.setVisibility(VISIBLE);
            mTvTitle.setText("下拉刷新...");
            if (success) {
                setCurrentTime();
            }
        } else {
            mFooterView.setPadding(0,-mFooterViewHeight,0,0);
            isLoadMore=false;
        }

    }
    /**
     * 3.定义成员对象，接收监听对象
     */
    private OnRefreshListener mListener;

    /**
     * 2.暴露接口，设置监听
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mListener=onRefreshListener;
    }
    private boolean isLoadMore;
    //滑动状态发生变化

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==SCROLL_STATE_IDLE){//空闲状态
            int lastVisiblePosition = getLastVisiblePosition();
            if(lastVisiblePosition==getCount()-1&&!isLoadMore){
                //到底了
                isLoadMore=true;
                System.out.println("加载更多...");
           mFooterView.setPadding(0,0,0,0);//显示加载更多的布局
                setSelection(getCount()-1);//将ListView  的显示在最后一个item，从而加载更多会主动显示出来

            }
            //通知主界面加载下一页
            if(mListener!=null){
                mListener.onLoadMore();
            }



        }
    }
    //滑动过程

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * 1.下拉刷新的回调接口
     */
    public interface OnRefreshListener{
         void onRefresh();

        void onLoadMore();
    }
}
