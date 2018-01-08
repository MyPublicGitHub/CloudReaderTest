package com.example.cloudreadertest.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 冯涛 on 2018/1/3.
 */

public class HotMovieBean implements Serializable{

    /**
     * https://api.douban.com/v2/movie/in_theaters
     */

    public int count;
    public int start;
    public int total;
    public String title;
    public List<SubjectsBean> subjects;

    public static class SubjectsBean implements Serializable{

        public RatingBean rating;
        public String title;
        public int collect_count;
        public String original_title;
        public String subtype;
        public String year;
        public ImagesBean images;
        public String alt;
        public String id;
        public List<String> genres;
        public List<CastsBean> casts;
        public List<CastsBean> directors;

        public static class RatingBean {
            /**
             * max : 10
             * average : 6.2
             * stars : 30
             * min : 0
             */

            public int max;
            public double average;
            public String stars;
            public int min;
        }

        public static class ImagesBean {
            /**
             * small : https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2508926717.jpg
             * large : https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2508926717.jpg
             * medium : https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2508926717.jpg
             */

            public String small;
            public String large;
            public String medium;
        }

        public static class CastsBean {

            public String alt;
            public AvatarsBean avatars;
            public String name;
            public String id;

            public static class AvatarsBean {
                /**
                 * small : https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p14025.jpg
                 * large : https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p14025.jpg
                 * medium : https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p14025.jpg
                 */

                public String small;
                public String large;
                public String medium;
            }
        }
    }
}
