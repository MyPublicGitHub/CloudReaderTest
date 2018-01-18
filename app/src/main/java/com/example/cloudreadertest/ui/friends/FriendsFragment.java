package com.example.cloudreadertest.ui.friends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.base.BaseFragment;
import com.example.cloudreadertest.databinding.FragmentFriendsBinding;
import com.example.cloudreadertest.ui.friends.child.BookCustomFragment;
import com.example.cloudreadertest.view.MyFragmentPagerAdapter;

import java.util.ArrayList;

public class FriendsFragment extends BaseFragment<FragmentFriendsBinding> {

    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showLoading();
        initFragmentList();
        /**
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻2个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), mFragmentList, mTitleList);
        bindingView.vpBook.setAdapter(adapter);
        //左右预加载的个数
        bindingView.vpBook.setOffscreenPageLimit(2);
        adapter.notifyDataSetChanged();
        bindingView.tlBook.setTabMode(TabLayout.MODE_FIXED);
        bindingView.tlBook.setupWithViewPager(bindingView.vpBook);
        showLoadSuccess();
    }

    private void initFragmentList() {
        mTitleList.add("文学");
        mTitleList.add("文化");
        mTitleList.add("生活");
        mFragmentList.add(BookCustomFragment.newInstance("文学"));
        mFragmentList.add(BookCustomFragment.newInstance("文化"));
        mFragmentList.add(BookCustomFragment.newInstance("生活"));
    }

    @Override
    public int setContentView() {
        return R.layout.fragment_friends;
    }
}
