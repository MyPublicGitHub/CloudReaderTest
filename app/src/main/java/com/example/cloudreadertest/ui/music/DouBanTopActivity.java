package com.example.cloudreadertest.ui.music;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.adapter.DouBanTopAdapter;
import com.example.cloudreadertest.base.BaseActivity;
import com.example.cloudreadertest.bean.HotMovieBean;
import com.example.cloudreadertest.databinding.ActivityDouBanTopBinding;
import com.example.cloudreadertest.http.HttpClient;
import com.example.xrecyclerview.XRecyclerView;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DouBanTopActivity extends BaseActivity<ActivityDouBanTopBinding> {

    private DouBanTopAdapter mAdapter;
    private int mStart = 0, mCount = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dou_ban_top);
        setTitle("豆瓣电影Top250");
        mAdapter = new DouBanTopAdapter(this);
        initXrv();
        loadDouBanTop250();
        bindingView.xrvTop.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mStart += mCount;
                loadDouBanTop250();
            }
        });
    }

    private void loadDouBanTop250() {
        Subscription subscription = HttpClient.Builder.getDouBanService().getMovieTop250(mStart, mCount)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HotMovieBean>() {
                    @Override
                    public void onCompleted() {
                        showLoadSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        bindingView.xrvTop.refreshComplete();
                        if (mAdapter.getItemCount() == 0) {
                            showLoadError();
                        }
                    }

                    @Override
                    public void onNext(HotMovieBean bean) {
                        if (mStart == 0) {
                            if (bean != null && bean.subjects != null && bean.subjects.size() > 0) {
                                mAdapter.clear();
                                mAdapter.addAll(bean.subjects);
                                bindingView.xrvTop.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                bindingView.xrvTop.setVisibility(View.GONE);
                            }
                        } else {
                            if (bean != null && bean.subjects != null && bean.subjects.size() > 0) {
                                bindingView.xrvTop.refreshComplete();
                                mAdapter.addAll(bean.subjects);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                bindingView.xrvTop.noMoreLoading();
                            }
                        }
                    }
                });
        addSubscription(subscription);
    }

    private void initXrv() {
        //第一个参数表示行数或者列数，第二个表示滑动方向
        bindingView.xrvTop.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        bindingView.xrvTop.setPullRefreshEnabled(false);
        bindingView.xrvTop.clearHeader();
        bindingView.xrvTop.setLoadingMoreEnabled(true);
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, DouBanTopActivity.class));
    }
}
