package com.example.cloudreadertest.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.base.baseadapter.BaseRecyclerViewAdapter;
import com.example.cloudreadertest.base.baseadapter.BaseRecyclerViewHolder;
import com.example.cloudreadertest.bean.GankIODataBean;
import com.example.cloudreadertest.databinding.ItemCustomBinding;
import com.example.cloudreadertest.utils.ImageLoadUtils;
import com.example.cloudreadertest.view.webview.WebViewActivity;

/**
 * Created by Administrator on 2017/12/22 0022.
 */

public class CustomAdapter extends BaseRecyclerViewAdapter<GankIODataBean.ResultsBean> {

    private boolean isAll = false;

    public void setAllType(boolean isAllType) {
        isAll = isAllType;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_custom);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<GankIODataBean.ResultsBean, ItemCustomBinding> {


        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(final GankIODataBean.ResultsBean bean, int position) {
            if ("福利".equals(bean.type)) {
                binding.ivOnlyImage.setVisibility(View.VISIBLE);
                binding.llOther.setVisibility(View.GONE);
                ImageLoadUtils.displayEspImage(binding.ivOnlyImage, bean.url, 1);
            } else {
                binding.ivOnlyImage.setVisibility(View.GONE);
                binding.llOther.setVisibility(View.VISIBLE);
            }
            if (isAll) {
                binding.tvContent.setVisibility(View.VISIBLE);
                binding.tvContent.setText(" · " + bean.type);
            } else {
                binding.tvContent.setVisibility(View.GONE);
            }
            binding.setBean(bean);
            binding.executePendingBindings();
            //显示git很消耗内存
            if (bean.images != null && bean.images.size() > 0 && !TextUtils.isEmpty(bean.images.get(0))) {
                binding.ivImage.setVisibility(View.VISIBLE);
                ImageLoadUtils.displayGif(bean.images.get(0), binding.ivImage);
            } else {
                binding.ivImage.setVisibility(View.GONE);
            }
            binding.llAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewActivity.loadUrl(v.getContext(), bean.url, "加载中...");
                }
            });
        }
    }
}
