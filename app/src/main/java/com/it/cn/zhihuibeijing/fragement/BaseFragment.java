package com.it.cn.zhihuibeijing.fragement;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/2/2.
 */

public abstract class BaseFragment extends Fragment {
    public  Activity mActivity;
    //fragment创建
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();//获取当前activity所依赖的activity
    }
    //初始化frangment布局


    @Override
    public View onCreateView(LayoutInflater inflater,
                              ViewGroup container,
                             Bundle savedInstanceState) {
        View view = initView();
        return view;
    }
    //所依赖的activity执行结束

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    //初始化布局，必须要子类去实现
    public abstract View initView();
    //初始化数据 ，必须要子类去实现
    public abstract void initData() ;
}
