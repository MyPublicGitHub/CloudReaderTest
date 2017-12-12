package com.example.cloudreadertest.bean;

import java.util.List;

/**
 * 作者：冯涛 on 2017/12/1 17:56
 * <p>
 * 邮箱：716774214@qq.com
 */
public class FrontpageBean {

    /**
     * https://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.8.1.0&channel=ppzs&operator=3&method=baidu.ting.plaza.index&cuid=89CF1E1A06826F9AB95A34DC0F6AAA14
     */

    public ResultBean result;
    public int error_code;
    public List<ModuleBean> module;

    public static class ResultBean {
        public Mix1Bean mix_1;
        public Mod29Bean mod_29;
        public ShowListBean show_list;
        public Mix22Bean mix_22;
        public Mix29Bean mix_29;
        public EntryBean entry;
        public SceneBean scene;
        public NewSongBean new_song;
        public Mix9Bean mix_9;
        public RecsongBean recsong;
        public DiyBean diy;
        public FocusBean focus;
        public Mod27Bean mod_27;
        public RadioBean radio;
        public KingBean king;

        public static class Mix1Bean {
            public int error_code;
            public List<ResultBeanX> result;

            public static class ResultBeanX {
                /**
                 * desc : 11月新歌速递
                 * pic : http://business.cdn.qianqian.com/qianqian/pic/bos_client_15096177959ccd43ca58ac2366d2edc5bbc787671b.jpg
                 * type_id : 512295440
                 * type : 0
                 * title : 新歌抢鲜听
                 * tip_type : 0
                 * author : 11月新歌速递
                 */

                public String desc;
                public String pic;
                public String type_id;
                public int type;
                public String title;
                public int tip_type;
                public String author;
            }
        }

        public static class Mod29Bean {
            /**
             * error_code : 22000
             * result : [{"desc":"","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_149217051946506880bece60f7fc94641aa8ae8bc5.jpg","type_id":"http://music.baidu.com/cms/webview/xiha/gs/index.html","type":4,"title":"滚石唱片音乐专区","tip_type":0,"author":""}]
             */

            public int error_code;
            public List<ResultBeanX> result;

            public static class ResultBeanX {
                /**
                 * desc :
                 * pic : http://business.cdn.qianqian.com/qianqian/pic/bos_client_149217051946506880bece60f7fc94641aa8ae8bc5.jpg
                 * type_id : http://music.baidu.com/cms/webview/xiha/gs/index.html
                 * type : 4
                 * title : 滚石唱片音乐专区
                 * tip_type : 0
                 * author :
                 */

                public String desc;
                public String pic;
                public String type_id;
                public int type;
                public String title;
                public int tip_type;
                public String author;
            }
        }

        public static class ShowListBean {


            public int error_code;
            public List<ResultBeanXX> result;

            public static class ResultBeanXX {


                public String type;
                public String picture_iphone6;
                public String picture;
                public String web_url;
            }
        }

        public static class Mix22Bean {


            public int error_code;
            public List<ResultBeanXXX> result;

            public static class ResultBeanXXX {


                public String desc;
                public String pic;
                public String type_id;
                public int type;
                public String title;
                public int tip_type;
                public String author;
            }
        }

        public static class Mix29Bean {


            public int error_code;
            public List<ResultBeanXXXX> result;

            public static class ResultBeanXXXX {

                public String desc;
                public String pic;
                public String type_id;
                public int type;
                public String title;
                public int tip_type;
                public String author;
            }
        }

        public static class EntryBean {

            public int error_code;
            public List<ResultBeanXXXXX> result;

            public static class ResultBeanXXXXX {


                public String day;
                public String title;
                public String icon;
                public String jump;
            }
        }

        public static class SceneBean {

            public ResultBeanXXXXXX result;
            public int error_code;
            public List<ConfigBean> config;

            public static class ResultBeanXXXXXX {
                public List<ActionBean> action;
                public List<EmotionBean> emotion;
                public List<OperationBean> operation;
                public List<OtherBean> other;

                public static class ActionBean {

                    public String icon_ios;
                    public String scene_name;
                    public String bgpic_android;
                    public String icon_android;
                    public String scene_model;
                    public String scene_desc;
                    public String bgpic_ios;
                    public String scene_id;
                }

