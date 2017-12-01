package com.example.cloudreadertest.view.viewbigimage;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 作者：冯涛 on 2017/11/27 17:30
 * <p>
 * 邮箱：716774214@qq.com
 */
public class HackyViewPager extends ViewPager {
    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        try {
            return super.onInterceptHoverEvent(event);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}
