package com.example.cloudreadertest.base.baseadapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 筑库 on 2017/11/25.
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    protected List<T> datas = new ArrayList<>();
    protected OnItemClickListener<T> onClick;
    protected OnItemLongClickListener<T> onLongClick;

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {

        holder.onBindViewHolder(datas.get(position),position);

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public List<T> getData(){
        return datas;
    }

    public void addAll(List<T> data){
        datas.addAll(data);
    }

    public void add(T t){
        datas.add(t);
    }

    public void clear(){
        datas.clear();
    }

    public void remove(T t){
        datas.remove(t);
    }

    public void remove(int inder){
        datas.remove(inder);
    }

    public void removeAll(List<T> data){
        datas.removeAll(data);
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener){
        onClick = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> listener){
        onLongClick = listener;
    }
}
