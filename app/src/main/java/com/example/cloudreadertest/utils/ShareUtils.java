package com.example.cloudreadertest.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.cloudreadertest.R;

/**
 * Created by 冯涛 on 2018/1/19.
 * E-mail:716774214@qq.com
 */

public class ShareUtils {
    public static void share(Context context,int stringRes){
        share(context,context.getString(stringRes));
    }

    public static void share(Context context,String string){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.action_share));
        intent.putExtra(Intent.EXTRA_TEXT, string);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(
                Intent.createChooser(intent, context.getString(R.string.action_share)));
    }

    public static void shareImage(Context context, Uri uri, String title) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(shareIntent, title));
    }
}
