package com.example.cloudreadertest.ui.music;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.adapter.MovieDetailAdapter;
import com.example.cloudreadertest.base.BaseHeaderActivity;
import com.example.cloudreadertest.bean.HotMovieBean.SubjectsBean;
import com.example.cloudreadertest.bean.MovieDetailBean;
import com.example.cloudreadertest.databinding.ActivityOneMovieDetailBinding;
import com.example.cloudreadertest.databinding.HeaderSlideShapeBinding;
import com.example.cloudreadertest.http.HttpClient;
import com.example.cloudreadertest.utils.CommonUtils;
import com.example.cloudreadertest.utils.DebugUtil;
import com.example.cloudreadertest.utils.StringFormatUtil;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OneMovieDetailActivity extends BaseHeaderActivity<HeaderSlideShapeBinding, ActivityOneMovieDetailBinding> {

    private SubjectsBean subjectsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_movie_detail);
        if (getIntent() != null) {
            subjectsBean = (SubjectsBean) getIntent().getSerializableExtra("bean");
        }
        initSlideShapeTheme(setHeaderImgUrl(), setHeaderImageView());
        setTitle(subjectsBean.title);
        setSubtitle(String.format("主演：", StringFormatUtil.formatName(subjectsBean.casts)));
        bindingHeaderView.setSubjectsBean(subjectsBean);
        bindingHeaderView.executePendingBindings();
        loadMovieDetail();
    }

    private void loadMovieDetail() {
        Subscription subscription = HttpClient.Builder.getDouBanService().getMovieDetail(subjectsBean.id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieDetailBean>() {
                    @Override
                    public void onCompleted() {
                        showLoadSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showLoadError();
                    }

                    @Override
                    public void onNext(MovieDetailBean movieDetailBean) {
                        bindingContentView.setBean(movieDetailBean);
                        bindingHeaderView.setBean(movieDetailBean);
                        bindingContentView.executePendingBindings();
                        transformDate(movieDetailBean);
                    }
                });
        addSubscription(subscription);
    }

    private void transformDate(final MovieDetailBean movieDetailBean) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < movieDetailBean.directors.size(); i++) {
                    movieDetailBean.directors.get(i).type = "导演";
                }
                for (int i = 0; i < movieDetailBean.casts.size(); i++) {
                    movieDetailBean.casts.get(i).type = "演员";
                }
                OneMovieDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAdapter(movieDetailBean);
                    }
                });
            }
        }).start();
    }

    /**
     * 导演和演员列表
     *
     * @param movieDetailBean
     */
    private void setAdapter(MovieDetailBean movieDetailBean) {
        bindingContentView.xrvMovieDetail.setVisibility(View.VISIBLE);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        bindingContentView.xrvMovieDetail.setLayoutManager(manager);
        bindingContentView.xrvMovieDetail.setPullRefreshEnabled(false);
        bindingContentView.xrvMovieDetail.setLoadingMoreEnabled(false);
        bindingContentView.xrvMovieDetail.setNestedScrollingEnabled(false);
        bindingContentView.xrvMovieDetail.setHasFixedSize(false);

        MovieDetailAdapter adapter = new MovieDetailAdapter();
        adapter.addAll(movieDetailBean.directors);
        adapter.addAll(movieDetailBean.casts);
        bindingContentView.xrvMovieDetail.setAdapter(adapter);
    }

    @Override
    protected void onRefresh() {
        loadMovieDetail();
    }

    @Override
    protected int setHeaderLayout() {
        return R.layout.header_slide_shape;
    }

    @Override
    protected String setHeaderImgUrl() {
        if (subjectsBean == null) {
            return "";
        }
        return subjectsBean.images.medium;
    }

    @Override
    protected ImageView setHeaderImageView() {
        return bindingHeaderView.ivBg;
    }

    public static void start(Activity context, SubjectsBean subjectsBean, ImageView imageView) {
        Intent intent = new Intent(context, OneMovieDetailActivity.class);
        intent.putExtra("bean", subjectsBean);//有些子类不序列化的话会报错
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(context, imageView, CommonUtils.getString(R.string.transition_movie_img));
        ActivityCompat.startActivity(context, intent, compat.toBundle());
    }
}
