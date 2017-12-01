package com.example.cloudreadertest.bean;

import com.example.http.ParamNames;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：冯涛 on 2017/11/25 15:35
 * <p>
 * 邮箱：716774214@qq.com
 */
public class GankIODataBean implements Serializable{

    /**
     * error : false
     * results : [
     * {"_id":"5a137c67421aa90fef20354d","createdAt":"2017-11-21T09:07:51.275Z","desc":"iOS 和 Android 开发是否要采用 React Native?","publishedAt":"2017-11-24T11:08:03.624Z","source":"web","type":"Android","url":"https://mp.weixin.qq.com/s?__biz=MzU4MjAzNTAwMA==&mid=2247483866&idx=1&sn=b5cad7e5c26d001d920b0eff0625a995&key=6571080d88416c7220506cd2febefb7313338d6e5ef958258d4c8740d2dfc118bcca1c3edef0a834a293256d27046ecda4902f4b359f8550d6db0b150d4de080cfb515dbef6bd2654b3e3f35e3a625e7&ascene=0&uin=NTI1MzE1NDE1&devicetype=iMac+MacBookPro12%2C1+OSX+OSX+10.12.6+build(16G29)&version=12020610&nettype=WIFI&fontScale=100&pass_ticket=B5BwzwdLM8k1Q49ldLzImBZOjI9TR1ZOuqEJVskf%2B3xP0nK44%2FBkaEEXGpYDvsxG","used":true,"who":null},
     * {"_id":"5a1627fe421aa90fe2f02c80","createdAt":"2017-11-23T09:44:30.245Z","desc":"适配三星Galaxy S8及S8+","publishedAt":"2017-11-24T11:08:03.624Z","source":"web","type":"Android","url":"https://mp.weixin.qq.com/s?__biz=MzIwMzYwMTk1NA==&mid=2247488324&idx=1&sn=e043220e37d6eaaff217769f4061c04e","used":true,"who":"陈宇明"}
     * ]
     */

    public boolean error;
    public List<ResultsBean> results;

    public static class ResultsBean implements Serializable{
        /**
         * _id : 5a137c67421aa90fef20354d
         * createdAt : 2017-11-21T09:07:51.275Z
         * desc : iOS 和 Android 开发是否要采用 React Native?
         * publishedAt : 2017-11-24T11:08:03.624Z
         * source : web
         * type : Android
         * url : https://mp.weixin.qq.com/s?__biz=MzU4MjAzNTAwMA==&mid=2247483866&idx=1&sn=b5cad7e5c26d001d920b0eff0625a995&key=6571080d88416c7220506cd2febefb7313338d6e5ef958258d4c8740d2dfc118bcca1c3edef0a834a293256d27046ecda4902f4b359f8550d6db0b150d4de080cfb515dbef6bd2654b3e3f35e3a625e7&ascene=0&uin=NTI1MzE1NDE1&devicetype=iMac+MacBookPro12%2C1+OSX+OSX+10.12.6+build(16G29)&version=12020610&nettype=WIFI&fontScale=100&pass_ticket=B5BwzwdLM8k1Q49ldLzImBZOjI9TR1ZOuqEJVskf%2B3xP0nK44%2FBkaEEXGpYDvsxG
         * used : true
         * who : null
         * images : ["http://img.gank.io/fef497ed-83ba-46f6-8a94-0e7b724e1c10"]
         */

        public String _id;
        public String createdAt;
        public String desc;
        public String publishedAt;
        public String source;
        public String type;
        @ParamNames("url")
        public String url;
        public boolean used;
        public String who;
        public List<String> images;
    }
}
