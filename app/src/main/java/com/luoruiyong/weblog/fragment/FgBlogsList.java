package com.luoruiyong.weblog.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luoruiyong.weblog.R;
import com.luoruiyong.weblog.adapter.BlogsListAdapter;
import com.luoruiyong.weblog.model.Blog;
import com.luoruiyong.weblog.util.LogUtil;

import java.util.ArrayList;

/**微博列表
 * Created by Administrator on 2017/9/15.
 */

public class FgBlogsList extends Fragment implements BlogsListAdapter.OnItemClickListener,BlogsListAdapter.OnBindViewHolderListener {

    private static final String CLASS_NAME = FgBlogsList.class.getSimpleName()+"-->";
    private View view;
    private RecyclerView rv_blogs_list;
    private BlogsListAdapter adapter;
    private OnItemClickListener itemClickListener;
    private OnBindViewHolderListener bindViewHolderListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_blogs_list,container,false);
        init();
        return  view;
    }

    private void init() {
        rv_blogs_list = view.findViewById(R.id.blogs_list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        rv_blogs_list.setLayoutManager(manager);
        adapter = new BlogsListAdapter(getContext());
        adapter.setOnItemClickListener(this);
        adapter.setOnBindViewHolderListener(this);
        rv_blogs_list.setAdapter(adapter);
    }

    public void initData( ArrayList<Blog> blogsList){
        adapter.setBlogsList(blogsList);
        adapter.notifyDataSetChanged();
        LogUtil.d(CLASS_NAME+"加载数据到碎片中");
    }

    public void notifyDataChange(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            itemClickListener = (OnItemClickListener) context;
            bindViewHolderListener = (OnBindViewHolderListener) context;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if(itemClickListener != null){
            LogUtil.d(CLASS_NAME+"用户产生点击事件，回传到Activity");
            itemClickListener.onItemClick(view,position);
        }
    }

    @Override
    public void bindViewHolder(int position) {
        if(bindViewHolderListener != null){
            LogUtil.d(CLASS_NAME+"绑定ViewHolder事件，回传到Activity");
            bindViewHolderListener.bindViewHolder(position);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public interface OnBindViewHolderListener {
        void bindViewHolder(int position);
    }
}
