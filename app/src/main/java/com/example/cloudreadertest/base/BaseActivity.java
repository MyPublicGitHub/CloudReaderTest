package com.example.cloudreadertest.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.databinding.ActivityBaseBinding;
import com.example.cloudreadertest.utils.CommonUtils;
import com.example.cloudreadertest.view.statusbar.StatusBarUtil;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BaseActivity<SV extends ViewDataBinding> extends AppCompatActivity {

    protected SV bindingView;
    private CompositeSubscription mCompositeSubscription;
    protected AnimationDrawable mAnimationDrawable;
    private ActivityBaseBinding mBaseBinding;
    private LinearLayout llLoading,llLoadError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        mBaseBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_base, null, false);
        bindingView = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, null, false);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        mBaseBinding.rlContainerLoad.addView(bindingView.getRoot());
        getWindow().setContentView(mBaseBinding.getRoot());

        //设置透明状态栏
        StatusBarUtil.setColor(this, CommonUtils.getColor(R.color.colorTheme), 0);
        llLoading = getView(R.id.ll_loading);//如果用mBaseBinding调用的话，会隐藏不了
        llLoadError = getView(R.id.ll_load_error);
        ImageView img = getView(R.id.iv_loading);
        //加载动画
        mAnimationDrawable = (AnimationDrawable) img.getDrawable();
        //默认进入页面就开启动画
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        setToolBar();
        llLoadError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();
                onRefresh();
            }
        });
        bindingView.getRoot().setVisibility(View.GONE);
    }

    protected void showLoading(){
        if (llLoading.getVisibility()!=View.VISIBLE){
            llLoading.setVisibility(View.VISIBLE);
        }
        if (!mAnimationDrawable.isRunning()){
            mAnimationDrawable.start();
        }
        if (bindingView.getRoot().getVisibility()!=View.GONE){
            bindingView.getRoot().setVisibility(View.GONE);
        }
        if (llLoadError.getVisibility()!=View.GONE){
            llLoadError.setVisibility(View.GONE);
        }
    }

    protected void showLoadError(){
        if (llLoading.getVisibility()!=View.GONE){
            llLoading.setVisibility(View.GONE);
        }
        if (mAnimationDrawable.isRunning()){
            mAnimationDrawable.stop();
        }
        if (bindingView.getRoot().getVisibility()!=View.GONE){
            bindingView.getRoot().setVisibility(View.GONE);
        }
        if (llLoadError.getVisibility()!=View.VISIBLE){
            llLoadError.setVisibility(View.VISIBLE);
        }
    }

    protected void showLoadSuccess(){
        if (llLoading.getVisibility()!=View.GONE){
            llLoading.setVisibility(View.GONE);
        }
        if (mAnimationDrawable.isRunning()){
            mAnimationDrawable.stop();
        }
        if (bindingView.getRoot().getVisibility()!=View.VISIBLE){
            bindingView.getRoot().setVisibility(View.VISIBLE);
        }
        if (llLoadError.getVisibility()!=View.GONE){
            llLoadError.setVisibility(View.GONE);
        }
    }

    protected  void onRefresh(){

    }

    public void setTitle(CharSequence charSequence){
        mBaseBinding.toolBar.setTitle(charSequence);
    }
    protected void setToolBar() {
        setSupportActionBar(mBaseBinding.toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除title的默认显示
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.icon_back);
        }
        mBaseBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    protected <T extends View> T getView(int viewID) {
        return (T) findViewById(viewID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription!=null&&mCompositeSubscription.hasSubscriptions()){
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Subscription subscription){
        if (mCompositeSubscription == null){
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    public void removeSubscription(){
        if (mCompositeSubscription!=null&&mCompositeSubscription.hasSubscriptions()){
            mCompositeSubscription.unsubscribe();
        }
    }
}
