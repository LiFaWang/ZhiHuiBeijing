package com.it.cn.recyclerviewdemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ArrayList<String> datas;
    private RecyclerViewAdapter adapter;
    private Button btn_add;
    private Button btn_remove;
    private Button btn_list;
    private Button btn_grid;
    private SwipeRefreshLayout swipe_refresh_Layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        btn_add= (Button) findViewById(R.id.btn_add);
        btn_remove= (Button) findViewById(R.id.btn_remove);
        btn_list= (Button) findViewById(R.id.btn_list);
        btn_grid= (Button) findViewById(R.id.btn_grid);
        swipe_refresh_Layout= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_Layout);
        //设置点击事件
        btn_add.setOnClickListener(this);
        btn_remove.setOnClickListener(this);
        btn_list.setOnClickListener(this);
        btn_grid.setOnClickListener(this);
        //添加数据
        datas=new ArrayList<>();
        for (int i = 0; i <50 ; i++) {
            datas.add("data_"+i);
        }
        //设置适配器
        adapter=new RecyclerViewAdapter(this,datas);
        recyclerView.setAdapter(adapter);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //        recyclerView.scrollToPosition(49);
//        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        //设置分割线
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this,MyDividerItemDecoration.VERTICAL_LIST){
        });
        //设置点击某一条
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String data) {
                Toast.makeText(getApplicationContext(), "data="+data, Toast.LENGTH_SHORT).show();
            }
        });
        //设置动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        initRefresh();
    }
    private void initRefresh(){
        //设置刷新控件圆圈的颜色
//        swipe_refresh_Layout.setColorSchemeColors();
        swipe_refresh_Layout.setColorSchemeResources(android.R.color.holo_blue_light,
               android.R.color.holo_orange_light,android.R.color.holo_green_light);
        //设置刷新控件背景的颜色
//        swipe_refresh_Layout.setBackgroundColor(Color.);
        swipe_refresh_Layout.setProgressBackgroundColorSchemeColor(Color.GREEN);
//设置滑动距离
        swipe_refresh_Layout.setDistanceToTriggerSync(100);
        //设置大小模式
        swipe_refresh_Layout.setSize(SwipeRefreshLayout.LARGE);
        //设置下拉刷新的监听
        swipe_refresh_Layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.removeAll();
                        swipe_refresh_Layout.setRefreshing(false);
                        refreshData();
                        adapter.notifyItemRangeInserted(0,50);
                        recyclerView.scrollToPosition(0);

                    }
                },2000);
            }
        });
    }

    private void refreshData() {
//        datas=new ArrayList<>();
        for (int i = 0; i <50 ; i++) {
            datas.add("new data_"+i);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                adapter.addData(0,"new data");
                recyclerView.scrollToPosition(0);
                break;
            case R.id.btn_remove:
                adapter.remove(0);
                break;
            case R.id.btn_list:
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                break;
            case R.id.btn_grid:
                GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
                 recyclerView.setLayoutManager(gridLayoutManager);
                break;
            default:
                break;
        }
        
    }
}
