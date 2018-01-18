package com.example.cloudreadertest.bean;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by 冯涛 on 2018/1/8.
 * E-mail:716774214@qq.com
 */

public class MovieDetailBean extends BaseObservable {

    /**
     * https://api.douban.com/v2/movie/subject/1292052
     * <p>
     * rating : {"max":10,"average":9.6,"stars":"50","min":0}
     * reviews_count : 5750
     * wish_count : 96888
     * douban_site :
     * year : 1994
     * images : {"small":"https://img3.doubanio.com/view/photo/s_ratio_poster/public/p480747492.jpg","large":"https://img3.doubanio.com/view/photo/s_ratio_poster/public/p480747492.jpg","medium":"https://img3.doubanio.com/view/photo/s_ratio_poster/public/p480747492.jpg"}
     * alt : https://movie.douban.com/subject/1292052/
     * id : 1292052
     * mobile_url : https://movie.douban.com/subject/1292052/mobile
     * title : 肖申克的救赎
     * do_count : null
     * share_url : https://m.douban.com/movie/subject/1292052
     * seasons_count : null
     * schedule_url :
     * episodes_count : null
     * countries : ["美国"]
     * genres : ["犯罪","剧情"]
     * collect_count : 1190066
     * casts : [{"alt":"https://movie.douban.com/celebrity/1054521/","avatars":{"small":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p17525.jpg","large":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p17525.jpg","medium":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p17525.jpg"},"name":"蒂姆·罗宾斯","id":"1054521"},{"alt":"https://movie.douban.com/celebrity/1054534/","avatars":{"small":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p34642.jpg","large":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p34642.jpg","medium":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p34642.jpg"},"name":"摩根·弗里曼","id":"1054534"},{"alt":"https://movie.douban.com/celebrity/1041179/","avatars":{"small":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p5837.jpg","large":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p5837.jpg","medium":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p5837.jpg"},"name":"鲍勃·冈顿","id":"1041179"},{"alt":"https://movie.douban.com/celebrity/1000095/","avatars":{"small":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p7827.jpg","large":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p7827.jpg","medium":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p7827.jpg"},"name":"威廉姆·赛德勒","id":"1000095"}]
     * current_season : null
     * original_title : The Shawshank Redemption
     * summary : 20世纪40年代末，小有成就的青年银行家安迪（蒂姆·罗宾斯 Tim Robbins 饰）因涉嫌杀害妻子及她的情人而锒铛入狱。在这座名为肖申克的监狱内，希望似乎虚无缥缈，终身监禁的惩罚无疑注定了安迪接下来灰暗绝望的人生。未过多久，安迪尝试接近囚犯中颇有声望的瑞德（摩根·弗里曼 Morgan Freeman 饰），请求对方帮自己搞来小锤子。以此为契机，二人逐渐熟稔，安迪也仿佛在鱼龙混杂、罪恶横生、黑白混淆的牢狱中找到属于自己的求生之道。他利用自身的专业知识，帮助监狱管理层逃税、洗黑钱，同时凭借与瑞德的交往在犯人中间也渐渐受到礼遇。表面看来，他已如瑞德那样对那堵高墙从憎恨转变为处之泰然，但是对自由的渴望仍促使他朝着心中的希望和目标前进。而关于其罪行的真相，似乎更使这一切朝前推进了一步……
     * 本片根据著名作家斯蒂芬·金（Stephen Edwin King）的原著改编。©豆瓣
     * subtype : movie
     * directors : [{"alt":"https://movie.douban.com/celebrity/1047973/","avatars":{"small":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p230.jpg","large":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p230.jpg","medium":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p230.jpg"},"name":"弗兰克·德拉邦特","id":"1047973"}]
     * comments_count : 220739
     * ratings_count : 943067
     * aka : ["月黑高飞(港)","刺激1995(台)","地狱诺言","铁窗岁月","消香克的救赎"]
     */
    public RatingBean rating;
    public int reviews_count;
    public int wish_count;
    public String douban_site;
    public String year;
    public ImagesBean images;
    public String alt;
    public String id;
    public String mobile_url;
    public String title;
    public Object do_count;
    public String share_url;
    public Object seasons_count;
    public String schedule_url;
    public Object episodes_count;
    public int collect_count;
    public Object current_season;
    public String original_title;
    public String summary;
    public String subtype;
    public int comments_count;
    public int ratings_count;
    public List<String> countries;
    public List<String> genres;
    public List<DirectorsBean> casts;
    public List<DirectorsBean> directors;
    public List<String> aka;

    public static class RatingBean {
        /**
         * max : 10
         * average : 9.6
         * stars : 50
         * min : 0
         */
        public int max;
        public double average;
        public String stars;
        public int min;
    }

    public static class ImagesBean {
        /**
         * small : https://img3.doubanio.com/view/photo/s_ratio_poster/public/p480747492.jpg
         * large : https://img3.doubanio.com/view/photo/s_ratio_poster/public/p480747492.jpg
         * medium : https://img3.doubanio.com/view/photo/s_ratio_poster/public/p480747492.jpg
         */
        public String small;
        public String large;
        public String medium;
    }


    public static class DirectorsBean {
        /**
         * alt : https://movie.douban.com/celebrity/1047973/
         * avatars : {"small":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p230.jpg","large":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p230.jpg","medium":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p230.jpg"}
         * name : 弗兰克·德拉邦特
         * id : 1047973
         * type : 导演
         */
        public String alt;
        public ImagesBean avatars;
        public String name;
        public String id;
        public String type;

        @Override
        public String toString() {
            return "DirectorsBean{" +
                    "alt='" + alt + '\'' +
                    ", avatars=" + avatars +
                    ", name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

}
