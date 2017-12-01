package com.example.cloudreadertest.model;

import com.example.cloudreadertest.http.rx.HttpClient;
import com.example.cloudreadertest.http.rx.RequestImplements;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：冯涛 on 2017/11/25 15:11
 * <p> 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页  的Model
 * 好处之一是请求数据接口可以统一，不用每个地方都写请求的接口，更换接口方便。
 * 其实代码量也没有减少多少，但维护起来方便。
 * 邮箱：716774214@qq.com
 */
public class GankOtherModel {

    private String dataType;//数据类型
    private int pre_page;//数量
    private int page;//页码

    public GankOtherModel(String dataType, int pre_page, int page) {
        this.dataType = dataType;
        this.pre_page = pre_page;
        this.page = page;
    }

    public void getGankIOData(final RequestImplements impl) {

        Subscription subscription = HttpClient.Builder.getGankIOServer().getGankIOData(dataType, pre_page, page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        impl.loadFailed();
                    }

                    @Override
                    public void onNext(Object o) {
                        impl.loadSuccess(o);
                    }
                });

        impl.addSubscription(subscription);
    }

}
