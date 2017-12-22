package com.example.cloudreadertest.model;

import android.text.TextUtils;

import com.example.cloudreadertest.app.ConstantsImageUrl;
import com.example.cloudreadertest.bean.FrontpageBean;
import com.example.cloudreadertest.bean.GankIODayBean;
import com.example.cloudreadertest.bean.GankIODayBean.ResultsBean.AndroidBean;
import com.example.cloudreadertest.http.HttpClient;
import com.example.cloudreadertest.http.RequestImplements;
import com.example.cloudreadertest.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 作者：冯涛 on 2017/12/1 17:27
 * <p>
 * 邮箱：716774214@qq.com
 */
public class EverydayModel {

    public void showBannerPage(final RequestImplements requestImplements) {
        Subscription subscription = HttpClient.Builder.getTingServer().getFrontpage()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FrontpageBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        requestImplements.loadFailed();
                    }

                    @Override
                    public void onNext(FrontpageBean frontpageBean) {
                        requestImplements.loadSuccess(frontpageBean);
                    }
                });
        requestImplements.addSubscription(subscription);
    }

    public void showRecyclerViewData(String year, String month, String day, final RequestImplements requestImplements) {
        SPUtils.putString(HOME_ONE, "");
        SPUtils.putString(HOME_TWO, "");
        SPUtils.putString(HOME_SIX, "");
        Func1<GankIODayBean, Observable<List<List<AndroidBean>>>> func1 = new Func1<GankIODayBean, Observable<List<List<AndroidBean>>>>() {
            @Override
            public Observable<List<List<AndroidBean>>> call(GankIODayBean gankIODayBean) {
                List<List<AndroidBean>> lists = new ArrayList<>();
                GankIODayBean.ResultsBean results = gankIODayBean.results;
                if (results.Android != null && results.Android.size() > 0) {
                    addUrlList("Android", results.Android, lists);
                }
                if (results.welfare != null && results.welfare.size() > 0) {
                    addUrlList("福利", results.welfare, lists);
                }
                if (results.iOS != null && results.iOS.size() > 0) {
                    addUrlList("iOS", results.iOS, lists);
                }
                if (results.restMovie != null && results.restMovie.size() > 0) {
                    addUrlList("休息视频", results.restMovie, lists);
                }
                if (results.resource != null && results.resource.size() > 0) {
                    addUrlList("拓展资源", results.resource, lists);
                }
                if (results.front != null && results.front.size() > 0) {
                    addUrlList("前端", results.front, lists);
                }
                if (results.app != null && results.app.size() > 0) {
                    addUrlList("App", results.app, lists);
                }
                if (results.recommend != null && results.recommend.size() > 0) {
                    addUrlList("瞎推荐", results.recommend, lists);
                }
                return Observable.just(lists);
            }
        };
        Subscription subscription = HttpClient.Builder.getGankIOServer().getGankIoDay(year, month, day)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).flatMap(func1)
                .subscribe(new Observer<List<List<AndroidBean>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        requestImplements.loadFailed();
                    }

                    @Override
                    public void onNext(List<List<AndroidBean>> lists) {
                        requestImplements.loadSuccess(lists);
                    }
                });

        requestImplements.addSubscription(subscription);
    }

    // subList没有实现序列化！缓存时会出错！
    private void addUrlList(String typeTitle, List<AndroidBean> arrayList, List<List<AndroidBean>> lists) {
        // title
        AndroidBean bean = new AndroidBean();
        bean.type_title = typeTitle;
        ArrayList<AndroidBean> androidBeen = new ArrayList<>();
        androidBeen.add(bean);
        lists.add(androidBeen);

        int androidSize = arrayList.size();

        if (androidSize > 0 && androidSize < 4) {

            lists.add(addUrlList(arrayList, androidSize));
        } else if (androidSize >= 4) {

            ArrayList<AndroidBean> list1 = new ArrayList<>();
            ArrayList<AndroidBean> list2 = new ArrayList<>();

            for (int i = 0; i < androidSize; i++) {
                if (i < 3) {
                    list1.add(getAndroidBean(arrayList, i, androidSize));
                } else if (i < 6) {
                    list2.add(getAndroidBean(arrayList, i, androidSize));
                }
            }
            lists.add(list1);
            lists.add(list2);
        }
    }

    private AndroidBean getAndroidBean(List<AndroidBean> arrayList, int i, int androidSize) {

        AndroidBean androidBean = new AndroidBean();
        // 标题
        androidBean.desc = arrayList.get(i).desc;
        // 类型
        androidBean.type = arrayList.get(i).type;
        // 跳转链接
        androidBean.url = arrayList.get(i).url;
        // 随机图的url
        if (i < 3) {
            androidBean.image_url = ConstantsImageUrl.HOME_SIX_URLS[getRandom(3)];//三小图
        } else if (androidSize == 4) {
            androidBean.image_url = ConstantsImageUrl.HOME_ONE_URLS[getRandom(1)];//一图
        } else if (androidSize == 5) {
            androidBean.image_url = ConstantsImageUrl.HOME_TWO_URLS[getRandom(2)];//两图
        } else if (androidSize >= 6) {
            androidBean.image_url = ConstantsImageUrl.HOME_SIX_URLS[getRandom(3)];//三小图
        }
        return androidBean;
    }


    private List<AndroidBean> addUrlList(List<AndroidBean> arrayList, int androidSize) {
        List<AndroidBean> tempList = new ArrayList<>();
        for (int i = 0; i < androidSize; i++) {
            AndroidBean androidBean = new AndroidBean();
            // 标题
            androidBean.desc = arrayList.get(i).desc;
            // 类型
            androidBean.type = arrayList.get(i).type;
            // 跳转链接
            androidBean.url = arrayList.get(i).url;
//            DebugUtil.error("---androidSize:  " + androidSize);
            // 随机图的url
            if (androidSize == 1) {
                androidBean.image_url = ConstantsImageUrl.HOME_ONE_URLS[getRandom(1)];//一图
            } else if (androidSize == 2) {
                androidBean.image_url = ConstantsImageUrl.HOME_TWO_URLS[getRandom(2)];//两图
            } else if (androidSize == 3) {
                androidBean.image_url = ConstantsImageUrl.HOME_SIX_URLS[getRandom(3)];//三图
            }
            tempList.add(androidBean);
        }
        return tempList;
    }

    private String year = "2016";
    private String month = "11";
    private String day = "24";
    private static final String HOME_ONE = "home_one";
    private static final String HOME_TWO = "home_two";
    private static final String HOME_SIX = "home_six";

    /**
     * 取不同的随即图，在每次联网时重置
     *
     * @param type
     * @return
     */
    private int getRandom(int type) {
        String saveWhere = null;
        int urlLength = 0;
        if (type == 1) {
            saveWhere = HOME_ONE;
            urlLength = ConstantsImageUrl.HOME_ONE_URLS.length;
        } else if (type == 2) {
            saveWhere = HOME_TWO;
            urlLength = ConstantsImageUrl.HOME_TWO_URLS.length;
        } else if (type == 3) {
            saveWhere = HOME_SIX;
            urlLength = ConstantsImageUrl.HOME_SIX_URLS.length;
        }

        String home_six = SPUtils.getString(saveWhere, "");
        if (!TextUtils.isEmpty(home_six)) {
            // 已取到的值
            String[] split = home_six.split(",");

            Random random = new Random();
            for (int j = 0; j < urlLength; j++) {
                int randomInt = random.nextInt(urlLength);

                boolean isUse = false;
                for (String aSplit : split) {
                    if (!TextUtils.isEmpty(aSplit) && String.valueOf(randomInt).equals(aSplit)) {
                        isUse = true;
                        break;
                    }
                }
                if (!isUse) {
                    StringBuilder sb = new StringBuilder(home_six);
                    sb.insert(0, randomInt + ",");
                    SPUtils.putString(saveWhere, sb.toString());
                    return randomInt;
                }
            }

        } else {
            Random random = new Random();
            int randomInt = random.nextInt(urlLength);
            SPUtils.putString(saveWhere, randomInt + ",");
            return randomInt;
        }
        return 0;
    }
}
