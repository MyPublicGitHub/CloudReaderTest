package com.example.cloudreadertest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.base.baseadapter.BaseRecyclerViewAdapter;
import com.example.cloudreadertest.base.baseadapter.BaseRecyclerViewHolder;
import com.example.cloudreadertest.bean.GankIODataBean;
import com.example.cloudreadertest.databinding.ItemWelfareBinding;
import com.example.cloudreadertest.utils.DensityUtil;

/**
 * 作者：冯涛 on 2017/11/25 11:55
 * <p>
 * 邮箱：716774214@qq.com
 */
public class WelfareAdapter extends BaseRecyclerViewAdapter<GankIODataBean.ResultsBean> {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_welfare);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<GankIODataBean.ResultsBean,ItemWelfareBinding>{

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(final GankIODataBean.ResultsBean resultsBean,final int position) {
            /**
             * 注意：DensityUtil.setViewMargin(itemView,true,5,3,5,0);
             * 如果这样使用，则每个item的左右边距是不一样的，
             * 这样item不能复用，所以下拉刷新成功后显示会闪一下
             * 换成每个item设置上下左右边距是一样的话，系统就会复用，就消除了图片不能复用 闪跳的情况
             */
            if (position % 2 == 0){
                DensityUtil.setViewMargin(itemView,false,12,6,12,0);
            }else {
                DensityUtil.setViewMargin(itemView,false,6,12,12,0);
            }
            binding.setBean(resultsBean);

            //抖动
            binding.executePendingBindings();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClick!=null){
                        onClick.onItemClick(resultsBean,position);
                    }
                }
            });
        }
    }
}
