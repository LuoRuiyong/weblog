package com.luoruiyong.weblog.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luoruiyong.weblog.R;
import com.luoruiyong.weblog.adapter.BlogsListAdapter;
import com.luoruiyong.weblog.model.Blog;
import com.luoruiyong.weblog.myview.PullRefreshViewGroup;
import com.luoruiyong.weblog.util.LogUtil;

import java.util.ArrayList;

/**微博列表
 * Created by Administrator on 2017/9/15.
 */

public class FgBlogsList extends Fragment implements BlogsListAdapter.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener{

    private static final String CLASS_NAME = FgBlogsList.class.getSimpleName()+"-->";
    private View view;
    private RecyclerView rv_blogs_list;
    private PullRefreshViewGroup vg_refresh;
    //private SwipeRefreshLayout srl_down_pull_refresh;
    private BlogsListAdapter adapter;
    private OnRefreshDataListener refreshDataListener;
    private OnItemClickListener itemClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_blogs_list,container,false);
        init();
        return  view;
    }

    private void init() {
        rv_blogs_list = view.findViewById(R.id.blogs_list);
        vg_refresh = view.findViewById(R.id.refresh);
        vg_refresh.setOnRefreshDataListener(getActivity());
        //srl_down_pull_refresh = view.findViewById(R.id.down_pull_refresh);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rv_blogs_list.setLayoutManager(manager);
        adapter = new BlogsListAdapter(getContext());
        adapter.setOnItemClickListener(this);
        //srl_down_pull_refresh.setOnRefreshListener(this);
        rv_blogs_list.setAdapter(adapter);
    }

    public void initData( ArrayList<Blog> blogsList){
        adapter.setBlogsList(blogsList);
        adapter.notifyDataSetChanged();
        LogUtil.d(CLASS_NAME+"加载数据到碎片中");
    }

    public void refreshDataSucceed(){
        vg_refresh.refreshDataSucceed();
    }

    public void refreshDataError(){
        vg_refresh.refreshDataError();
    }

    public void loadDataSucceed(){
        vg_refresh.loadDataSucceed();
    }

    public void loadDataError(){
        vg_refresh.loadDataError();
    }

    public void notifyDataChange(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            //绑定活动时，初始化接口
            itemClickListener = (OnItemClickListener) context;
            refreshDataListener = (OnRefreshDataListener) context;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 点击事件，精确到每个列表项中的子控件
     * @param view  事件源
     * @param position  事件源在当前列表中的位置
     */
    @Override
    public void onItemClick(View view, int position) {
        if(itemClickListener != null){
            itemClickListener.onItemClick(view,position);
        }
    }

    @Override
    public void onRefresh() {
        if(refreshDataListener != null){
            refreshDataListener.onDownPullRefreshData();
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public interface OnRefreshDataListener{
        void onDownPullRefreshData();
        void onUpPullRefreshData();
    }

}
