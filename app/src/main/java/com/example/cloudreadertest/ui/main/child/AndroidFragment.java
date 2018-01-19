package com.example.cloudreadertest.ui.main.child;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.adapter.AndroidAdapter;
import com.example.cloudreadertest.app.Constants;
import com.example.cloudreadertest.base.BaseFragment;
import com.example.cloudreadertest.bean.GankIODataBean;
import com.example.cloudreadertest.databinding.FragmentAndroidBinding;
import com.example.cloudreadertest.http.RequestImplements;
import com.example.cloudreadertest.http.cache.Cache;
import com.example.cloudreadertest.model.GankOtherModel;
import com.example.http.HttpUtils;
import com.example.xrecyclerview.XRecyclerView;

import rx.Subscription;

/**
 * A simple {@link Fragment} subclass.
 */
public class AndroidFragment extends BaseFragment<FragmentAndroidBinding> {

    private static final String TYPE = "mType";
    private String mType = "Android";
    private Cache mCache;
    private GankOtherModel model;
    private int mPage = 1;
    private AndroidAdapter adapter;
    private boolean mIsPrepared, mIsFirst = true;
    private GankIODataBean androidBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCache = Cache.get(getContext());
        model = new GankOtherModel();
        adapter = new AndroidAdapter();
        bindingView.xrvAndroid.setLoadingListener(loadingListener);
        mIsPrepared = true;
    }

    @Override
    protected void loadData() {
        if (mIsPrepared && mIsFirst && mIsVisible) {
            if (androidBean != null && androidBean.results != null && androidBean.results.size() > 0) {
                showLoadSuccess();
                androidBean = (GankIODataBean) mCache.getAsObject(Constants.GANK_ANDROID);
                setAdapter(androidBean);
            } else {
                loadAndroidData();
            }
        }
    }

    private void loadAndroidData() {
        model.setData(mType, HttpUtils.per_page, mPage);
        model.getGankIOData(new RequestImplements() {
            @Override
            public void loadSuccess(Object object) {
                showLoadSuccess();
                GankIODataBean bean = (GankIODataBean) object;
                if (mPage == 1) {
                    if (bean != null && bean.results != null && bean.results.size() > 0) {
                        setAdapter(bean);
                        mCache.remove(Constants.GANK_ANDROID);
                        mCache.put(Constants.GANK_ANDROID, bean, 30000);
                    }
                } else {
                    if (bean != null && bean.results != null && bean.results.size() > 0) {
                        bindingView.xrvAndroid.refreshComplete();
                        adapter.addAll(bean.results);
                        adapter.notifyDataSetChanged();
                    } else {
                        bindingView.xrvAndroid.noMoreLoading();
                    }
                }
            }

            @Override
            public void loadFailed() {
                bindingView.xrvAndroid.refreshComplete();
                if (adapter.getItemCount() == 0) {
                    showLoadError();
                }
                if (mPage > 1) {
                    mPage--;
                }
            }

            @Override
            public void addSubscription(Subscription subscription) {
                AndroidFragment.this.addSubscription(subscription);
            }
        });
    }

    private void setAdapter(GankIODataBean bean) {
        adapter.clear();
        adapter.addAll(bean.results);
        bindingView.xrvAndroid.setLayoutManager(new LinearLayoutManager(getActivity()));
        bindingView.xrvAndroid.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        bindingView.xrvAndroid.refreshComplete();

        mIsFirst = false;
    }

    private XRecyclerView.LoadingListener loadingListener = new XRecyclerView.LoadingListener() {
        @Override
        public void onRefresh() {
            mPage = 1;
            loadAndroidData();
        }

        @Override
        public void onLoadMore() {
            mPage++;
            loadAndroidData();
        }
    };

    public static AndroidFragment newInstance(String type) {
        AndroidFragment fragment = new AndroidFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int setContentView() {
        return R.layout.fragment_android;
    }

}
