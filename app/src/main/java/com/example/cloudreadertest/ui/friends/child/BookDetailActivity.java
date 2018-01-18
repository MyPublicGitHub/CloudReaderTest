package com.example.cloudreadertest.ui.friends.child;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.widget.ImageView;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.base.BaseHeaderActivity;
import com.example.cloudreadertest.bean.BookBean.BooksBean;
import com.example.cloudreadertest.bean.BookDetailBean;
import com.example.cloudreadertest.databinding.ActivityBookDetailBinding;
import com.example.cloudreadertest.databinding.HeaderBookDetailBinding;
import com.example.cloudreadertest.http.HttpClient;
import com.example.cloudreadertest.utils.CommonUtils;
import com.example.cloudreadertest.view.webview.WebViewActivity;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BookDetailActivity extends BaseHeaderActivity<HeaderBookDetailBinding, ActivityBookDetailBinding> {

    private BooksBean booksBean;
    private String mBookDetailUrl, mBookDetailName;
    public static final String EXTRA_PARAM = "bookBean";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        if (getIntent() != null) {
            booksBean = (BooksBean) getIntent().getSerializableExtra(EXTRA_PARAM);
        }
        setMotion(setHeaderPicView(), true);
        initSlideShapeTheme(setHeaderImgUrl(), setHeaderImageView());
        setTitle(booksBean.title);
        setSubtitle("作者：" + booksBean.author);
        bindingHeaderView.setBooksBean(booksBean);
        bindingHeaderView.executePendingBindings();
        loadBookDetail();
    }

    private void loadBookDetail() {
        Subscription subscription = HttpClient.Builder.getDouBanService().getBookDetail(booksBean.id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BookDetailBean>() {
                    @Override
                    public void onCompleted() {
                        showLoadSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showLoadError();
                    }

                    @Override
                    public void onNext(BookDetailBean bookDetailBean) {
                        bindingContentView.setBookDetailBean(bookDetailBean);
                        mBookDetailName = bookDetailBean.title;
                        mBookDetailUrl = bookDetailBean.alt;
                        bindingContentView.executePendingBindings();
                    }
                });
        addSubscription(subscription);
    }

    @Override
    protected void onRefresh() {
        loadBookDetail();
    }

    @Override
    protected void setTitleClickMore() {
        WebViewActivity.loadUrl(this, mBookDetailUrl, mBookDetailName);
    }

    @Override
    protected int setHeaderLayout() {
        return R.layout.header_book_detail;
    }

    @Override
    protected String setHeaderImgUrl() {
        return booksBean == null ? "" : booksBean.images.medium;
    }

    @Override
    protected ImageView setHeaderImageView() {
        return bindingHeaderView.ivBg;
    }

    @Override
    protected ImageView setHeaderPicView() {
        return bindingHeaderView.ivPic;
    }

    public static void start(Activity activity, BooksBean booksBean, ImageView imageView) {
        Intent intent = new Intent(activity, BookDetailActivity.class);
        intent.putExtra(EXTRA_PARAM, booksBean);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, imageView, CommonUtils.getString(R.string.transition_book_img));
        ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
    }
}
