package com.example.cloudreadertest.utils;

import android.view.View;
import android.view.View.OnClickListener;

import java.util.Calendar;

/**
 * 避免一秒内多次触发点击事件
 */

public abstract class PerfectClickListener implements OnClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    private int id = -1;

    @Override
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        int mId = view.getId();
        if (id!=mId){
            id = mId;
            lastClickTime = currentTime;
            onNoDoubleClick(view);
            return;
        }
        if (currentTime -lastClickTime>MIN_CLICK_DELAY_TIME){
            lastClickTime=currentTime;
            onNoDoubleClick(view);
        }
    }

    protected abstract void onNoDoubleClick(View view);
}
