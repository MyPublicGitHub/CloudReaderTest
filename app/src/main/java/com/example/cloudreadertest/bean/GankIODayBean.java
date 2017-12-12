package com.example.cloudreadertest.bean;

import com.example.http.ParamNames;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：冯涛 on 2017/12/4 10:50
 * <p>
 * 邮箱：716774214@qq.com
 */
public class GankIODayBean implements Serializable {

    /**
     * http://gank.io/api/day/2015/08/06
     */

    public boolean error;
    public ResultsBean results;
    public List<String> category;

    public static class ResultsBean {
        @ParamNames("Android")
        public List<AndroidBean> Android;

        @ParamNames("iOS")
        public List<AndroidBean> iOS;

        @ParamNames("前端")
        public List<AndroidBean> front;

        @ParamNames("App")
        public List<AndroidBean> app;

        @ParamNames("休息视频")
        public List<AndroidBean> restMovie;

        @ParamNames("拓展资源")
        public List<AndroidBean> resource;

        @ParamNames("瞎推荐")
        public List<AndroidBean> recommend;

        @ParamNames("福利")
        public List<AndroidBean> welfare;

        public static class AndroidBean {
            public String type_title;
            public String image_url;
            public String _id;
            public String createdAt;
            public String desc;
            public String publishedAt;
            public String type;
            public String url;
            public boolean used;
            public String who;
        }

    }
}
