package com.example.cloudreadertest.view.webview.config;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.example.cloudreadertest.utils.DebugUtil;
import com.example.cloudreadertest.utils.ToastUtil;

/**
 * Created by Administrator on 2017/12/20 0020.
 */

public class ImageClickInterface {
    private Context mContext;

    public ImageClickInterface(Context context){
        mContext = context;
    }

    @JavascriptInterface
    public void imageClick(String imgUrl, String hasLink) {
//        Toast.makeText(context, "----点击了图片", Toast.LENGTH_SHORT).show();
        // 查看大图
//        Intent intent = new Intent(context, ViewBigImageActivity.class);
//        context.startActivity(intent);
        DebugUtil.debug("----点击了图片 url: ", "" + imgUrl);
    }

    @JavascriptInterface
    public void textClick(String type, String item_pk) {
        if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(item_pk)) {
            ToastUtil.showToast("----点击了文字");
        }
    }
}
