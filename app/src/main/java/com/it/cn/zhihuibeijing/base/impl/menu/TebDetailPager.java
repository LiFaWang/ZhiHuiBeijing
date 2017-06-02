package com.it.cn.zhihuibeijing.base.impl.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.it.cn.zhihuibeijing.NewsDetailActivity;
import com.it.cn.zhihuibeijing.R;
import com.it.cn.zhihuibeijing.base.BaseMenuDetailPager;
import com.it.cn.zhihuibeijing.domain.NewsMenu;
import com.it.cn.zhihuibeijing.domain.NewsTabBean;
import com.it.cn.zhihuibeijing.global.GlobalConstants;
import com.it.cn.zhihuibeijing.utils.CacheUtils;
import com.it.cn.zhihuibeijing.utils.PrefUtils;
import com.it.cn.zhihuibeijing.view.PullTORefreshListView;
import com.it.cn.zhihuibeijing.view.TopNewsViewPager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * 页标签页面对象
 * Created by Administrator on 2017/2/8.
 */

public class TebDetailPager extends BaseMenuDetailPager {
    private NewsMenu.NewsTabData mTabData;//单个页签的网络数据
    @ViewInject(R.id.top_news)
    private TopNewsViewPager mViewPager;
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;
    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator;
    @ViewInject(R.id.lv_list)
    private PullTORefreshListView lvList;
    private String mUrl;
    private ArrayList<NewsTabBean.TopNews> mTopNews;
    private ArrayList<NewsTabBean.NewsData> mNewsList;
    private NewsAdapter mNewsAdapter;
    private String mMoreUrl;//下一页数据连接
    private Handler mHandler;
    //    private TextView mView;

//    public TebDetailPager(Activity activity) {
//        super(activity);
//    }

