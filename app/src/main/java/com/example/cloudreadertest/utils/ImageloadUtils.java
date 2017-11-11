package com.example.cloudreadertest.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.cloudreadertest.R;

/**
 * Created by 筑库 on 2017/11/9.
 */

public class ImageloadUtils {

    public static void displayCircle(ImageView imageView,String url){
        Glide.with(imageView.getContext())
                .load(url)
                .crossFade(500)
                .error(R.mipmap.ic_avatar_default)
                .transform(new GlideCircleTransformUtils(imageView.getContext()))
                .into(imageView);
    }
    public static void displayCircle(ImageView imageView,Integer resId){
        Glide.with(imageView.getContext())
                .load(resId)
                .crossFade(500)
                .error(R.mipmap.ic_avatar_default)
                .transform(new GlideCircleTransformUtils(imageView.getContext()))
                .into(imageView);
    }
}
