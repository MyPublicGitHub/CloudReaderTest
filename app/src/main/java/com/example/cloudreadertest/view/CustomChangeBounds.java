package com.example.cloudreadertest.view;

import android.animation.Animator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.ChangeBounds;
import android.transition.TransitionValues;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

/**
 * Created by 冯涛 on 2018/1/6.
 * E-mail:716774214@qq.com
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class CustomChangeBounds extends ChangeBounds {
    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        Animator animator = super.createAnimator(sceneRoot, startValues, endValues);
        if (startValues == null || endValues == null || animator == null) {
            return null;
        }
        animator.setDuration(500);
        animator.setInterpolator(AnimationUtils.loadInterpolator(sceneRoot.getContext(), android.R.interpolator.fast_out_slow_in));
        return animator;
    }
}
