package com.example.cloudreadertest.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.ArcMotion;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.cloudreadertest.R;
import com.example.cloudreadertest.databinding.BaseHeaderTitleBarBinding;
import com.example.cloudreadertest.utils.CommonUtils;
import com.example.cloudreadertest.view.CustomChangeBounds;
import com.example.cloudreadertest.view.MyNestedScrollView;
import com.example.cloudreadertest.view.statusbar.StatusBarUtil;
import com.example.cloudreadertest.view.test.StatusBarUtils;

import java.lang.reflect.Method;

import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseHeaderActivity<HV extends ViewDataBinding, SV extends ViewDataBinding> extends AppCompatActivity {

    protected HV bindingHeaderView;//头布局
    protected SV bindingContentView;//内容布局
    protected BaseHeaderTitleBarBinding bindingTitleView;//标题
    private LinearLayout llLoading, llLoadError;
    private CompositeSubscription mCompositeSubscription;
    protected AnimationDrawable mAnimationDrawable;
    private int imageBgHeight;//高斯背景的高度
    private int slidingDistancs;//滑动多少后标题不透明

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        View root = getLayoutInflater().inflate(R.layout.activity_base_header, null);
        //头部
        bindingHeaderView = DataBindingUtil.inflate(getLayoutInflater(), setHeaderLayout(), null, false);
        RelativeLayout.LayoutParams header = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bindingHeaderView.getRoot().setLayoutParams(header);
        RelativeLayout rlHeaderContainer = root.findViewById(R.id.rl_header_container);
        rlHeaderContainer.addView(bindingHeaderView.getRoot());
        getWindow().setContentView(root);
        //标题
        bindingTitleView = DataBindingUtil.inflate(getLayoutInflater(), R.layout.base_header_title_bar, null, false);
        RelativeLayout.LayoutParams title = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bindingTitleView.getRoot().setLayoutParams(title);
        RelativeLayout rlTitle = root.findViewById(R.id.rl_title);
        rlTitle.addView(bindingTitleView.getRoot());
        getWindow().setContentView(root);
        //内容
        bindingContentView = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, null, false);
        RelativeLayout.LayoutParams content = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingContentView.getRoot().setLayoutParams(content);
        RelativeLayout rlContainer = root.findViewById(R.id.rl_container);
        rlContainer.addView(bindingContentView.getRoot());
        getWindow().setContentView(root);

        llLoadError = getView(R.id.ll_load_error);
        llLoading = getView(R.id.ll_loading);
        //设置自定义元素共享动画
        setMotion(setHeaderPicView(), false);
        //初始化滑动渐变
        initSlideShapeTheme(setHeaderImgUrl(), setHeaderImageView());
        //设置toolbar
        setToolBar();
        ImageView imageView = getView(R.id.iv_loading);
        //加载动画
        mAnimationDrawable = (AnimationDrawable) imageView.getDrawable();
        //默认进入页面就加载动画
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        //点击页面加载出错
        llLoadError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();
                onRefresh();
            }
        });
        bindingContentView.getRoot().setVisibility(View.GONE);
    }

    protected void onRefresh() {
    }

    protected void setToolBar() {
        setSupportActionBar(bindingTitleView.tbBaseTitle);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认的title显示
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.icon_back);
        }
        bindingTitleView.tbBaseTitle.setTitleTextAppearance(this, R.style.ToolBar_Title);
        bindingTitleView.tbBaseTitle.setSubtitleTextAppearance(this, R.style.Toolbar_SubTitle);
        bindingTitleView.tbBaseTitle.inflateMenu(R.menu.menu_base_header);
        bindingTitleView.tbBaseTitle.setOverflowIcon(ContextCompat.getDrawable(this, R.mipmap.actionbar_more));
        bindingTitleView.tbBaseTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        bindingTitleView.tbBaseTitle.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_more:
                        setTitleClickMore();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 1.设置标题
     */
    public void setTitle(CharSequence title) {
        bindingTitleView.tbBaseTitle.setTitle(title);
    }

    /**
     * 2.设置副标题
     */
    protected void setSubtitle(CharSequence title) {
        bindingTitleView.tbBaseTitle.setSubtitle(title);
    }

    /**
     * 3.toolbar单击更多
     */
    protected void setTitleClickMore() {

    }

    /**
     * 设置自定义 Shared Element切换动画
     * 默认不开启曲线路径切换动画，
     * 开启需要重写setHeaderPicView()，和调用此方法并将isShow值设为true
     *
     * @param imageView
     * @param isShow
     */
    protected void setMotion(ImageView imageView, boolean isShow) {
        if (isShow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //自定义ArcMotion
                ArcMotion arcMotion = new ArcMotion();
                arcMotion.setMinimumHorizontalAngle(50f);
                arcMotion.setMinimumVerticalAngle(50f);
                //插值器，控制速度
                Interpolator interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in);
                //实例化自定义的ChangeBounds
                CustomChangeBounds customChangeBounds = new CustomChangeBounds();
                customChangeBounds.setPathMotion(arcMotion);
                customChangeBounds.setInterpolator(interpolator);
                customChangeBounds.addTarget(imageView);
                //将切换动画应用到当前的Activity的进入和返回
                getWindow().setSharedElementEnterTransition(customChangeBounds);
                getWindow().setSharedElementReturnTransition(customChangeBounds);
            }
        }
    }

    /**
     * 设置头部header布局 左侧的图片（需要设置曲线路径切换动画时重写）
     */
    protected ImageView setHeaderPicView() {
        return new ImageView(this);
    }

    /**
     * *** 初始化滑动渐变 一定要实现 ******
     *
     * @param imgUrl    header头部的高斯背景imageUrl
     * @param mHeaderBg header头部高斯背景ImageView控件
     */
    protected void initSlideShapeTheme(String imgUrl, ImageView mHeaderBg) {
        setImageHeaderBg(imgUrl);
        //toolbar的高
        int toolbarHeight = bindingTitleView.tbBaseTitle.getLayoutParams().height;
        final int headerBgHeight = toolbarHeight + StatusBarUtil.getStatusBarHeight(this);
        //使背景图向上移动到图片的最低端，保留titleBar+statusBar的高度
        ViewGroup.LayoutParams params = bindingTitleView.ivBaseTitleBarBg.getLayoutParams();
        ViewGroup.MarginLayoutParams ivTitleHeadBgParams = (ViewGroup.MarginLayoutParams) bindingTitleView.ivBaseTitleBarBg.getLayoutParams();
        int marginTop = params.height - headerBgHeight;
        ivTitleHeadBgParams.setMargins(0, -marginTop, 0, 0);
        bindingTitleView.ivBaseTitleBarBg.setImageAlpha(0);
        StatusBarUtils.setTranslucentImageHeader(this, 0, bindingTitleView.tbBaseTitle);
        //上滑背景图片，使空白状态栏消失（这样下方就空了状态栏的高度）
        if (mHeaderBg != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mHeaderBg.getLayoutParams();
            layoutParams.setMargins(0, -StatusBarUtil.getStatusBarHeight(this), 0, 0);
            ViewGroup.LayoutParams imageItemBgParams = mHeaderBg.getLayoutParams();
            //获得高斯背景的高度
            imageBgHeight = imageItemBgParams.height;
        }

        //变色
        initScrollViewListener();
        initNewSlidingParams();
    }

    private void initScrollViewListener() {
        //为了兼容23以下
        ((MyNestedScrollView) findViewById(R.id.mns_base)).setOnScrollChangeLintener(new MyNestedScrollView.ScrollInterface() {
            @Override
            public void onScrollChange(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                scrollChangeHeader(scrollY);
            }
        });
    }

    private void scrollChangeHeader(int scrollY) {
        if (scrollY < 0) {
            scrollY = 0;
        }
        float alpha = Math.abs(scrollY) * 1.0f / (slidingDistancs);
        Drawable drawable = bindingTitleView.ivBaseTitleBarBg.getDrawable();
        if (drawable != null) {
            if (scrollY <= slidingDistancs) {
                //title部分的渐变
                drawable.mutate().setAlpha((int) alpha * 255);
                bindingTitleView.ivBaseTitleBarBg.setImageDrawable(drawable);
            } else {
                drawable.mutate().setAlpha(255);
                bindingTitleView.ivBaseTitleBarBg.setImageDrawable(drawable);
            }

        }
    }

    private void initNewSlidingParams() {
        int titleBarAndStatusHeight = (int) (CommonUtils.getDimens(R.dimen.nav_bar_height) + StatusBarUtil.getStatusBarHeight(this));
        //减掉后，没到顶部就不透明了
        slidingDistancs = imageBgHeight - titleBarAndStatusHeight - (int) (CommonUtils.getDimens(R.dimen.nav_bar_height));
    }

    private void setImageHeaderBg(String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl)) {
            //高斯模糊背景 原来 参数 12，5  23，4
            Glide.with(this).load(imgUrl)
                    .error(R.mipmap.stackblur_default)
                    .bitmapTransform(new BlurTransformation(this, 23, 4)).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    bindingTitleView.tbBaseTitle.setBackgroundColor(Color.TRANSPARENT);
                    bindingTitleView.ivBaseTitleBarBg.setImageAlpha(0);
                    bindingTitleView.ivBaseTitleBarBg.setVisibility(View.VISIBLE);
                    return false;
                }
            }).into(bindingTitleView.ivBaseTitleBarBg);
        }
    }

    protected void showLoading() {
        if (llLoading.getVisibility() != View.VISIBLE) {
            llLoading.setVisibility(View.VISIBLE);
        }
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        if (bindingContentView.getRoot().getVisibility() != View.GONE) {
            bindingContentView.getRoot().setVisibility(View.GONE);
        }
        if (llLoadError.getVisibility() != View.GONE) {
            llLoadError.setVisibility(View.GONE);
        }
    }

    protected void showLoadError() {
        if (llLoading.getVisibility() != View.GONE) {
            llLoading.setVisibility(View.GONE);
        }
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (bindingContentView.getRoot().getVisibility() != View.GONE) {
            bindingContentView.getRoot().setVisibility(View.GONE);
        }
        if (llLoadError.getVisibility() != View.VISIBLE) {
            llLoadError.setVisibility(View.VISIBLE);
        }
    }

    protected void showLoadSuccess() {
        if (llLoading.getVisibility() != View.GONE) {
            llLoading.setVisibility(View.GONE);
        }
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (bindingContentView.getRoot().getVisibility() != View.VISIBLE) {
            bindingContentView.getRoot().setVisibility(View.VISIBLE);
        }
        if (llLoadError.getVisibility() != View.GONE) {
            llLoadError.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base_header, menu);
        return true;
    }

    /**
     * 显示pop内的图片
     */
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible",Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu,true);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    public void addSubscription(Subscription subscription){
        if (mCompositeSubscription==null){
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    public void removeSubscription(Subscription subscription){
        if (mCompositeSubscription!=null&&mCompositeSubscription.hasSubscriptions()){
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription!=null&&mCompositeSubscription.hasSubscriptions()){
            mCompositeSubscription.unsubscribe();
        }
    }

    /**
     * a.布局高斯透明图header布局
     */
    protected abstract int setHeaderLayout();

    /**
     * b.设置头部header高斯背景 imageUrl
     */
    protected abstract String setHeaderImgUrl();

    /**
     * c.设置头部header布局 高斯背景ImageView控件
     */
    protected abstract ImageView setHeaderImageView();

}
