package com.example.cloudreadertest.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by 筑库 on 2017/11/8.
 */

public class MainActivityPagerAdapter extends FragmentPagerAdapter {
    FragmentManager fragmentManager;
    ArrayList<Fragment> fragments;
    public MainActivityPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
    }
    public MainActivityPagerAdapter(FragmentManager fm, ArrayList fragment) {
        super(fm);
        fragmentManager = fm;
        fragments =  fragment;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
