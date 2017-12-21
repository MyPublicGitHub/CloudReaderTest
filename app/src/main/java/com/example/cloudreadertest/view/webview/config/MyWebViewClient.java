package com.example.cloudreadertest.view.webview.config;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.cloudreadertest.view.webview.WebViewActivity;
import com.example.http.utils.CheckNetwork;

/**
 * 监听网页链接:
 * - 优酷视频直接跳到自带浏览器
 * - 根据标识:打电话、发短信、发邮件
 * - 进度条的显示
 * - 添加javascript监听
 */

public class MyWebViewClient extends WebViewClient {

    private IWebPageView mIWebPageView;
    private WebViewActivity mActivity;

    public MyWebViewClient(IWebPageView iWebPageView) {
        mIWebPageView = iWebPageView;
        mActivity = (WebViewActivity) iWebPageView;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //优酷视频跳转到浏览器播放
        if (url.startsWith("http://v.youku.com/")) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addCategory("android.intent.category.BROWSABLE");
            Uri uri = Uri.parse(url);
            intent.setData(uri);
            mActivity.startActivity(intent);
            return true;
        } else if (url.startsWith(WebView.SCHEME_TEL) || url.startsWith(WebView.SCHEME_MAILTO) || url.startsWith("sms:")) {//电话短信邮箱
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mActivity.startActivity(intent);
            } catch (ActivityNotFoundException e) {

            }
            return true;
        }
        mIWebPageView.startsProgress();
        view.loadUrl(url);
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (mActivity.mProgress90) {
            mIWebPageView.hindProgressBar();
        } else {
            mActivity.mPageFinish = true;
        }
        if (!CheckNetwork.isNetworkConnected(mActivity)){
            mIWebPageView.hindProgressBar();
        }
        //html加载完成后添加图片的点击JS函数
        mIWebPageView.addImageClickListener();
        super.onPageFinished(view, url);
    }

    //视频全屏播放按返回页面被放大的问题
    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
        if (newScale - oldScale>7){
            view.setInitialScale((int)(oldScale/newScale*100));//异常放大，缩小回去
        }
    }
}
