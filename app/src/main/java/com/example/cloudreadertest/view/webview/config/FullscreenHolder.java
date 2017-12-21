package com.example.cloudreadertest.view.webview.config;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/12/20 0020.
 */

public class FullscreenHolder extends FrameLayout {

    public FullscreenHolder(Context context){
        super(context);
        setBackgroundColor(context.getResources().getColor(android.R.color.black));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
