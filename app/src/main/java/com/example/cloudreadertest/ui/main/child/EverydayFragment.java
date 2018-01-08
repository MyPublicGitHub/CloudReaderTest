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
import com.example.cloudreadertest.databinding.ItemFooterEverydayBinding;
import com.example.cloudreadertest.databinding.ItemHeaderEverydayBinding;
import com.example.cloudreadertest.http.RequestImplements;
import com.example.cloudreadertest.http.cache.Cache;
import com.example.cloudreadertest.http.rx.RxBus;
import com.example.cloudreadertest.http.rx.RxBusBaseMessage;
import com.example.cloudreadertest.http.rx.RxCodeConstants;
import com.example.cloudreadertest.model.EverydayModel;
import com.example.cloudreadertest.utils.DebugUtil;
import com.example.cloudreadertest.utils.GlideImageLoader;
import com.example.cloudreadertest.utils.PerfectClickListener;
import com.example.cloudreadertest.utils.SPUtils;
import com.example.cloudreadertest.utils.TimeUtil;
import com.example.cloudreadertest.utils.ToastUtil;
import com.example.cloudreadertest.view.webview.WebViewActivity;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * 每日推荐
 */
public class EverydayFragment extends BaseFragment<FragmentEverydayBinding> {
    private RotateAnimation animation;
    private ItemHeaderEverydayBinding mHeaderBinding;
    private ItemFooterEverydayBinding mFooterBinding;
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
        initLocalSetting();
        showLoadSuccess();
        mIsPrepared = true;
        loadData();
    }

    private void initLocalSetting() {
        mHeaderBinding.includeHeader.tvDay.setText(TimeUtil.getDayFromData().startsWith("0") ? TimeUtil.getDayFromData().substring(1) : TimeUtil.getDayFromData());
        mHeaderBinding.includeHeader.ibReader.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                WebViewActivity.loadUrl(view.getContext(), "https://gank.io/xiandu", "加载中...");
            }
        });
        mHeaderBinding.includeHeader.ibMovie.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                RxBus.getDefault().post(RxCodeConstants.JUMP_TYPE_TO_ONE, new RxBusBaseMessage());
            }
        });
    }

    private void initData() {
        mCache = Cache.get(getContext());
        mBannerImages = (ArrayList<String>) mCache.getAsObject(Constants.BANNER_PIC);
        mEverydayModel = new EverydayModel();
        mHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_header_everyday, null, false);
        mFooterBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_footer_everyday, null, false);
        mYear = TimeUtil.getYearFromData();
        mMonth = TimeUtil.getMonthFromData();
        mDay = TimeUtil.getDayFromData();
    }

    @Override
    protected void loadData() {
        if (mHeaderBinding != null && mHeaderBinding.bannerHome != null) {
            mHeaderBinding.bannerHome.setDelayTime(5000);
            mHeaderBinding.bannerHome.startAutoPlay();
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
                showContentData(mYear, mMonth, mDay);
                DebugUtil.debug("请求新数据");
            } else {//取缓存
                isOldDayRequest = true;
                ArrayList<String> lastTime = TimeUtil.getLastTime(TimeUtil.getYearFromData(), TimeUtil.getMonthFromData(), TimeUtil.getDayFromData());
                mYear = lastTime.get(0);
                mMonth = lastTime.get(1);
                mDay = lastTime.get(2);
                DebugUtil.debug("缓存"+mDay);
                getCacheData(mYear, mMonth, mDay);
            }
        } else {//当天，取缓存，如果没有请求网络当天
            DebugUtil.debug("缓存"+mDay);
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
                mHeaderBinding.bannerHome.setImages(mBannerImages).setImageLoader(new GlideImageLoader()).start();
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

    EverydayModel mEverydayModel;
    ArrayList<String> mBannerImages;

    /**
     * 获取banner
     */
    private void showBanner() {
        mEverydayModel.showBannerPage(new RequestImplements() {
            @Override
            public void loadSuccess(Object object) {
                FrontpageBean bean = (FrontpageBean) object;
                if (mBannerImages == null) {
                    mBannerImages = new ArrayList<>();
                } else {
                    mBannerImages.clear();
                }

                if (bean != null && bean.result != null && bean.result.focus != null && bean.result.focus.result != null) {
                    final List<FrontpageBean.ResultBean.FocusBean.ResultBeanXXXXXXXXXXX> result = bean.result.focus.result;
                    if (result != null && result.size() > 0) {
                        for (int i = 0; i < result.size(); i++) {
                            mBannerImages.add(result.get(i).randpic);
                        }
                        mHeaderBinding.bannerHome.setImages(mBannerImages).setImageLoader(new GlideImageLoader()).start();
                        mHeaderBinding.bannerHome.setOnBannerClickListener(new OnBannerClickListener() {
                            @Override
                            public void OnBannerClick(int position) {
                                position = position - 1;
                                if (result.get(position) != null && result.get(position).code != null && result.get(position).code.startsWith("http")) {
                                    WebViewActivity.loadUrl(getContext(), result.get(position).code, result.get(position).randpic_desc + "详情");
                                }
                            }
                        });
                        mCache.remove(Constants.BANNER_PIC);
                        mCache.put(Constants.BANNER_PIC, mBannerImages, 30000);
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

    /**
     * 获取推荐内容
     */
    private void showContentData(String year, String month, String day) {
        mEverydayModel.showRecyclerViewData(year, month, day, new RequestImplements() {
            @Override
            public void loadSuccess(Object object) {
                if (mLists != null) {
                    mLists.clear();
                }
                mLists = (ArrayList<List<AndroidBean>>) object;
                if (mLists != null && mLists.size() > 0 && mLists.get(0).size() > 0) {
                    setAdapter(mLists);
                } else {
                    ToastUtil.showToast("加载旧数据：" + mYear + mMonth + mDay);
                    requestBeforeData();
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

    private String mYear, mMonth, mDay;

    /**
     * 取昨天的数据
     */
    private void requestBeforeData() {
        mLists = (ArrayList<List<AndroidBean>>) mCache.getAsObject(Constants.EVERYDAY_CONTENT);
        if (mLists != null && mLists.size() > 0) {
            setAdapter(mLists);
        } else {
            ArrayList<String> lastData = TimeUtil.getLastTime(mYear, mMonth, mDay);
            mYear = lastData.get(0);
            mMonth = lastData.get(1);
            mDay = lastData.get(2);
            showContentData(mYear, mMonth, mDay);
        }
    }

    private void initXRV() {
        bindingView.xrvEveryday.setPullRefreshEnabled(false);
        bindingView.xrvEveryday.setLoadingMoreEnabled(false);
        bindingView.xrvEveryday.addHeaderView(mHeaderBinding.getRoot());
        bindingView.xrvEveryday.addFootView(mFooterBinding.getRoot(), true);
        bindingView.xrvEveryday.noMoreLoading();

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
        if (mHeaderBinding != null && mHeaderBinding.bannerHome != null) {
            mHeaderBinding.bannerHome.stopAutoPlay();
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

    @Override
    protected void onRefresh() {
        showLoadSuccess();
        showRotaLoading(true);
        loadData();
    }
}