                public static class EmotionBean {

                    public String icon_ios;
                    public String scene_name;
                    public String bgpic_android;
                    public String icon_android;
                    public String scene_model;
                    public String scene_desc;
                    public String bgpic_ios;
                    public String scene_id;
                }

                public static class OperationBean {

                    public String icon_ios;
                    public String scene_name;
                    public String bgpic_android;
                    public String icon_android;
                    public String scene_model;
                    public String scene_desc;
                    public String bgpic_ios;
                    public String scene_id;
                }

                public static class OtherBean {

                    public String icon_ios;
                    public String scene_name;
                    public String bgpic_android;
                    public String icon_android;
                    public String scene_model;
                    public String scene_desc;
                    public String bgpic_ios;
                    public String scene_id;
                }
            }

            public static class ConfigBean {
                public String color_other;
                public String play_color;
                public int scene_version;
                public String desc;
                public int end_time;
                public int start_time;
                public String scene_color;
                public String bgpic;
                public String bgpic_special;
                public String button_color;
            }
        }

        public static class NewSongBean {

            public int error_code;
            public ResultBeanXXXXXXX result;

            public static class ResultBeanXXXXXXX {

                public String pic_500;
                public String listid;
                public List<SongInfoBean> song_info;

                public static class SongInfoBean {

                    public String song_id;
                    public String title;
                    public String pic_premium;
                    public String author;
                }
            }
        }

        public static class Mix9Bean {

            public int error_code;
            public List<ResultBeanXXXXXXXX> result;

            public static class ResultBeanXXXXXXXX {

                public String desc;
                public String pic;
                public String type_id;
                public int type;
                public String title;
                public int tip_type;
                public String author;
            }
        }

        public static class RecsongBean {

            public int error_code;
            public List<ResultBeanXXXXXXXXX> result;

            public static class ResultBeanXXXXXXXXX {

                public String resource_type_ext;
                public String distribution;
                public String has_filmtv;
                public String learn;
                public String pic_premium;
                public String album_id;
                public String biaoshi;
                public String si_proxycompany;
                public String versions;
                public String bitrate_fee;
                public String info;
                public String copy_type;
                public String album_title;
                public String method;
                public String title;
                public String song_id;
                public String author;
                public String del_status;
                public String korean_bb_song;
                public String has_mv_mobile;
            }
        }

        public static class DiyBean {

            public int error_code;
            public List<ResultBeanXXXXXXXXXX> result;

            public static class ResultBeanXXXXXXXXXX {

                public int position;
                public String tag;
                public String pic;
                public String title;
                public int collectnum;
                public String type;
                public int listenum;
                public String listid;
                public List<?> songidlist;
            }
        }

        public static class FocusBean {

            public int error_code;
            public List<ResultBeanXXXXXXXXXXX> result;

            public static class ResultBeanXXXXXXXXXXX {

                public String is_publish;
                public String code;
                public int type;
                public String randpic_desc;
                public int mo_type;
                public String randpic_iphone6;
                public String randpic;
            }
        }

        public static class Mod27Bean {

            public int error_code;
            public List<ResultBeanXXXXXXXXXXXX> result;

            public static class ResultBeanXXXXXXXXXXXX {

                public String desc;
                public String pic;
                public String type_id;
                public int type;
                public String title;
                public int tip_type;
                public String author;
            }
        }

        public static class RadioBean {

            public int error_code;
            public List<ResultBeanXXXXXXXXXXXXX> result;

            public static class ResultBeanXXXXXXXXXXXXX {

                public String desc;
                public String itemid;
                public String title;
                public String album_id;
                public String type;
                public String channelid;
                public String pic;
            }
        }

        public static class KingBean {

            public int error_code;
            public List<ResultBeanXXXXXXXXXXXXXX> result;

            public static class ResultBeanXXXXXXXXXXXXXX {
                public String pic_big;
                public String title;
                public String author;
            }
        }
    }

    public static class ModuleBean {
        public String id;
        public int style;
        public String link_url;
        public String style_nums;
        public int pos;
        public String title;
        public String key;
        public String picurl;
        public String title_more;
        public int nums;
        public String jump;
    }
}
