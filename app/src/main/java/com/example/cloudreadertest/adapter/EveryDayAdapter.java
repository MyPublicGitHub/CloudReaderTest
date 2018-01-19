package com.example.cloudreadertest.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cloudreadertest.R;
import com.example.cloudreadertest.base.baseadapter.BaseRecyclerViewAdapter;
import com.example.cloudreadertest.base.baseadapter.BaseRecyclerViewHolder;
import com.example.cloudreadertest.bean.GankIODayBean.ResultsBean.AndroidBean;
import com.example.cloudreadertest.databinding.ItemEverydayOneBinding;
import com.example.cloudreadertest.databinding.ItemEverydayThreeBinding;
import com.example.cloudreadertest.databinding.ItemEverydayTitleBinding;
import com.example.cloudreadertest.databinding.ItemEverydayTwoBinding;
import com.example.cloudreadertest.http.rx.RxBus;
import com.example.cloudreadertest.http.rx.RxCodeConstants;
import com.example.cloudreadertest.utils.CommonUtils;
import com.example.cloudreadertest.utils.DebugUtil;
import com.example.cloudreadertest.utils.ImageLoadUtils;
import com.example.cloudreadertest.utils.PerfectClickListener;
import com.example.cloudreadertest.view.webview.WebViewActivity;

import java.util.List;

/**
 * 作者：冯涛 on 2017/12/4 17:41
 * <p>
 * 邮箱：716774214@qq.com
 */
public class EveryDayAdapter extends BaseRecyclerViewAdapter<List<AndroidBean>> {
    private static final int TYPE_TITLE = 1; // title
    private static final int TYPE_ONE = 2;// 一张图
    private static final int TYPE_TWO = 3;// 二张图
    private static final int TYPE_THREE = 4;// 三张图

    @Override
    public int getItemViewType(int position) {
        if (!TextUtils.isEmpty(getData().get(position).get(0).type_title)) {
            return TYPE_TITLE;
        } else if (getData().get(position).size() == 1) {
            return TYPE_ONE;
        } else if (getData().get(position).size() == 2) {
            return TYPE_TWO;
        } else if (getData().get(position).size() == 3) {
            return TYPE_THREE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TITLE:
                return new TitleHolder(parent, R.layout.item_everyday_title);
            case TYPE_ONE:
                return new OneHolder(parent, R.layout.item_everyday_one);
            case TYPE_TWO:
                return new TwoHolder(parent, R.layout.item_everyday_two);
            default:
                return new ThreeHolder(parent, R.layout.item_everyday_three);
        }
    }

    private class TitleHolder extends BaseRecyclerViewHolder<List<AndroidBean>, ItemEverydayTitleBinding> {

