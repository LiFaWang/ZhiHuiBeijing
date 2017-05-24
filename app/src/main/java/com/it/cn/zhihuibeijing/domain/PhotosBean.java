package com.it.cn.zhihuibeijing.domain;

import java.util.ArrayList;

/**
 * 组图对象
 * Created by Administrator on 2017/2/18.
 */

public class PhotosBean {
    public PhotosData data;
    public class PhotosData{
        public ArrayList <PhotoNews>news;
    }
    public class PhotoNews{
        public int id;
        public String listimage;
        public String title;

    }

}
