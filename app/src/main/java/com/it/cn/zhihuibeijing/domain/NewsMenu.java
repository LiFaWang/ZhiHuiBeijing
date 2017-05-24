package com.it.cn.zhihuibeijing.domain;

import java.util.ArrayList;

/**
 * 分类信息
 * Created by Administrator on 2017/2/4.
 */

public class NewsMenu {
    @Override
    public String toString() {
        return "NewsMenu{" +
                "retcode=" + retcode +
                ", extend=" + extend +
                ", data=" + data +
                '}';
    }

    public int retcode;
    public ArrayList<Integer> extend;
    public ArrayList <NewsMenuData>data;
    //侧边栏的对象
    public class NewsMenuData{
        public int id ;
        public String   title;
        public int type;
        public ArrayList<NewsTabData> children;

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", children=" + children +
                    '}';
        }
    }
    //页标签的对象
    public class NewsTabData{
        public int id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }
}