        public TitleHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(List<AndroidBean> object, int position) {
            int index = 0;
            String title = object.get(0).type_title;
            binding.tvTitle.setText(title);
            if ("Android".equals(title)) {
                index = 0;
                binding.ivTitle.setImageDrawable(CommonUtils.setDrawable(R.mipmap.home_title_android));
            } else if ("福利".equals(title)) {
                index = 1;
                binding.ivTitle.setImageDrawable(CommonUtils.setDrawable(R.mipmap.home_title_meizi));
            } else if ("iOS".equals(title)) {
                index = 2;
                binding.ivTitle.setImageDrawable(CommonUtils.setDrawable(R.mipmap.home_title_ios));
            } else if ("休息视频".equals(title)) {
                index = 2;
                binding.ivTitle.setImageDrawable(CommonUtils.setDrawable(R.mipmap.home_title_movie));
            } else if ("拓展资源".equals(title)) {
                index = 2;
                binding.ivTitle.setImageDrawable(CommonUtils.setDrawable(R.mipmap.home_title_source));
            } else if ("瞎推荐".equals(title)) {
                index = 2;
                binding.ivTitle.setImageDrawable(CommonUtils.setDrawable(R.mipmap.home_title_xia));
            } else if ("前端".equals(title)) {
                index = 2;
                binding.ivTitle.setImageDrawable(CommonUtils.setDrawable(R.mipmap.home_title_qian));
            } else if ("App".equals(title)) {
                index = 2;
                binding.ivTitle.setImageDrawable(CommonUtils.setDrawable(R.mipmap.home_title_app));
            }
            if (position == 0) {
                binding.viewLine.setVisibility(View.GONE);
            } else {
                binding.viewLine.setVisibility(View.VISIBLE);
            }

            final int finalIndex = index;
            binding.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DebugUtil.debug("点击更多");
                    RxBus.getDefault().post(RxCodeConstants.JUMP_TYPE, finalIndex);
                }
            });
        }
    }

    private class OneHolder extends BaseRecyclerViewHolder<List<AndroidBean>, ItemEverydayOneBinding> {

        public OneHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(List<AndroidBean> object, int position) {
            AndroidBean bean = object.get(0);
            if ("福利".equals(bean.type)) {
                binding.ivContent.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(binding.ivContent.getContext())
                        .load(bean.url)
                        .crossFade(1500)
                        .placeholder(R.mipmap.img_two_bi_one)
                        .error(R.mipmap.img_two_bi_one)
                        .into(binding.ivContent);
            } else {
                ImageLoadUtils.displayRandom(1, bean.image_url, binding.ivContent);
            }
            binding.tvContent.setText(object.get(0).desc);
            setOnClick(binding.llContent, bean);
        }
    }

    private class TwoHolder extends BaseRecyclerViewHolder<List<AndroidBean>, ItemEverydayTwoBinding> {

        public TwoHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(List<AndroidBean> object, int position) {
            AndroidBean bean = object.get(0);
            AndroidBean bean1 = object.get(1);
            ImageLoadUtils.displayRandom(2, bean.image_url, binding.ivLeft);
            ImageLoadUtils.displayRandom(2, bean1.image_url, binding.ivRight);
            binding.tvLeft.setText(bean.desc);
            binding.tvRight.setText(bean1.desc);
            setOnClick(binding.llLeft, bean);
            setOnClick(binding.llRight, bean1);
        }
    }

    private class ThreeHolder extends BaseRecyclerViewHolder<List<AndroidBean>, ItemEverydayThreeBinding> {

        public ThreeHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(List<AndroidBean> object, int position) {
            AndroidBean bean = object.get(0);
            AndroidBean bean1 = object.get(1);
            AndroidBean bean2 = object.get(2);
            ImageLoadUtils.displayRandom(3, bean.image_url, binding.ivLeft);
            ImageLoadUtils.displayRandom(3, bean1.image_url, binding.ivCenter);
            ImageLoadUtils.displayRandom(3, bean2.image_url, binding.ivRight);
            binding.tvLeft.setText(bean.desc);
            binding.tvCenter.setText(bean1.desc);
            binding.tvRight.setText(bean2.desc);
            setOnClick(binding.llLeft, bean);
            setOnClick(binding.llCenter, bean1);
            setOnClick(binding.llRight, bean2);

        }
    }

    private void setOnClick(final LinearLayout linearLayout, final AndroidBean bean) {
        linearLayout.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                WebViewActivity.loadUrl(view.getContext(), bean.url, bean.desc);
            }
        });
        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                View v = View.inflate(view.getContext(), R.layout.title_douban_top, null);
                TextView textView = v.findViewById(R.id.tv_title);
                textView.setTextSize(14);
                textView.setText(TextUtils.isEmpty(bean.type) ? bean.desc : bean.type + "：" + bean.desc);
                builder.setCustomTitle(v);
                builder.setPositiveButton("查看详情", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        WebViewActivity.loadUrl(linearLayout.getContext(), bean.url, bean.desc);
                    }
                });
                builder.show();
                return false;
            }
        });
    }
}
