package com.example.cloudreadertest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.cloudreadertest.adapter.MainActivityPagerAdapter;
import com.example.cloudreadertest.app.ConstantsImageUrl;
import com.example.cloudreadertest.databinding.ActivityMainBinding;
import com.example.cloudreadertest.databinding.LayoutHeaderNavigationViewBinding;
import com.example.cloudreadertest.http.rx.RxBus;
import com.example.cloudreadertest.http.rx.RxBusBaseMessage;
import com.example.cloudreadertest.http.rx.RxCodeConstants;
import com.example.cloudreadertest.ui.friends.FriendsFragment;
import com.example.cloudreadertest.ui.main.MainFragment;
import com.example.cloudreadertest.ui.music.MusicFragment;
import com.example.cloudreadertest.utils.CommonUtils;
import com.example.cloudreadertest.utils.ImageLoadUtils;
import com.example.cloudreadertest.view.statusbar.StatusBarUtil;

import java.util.ArrayList;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private ActivityMainBinding mBinding;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initView();
        initRxBus();
        StatusBarUtil.setColorNoTranslucentForDrawerLayout(this, mBinding.drawerLayout, CommonUtils.getColor(R.color.colorTheme));
        initFragments();
        initListener();
    }

    private void initView() {
        View headerView = mBinding.navigationView.getHeaderView(0);
        LayoutHeaderNavigationViewBinding bind = DataBindingUtil.bind(headerView);
        ImageLoadUtils.displayCircle(bind.ivHeadPortrait, ConstantsImageUrl.IC_AVATAR);

    }

    private void initFragments() {
        fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        fragments.add(new MusicFragment());
        fragments.add(new FriendsFragment());
        MainActivityPagerAdapter adapter = new MainActivityPagerAdapter(getSupportFragmentManager(), fragments);
        mBinding.include.viewPager.setAdapter(adapter);
        mBinding.include.viewPager.setOffscreenPageLimit(2);
        mBinding.include.viewPager.addOnPageChangeListener(mOnPageChangeListener);
        mBinding.include.ivMain.setSelected(true);
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    mBinding.include.ivMain.setSelected(true);
                    mBinding.include.ivMusic.setSelected(false);
                    mBinding.include.ivFriend.setSelected(false);
                    break;
                case 1:
                    mBinding.include.ivMain.setSelected(false);
                    mBinding.include.ivMusic.setSelected(true);
                    mBinding.include.ivFriend.setSelected(false);
                    break;
                case 2:
                    mBinding.include.ivMain.setSelected(false);
                    mBinding.include.ivMusic.setSelected(false);
                    mBinding.include.ivFriend.setSelected(true);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void initListener() {
        mBinding.include.ivMain.setOnClickListener(this);
        mBinding.include.ivMusic.setOnClickListener(this);
        mBinding.include.ivFriend.setOnClickListener(this);
        mBinding.include.menu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                mBinding.drawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.iv_main:
                mBinding.include.ivMain.setSelected(true);
                mBinding.include.ivMusic.setSelected(false);
                mBinding.include.ivFriend.setSelected(false);
                mBinding.include.viewPager.setCurrentItem(0);
                break;
            case R.id.iv_music:
                mBinding.include.ivMain.setSelected(false);
                mBinding.include.ivMusic.setSelected(true);
                mBinding.include.ivFriend.setSelected(false);
                mBinding.include.viewPager.setCurrentItem(1);
                break;
            case R.id.iv_friend:
                mBinding.include.ivMain.setSelected(false);
                mBinding.include.ivMusic.setSelected(false);
                mBinding.include.ivFriend.setSelected(true);
                mBinding.include.viewPager.setCurrentItem(2);
                break;
        }
    }

    /**
     * 每日推荐点击“电影按钮”跳转
     */
    private void initRxBus() {
        RxBus.getDefault().toObservable(RxCodeConstants.JUMP_TYPE_TO_ONE, RxBusBaseMessage.class)
                .subscribe(new Action1<RxBusBaseMessage>() {
                    @Override
                    public void call(RxBusBaseMessage message) {
                        mBinding.include.viewPager.setCurrentItem(1);
                    }
                });
    }
}
