package com.example.cloudreadertest.utils;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.cloudreadertest.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 筑库 on 2017/11/9.
 */

public class ImageLoadUtils {

    //圆角
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

    /**
     *
     * @param imageView
     * @param url
     * @param defaultPicType 电影：0；妹子：1； 书籍：2
     */
    @BindingAdapter({"android:displayFadeImage","android:defaultPicType"})
    public static void displayFadeImage(ImageView imageView,String url,int defaultPicType){
        displayEspImage(imageView, url, defaultPicType);
    }

    /**
     * 区别显示默认图片
     * @param imageView
     * @param url
     * @param defaultPicType 由这个参数辨别默认图片显示什么
     */
    public static void displayEspImage(ImageView imageView,String url,int defaultPicType){
        Glide.with(imageView.getContext())
                .load(url)
                .error(getDefaultPic(defaultPicType))
                .placeholder(getDefaultPic(defaultPicType))
                .into(imageView);
    }

    public static int getDefaultPic(int defaultPicType){
        switch (defaultPicType){
            case 0://电影
                return R.mipmap.img_default_movie;
            case 1://妹子
                return R.mipmap.img_default_meizi;
            case 2://书籍
                return R.mipmap.img_default_book;
        }
        return R.mipmap.img_default_movie;
    }

    /**
     * 保存图片到系统相册
     *
     * @param context
     * @param bmp
     */
    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        if (bmp == null) {
            ToastUtil.showToast( "保存出错了");
            return;
        }
        File appDir = new File(Environment.getExternalStorageDirectory(), "CouldReaderTest");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            ToastUtil.showToast( "FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            ToastUtil.showToast( "IOException");
            e.printStackTrace();
        } catch (Exception e) {
            ToastUtil.showToast( "Exception");
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        ToastUtil.showToast( "保存成功");
        DebugUtil.debug("已保存至" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/云阅相册 ");
        // 最后通知图库更新
        //context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        //更新系统图库的方法
        //context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
    }

}
