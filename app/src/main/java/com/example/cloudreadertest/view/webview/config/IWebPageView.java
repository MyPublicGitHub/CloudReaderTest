package com.example.cloudreadertest.view.webview.config;

import android.view.View;

/**
 * Created by Administrator on 2017/12/20 0020.
 */

public interface IWebPageView {
    //隐藏进度条
    void hindProgressBar();

    //显示WebView
    void showWebView();

    //隐藏WebView
    void hindWebView();

    //进度条先加载到90%再加载到100%
    void startsProgress();

    /**
     * 进度条变化时调用
     */
    void progressChanged(int newProgress);

    /**
     *添加js监听
     */
    void addImageClickListener();

    /**
     * 播放网络视频全屏调用
     */
    void fullViewAddView(View view);

    void showVideoFullView();

    void hindViewPullView();
}
