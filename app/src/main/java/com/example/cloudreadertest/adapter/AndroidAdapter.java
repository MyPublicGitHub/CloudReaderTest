package com.example.cloudreadertest.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.base.baseadapter.BaseRecyclerViewAdapter;
import com.example.cloudreadertest.base.baseadapter.BaseRecyclerViewHolder;
import com.example.cloudreadertest.bean.GankIODataBean;
import com.example.cloudreadertest.databinding.ItemAndroidBinding;
import com.example.cloudreadertest.utils.ImageLoadUtils;
import com.example.cloudreadertest.view.webview.WebViewActivity;

/**
 * Created by 冯涛 on 2018/1/19.
 * E-mail:716774214@qq.com
 */

public class AndroidAdapter extends BaseRecyclerViewAdapter<GankIODataBean.ResultsBean> {
    private boolean isAll = false;

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_android);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<GankIODataBean.ResultsBean, ItemAndroidBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(final GankIODataBean.ResultsBean object, int position) {
            if (isAll && "福利".equals(object.type)) {
                binding.ivImageOnly.setVisibility(View.VISIBLE);
                binding.llOther.setVisibility(View.GONE);
                ImageLoadUtils.displayEspImage(binding.ivImageOnly, object.url, 1);
            } else {
                binding.ivImageOnly.setVisibility(View.GONE);
                binding.llOther.setVisibility(View.VISIBLE);
            }

            if (isAll){
                binding.tvType.setVisibility(View.VISIBLE);
                binding.tvType.setText("·"+object.type);
            }else {
                binding.tvType.setVisibility(View.GONE);
            }
            binding.setBean(object);
            if (object.images!=null&&object.images.size()>0&&!TextUtils.isEmpty(object.images.get(0))){
                binding.ivPic.setVisibility(View.VISIBLE);
                ImageLoadUtils.displayGif(object.images.get(0),binding.ivPic);
            }else {
                binding.ivPic.setVisibility(View.GONE);
            }
            binding.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WebViewActivity.loadUrl(view.getContext(),object.url,"加载中...");
                }
            });
        }
    }
}
