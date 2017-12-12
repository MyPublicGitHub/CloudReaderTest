package com.example.cloudreadertest.base;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.cloudreadertest.R;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment<SV extends ViewDataBinding> extends Fragment {

    protected SV bindingView;
    protected RelativeLayout mContainer;
    private CompositeSubscription mCompositeSubscription;
    private LinearLayout llLoading;
    private ImageView ivLoading;
    protected AnimationDrawable mAnimationDrawable;
    private LinearLayout llLoadError;
    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_base, null);
        bindingView = DataBindingUtil.inflate(getActivity().getLayoutInflater(), setContentView(), null, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        mContainer = inflate.findViewById(R.id.rl_container);
        mContainer.addView(bindingView.getRoot());
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 初始化加载中动画
        llLoading = getView(R.id.ll_loading);
        ivLoading = getView(R.id.iv_loading);
        mAnimationDrawable = (AnimationDrawable) ivLoading.getDrawable();
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        //初始化失败布局
        llLoadError = getView(R.id.ll_load_error);
        llLoadError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();
                onRefresh();
            }
        });
        bindingView.getRoot().setVisibility(View.GONE);
    }

    // fragment是否显示了
    protected boolean mIsVisible = false;
    /**
     * 在这里实现Fragment数据的缓加载.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            mIsVisible = true;
            onVisible();
        }else {
            mIsVisible = false;
            onInvisible();
        }
    }
    protected void onVisible() {
        loadData();
    }
    /**
     * 显示时加载数据,需要这样的使用
     * 注意声明 isPrepared，先初始化
     * 生命周期会先执行 setUserVisibleHint 再执行onActivityCreated
     * 在 onActivityCreated 之后第一次显示加载数据，只加载一次
     */
    protected void loadData() {

    }
    protected void onInvisible() {

    }
    /**
     * 加载失败，点击重试
     */
    protected void onRefresh() {

    }

    /**
     * @param viewId
     * @param <T>
     * @return
     */
    protected <T extends View> T getView(int viewId) {
        return (T) getView().findViewById(viewId);
    }

    public abstract int setContentView();

    public void addSubscription(Subscription subscription){
        if (this.mCompositeSubscription ==null){
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription !=null&&this.mCompositeSubscription.hasSubscriptions()){
            this.mCompositeSubscription.unsubscribe();
        }
    }

    /**
     * 加载中
     */
    protected void showLoading() {
        //显示加载中布局
        if (llLoading.getVisibility() != View.VISIBLE)
            llLoading.setVisibility(View.VISIBLE);
        //运行动画
        if (!mAnimationDrawable.isRunning())
            mAnimationDrawable.start();
        //隐藏内容布局
        if (bindingView.getRoot().getVisibility() == View.VISIBLE)
            bindingView.getRoot().setVisibility(View.GONE);
        //隐藏加载失败布局
        if (llLoadError.getVisibility() == View.VISIBLE)
            llLoadError.setVisibility(View.GONE);
    }

    /**
     * 加载失败
     */
    protected void showLoadError() {
        //隐藏加载中布局
        if (llLoading.getVisibility() == View.VISIBLE)
            llLoading.setVisibility(View.GONE);
        //结束动画
        if (mAnimationDrawable.isRunning())
            mAnimationDrawable.stop();
        //隐藏内容布局
        if (bindingView.getRoot().getVisibility() == View.VISIBLE)
            bindingView.getRoot().setVisibility(View.GONE);
        //显示加载失败布局
        if (llLoadError.getVisibility() != View.VISIBLE)
            llLoadError.setVisibility(View.VISIBLE);
    }

    /**
     * 加载成功，显示内容布局
     */
    protected void showLoadSuccess() {
        //隐藏加载中布局
        if (llLoading.getVisibility() == View.VISIBLE)
            llLoading.setVisibility(View.GONE);
        //结束动画
        if (mAnimationDrawable.isRunning())
            mAnimationDrawable.stop();
        //显示内容布局
        if (bindingView.getRoot().getVisibility() != View.VISIBLE)
            bindingView.getRoot().setVisibility(View.VISIBLE);
        //隐藏加载失败布局
        if (llLoadError.getVisibility() == View.VISIBLE)
            llLoadError.setVisibility(View.GONE);
    }
}
