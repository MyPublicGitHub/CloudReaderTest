package com.example.cloudreadertest.view.webview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.FrameLayout;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.databinding.ActivityWebViewBinding;
import com.example.cloudreadertest.utils.CommonUtils;
import com.example.cloudreadertest.utils.ToastUtil;
import com.example.cloudreadertest.view.statusbar.StatusBarUtil;
import com.example.cloudreadertest.view.webview.config.FullscreenHolder;
import com.example.cloudreadertest.view.webview.config.IWebPageView;
import com.example.cloudreadertest.view.webview.config.ImageClickInterface;
import com.example.cloudreadertest.view.webview.config.MyWebChromeClient;
import com.example.cloudreadertest.view.webview.config.MyWebViewClient;

public class WebViewActivity extends AppCompatActivity implements IWebPageView {

    private ActivityWebViewBinding mBinding;
    private String mTitle;
    private String mUrl;

    public boolean mProgress90;//进度条是否走到90%
    public boolean mPageFinish;//页面是否加载完成
    private MyWebChromeClient mWebChromeClient;
    private FrameLayout flVideoFullView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);
        flVideoFullView = findViewById(R.id.fl_video_fullView);
        getIntentData();
        initTitle();
        initWebView();
        mBinding.webView.loadUrl(mUrl);
    }

    private void initWebView() {
        mBinding.progressBar.setVisibility(View.VISIBLE);
        WebSettings settings = mBinding.webView.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        settings.setLoadWithOverviewMode(false);
        // 保存表单数据
        settings.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        // 启动应用缓存
        settings.setAppCacheEnabled(true);
        // 设置缓存模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        settings.setUseWideViewPort(true);
        // 缩放比例 1
        mBinding.webView.setInitialScale(1);
        // 告诉WebView启用JavaScript执行。默认的是false。
        settings.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        settings.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        settings.setDomStorageEnabled(true);
        // 排版适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否支持多个窗口。
        settings.setSupportMultipleWindows(true);

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)*/
        settings.setTextZoom(100);

        mWebChromeClient = new MyWebChromeClient(this);
        mBinding.webView.setWebChromeClient(mWebChromeClient);
        // 与js交互
        mBinding.webView.addJavascriptInterface(new ImageClickInterface(this), "injectedObject");
        mBinding.webView.setWebViewClient(new MyWebViewClient(this));
    }

    private void initTitle() {
        StatusBarUtil.setColor(this, CommonUtils.getColor(R.color.colorTheme), 0);
        setSupportActionBar(mBinding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.icon_back);
        }
        mBinding.toolbar.setTitle(mTitle);
        mBinding.toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        mBinding.toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.mipmap.actionbar_more));
        mBinding.toolbar.setNavigationOnClickListener(onClickListener);
        mBinding.toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };
    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_share:
                    ToastUtil.showToast("item_share");
                    break;
                case R.id.item_cope:

                    break;
                case R.id.item_open:

                    break;
            }
            return false;
        }
    };

    private void getIntentData() {
        if (getIntent() != null) {
            mTitle = getIntent().getStringExtra("title");
            mUrl = getIntent().getStringExtra("url");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }

    public FrameLayout getVideoFullView() {
        return flVideoFullView;
    }

    @Override
    public void hindProgressBar() {
        mBinding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showWebView() {
        mBinding.webView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hindWebView() {
        mBinding.webView.setVisibility(View.GONE);
    }

    @Override
    public void startsProgress() {
        startProgress90();
    }

    @Override
    public void progressChanged(int newProgress) {
        if (mProgress90) {
            int progress = newProgress * 100;
            if (progress > 900) {
                mBinding.progressBar.setProgress(progress);
                if (progress == 1000) {
                    mBinding.progressBar.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void addImageClickListener() {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        // 如要点击一张图片在弹出的页面查看所有的图片集合,则获取的值应该是个图片数组
        mBinding.webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                //  "objs[i].onclick=function(){alert(this.getAttribute(\"has_link\"));}" +
                "objs[i].onclick=function(){window.injectedObject.imageClick(this.getAttribute(\"src\"),this.getAttribute(\"has_link\"));}" +
                "}" +
                "})()");

        // 遍历所有的a节点,将节点里的属性传递过去(属性自定义,用于页面跳转)
        mBinding.webView.loadUrl("javascript:(function(){" +
                "var objs =document.getElementsByTagName(\"a\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                "objs[i].onclick=function(){" +
                "window.injectedObject.textClick(this.getAttribute(\"type\"),this.getAttribute(\"item_pk\"));}" +
                "}" +
                "})()");
    }

    @Override
    public void fullViewAddView(View view) {
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        flVideoFullView = new FullscreenHolder(this);
        flVideoFullView.addView(view);
        decor.addView(flVideoFullView);
    }

    @Override
    public void showVideoFullView() {
        flVideoFullView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hindViewPullView() {
        flVideoFullView.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MyWebChromeClient.FILECHOOSER_RESULTCODE) {
            mWebChromeClient.mUploadMessage(data, resultCode);
        } else if (requestCode == MyWebChromeClient.FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            mWebChromeClient.mUploadMessageForAndroid5(data, resultCode);
        }
    }

    /**
     * 全屏是按返回最退出全屏方法
     */
    public void hindCustomView() {
        mWebChromeClient.onHideCustomView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (mWebChromeClient.inCustomView()) {//全屏播放退出全屏
                hindCustomView();
                return true;
            } else if (mBinding.webView.canGoBack()) {//可以返回就返回
                mBinding.webView.goBack();
                return true;
            } else {
                mBinding.webView.loadUrl("about:blank");
                finish();
            }
        }
        return false;
    }

    public void startProgress90() {
        for (int i = 0; i < 900; i++) {
            final int progress = i + 1;
            mBinding.progressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.progressBar.setProgress(progress);
                    if (progress == 900) {
                        mProgress90 = true;
                        if (mPageFinish) {
                            startProgress90To100();
                        }
                    }
                }
            }, (i + 1) * 2);
        }
    }

    public void startProgress90To100() {
        for (int i = 900; i < 1000; i++) {
            final int progress = i + 1;
            mBinding.progressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.progressBar.setProgress(progress);
                    if (progress == 1000) {
                        mBinding.progressBar.setVisibility(View.GONE);
                    }
                }
            }, (i + 1) * 2);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.webView.onResume();
        //支付宝网页版再打开文章详情后，无法点击按钮下一步
        mBinding.webView.resumeTimers();
        //设置为横屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flVideoFullView.removeAllViews();
        if (mBinding.webView != null) {
            ViewGroup viewGroup = (ViewGroup) mBinding.webView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(flVideoFullView);
            }
            mBinding.webView.removeAllViews();
            mBinding.webView.loadUrl("about:blank");
            mBinding.webView.stopLoading();
            mBinding.webView.setWebChromeClient(null);
            mBinding.webView.setWebViewClient(null);
            mBinding.webView.destroy();
//            mBinding.webView = null;
        }
    }

    /**
     * 打开网页
     *
     * @param context
     * @param url
     * @param title
     */
    public static void loadUrl(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }
}
