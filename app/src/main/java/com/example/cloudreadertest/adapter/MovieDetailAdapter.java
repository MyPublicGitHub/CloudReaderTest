package com.example.cloudreadertest.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.base.baseadapter.BaseRecyclerViewAdapter;
import com.example.cloudreadertest.base.baseadapter.BaseRecyclerViewHolder;
import com.example.cloudreadertest.bean.MovieDetailBean.DirectorsBean;
import com.example.cloudreadertest.databinding.ItemMovieDetailBinding;
import com.example.cloudreadertest.view.webview.WebViewActivity;

/**
 * Created by 冯涛 on 2018/1/17.
 * E-mail:716774214@qq.com
 */

public class MovieDetailAdapter extends BaseRecyclerViewAdapter<DirectorsBean> {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_movie_detail);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<DirectorsBean, ItemMovieDetailBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(final DirectorsBean object, int position) {
            binding.setDirectorsBean(object);
            binding.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (object != null && !TextUtils.isEmpty(object.alt)) {
                        WebViewActivity.loadUrl(view.getContext(), object.alt, object.name);
                    }
                }
            });
        }
    }
}
