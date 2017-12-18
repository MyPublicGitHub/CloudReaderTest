package com.example.cloudreadertest.view.webview;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.databinding.ActivityWebViewBinding;
import com.example.cloudreadertest.utils.CommonUtils;
import com.example.cloudreadertest.utils.ToastUtil;
import com.example.cloudreadertest.view.statusbar.StatusBarUtil;

public class WebViewActivity extends AppCompatActivity {

    private ActivityWebViewBinding mBinding;
    private String mTitle;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);
        getIntentData();
        initTitle();
        initWebView();
        mBinding.webView.loadUrl(mUrl);
    }

    private void initWebView(){
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

//        mWebChromeClient = new MyWebChromeClient(this);
//        webView.setWebChromeClient(mWebChromeClient);
//        // 与js交互
//        mBinding.webView.addJavascriptInterface(new ImageClickInterface(this), "injectedObject");
//        mBinding.webView.setWebViewClient(new MyWebViewClient(this));
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
        mBinding.toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.mipmap.actionbar_more));
        mBinding.toolbar.setNavigationOnClickListener(onClickListener);
        mBinding.toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

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
}
