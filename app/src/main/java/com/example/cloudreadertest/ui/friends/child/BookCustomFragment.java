package com.example.cloudreadertest.ui.friends.child;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.cloudreadertest.MainActivity;
import com.example.cloudreadertest.R;
import com.example.cloudreadertest.adapter.BookAdapter;
import com.example.cloudreadertest.base.BaseFragment;
import com.example.cloudreadertest.bean.BookBean;
import com.example.cloudreadertest.databinding.FragmentBookCustomBinding;
import com.example.cloudreadertest.http.HttpClient;
import com.example.cloudreadertest.utils.CommonUtils;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class BookCustomFragment extends BaseFragment<FragmentBookCustomBinding> {
    private static final String TYPE = "param1";
    private MainActivity mActivity;
    private boolean mIsPrepared, mIsFirst = true;//是否准备好了
    private String mType = "综合";
    private int mStart = 0;//开始请求的下标
    private int mCount = 18;//没次请求的数量
    private GridLayoutManager mLayoutManager;
    private BookAdapter mAdapter;

    public static BookCustomFragment newInstance(String param1) {
        BookCustomFragment bookCustomFragment = new BookCustomFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, param1);
        bookCustomFragment.setArguments(bundle);
        return bookCustomFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindingView.srlBook.setColorSchemeColors(CommonUtils.getColor(R.color.colorTheme));
        bindingView.srlBook.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bindingView.srlBook.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mStart = 0;
                        loadCustomData();
                    }
                }, 1000);
            }
        });
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        bindingView.rvBook.setLayoutManager(mLayoutManager);
        bindingView.rvBook.addOnScrollListener(mOnScrollListener);
        mIsPrepared = true;
        /**
         * 因为启动时先走loadData()再走onActivityCreated，
         * 所以此处要额外调用load(),不然最初不会加载内容
         */
        loadData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    protected void loadData() {
        if (mIsPrepared && mIsVisible && mIsFirst) {
            bindingView.srlBook.setRefreshing(true);
            bindingView.srlBook.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadCustomData();
                }
            }, 500);
        }
    }

    private void loadCustomData() {
        Subscription subscription = HttpClient.Builder.getDouBanService().getBook(mType, mStart, mCount)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BookBean>() {
                    @Override
                    public void onCompleted() {
                        showLoadSuccess();
                        if (bindingView.srlBook.isRefreshing()) {
                            bindingView.srlBook.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showLoadSuccess();
                        if (bindingView.srlBook.isRefreshing()) {
                            bindingView.srlBook.setRefreshing(false);
                        }
                        if (mStart == 0) {
                            showLoadError();
                        }
                    }

                    @Override
                    public void onNext(BookBean bookBean) {
                        if (mStart == 0) {
                            if (bookBean != null && bookBean.books != null && bookBean.books.size() > 0) {
                                if (mAdapter == null) {
                                    mAdapter = new BookAdapter(getContext());
                                }
                                mAdapter.setList(bookBean.books);
                                mAdapter.notifyDataSetChanged();
                                bindingView.rvBook.setAdapter(mAdapter);
                            }
                            mIsFirst = false;
                        } else {
                            mAdapter.addAll(bookBean.books);
                            mAdapter.notifyDataSetChanged();
                        }
                        if (mAdapter != null) {
                            mAdapter.updateLoadStatus(BookAdapter.LOAD_PULL_TO);
                        }
                    }
                });
        addSubscription(subscription);
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        int lastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                if (mAdapter != null)
                    if (mLayoutManager.getItemCount() == 0) {
                        mAdapter.updateLoadStatus(BookAdapter.LOAD_NONE);
                        return;
                    }
                if (lastVisibleItem + 1 == mLayoutManager.getItemCount() && mAdapter.getLoadStatus() != BookAdapter.LOAD_MORE) {
                    mAdapter.updateLoadStatus(BookAdapter.LOAD_MORE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mStart += mCount;
                            loadCustomData();
                        }
                    }, 1000);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }
    };

    @Override
    protected void onRefresh() {
        bindingView.srlBook.setRefreshing(true);
        loadCustomData();
    }

    @Override
    public int setContentView() {
        return R.layout.fragment_book_custom;
    }

}
