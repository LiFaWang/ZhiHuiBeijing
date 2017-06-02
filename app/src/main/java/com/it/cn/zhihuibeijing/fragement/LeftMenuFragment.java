package com.it.cn.zhihuibeijing.fragement;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.it.cn.zhihuibeijing.MainActivity;
import com.it.cn.zhihuibeijing.R;
import com.it.cn.zhihuibeijing.base.impl.NewsCenterPager;
import com.it.cn.zhihuibeijing.domain.NewsMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 侧边栏fragment
 * Created by Administrator on 2017/2/2.
 */

public class LeftMenuFragment extends BaseFragment {
    @ViewInject(R.id.lv_list)
    private ListView lvList;
    public int currentPosition;
    private ArrayList<NewsMenu.NewsMenuData> mNewsMenuData;//侧边栏网络数据对象
    private LeftMenuAdapter mAdapter;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        ViewUtils.inject(this, view);//注入view和事件
        return view;
    }

    @Override
    public void initData() {

    }

    public void setMenuData(ArrayList<NewsMenu.NewsMenuData> data) {
        currentPosition=0;//当前选中页归零
        //更新页面
        mNewsMenuData = data;
        mAdapter = new LeftMenuAdapter();
        lvList.setAdapter(mAdapter);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition=position;
                mAdapter.notifyDataSetChanged();
               // 收起侧边栏
                toggle();
                //侧边栏点击之后，要修改新闻中心的Fragment中的内容
                setCurrentDetailPager(position);


            }
        });


    }

    /**
     * 设置当前的详情页
     * @param position
     */

    private void setCurrentDetailPager(int position) {
        //获取新闻中心对象
        MainActivity mainUI= (MainActivity) mActivity;
        //获取ContentFragment
        ContentFragment fragment = mainUI.getContentFragment();
        //获取新闻中心
        NewsCenterPager newsCenterPager = fragment.getNewsCenterPager();
        //修改新闻中心Fragment
        newsCenterPager.setCurrentDetailPager(position);


    }

    /**
     * 打开或关闭侧边栏
     */

    private void toggle() {
        MainActivity mainUI=(MainActivity)mActivity;
        mainUI.getSlidingMenu().toggle();

    }

  private class LeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNewsMenuData.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int i) {

            return mNewsMenuData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = View.inflate(mActivity, R.layout.list_item_left, null);
            TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);
            NewsMenu.NewsMenuData item = getItem(position);
            tvMenu.setText(item.title);
//            if (position == currentPosition) {
//                tvMenu.setEnabled(true);
//            }else {
//                tvMenu.setEnabled(false);
//            }
            tvMenu.setEnabled(position == currentPosition);
            return view;
        }
    }
}
