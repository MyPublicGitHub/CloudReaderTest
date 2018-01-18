package com.example.cloudreadertest.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 冯涛 on 2018/1/18.
 * E-mail:716774214@qq.com
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<?> mFragmets;
    private List<String> mTitle;

    /**
     * 普通主页使用
     */
    public MyFragmentPagerAdapter(FragmentManager fm, List<?> fragmets) {
        super(fm);
        mFragmets = fragmets;
    }

    /**
     * 接收传递的标题
     */
    public MyFragmentPagerAdapter(FragmentManager fm, List<?> fragmets, List<String> title) {
        super(fm);
        mFragmets = fragmets;
        mTitle = title;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) mFragmets.get(position);
    }

    @Override
    public int getCount() {
        return mFragmets.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    /**
     * 首页显示title，每日推荐等..
     * 若有问题，移到对应单独页面
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle == null ? "" : mTitle.get(position);
    }
}
