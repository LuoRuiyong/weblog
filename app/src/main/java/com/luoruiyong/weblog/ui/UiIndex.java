package com.luoruiyong.weblog.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luoruiyong.weblog.R;
import com.luoruiyong.weblog.adapter.MyFragmentPagerAdapter;
import com.luoruiyong.weblog.base.BaseUi;
import com.luoruiyong.weblog.fragment.FgHome;
import com.luoruiyong.weblog.fragment.FgMessage;
import com.luoruiyong.weblog.fragment.FgProfile;
import com.luoruiyong.weblog.util.LogUtil;

import java.util.ArrayList;

/**应用首页
 * Created by Administrator on 2017/9/9.
 */

public class UiIndex extends BaseUi {
    private static final String CLASS_NAME = UiIndex.class.getSimpleName() + "-->";
    private ViewPager vp_body ;
    private ImageView iv_home;
    private ImageView iv_message;
    private ImageView iv_write_blog;
    private ImageView iv_profile;
    private TextView tv_home;
    private TextView tv_message;
    private TextView tv_write_blog;
    private TextView tv_profile;

    private ArrayList<Fragment> fragmentList;
    private MyFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(CLASS_NAME+"界面初始化");
        if(Build.VERSION.SDK_INT >= 21){
            //android5.0以上的系统，隐藏状态栏
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.ui_index);
        bindControls();
        initFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iv_home.setImageResource(R.drawable.home_selected);
        tv_home.setTextColor(getResources().getColor(R.color.lightblue));
        vp_body.setCurrentItem(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(CLASS_NAME+"活动已销毁");
    }


    private void bindControls(){
        LogUtil.d(CLASS_NAME+"绑定控件");
        vp_body = (ViewPager) findViewById(R.id.body);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_message = (ImageView) findViewById(R.id.iv_message);
        iv_write_blog = (ImageView) findViewById(R.id.iv_write_blog);
        iv_profile = (ImageView) findViewById(R.id.iv_profile);
        tv_home = (TextView) findViewById(R.id.label_home);
        tv_message = (TextView) findViewById(R.id.label_message);
        tv_write_blog = (TextView) findViewById(R.id.label_write_blog);
        tv_profile = (TextView) findViewById(R.id.label_profile);
        MyOnClickListener listener = new MyOnClickListener();
        iv_home.setOnClickListener(listener);
        iv_message.setOnClickListener(listener);
        iv_write_blog.setOnClickListener(listener);
        iv_profile.setOnClickListener(listener);
    }

    private void initFragment() {
        LogUtil.d(CLASS_NAME+"加载碎片布局");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transition = manager.beginTransaction();
        fragmentList = new ArrayList<>();
        fragmentList.add(new FgHome());
        fragmentList.add(new FgMessage());
        fragmentList.add(new FgProfile());
        adapter = new MyFragmentPagerAdapter(manager,fragmentList);
        vp_body.setAdapter(adapter);
        vp_body.setCurrentItem(0);
        transition.commit();
    }

    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int oldSelectedItem = vp_body.getCurrentItem();
            setNormal(oldSelectedItem);
            switch (view.getId()){
                case R.id.iv_home:
                    setSelect(0);
                    break;
                case R.id.iv_message:
                    setSelect(1);
                    break;
                case R.id.iv_profile:
                    setSelect(2);
                    break;
                case R.id.iv_write_blog:
                    setSelect(3);
                    break;
            }
        }
    }

    //功能按钮状态恢复正常
    private void setNormal(int oldSelectedItem){
        int imageResource;
        ImageView imageView;
        TextView textView;
        switch (oldSelectedItem){
            case 0:
                imageResource = R.drawable.home_normal;
                imageView = iv_home;
                textView = tv_home;
                break;
            case 1:
                imageResource = R.drawable.message_normal;
                imageView = iv_message;
                textView = tv_message;
                break;
            case 2:
                imageResource = R.drawable.account_normal;
                imageView = iv_profile;
                textView = tv_profile;
                break;
            default:
                imageResource = R.drawable.write_blog_normal;
                imageView = iv_write_blog;
                textView = tv_write_blog;
                break;
        }
        imageView.setImageResource(imageResource);
        textView.setTextColor(getResources().getColor(R.color.lightgray));
    }

    //功能按钮被选中
    private void setSelect(int item){
        int imageResource;
        ImageView imageView;
        TextView textView;
        switch (item){
            case 0:
                imageResource = R.drawable.home_selected;
                imageView = iv_home;
                textView = tv_home;
                break;
            case 1:
                imageResource = R.drawable.message_selected;
                imageView = iv_message;
                textView = tv_message;
                break;
            case 2:
                imageResource = R.drawable.account_selected;
                imageView = iv_profile;
                textView = tv_profile;
                break;
            default:
                overlay(UiWriteBlog.class);
                return;
        }
        vp_body.setCurrentItem(item);
        imageView.setImageResource(imageResource);
        textView.setTextColor(getResources().getColor(R.color.lightblue));
    }
}
