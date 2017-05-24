package com.it.cn.zhihuibeijing.base.impl;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.it.cn.zhihuibeijing.R;
import com.it.cn.zhihuibeijing.adapter.SmartServicePagerAdapter;
import com.it.cn.zhihuibeijing.base.BasePager;
import com.it.cn.zhihuibeijing.domain.SmartServicePagerBean;
import com.it.cn.zhihuibeijing.global.GlobalConstants;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 智慧
 * Created by Administrator on 2017/2/2.
 */

public class SmartServicePager extends BasePager {
    private MaterialRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private String mNetUrl;
    private int pageSize = 10;
    private int totalCount ;
    private int curpag;
    private SmartServicePagerAdapter mServicePagerAdapter;
    /**
     * 默认状态
     */
    private static final int STATE_NORMAL=1;
    /**
     * 下拉刷新
     */
    private static final int STATE_REFRESH=2;
    /**
     * 加载更多
     */
    private static final int STATE_MORE=3;
    private int state =STATE_NORMAL;

    /**
     * 商品列表数据
     */
    private List<SmartServicePagerBean.ListBean> datas;


    public SmartServicePager(Activity activity) {
        super(activity);


    }

    @Override
    public void initData() throws IOException {
        System.out.println("智慧初始化了");
        tvTitle.setText("商城热卖");
        //要给帧布局填充布局对象
        View view = View.inflate(mActivity, R.layout.smart_service_pager, null);
        mRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        mRefreshLayout.setLoadMore(true);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        mProgressBar.setVisibility(View.VISIBLE);
        if (flContent!=null){
            flContent.removeAllViews();
        }
        flContent.addView(view);
        //显示菜单
        btnMenu.setVisibility(View.VISIBLE);
        setRequestParams();
        getDataFromNet();
        initRefresh();



    }

    private void initRefresh() {
        //设置下拉和上拉刷新
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            /**
             * 下拉刷新
             * @param materialRefreshLayout
             */
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                state=STATE_REFRESH;
                curpag=1;
                mNetUrl = GlobalConstants.WARES_URL + pageSize + "&curPage=" + curpag;

                getDataFromNet();

                materialRefreshLayout.finishRefresh();

            }

            /**
             * 加载更多
             * @param materialRefreshLayout
             */

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);

                    state=STATE_MORE;
                    curpag+=1;
                    mNetUrl = GlobalConstants.WARES_URL + pageSize + "&curPage=" + curpag;

                    getDataFromNet();



            }
        });

    }

    private void getDataFromNet() {
        OkGo.get(mNetUrl)
                .tag(this)
                .cacheKey("picture")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        System.out.println("结果"+s);
                        processData(s);
                    }
                });

    }

    private void processData(String s) {
        Gson gson=new Gson();
        SmartServicePagerBean bean = gson.fromJson(s, SmartServicePagerBean.class);
        curpag= bean.getCurrentPage();
//        totalPage= bean.getTotalPage();
        datas = bean.getList();
//        System.out.println("curpag="+curpag+",total="+totalPage+",datas="+datas.get(1).getName());
        mProgressBar.setVisibility(View.GONE);
        showData();


    }

    private void showData() {
        switch (state) {
            case STATE_NORMAL://默认
                //设置适配器
                mServicePagerAdapter=new SmartServicePagerAdapter(mActivity,datas);
                mRecyclerView.setAdapter(mServicePagerAdapter);
                //设置适配器布局
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false));
                break;
            case STATE_REFRESH://下拉
                //1 清空适配器
                mServicePagerAdapter.clearData();
                //2 添加数据
                mServicePagerAdapter.addData(0,datas);
//                //3 状态还原
                mRefreshLayout.finishRefresh();

                break;
            case STATE_MORE://加载更多
                //1 添加数据
                mServicePagerAdapter.addData(mServicePagerAdapter.getDataCount(),datas);
                //2 状态还原
                mRefreshLayout.finishRefreshLoadMore();
                Toast.makeText(mActivity, "已经到底了...", Toast.LENGTH_SHORT).show();

                break;

            default:
                break;
        }

    }

    private void setRequestParams() {
        state=STATE_NORMAL;
        curpag=1;

        mNetUrl = GlobalConstants.WARES_URL + pageSize + "&curPage=" + curpag;
    }
}
