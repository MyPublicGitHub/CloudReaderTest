package com.example.cloudreadertest.utils;

import android.widget.Toast;

import com.example.cloudreadertest.app.CloudReaderTestApplication;

/**
 * 作者：冯涛 on 2017/11/28 15:50
 * <p>
 * 邮箱：716774214@qq.com
 */
public class ToastUtil {
    private static Toast mToast;
    public static void showToast(String msg){
        if (mToast == null){
            mToast = Toast.makeText(CloudReaderTestApplication.getInstance(),"",Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }
}
