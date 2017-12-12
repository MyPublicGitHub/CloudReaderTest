package com.example.cloudreadertest.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.example.cloudreadertest.app.CloudReaderTestApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by 筑库 on 2017/11/6.
 * 获取原生资源
 */

public class CommonUtils {
    /**
     * 随机颜色
     */
    public static int randomColor(){
        Random random = new Random();
        int red = random.nextInt(150)+50;
        int green = random.nextInt(150)+50;
        int blue = random.nextInt(150)+50;
        return Color.rgb(red,green,blue);
    }
    /**
     * 获取年月日中的‘日’
     */
    public static String getDateForDay(){
        Date date = new Date();
        SimpleDateFormat simple = new SimpleDateFormat("dd");
        return simple.format(date);
    }
    /**
     * 获取屏幕PX
     */
    public static int getSrceenPixels(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static Drawable setDrawable(int resId){
        return getResources().getDrawable(resId);
    }

    public static Resources getResources(){
        return CloudReaderTestApplication.getInstance().getResources();
    }


}
