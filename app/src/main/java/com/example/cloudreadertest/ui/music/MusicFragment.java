package com.example.cloudreadertest.ui.music;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.cloudreadertest.MainActivity;
import com.example.cloudreadertest.R;
import com.example.cloudreadertest.base.BaseFragment;
import com.example.cloudreadertest.databinding.FragmentMusicBinding;
import com.example.cloudreadertest.http.HttpClient;
import com.example.cloudreadertest.http.cache.Cache;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends BaseFragment<FragmentMusicBinding> {
    // 是否初始化完成      是否是第一次加载数据    是否正在刷新
    private boolean mIsPrepared = false,mIsFirst = true,mIsLoading;
    private Cache mCache;
    private MainActivity mMainActivity;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCache = Cache.get(getActivity());

        mIsPrepared = true;
    }

    @Override
    protected void loadData() {
        if (mIsPrepared&&mIsVisible){
            loadHotMovie();
        }
    }

    private void loadHotMovie(){
        Subscription subscription = HttpClient.Builder.getDouBanService().getHotMovie().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<HotMovieBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HotMovieBean hotMovieBean) {

                    }
                });
    }

    @Override
    public int setContentView() {
        return R.layout.fragment_music;
    }

}