    public TebDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData=newsTabData;
        mUrl= GlobalConstants.SERVER_URL+mTabData.url;
    }

    @Override
    public View initView() {
        //要给帧布局填充布局对象
//        mView = new TextView(mActivity);
////        mView.setText(mTabData.title);此处设置会空指针异常
//        mView.setTextColor(Color.RED);
//        mView.setTextSize(22);
//        mView.setGravity(Gravity.CENTER);
        View view =View.inflate(mActivity, R.layout.pager_tab_detail,null);
        ViewUtils.inject(this,view);
        //给listView添加头布局
        View mHeaderView=View.inflate(mActivity,R.layout.list_item_header,null);
        ViewUtils.inject(this,mHeaderView);
        lvList.addHeaderView(mHeaderView);
        lvList.setOnRefreshListener(new PullTORefreshListView.OnRefreshListener() {
            //前端界面设置回调
            @Override
            public void onRefresh() {
              //刷新数据
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                //加载下一页数据
                if(mMoreUrl!=null){
                    //加载更所数据
                    getMoreDataFromServer();

            }else {
                    Toast.makeText(mActivity, "没有更多数据", Toast.LENGTH_SHORT).show();
                    lvList.onRefreshComplete(true);
                }
            }
        });
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int headerViewsCount = lvList.getHeaderViewsCount();
                position=position-headerViewsCount;
                System.out.println("第"+position+"个被点击了");
                NewsTabBean.NewsData news = mNewsList.get(position);
                //readIds:1101,1102,1103
                String readIds = PrefUtils.getString(mActivity, "read_ids", "");
                if(!readIds.contains(news.id+"")){//只有不包含当前才添加ID
                    readIds=readIds+news.id+",";
                    PrefUtils.setString(mActivity,"read_ids",readIds);
                }
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvTitle.setTextColor(Color.GRAY);
                //跳到新闻详情页
                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url",news.url);
                mActivity.startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void initData() {
        super.initData();
//        view.setText(mTabData.title);
        String cache = CacheUtils.getCache(mUrl, mActivity);
        if(!TextUtils.isEmpty(cache)){
//        if(cache.length()>0){
            processData(cache,false);
        }
            getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils=new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET,mUrl,new RequestCallBack<String>(){

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result,false);
                CacheUtils.setCache(mUrl,result,mActivity);
                //收起下拉刷新控件
                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                //收起下拉刷新控件
                lvList.onRefreshComplete(false);
            }
        });
    }

    /**
     *  加载下一页数据
     */
    private void getMoreDataFromServer() {
        HttpUtils utils=new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET,mMoreUrl,new RequestCallBack<String>(){

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result,true);
                //收起下拉刷新控件
                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                //收起下拉刷新控件
                lvList.onRefreshComplete(false);
            }
        });
    }


    private void processData(String result,boolean isMore) {
        Gson gson=new Gson();
        NewsTabBean newsTabBean = gson.fromJson(result, NewsTabBean.class);
        String moreUrl = newsTabBean.data.more;
        if(!TextUtils.isEmpty(moreUrl)){
            mMoreUrl = GlobalConstants.SERVER_URL+ moreUrl;
        }else {
            mMoreUrl=null;
        }
        if(!isMore){
            //头条新闻填充数据
            mTopNews = newsTabBean.data.topnews;
            if(mTopNews!=null){
//           System.out.println("laileee//9nnkk");

                mViewPager.setAdapter(new TopNewsAdapter());
                mIndicator.setViewPager(mViewPager);
                mIndicator.setSnap(true);//快照方式展示
                //事件设置给Indicator
                mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        //更新头条新闻的标题
                        NewsTabBean.TopNews topNews = mTopNews.get(position);
                        tvTitle.setText(topNews.title);

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                //更新第一个头条新闻标题
                tvTitle.setText(mTopNews.get(0).title);
                mIndicator.onPageSelected(0);//默认让第一个选中（页面销毁后，框架默认选择出bug'问题）
            }
            //列表新闻
            mNewsList = newsTabBean.data.news;
            if(mNewsList!=null){
                mNewsAdapter = new NewsAdapter();
                lvList.setAdapter(mNewsAdapter);
            }
            if(mHandler==null){
                mHandler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        int currentItem = mViewPager.getCurrentItem();
                        currentItem++;
                        if(currentItem>mTopNews.size()-1){
                            currentItem=0;//如果已经到了最后一页，跳转到第一页
                        }
                        mViewPager.setCurrentItem(currentItem);

                        mHandler.sendEmptyMessageDelayed(0,3000);
                    }
                };
                mHandler.sendEmptyMessageDelayed(0,3000);//发送延时3秒的消息
                mViewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                mHandler.removeCallbacksAndMessages(null);//移除所有消息，确保手指触碰时停止自动轮询

                                break;
                            case MotionEvent.ACTION_CANCEL://当用户按下时，没有抬起就滑动ListView，会调用此方法
                                mHandler.sendEmptyMessageDelayed(0,3000);//发送延时3秒的消息
                            case MotionEvent.ACTION_UP:
                                mHandler.sendEmptyMessageDelayed(0,3000);//发送延时3秒的消息
                            default:
                                break;
                        }
                        return false;
                    }
                });
            }
        } else {
            //加载更多数据
            ArrayList<NewsTabBean.NewsData> moreNews = newsTabBean.data.news;
            mNewsList.addAll(moreNews);//将数据追加在集合中
            //刷新listView
            mNewsAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 头条新闻的数据适配器
     */
    class TopNewsAdapter extends PagerAdapter{

        private  BitmapUtils mBitmapUtils;

        public TopNewsAdapter(){
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils
                    .configDefaultLoadingImage(R.mipmap.topnews_item_default);// 设置加载中的默认图片

        }

        @Override
        public int getCount() {
            return mTopNews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view =new ImageView(mActivity);
//            view.setImageResource(R.mipmap.topnews_item_default);
            view.setScaleType(ImageView.ScaleType.FIT_XY);//设置缩放方式，宽高填充父控件
            String imageUrl = mTopNews.get(position).topimage;//图片的下载链接
            //下载图片，将图片设置给imageView避免内存溢出-缓存
            //BitmapUtils-Xutils
            mBitmapUtils.display(view,imageUrl);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    class NewsAdapter extends BaseAdapter{
        private  BitmapUtils mBitmapUtils;

        public NewsAdapter(){
           mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.mipmap.news_pic_default);

       }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public NewsTabBean.NewsData getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                convertView=View.inflate(mActivity,R.layout.list_item_news,null);

                holder=new ViewHolder();
                holder.ivIcon= (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvTitle= (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvData= (TextView) convertView.findViewById(R.id.tv_data);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();

            }
            NewsTabBean.NewsData news =getItem(position);
            holder.tvTitle.setText(news.title);
            holder.tvData.setText(news.pubdate);
            //根据记录来标记已读和未读
            String readIds = PrefUtils.getString(mActivity, "read_ids", "");
            if(readIds.contains(news.id+"")){
                holder.tvTitle.setTextColor(Color.GRAY);
            }else {
                holder.tvTitle.setTextColor(Color.BLACK);

            }
            mBitmapUtils.display(holder.ivIcon,news.listimage);
            return convertView;
        }
    }
    static class ViewHolder{
        public ImageView ivIcon;
        public TextView tvTitle;
        public TextView tvData;
    }
}
