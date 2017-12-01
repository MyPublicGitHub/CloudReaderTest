package com.example.cloudreadertest.ui.main.child;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.base.BaseFragment;
import com.example.cloudreadertest.databinding.FragmentEverydayBinding;
import com.example.cloudreadertest.databinding.ItemHeaderEverydayBinding;
import com.example.cloudreadertest.databinding.LayoutHomeMiddleBinding;
import com.example.cloudreadertest.utils.DebugUtil;
import com.example.cloudreadertest.utils.TimeUtil;

/**
 * 每日推荐
 */
public class EverydayFragment extends BaseFragment<FragmentEverydayBinding> {
    private RotateAnimation animation;
    private ItemHeaderEverydayBinding mItemHeaderEverydayBinding;
    private boolean mPrepared;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showLoadSuccess();
        bindingView.ivLoading.setVisibility(View.VISIBLE);
        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(3000);//持续时间
        animation.setInterpolator(new LinearInterpolator());//不停顿
        animation.setRepeatCount(10);
        bindingView.ivLoading.setAnimation(animation);
        animation.startNow();

        mItemHeaderEverydayBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_header_everyday, null, false);
        initXRV();
        mPrepared = false;
        loadData();
    }

    @Override
    protected void loadData() {
        if (mItemHeaderEverydayBinding != null && mItemHeaderEverydayBinding.bannerHome != null) {
            mItemHeaderEverydayBinding.bannerHome.setDelayTime(5000);
            mItemHeaderEverydayBinding.bannerHome.startAutoPlay();
        }
        if (mPrepared&&mIsVisible){


            showContentData();


        }

    }
    private void showContentData(){

    }
    private void initXRV() {
        bindingView.xrvEveryday.setPullRefreshEnabled(true);
        bindingView.xrvEveryday.setLoadingMoreEnabled(false);
        bindingView.xrvEveryday.addHeaderView(mItemHeaderEverydayBinding.getRoot());

    }

    @Override
    public int setContentView() {
        return R.layout.fragment_everyday;
    }

}
