package com.example.cloudreadertest.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.cloudreadertest.R;
import com.youth.banner.loader.ImageLoader;

/**
 * 作者：冯涛 on 2017/12/4 10:26
 * <p>  banner的加载
 * 邮箱：716774214@qq.com
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .error(R.mipmap.img_two_bi_one)
                .placeholder(R.mipmap.img_two_bi_one)
                .crossFade(100)
                .into(imageView);
    }
}
