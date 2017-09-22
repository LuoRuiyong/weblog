package com.luoruiyong.weblog.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luoruiyong.weblog.R;
import com.luoruiyong.weblog.adapter.MyFragmentAdapter;
import com.luoruiyong.weblog.model.Blog;
import com.luoruiyong.weblog.ui.UiIndex;
import com.luoruiyong.weblog.util.LogUtil;

import java.util.ArrayList;

/**首页碎片
 * Created by Administrator on 2017/9/14.
 */

public class FgHome extends Fragment {

    private final static String CLASS_NAME = FgHome.class.getSimpleName()+"-->";
    private View view;
    private ViewPager vp_main;
    private PagerTabStrip pts_tab;
    private ArrayList<FgBlogsList> fragmentList ;
    private ArrayList<String> tabList;
    private MyFragmentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_home,container,false);
        LogUtil.d(CLASS_NAME+"成功创建home视图");
        init();
        return view;
    }

    private void init() {
        vp_main = view.findViewById(R.id.main);
        pts_tab = view.findViewById(R.id.tab);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        fragmentList.add(new FgBlogsList());
        fragmentList.add(new FgBlogsList());
        tabList = new ArrayList<>();
        tabList.add("广场");
        tabList.add("关注");
        pts_tab.setTabIndicatorColor(Color.BLUE);
        pts_tab.setDrawFullUnderline(false);
        adapter = new MyFragmentAdapter(manager,fragmentList,tabList);
        vp_main.setAdapter(adapter);
        vp_main.addOnPageChangeListener(new MyOnPageChangeListener());
        LogUtil.d(CLASS_NAME+"成功添加碎片");
    }

    @Override
    public void onResume() {
        super.onResume();
        vp_main.setCurrentItem(0);
    }

    public void initPublicBlogData(ArrayList<Blog> blogsList){
        fragmentList.get(0).initData(blogsList);

    }

    public void initConcernBlogData(ArrayList<Blog> blogsList){
       fragmentList.get(1).initData(blogsList);
    }

    public void notifyPublicDataChange(){
        fragmentList.get(0).notifyDataChange();
    }

    public void notifyConcernedDataChange(){
        fragmentList.get(1).notifyDataChange();
    }

   private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
       @Override
       public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
       @Override
       public void onPageSelected(int position) {
           ((UiIndex)getActivity()).setSelectedHomePage(position);
       }

       @Override
       public void onPageScrollStateChanged(int state) {}
   }
}
