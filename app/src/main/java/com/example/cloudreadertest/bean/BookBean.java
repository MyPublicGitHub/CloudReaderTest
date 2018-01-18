package com.example.cloudreadertest.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 冯涛 on 2018/1/17.
 * E-mail:716774214@qq.com
 */

public class BookBean {

    /**
     * https://api.douban.com/v2/book/search?tag=%E7%BB%BC%E5%90%88&start=0&count=18
     */

    public int count;
    public int start;
    public int total;
    public List<BooksBean> books;

    public static class BooksBean implements Serializable{

        public RatingBean rating;
        public String subtitle;
        public String pubdate;
        public String origin_title;
        public String image;
        public String binding;
        public String catalog;
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
        public SeriesBean series;
        public String price;
        public List<String> author;
        public List<TagsBean> tags;
        public List<String> translator;

        public static class RatingBean implements Serializable{
            /**
             * max : 10
             * numRaters : 945
             * average : 8.2
             * min : 0
             */
            public int max;
            public int numRaters;
            public String average;
            public int min;
        }

        public static class ImagesBean implements Serializable{
            /**
             * small : https://img1.doubanio.com/spic/s2619459.jpg
             * large : https://img1.doubanio.com/lpic/s2619459.jpg
             * medium : https://img1.doubanio.com/mpic/s2619459.jpg
             */
            public String small;
            public String large;
            public String medium;
        }

        public static class SeriesBean implements Serializable{
            /**
             * id : 782
             * title : 文化生活译丛（新版）
             */
            public String id;
            public String title;
        }

        public static class TagsBean implements Serializable{
            /**
             * count : 368
             * name : 德国
             * title : 德国
             */
            public int count;
            public String name;
            public String title;
        }
    }
}