package com.example.cloudreadertest.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.base.baseadapter.BaseRecyclerViewAdapter;
import com.example.cloudreadertest.base.baseadapter.BaseRecyclerViewHolder;
import com.example.cloudreadertest.bean.HotMovieBean.SubjectsBean;
import com.example.cloudreadertest.databinding.ItemTopBinding;
import com.example.cloudreadertest.ui.music.OneMovieDetailActivity;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * Created by 冯涛 on 2018/1/4.
 */

public class DouBanTopAdapter extends BaseRecyclerViewAdapter<SubjectsBean> {
    private Activity mActivity;
    public DouBanTopAdapter(Activity activity){
        mActivity = activity;
    }
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_top);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<SubjectsBean, ItemTopBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(final SubjectsBean bean, int position) {
            if (bean != null) {
                binding.setBean(bean);
                /**
                 * 当数据改变时，binding会在下一帧去改变数据，如果我们需要立即改变，就去调用executePendingBindings方法。
                 */
                ViewHelper.setScaleX(itemView, 0.8f);//布局是linaerlayout的时候回出错
                ViewHelper.setScaleY(itemView, 0.8f);
                ViewPropertyAnimator.animate(itemView).scaleX(1).setDuration(1000).setInterpolator(new OvershootInterpolator()).start();
                ViewPropertyAnimator.animate(itemView).scaleY(1).setDuration(1000).setInterpolator(new OvershootInterpolator()).start();
                binding.executePendingBindings();
                binding.llItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OneMovieDetailActivity.start(mActivity, bean, binding.imageView);
                    }
                });
                binding.llItem.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        //TODO
                        return false;
                    }
                });
            }
        }
    }
}
