package com.example.cloudreadertest.ui.main.child;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.bumptech.glide.Glide;
import com.example.cloudreadertest.R;
import com.example.cloudreadertest.adapter.EveryDayAdapter;
import com.example.cloudreadertest.app.Constants;
import com.example.cloudreadertest.base.BaseFragment;
import com.example.cloudreadertest.bean.FrontpageBean;
import com.example.cloudreadertest.bean.GankIODayBean.ResultsBean.AndroidBean;
import com.example.cloudreadertest.databinding.FragmentEverydayBinding;
import com.example.cloudreadertest.databinding.ItemHeaderEverydayBinding;
import com.example.cloudreadertest.http.cache.Cache;
import com.example.cloudreadertest.http.rx.RequestImplements;
import com.example.cloudreadertest.model.EverydayModel;
import com.example.cloudreadertest.utils.GlideImageLoader;
import com.example.cloudreadertest.utils.SPUtils;
import com.example.cloudreadertest.utils.TimeUtil;
import com.example.cloudreadertest.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * 每日推荐
 */
public class EverydayFragment extends BaseFragment<FragmentEverydayBinding> {
    private RotateAnimation animation;
    private ItemHeaderEverydayBinding mItemHeaderEverydayBinding;
    private boolean mIsPrepared;
    private Cache mCache;
    // 是否是上一天的请求
    private boolean isOldDayRequest;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initAnim();
        initXRV();
        showLoadSuccess();
        mIsPrepared = true;
        loadData();
    }

    private void initData() {
        mCache = Cache.get(getContext());
        mBannerImages = (ArrayList<String>) mCache.getAsObject(Constants.BANNER_PIC);
        mEverydayModle = new EverydayModel();
        mItemHeaderEverydayBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_header_everyday, null, false);
        mItemHeaderEverydayBinding.includeHeader.tvDay.setText(TimeUtil.getDayFromData().startsWith("0") ? TimeUtil.getDayFromData().substring(1) : TimeUtil.getDayFromData());
    }

    @Override
    protected void loadData() {
        if (mItemHeaderEverydayBinding != null && mItemHeaderEverydayBinding.bannerHome != null) {
            mItemHeaderEverydayBinding.bannerHome.setDelayTime(5000);
            mItemHeaderEverydayBinding.bannerHome.startAutoPlay();
        }
        if (!mIsVisible || !mIsPrepared) {
            return;
        }
        String oneData = SPUtils.getString("everyday_data", "2016-11-26");
        if (!oneData.equals(TimeUtil.getData())) {
            if (TimeUtil.isRightTime()) {//大于12：30,请求新数据
                isOldDayRequest = false;
                showRotaLoading(true);
                showBanner();
                showContentData(TimeUtil.getYearFromData(), TimeUtil.getMonthFromData(), TimeUtil.getDayFromData());
            } else {//取缓存
                isOldDayRequest = true;
                ArrayList<String> lastTime = TimeUtil.getLastTime(TimeUtil.getYearFromData(), TimeUtil.getMonthFromData(), TimeUtil.getDayFromData());
                getCacheData(lastTime.get(0), lastTime.get(1), lastTime.get(2));
            }
        } else {//当天，取缓存，如果没有请求网络当天
            isOldDayRequest = false;
            getCacheData(TimeUtil.getYearFromData(), TimeUtil.getMonthFromData(), TimeUtil.getDayFromData());
        }

    }

    /**
     * 取缓存
     */
    private void getCacheData(String year, String month, String day) {
        if (mIsFirst) {
            if (mBannerImages == null || mBannerImages.size() <= 0) {
                showBanner();
            } else {
                mItemHeaderEverydayBinding.bannerHome.setImages(mBannerImages).setImageLoader(new GlideImageLoader()).start();
            }
            mLists = (ArrayList<List<AndroidBean>>) mCache.getAsObject(Constants.EVERYDAY_CONTENT);
            if (mLists == null || mLists.size() <= 0) {
                showRotaLoading(true);
                showContentData(year, month, day);
            } else {
                setAdapter(mLists);
            }
        }
    }

    private void showRotaLoading(boolean isLoading) {
        if (isLoading) {
            bindingView.xrvEveryday.setVisibility(View.GONE);
            bindingView.llLoading.setVisibility(View.VISIBLE);
            animation.startNow();
        } else {
            bindingView.xrvEveryday.setVisibility(View.VISIBLE);
            bindingView.llLoading.setVisibility(View.GONE);
            animation.cancel();
        }
    }

    EverydayModel mEverydayModle;
    ArrayList<String> mBannerImages;

    private void showBanner() {
        mEverydayModle.showBannerPage(new RequestImplements() {
            @Override
            public void loadSuccess(Object object) {
                FrontpageBean bean = (FrontpageBean) object;
                if (mBannerImages == null) {
                    mBannerImages = new ArrayList<>();
                } else {
                    mBannerImages.clear();
                }
                if (bean != null && bean.result != null && bean.result.focus != null && bean.result.focus.result != null) {
                    List<FrontpageBean.ResultBean.FocusBean.ResultBeanXXXXXXXXXXX> result = bean.result.focus.result;
                    if (result != null && result.size() > 0) {
                        for (int i = 0; i < result.size(); i++) {
                            mBannerImages.add(result.get(i).randpic);
                        }
                        mItemHeaderEverydayBinding.bannerHome.setImages(mBannerImages).setImageLoader(new GlideImageLoader()).start();
                    }
                }
            }

            @Override
            public void loadFailed() {

            }

            @Override
            public void addSubscription(Subscription subscription) {
                EverydayFragment.this.addSubscription(subscription);
            }
        });
    }

    private ArrayList<List<AndroidBean>> mLists;

    private void showContentData(String year, String month, String day) {
        mEverydayModle.showRecylerViewData(year, month, day, new RequestImplements() {
            @Override
            public void loadSuccess(Object object) {
                if (mLists != null) {
                    mLists.clear();
                }
                mLists = (ArrayList<List<AndroidBean>>) object;
                if (mLists != null && mLists.size() > 0 && mLists.get(0).size() > 0) {
                    setAdapter(mLists);
                } else {
                    ToastUtil.showToast("错误");
                }
            }

            @Override
            public void loadFailed() {
                if (mLists == null || mLists.size() <= 0)
                    showLoadError();
            }

            @Override
            public void addSubscription(Subscription subscription) {
                EverydayFragment.this.addSubscription(subscription);
            }
        });
    }

    private void initXRV() {
        bindingView.xrvEveryday.setPullRefreshEnabled(false);
        bindingView.xrvEveryday.setLoadingMoreEnabled(false);
        bindingView.xrvEveryday.addHeaderView(mItemHeaderEverydayBinding.getRoot());

        bindingView.xrvEveryday.setLayoutManager(new LinearLayoutManager(getContext()));
        bindingView.xrvEveryday.setItemAnimator(new DefaultItemAnimator());
    }

    private EveryDayAdapter mEverydayAdapter;
    private boolean mIsFirst = true;

    private void setAdapter(ArrayList<List<AndroidBean>> lists) {
        showRotaLoading(false);
        if (mEverydayAdapter == null) {
            mEverydayAdapter = new EveryDayAdapter();
        } else {
            mEverydayAdapter.clear();
        }
        mEverydayAdapter.addAll(lists);
        mCache.remove(Constants.EVERYDAY_CONTENT);
        mCache.put(Constants.EVERYDAY_CONTENT, lists, 259200);
        if (isOldDayRequest) {
            ArrayList<String> lastTime = TimeUtil.getLastTime(TimeUtil.getYearFromData(), TimeUtil.getMonthFromData(), TimeUtil.getDayFromData());
            SPUtils.putString("everyday_data", lastTime.get(0) + "-" + lastTime.get(1) + "-" + lastTime.get(2));
        } else {
            // 保存请求的日期
            SPUtils.putString("everyday_data", TimeUtil.getData());
        }
        mIsFirst = false;
        bindingView.xrvEveryday.setAdapter(mEverydayAdapter);
        mEverydayAdapter.notifyDataSetChanged();
    }

    private void initAnim() {
        bindingView.ivLoading.setVisibility(View.VISIBLE);
        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(3000);//持续时间
        animation.setInterpolator(new LinearInterpolator());//不停顿
        animation.setRepeatCount(10);
        bindingView.ivLoading.setAnimation(animation);
        animation.startNow();
    }

    @Override
    public int setContentView() {
        return R.layout.fragment_everyday;
    }

    @Override
    protected void onInvisible() {
        if (mItemHeaderEverydayBinding != null && mItemHeaderEverydayBinding.bannerHome != null) {
            mItemHeaderEverydayBinding.bannerHome.stopAutoPlay();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        bindingView.xrvEveryday.setFocusable(false);
        Glide.with(getActivity()).resumeRequests();
    }

    @Override
    public void onPause() {
        super.onPause();
        Glide.with(getActivity()).pauseRequests();
    }
}
