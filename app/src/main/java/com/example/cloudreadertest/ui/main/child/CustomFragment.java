package com.example.cloudreadertest.ui.main.child;


import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.cocosw.bottomsheet.BottomSheet;
import com.example.cloudreadertest.R;
import com.example.cloudreadertest.adapter.CustomAdapter;
import com.example.cloudreadertest.app.Constants;
import com.example.cloudreadertest.base.BaseFragment;
import com.example.cloudreadertest.bean.GankIODataBean;
import com.example.cloudreadertest.databinding.FragmentCustomBinding;
import com.example.cloudreadertest.databinding.HeaderItemGankCustomBinding;
import com.example.cloudreadertest.http.RequestImplements;
import com.example.cloudreadertest.http.cache.Cache;
import com.example.cloudreadertest.model.GankOtherModel;
import com.example.cloudreadertest.utils.DebugUtil;
import com.example.cloudreadertest.utils.SPUtils;
import com.example.http.HttpUtils;
import com.example.xrecyclerview.XRecyclerView;

import rx.Subscription;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomFragment extends BaseFragment<FragmentCustomBinding> {
    Cache mCache;
    private int mPage = 1;
    private boolean mIsPrepared;
    private boolean mIsFirst = true;
    private GankOtherModel mModel;
    private GankIODataBean mAllBean;
    private String mType = "all";
    private CustomAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        mIsPrepared = true;
    }

    private void initData() {
        mCache = Cache.get(getContext());
        mType = SPUtils.getString(Constants.CUSTOM_CALA_TYPE, "all");
        mAdapter = new CustomAdapter();
        initXRV(bindingView.xrvCustom);
    }

    @Override
    protected void loadData() {
        if (mIsPrepared && mIsPrepared && mIsFirst) {
            if (mAllBean == null || mAllBean.results == null || mAllBean.results.size() == 0) {
                loadCustomData();
            } else {
                mAllBean = (GankIODataBean) mCache.getAsObject(Constants.GANK_CUSTOM);
                setAdapter(mAllBean);
            }
        }
    }

    private void loadCustomData() {
        mModel = new GankOtherModel(mType, HttpUtils.per_page_more, mPage);
        mModel.getGankIOData(new RequestImplements() {
            @Override
            public void loadSuccess(Object object) {
                showLoadSuccess();
                GankIODataBean gankIODataBean = (GankIODataBean) object;
                if (mPage == 1) {
                    if (gankIODataBean != null && gankIODataBean.results != null && gankIODataBean.results.size() > 0) {
                        setAdapter(gankIODataBean);
                        mCache.remove(Constants.GANK_CUSTOM);
                        mCache.put(Constants.GANK_CUSTOM, gankIODataBean, 30000);
                    }
                } else {
                    if (gankIODataBean != null && gankIODataBean.results != null && gankIODataBean.results.size() > 0) {
                        bindingView.xrvCustom.refreshComplete();
                        mAdapter.addAll(gankIODataBean.results);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        bindingView.xrvCustom.noMoreLoading();
                    }
                }
            }

            @Override
            public void loadFailed() {
                showLoadSuccess();
                bindingView.xrvCustom.refreshComplete();
                if (mAdapter.getItemCount() == 0) {
                    showLoadError();
                }
                if (mPage > 1) {
                    mPage--;
                }
            }

            @Override
            public void addSubscription(Subscription subscription) {
                CustomFragment.this.addSubscription(subscription);
            }
        });
    }

    HeaderItemGankCustomBinding mHeaderCustomBinding;

    private void setAdapter(GankIODataBean gankIODataBean) {
        if (mHeaderCustomBinding == null) {
            mHeaderCustomBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.header_item_gank_custom, null, false);
            bindingView.xrvCustom.addHeaderView(mHeaderCustomBinding.getRoot());
        }
        initHeader();

        boolean isAll = SPUtils.getString(Constants.CUSTOM_CALA_TYPE, "all").equals("all");
        mAdapter.clear();
        mAdapter.setAllType(isAll);
        mAdapter.addAll(gankIODataBean.results);
        bindingView.xrvCustom.setLayoutManager(new LinearLayoutManager(getActivity()));
        bindingView.xrvCustom.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mIsFirst = false;
    }

    private void initHeader() {
        String gankCala = SPUtils.getString(Constants.CUSTOM_CALA_TYPE, "all");
        mHeaderCustomBinding.tvType.setText(gankCala);
        mHeaderCustomBinding.llRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BottomSheet.Builder(getContext(), R.style.BottomSheet_Dialog).title("选择分类")
                        .sheet(R.menu.menu_bottomsheet_custom).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case R.id.item_all:
                                changeType("all");
                                break;
                            case R.id.item_iOS:
                                changeType("iOS");
                                break;
                            case R.id.item_android:
                                changeType("Android");
                                break;
                            case R.id.item_resource:
                                changeType("拓展资源");
                                break;
                            case R.id.item_front:
                                changeType("前端");
                                break;
                            case R.id.item_app:
                                changeType("App");
                                break;
                            case R.id.item_restMovie:
                                changeType("休息视频");
                                break;
                            case R.id.item_recommend:
                                changeType("瞎推荐");
                                break;
                            case R.id.item_welfare:
                                changeType("福利");
                                break;
                        }
                    }
                }).show();
            }
        });
    }

    private void changeType(String selectType) {
        if (selectType.equals(mType)) {
            DebugUtil.debug("分类已经相同" + selectType);
        } else {
            bindingView.xrvCustom.reset();
            mHeaderCustomBinding.tvType.setText(selectType);
            mType = selectType;
            mPage = 1;
            mAdapter.clear();
            SPUtils.putString(Constants.CUSTOM_CALA_TYPE, selectType);
            showLoading();
            loadCustomData();
        }
    }

    private void initXRV(XRecyclerView xrv) {
        //xrv.setPullRefreshEnabled(false);
        //xrv.clearHeader();
        xrv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                loadCustomData();
            }

            @Override
            public void onLoadMore() {
                mPage++;
                loadCustomData();
            }
        });
    }

    @Override
    protected void onRefresh() {
        loadCustomData();
    }

    @Override
    public int setContentView() {
        return R.layout.fragment_custom;
    }

}
