package com.example.cloudreadertest.bean;

import java.util.List;

/**
 * Created by 冯涛 on 2018/1/18.
 * E-mail:716774214@qq.com
 */

public class BookDetailBean {

    /**
     * https://api.douban.com/v2/book/25985021
     */
    public RatingBean rating;
    public String subtitle;
    public String pubdate;
    public String origin_title;
    public String image;
    public String binding;
    public String catalog;
    public String ebook_url;
    public String pages;
    public ImagesBean images;
    public String alt;
    public String id;
    public String publisher;
    public String isbn10;
    public String isbn13;
    public String title;
    public String url;
    public String alt_title;
    public String author_intro;
    public String summary;
    public String ebook_price;
    public SeriesBean series;
    public String price;
    public List<String> author;
    public List<TagsBean> tags;
    public List<String> translator;

    public static class RatingBean {
        /**
         * max : 10
         * numRaters : 42980
         * average : 9.1
         * min : 0
         */
        public int max;
        public int numRaters;
        public String average;
        public int min;
    }

    public static class ImagesBean {
        /**
         * small : https://img3.doubanio.com/spic/s27814883.jpg
         * large : https://img3.doubanio.com/lpic/s27814883.jpg
         * medium : https://img3.doubanio.com/mpic/s27814883.jpg
         */
        public String small;
        public String large;
        public String medium;
    }

    public static class SeriesBean {
        /**
         * id : 23574
         * title : “中信史学大师畅销经典”系列
         */
        public String id;
        public String title;
    }

    public static class TagsBean {
        /**
         * count : 15950
         * name : 历史
         * title : 历史
         */
        public int count;
        public String name;
        public String title;
    }
}
