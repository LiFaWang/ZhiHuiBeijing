package com.it.cn.zhihuibeijing.base.impl.menu;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.it.cn.zhihuibeijing.R;
import com.it.cn.zhihuibeijing.base.BaseMenuDetailPager;
import com.it.cn.zhihuibeijing.domain.PhotosBean;
import com.it.cn.zhihuibeijing.global.GlobalConstants;
import com.it.cn.zhihuibeijing.utils.CacheUtils;
import com.it.cn.zhihuibeijing.utils.MyBitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 新闻详情页-组图
 * Created by Administrator on 2017/2/5.
 */

public class PhotosMenuDetailPager extends BaseMenuDetailPager implements View.OnClickListener{
    @ViewInject(R.id.lv_photos)
    private ListView lvPhoto;
    @ViewInject(R.id.gv_photos)
    private GridView gvPhoto;
    private ArrayList<PhotosBean.PhotoNews> mNewsList;
    private  ImageButton btnPhoto;

    public PhotosMenuDetailPager(Activity activity, ImageButton btnPhoto) {
        super(activity);
        this.btnPhoto=btnPhoto;
        btnPhoto.setOnClickListener(this);//组图设置点击事件
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.photo_pagers_meau_detail, null);
        ViewUtils.inject(this,view);

        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(GlobalConstants.PHOTOS_URL, mActivity);
//        if(cache!=null){
//            processData(cache);
//        }
        if(!TextUtils.isEmpty(cache)){
            processData(cache);
        }


        getDataFromServer();

    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.PHOTOS_URL, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result);
                CacheUtils.setCache(GlobalConstants.PHOTOS_URL,result,mActivity);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                //请求失败
                e.printStackTrace();
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void processData(String result) {
        Gson gson=new Gson();
        PhotosBean photosBean = gson.fromJson(result, PhotosBean.class);
        mNewsList = photosBean.data.news;
        lvPhoto.setAdapter(new PhotoAdapter());
        gvPhoto.setAdapter(new PhotoAdapter());//gridview的布局结构和listview完全一样
        //所以可以共用一个adapter
    }
    private boolean isListView=true;

    @Override
    public void onClick(View v) {
       if(isListView){
           //切换成gridview
           this.lvPhoto.setVisibility(View.GONE);
           this.gvPhoto.setVisibility(View.VISIBLE);
           btnPhoto.setImageResource(R.mipmap.icon_pic_list_type);
           isListView=false;
       }else {
           //切换成listview
           this.gvPhoto.setVisibility(View.GONE);
           this.lvPhoto.setVisibility(View.VISIBLE);
           btnPhoto.setImageResource(R.mipmap.icon_pic_grid_type);

           isListView=true;
       }
    }

  private   class PhotoAdapter extends BaseAdapter{

//        private final BitmapUtils mBitmapUtils;
        private final MyBitmapUtils mBitmapUtils;

        public PhotoAdapter(){
//            mBitmapUtils = new BitmapUtils(mActivity);
//            mBitmapUtils.configDefaultLoadingImage(R.mipmap.pic_item_list_default);
            mBitmapUtils = new MyBitmapUtils();


        }


        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public PhotosBean.PhotoNews getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
             ViewHolder mHolder;
            if(convertView==null){
                convertView=View.inflate(mActivity,R.layout.list_item_photos,null);
                mHolder = new ViewHolder();
                mHolder.ivPic= (ImageView) convertView.findViewById(R.id.iv_pic);
                mHolder.tvTitle= (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(mHolder);
            }else {
               mHolder= (ViewHolder) convertView.getTag();
            }
            PhotosBean.PhotoNews item = getItem(position);
          mHolder.tvTitle.setText(item.title);
            mBitmapUtils.display(mHolder.ivPic,item.listimage );
            return convertView;
        }
    }
    class ViewHolder{
        public ImageView ivPic;
        public TextView tvTitle;

    }
}
