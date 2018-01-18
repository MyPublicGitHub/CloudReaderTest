package com.example.cloudreadertest.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cloudreadertest.MainActivity;
import com.example.cloudreadertest.R;
import com.example.cloudreadertest.bean.BookBean.BooksBean;
import com.example.cloudreadertest.databinding.FooterItemBookCustomBinding;
import com.example.cloudreadertest.databinding.HeaderItemBookCustomBinding;
import com.example.cloudreadertest.databinding.ItemBookCustomBinding;
import com.example.cloudreadertest.ui.friends.child.BookDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 冯涛 on 2018/1/17.
 * E-mail:716774214@qq.com
 */

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MainActivity mContext;

    private int status = 1;
    public static final int LOAD_MORE = 0;
    public static final int LOAD_PULL_TO = 1;
    public static final int LOAD_NONE = 2;
    private static final int LOAD_END = 3;
    private static final int TYPE_TOP = -1;

    private static final int TYPE_FOOTER_BOOK = -2;
    private static final int TYPE_HEADER_BOOK = -3;
    private static final int TYPE_CONTENT_BOOK = -4;
    private List<BooksBean> list;

    public BookAdapter(Context context) {
        mContext = (MainActivity) context;
        list = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER_BOOK;
        } else if (position + 1 == getItemCount()) {
            return TYPE_FOOTER_BOOK;
        } else {
            return TYPE_CONTENT_BOOK;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER_BOOK) {
            HeaderItemBookCustomBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.header_item_book_custom, parent, false);
            return new HeaderViewHolder(binding.getRoot());
        } else if (viewType == TYPE_FOOTER_BOOK) {
            FooterItemBookCustomBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.footer_item_book_custom, parent, false);
            return new FooterViewHolder(binding.getRoot());
        } else {
            ItemBookCustomBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_book_custom, parent, false);
            return new BookViewHolder(binding.getRoot());
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bindItem();
        } else if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).bindItem();
        } else if (holder instanceof BookViewHolder) {
            if (list != null && list.size() > 0) {
                ((BookViewHolder) holder).bindItem(list.get(position - 1), position - 1);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 2;
    }

    public List<BooksBean> getList() {
        return list;
    }

    public void setList(List<BooksBean> mList) {
        list.clear();
        list = mList;
    }

    public void addAll(List<BooksBean> mList) {
        list.addAll(mList);
    }

    public void updateLoadStatus(int status) {
        this.status = status;
        notifyDataSetChanged();
    }

    public int getLoadStatus() {
        return status;
    }

    /**
     * 处理 StaggeredGridLayoutManager 添加头尾布局占满屏幕宽的情况
     */
    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams
                && (isHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            params.setFullSpan(true);
        }
    }

    /**
     * 处理 GridLayoutManager 添加头尾布局占满屏幕宽的情况
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeader(position) || isFooter(position)) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * 这里规定 position = 0 时
     * 就为头布局，设置为占满整屏幕宽
     */
    private boolean isHeader(int position) {
        return position >= 0 && position < 1;
    }

    /**
     * 这里规定 position =  getItemCount() - 1时
     * 就为尾布局，设置为占满整屏幕宽
     * getItemCount() 改了 ，这里就不用改
     */
    private boolean isFooter(int position) {
        return position < getItemCount() && position >= getItemCount() - 1;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderItemBookCustomBinding mBinding;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.getBinding(itemView);
        }

        private void bindItem() {
        }
    }

    private class BookViewHolder extends RecyclerView.ViewHolder {
        ItemBookCustomBinding binding;

        public BookViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.getBinding(itemView);
        }

        private void bindItem(final BooksBean bean, int position) {
            binding.setBean(bean);
            binding.executePendingBindings();
            binding.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BookDetailActivity.start(mContext, bean, binding.ivImage);
                }
            });
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterItemBookCustomBinding binding;

        public FooterViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.getBinding(itemView);
        }

        private void bindItem() {
            switch (status) {
                case LOAD_MORE:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.tvLoadPrompt.setText("正在加载...");
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_PULL_TO:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.tvLoadPrompt.setText("上拉加载更多");
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_NONE:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.tvLoadPrompt.setText("没有更多内容了");
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_END:
                    itemView.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
