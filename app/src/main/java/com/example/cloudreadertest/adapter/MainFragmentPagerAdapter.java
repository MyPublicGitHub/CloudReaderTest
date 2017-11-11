package com.example.cloudreadertest.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by 筑库 on 2017/11/10.
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
    private ArrayList<String> tabs;
    public MainFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragment,ArrayList<String> tab) {
        super(fm);
        fragments = fragment;
        tabs= tab;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position);
    }

    public void setTabs(ArrayList<String> tabs) {
        this.tabs = tabs;
    }
}
