package com.luoruiyong.weblog.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.luoruiyong.weblog.fragment.FgBlogsList;

import java.util.ArrayList;

/**滑动页面数据适配器
 * Created by Administrator on 2017/9/14.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<String> tabList;
    private ArrayList<FgBlogsList> fragmentList;

    public MyFragmentAdapter(FragmentManager fm, ArrayList<FgBlogsList> fragmentList, ArrayList<String> tabList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.tabList = tabList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }
}
