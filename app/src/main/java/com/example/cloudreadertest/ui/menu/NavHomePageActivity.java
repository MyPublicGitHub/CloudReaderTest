package com.example.cloudreadertest.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.databinding.ActivityNavHomePageBinding;
import com.example.cloudreadertest.utils.ShareUtils;
import com.example.cloudreadertest.view.statusbar.StatusBarUtil;

public class NavHomePageActivity extends AppCompatActivity {
    private ActivityNavHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nav_home_page);

        binding.collapsingToolbarLayout.setTitle(getString(R.string.app_name));
        StatusBarUtil.setTranslucentForImageView(this, 0, binding.toolbar);
        binding.fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtils.share(view.getContext(), R.string.string_share_text);
            }
        });
    }

    public static void startHome(Context mContext) {
        Intent intent = new Intent(mContext, NavHomePageActivity.class);
        mContext.startActivity(intent);
    }
}
