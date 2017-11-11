package com.example.cloudreadertest.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;

import com.bumptech.glide.Glide;
import com.example.cloudreadertest.MainActivity;
import com.example.cloudreadertest.R;
import com.example.cloudreadertest.app.ConstantsImageUrl;
import com.example.cloudreadertest.databinding.ActivityTransitionBinding;
import com.example.cloudreadertest.utils.CommonUtils;

import java.util.Random;

public class TransitionActivity extends AppCompatActivity {
    private ActivityTransitionBinding mBinding;
    private boolean animationEnd;
    private boolean isIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_transition);

        int i = new Random().nextInt(ConstantsImageUrl.TRANSITION_URLS.length);
        mBinding.ivDefaultPic.setImageDrawable(CommonUtils.getDrawable(R.mipmap.img_transition_default));

        Glide.with(this)
                .load(ConstantsImageUrl.TRANSITION_URLS[i])
                .placeholder(R.mipmap.img_transition_default)
                .error(R.mipmap.ic_launcher)
                .into(mBinding.ivPic);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinding.ivDefaultPic.setVisibility(View.GONE);
            }
        }, 1500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toMainActivity();
            }
        }, 3000);

        mBinding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMainActivity();
            }
        });

    }

    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            onAnimtionEnd();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private void onAnimtionEnd(){
        synchronized (this){
            if (animationEnd==false){
                animationEnd = true;
                mBinding.ivPic.clearAnimation();
                toMainActivity();
            }
        }
    }

    private void toMainActivity(){
        if (isIn){
            return;
        }
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.screen_zoom_in,R.anim.screen_zoom_out);
        finish();
        isIn = true;
    }
}
