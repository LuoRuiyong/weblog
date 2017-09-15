package com.luoruiyong.weblog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luoruiyong.weblog.R;
import com.luoruiyong.weblog.adapter.MyFragmentPagerAdapter;
import com.luoruiyong.weblog.util.LogUtil;

import java.util.ArrayList;

/**首页碎片
 * Created by Administrator on 2017/9/14.
 */

public class FgHome extends Fragment {

    private final static String CLASS_NAME = FgHome.class.getSimpleName()+"-->";
    private View view;
    private ViewPager vp_main;
    private ArrayList<Fragment> list ;
    private MyFragmentPagerAdapter adapter;

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
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction bt = manager.beginTransaction();
        list = new ArrayList<>();
        list.add(new FgBlogsList());
        list.add(new FgBlogsList());
        adapter = new MyFragmentPagerAdapter(manager,list);
        vp_main.setAdapter(adapter);
        bt.commit();
    }

    public void update(){

    }
}
