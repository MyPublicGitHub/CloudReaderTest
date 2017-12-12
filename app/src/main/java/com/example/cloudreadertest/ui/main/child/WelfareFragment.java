package com.example.cloudreadertest.ui.main.child;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.adapter.WelfareAdapter;
import com.example.cloudreadertest.app.Constants;
import com.example.cloudreadertest.base.BaseFragment;
import com.example.cloudreadertest.base.baseadapter.OnItemClickListener;
import com.example.cloudreadertest.bean.GankIODataBean;
import com.example.cloudreadertest.databinding.FragmentWelfareBinding;
import com.example.cloudreadertest.http.cache.Cache;
import com.example.cloudreadertest.http.rx.RequestImplements;
import com.example.cloudreadertest.model.GankOtherModel;
import com.example.cloudreadertest.utils.DebugUtil;
import com.example.cloudreadertest.view.viewbigimage.ViewBigImageActivity;
import com.example.http.HttpUtils;
import com.example.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

import rx.Subscription;

/**
 * 福利.
 */
public class WelfareFragment extends BaseFragment<FragmentWelfareBinding> {

    private static final String TAG = "WelfareFragment";
    private boolean isPrepared = false;
    private boolean isFirst = true;
    private int mPage = 1;
    private WelfareAdapter welfareAdapter;

    @Override
    public int setContentView() {
        return R.layout.fragment_welfare;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cache = Cache.get(getContext());
        bindingView.xrvWelfare.setPullRefreshEnabled(false);
        bindingView.xrvWelfare.clearHeader();
        welfareAdapter = new WelfareAdapter();
        bindingView.xrvWelfare.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPage++;
                loadWelfareData();
            }
        });
        isPrepared = true;
    }

    @Override
    protected void loadData() {
        if (!mIsVisible || !isPrepared || !isFirst) {
            return;
        }
        if (meiziBean != null && meiziBean.results != null && meiziBean.results.size() > 0) {
            imageList.clear();
            for (int i = 0; i < meiziBean.results.size(); i++) {
                imageList.add(meiziBean.results.get(i).url);
            }
            meiziBean = (GankIODataBean) cache.getAsObject(Constants.GANK_MEIZI);
            setAdapter(meiziBean);
            showLoadSuccess();
        } else {
            loadWelfareData();
        }
    }

    GankIODataBean meiziBean;
    private Cache cache;

    private void loadWelfareData() {
        GankOtherModel mModel = new GankOtherModel("福利", HttpUtils.per_page_more, mPage);
        mModel.getGankIOData(new RequestImplements() {
            @Override
            public void loadSuccess(Object object) {
                DebugUtil.debug(TAG, "loadSuccess");
                GankIODataBean bean = (GankIODataBean) object;
                if (mPage == 1) {
                    if (bean.results != null && bean.results.size() > 0 && bean != null) {
                        imageList.clear();
                        //将联网获取的图片集合保存到imagelist 用于传递到大图片页面
                        for (int i = 0; i < bean.results.size(); i++) {
                            imageList.add(bean.results.get(i).url);
                        }
                        setAdapter(bean);
                        cache.remove(Constants.GANK_MEIZI);
                        cache.put(Constants.GANK_MEIZI, bean, 30000);
                    }
                } else {
                    if (bean.results != null && bean.results.size() > 0 && bean != null) {
                        bindingView.xrvWelfare.refreshComplete();
                        welfareAdapter.addAll(bean.results);
                        welfareAdapter.notifyDataSetChanged();
                        for (int i = 0; i < bean.results.size(); i++) {
                            imageList.add(bean.results.get(i).url);
                        }
                    } else {
                        bindingView.xrvWelfare.noMoreLoading();
                    }
                }
                showLoadSuccess();
            }

            @Override
            public void loadFailed() {
                DebugUtil.debug(TAG, "loadFailed");
                showLoadError();
                bindingView.xrvWelfare.refreshComplete();
                if (welfareAdapter.getItemCount() == 0) {
                    showLoadError();
                }
                if (mPage > 1) {
                    mPage--;
                }
            }

            @Override
            public void addSubscription(Subscription subscription) {
                WelfareFragment.this.addSubscription(subscription);
            }
        });
    }

    @Override
    protected void onRefresh() {
        loadWelfareData();
    }

    ArrayList<String> imageList = new ArrayList<>();

    private void setAdapter(GankIODataBean bean) {
        if (welfareAdapter == null) {
            welfareAdapter = new WelfareAdapter();
        }
        welfareAdapter.addAll(bean.results);
        //构造器中，第一个参数表示列数或者行数，第二个参数表示滑动方向,瀑布流，给RecyclerView设置行数和滑动方向
        bindingView.xrvWelfare.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        bindingView.xrvWelfare.setAdapter(welfareAdapter);
        welfareAdapter.notifyDataSetChanged();

        welfareAdapter.setOnItemClickListener(new OnItemClickListener<GankIODataBean.ResultsBean>() {
            @Override
            public void onItemClick(GankIODataBean.ResultsBean resultsBean, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("selet", 2);//1,头像不显示页码，2大图显示页码
                bundle.putInt("position", position);//第几张
                bundle.putStringArrayList("imageuri", imageList);
                Intent intent = new Intent(getContext(), ViewBigImageActivity.class);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });

        isFirst = false;
    }
}
