package com.example.cloudreadertest.ui.music;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.cloudreadertest.MainActivity;
import com.example.cloudreadertest.R;
import com.example.cloudreadertest.adapter.OneAdapter;
import com.example.cloudreadertest.app.Constants;
import com.example.cloudreadertest.app.ConstantsImageUrl;
import com.example.cloudreadertest.base.BaseFragment;
import com.example.cloudreadertest.bean.HotMovieBean;
import com.example.cloudreadertest.databinding.FragmentMusicBinding;
import com.example.cloudreadertest.databinding.HeaderItemOneBinding;
import com.example.cloudreadertest.http.HttpClient;
import com.example.cloudreadertest.http.cache.Cache;
import com.example.cloudreadertest.utils.DebugUtil;
import com.example.cloudreadertest.utils.ImageLoadUtils;
import com.example.cloudreadertest.utils.SPUtils;
import com.example.cloudreadertest.utils.TimeUtil;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends BaseFragment<FragmentMusicBinding> {
    // 是否初始化完成      是否是第一次加载数据    是否正在刷新
    private boolean mIsPrepared = false, mIsFirst = true, mIsLoading;
    private Cache mCache;
    private MainActivity mMainActivity;
    private HotMovieBean mHotMovieBean;
    private OneAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCache = Cache.get(getActivity());
        mHotMovieBean = (HotMovieBean) mCache.getAsObject(Constants.ONE_HOT_MOVIE);
        mIsPrepared = true;
    }

    @Override
    protected void loadData() {
        if (mIsPrepared && mIsVisible) {
            // 显示，准备完毕，不是当天，则请求数据（正在请求时避免再次请求）
            String oneData = SPUtils.getString(Constants.ONE_DATA, "2016-11-26");
            if (!oneData.equals(TimeUtil.getData()) && mIsLoading) {
                showLoading();
                /**延迟执行防止卡顿*/
                postDelayLoad();
            } else {
                if (!mIsLoading && mIsFirst) {
                    showLoading();
                    if (mHotMovieBean == null) {
                        if (!mIsLoading) {
                            postDelayLoad();
                        }
                    } else {
                        bindingView.xrvMusic.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setAdapter(mHotMovieBean);
                                showLoadSuccess();
                            }
                        }, 150);
                    }
                }
            }
        }
    }

    private void loadHotMovie() {
        Subscription subscription = HttpClient.Builder.getDouBanService().getHotMovie().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<HotMovieBean>() {
                    @Override
                    public void onCompleted() {
                        showLoadSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showLoadSuccess();
                        if (mAdapter == null || mAdapter.getItemCount() == 0) {
                            showLoadError();
                        }
                    }

                    @Override
                    public void onNext(HotMovieBean bean) {
                        showLoadSuccess();
                        if (bean != null) {
                            mCache.remove(Constants.ONE_HOT_MOVIE);
                            mCache.put(Constants.ONE_HOT_MOVIE, bean, 43200);
                            setAdapter(bean);
                            // 保存请求的日期
                            SPUtils.putString(Constants.ONE_DATA, TimeUtil.getData());
                            //刷新结束
                            mIsLoading = false;
                        }
                    }
                });
        addSubscription(subscription);
    }

    HeaderItemOneBinding mHeaderBinding;

    private void setAdapter(HotMovieBean bean) {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bindingView.xrvMusic.setLayoutManager(mLayoutManager);

        // 加上这两行代码，下拉出提示才不会产生出现刷新头的bug，不加拉不下来
        bindingView.xrvMusic.setPullRefreshEnabled(false);
        bindingView.xrvMusic.clearHeader();
        bindingView.xrvMusic.setLoadingMoreEnabled(false);
        // 需加，不然滑动不流畅
        bindingView.xrvMusic.setNestedScrollingEnabled(false);
        bindingView.xrvMusic.setHasFixedSize(false);

        if (mHeaderBinding == null) {
            //add的头布局的根布局必须是RelativeLayout否则显示不全
            mHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.header_item_one, null, false);
            bindingView.xrvMusic.addHeaderView(mHeaderBinding.getRoot());
        }

        ImageLoadUtils.displayRandom(3, ConstantsImageUrl.ONE_URL_01, mHeaderBinding.imageView);
        mHeaderBinding.llMovieTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DouBanTopActivity.start(getContext());
            }
        });
        if (mAdapter == null) {
            mAdapter = new OneAdapter(getActivity());
        }
        mAdapter.clear();
        mAdapter.addAll(bean.subjects);
        bindingView.xrvMusic.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mIsFirst = false;
    }

    private void postDelayLoad() {
        synchronized (this) {
            if (!mIsLoading) {
                mIsLoading = true;
                bindingView.xrvMusic.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadHotMovie();
                    }
                }, 150);
            }
        }
    }

    @Override
    protected void onRefresh() {
        loadHotMovie();
    }

    @Override
    public int setContentView() {
        return R.layout.fragment_music;
    }

}
