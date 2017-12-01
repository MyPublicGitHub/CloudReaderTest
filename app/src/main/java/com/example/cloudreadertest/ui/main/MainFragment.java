package com.example.cloudreadertest.ui.main;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.adapter.MainFragmentPagerAdapter;
import com.example.cloudreadertest.base.BaseFragment;
import com.example.cloudreadertest.databinding.FragmentMainBinding;
import com.example.cloudreadertest.ui.main.child.AndroidFragment;
import com.example.cloudreadertest.ui.main.child.CustomFragment;
import com.example.cloudreadertest.ui.main.child.EverydayFragment;
import com.example.cloudreadertest.ui.main.child.WelfareFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment<FragmentMainBinding> {


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFragments();
    }

    @Override
    public int setContentView() {
        return R.layout.fragment_main;
    }

    private void initFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> tabs = new ArrayList<>();
        fragments.add(new EverydayFragment());
        fragments.add(new WelfareFragment());
        fragments.add(new CustomFragment());
        fragments.add(new AndroidFragment());
        tabs.add("推荐");
        tabs.add("福利");
        tabs.add("干货");
        tabs.add("安卓");
        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getChildFragmentManager(), fragments, tabs);
        bindingView.viewPager.setAdapter(adapter);
        bindingView.tabLayout.setupWithViewPager(bindingView.viewPager);
        showLoadSuccess();
    }

}
