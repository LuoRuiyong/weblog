package com.luoruiyong.weblog.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luoruiyong.weblog.R;
import com.luoruiyong.weblog.adapter.ContactParticularsFragmentPagerAdapter;
import com.luoruiyong.weblog.base.BaseMessage;
import com.luoruiyong.weblog.base.BaseModel;
import com.luoruiyong.weblog.base.BaseUi;
import com.luoruiyong.weblog.base.C;
import com.luoruiyong.weblog.fragment.FgPersonAlbum;
import com.luoruiyong.weblog.fragment.FgPersonBlogs;
import com.luoruiyong.weblog.fragment.FgPersonBrief;
import com.luoruiyong.weblog.model.Contact;
import com.luoruiyong.weblog.util.AppCache;
import com.luoruiyong.weblog.util.IOUtil;
import com.luoruiyong.weblog.util.LogUtil;
import com.luoruiyong.weblog.util.SDUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static com.luoruiyong.weblog.util.AppCache.getSampleImage;

/**用户详情
 * Created by Administrator on 2017/9/23.
 */

public class UiContactParticulars extends BaseUi implements IOUtil.OnLoadPictureTaskListener {
    private static final String CLASS_NAME = UiContactParticulars.class.getSimpleName()+"-->";
    private ImageView iv_back;
    private ImageView iv_more;
    private ImageView iv_image;
    private ImageView iv_user_icon;
    private TextView tv_nick_name;
    private TextView tv_concern_count;
    private TextView tv_fans_count;
    private TextView tv_concern_count_label;
    private TextView tv_fans_count_label;
    private TextView tv_download_image;
    private ViewPager vp_body;
    private PagerTabStrip pts_tab;
    private RelativeLayout rl_header_layout;
    private FrameLayout fl_browse_image_layout;

    private ArrayList<String> tabs;
    private ArrayList<Fragment> fragments;
    private ContactParticularsFragmentPagerAdapter adapter;

    private MyOnClickListener clickListener;

    private Contact contact;
    private int currentPage = 0;
    private static final int PERSON_BRIEF = 0;
    private static final int PERSON_BLOGS = 1;
    private static final int PERSON_ALBUM = 2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(CLASS_NAME+"界面初始化");
        setContentView(R.layout.ui_contact_particulars);
        initControls();
        initFragments();
        initData();
    }

    @Override
    protected void onDestroy() {
        LogUtil.d(CLASS_NAME+"销毁活动");
        super.onDestroy();
    }

    private void initData(){
        Intent intent = getIntent();
        String contactId = intent.getExtras().getString("contact_id");
        LogUtil.d(CLASS_NAME+"获取上一个活动传过来的数据：contact_id="+contactId);
        HashMap<String,String> params = new HashMap<>();
        params.put("contact_id",contactId);
        doAsyncTask(C.task.getContactInfo,C.api.getContactInfo,params,0);
    }

    private void initControls() {
        LogUtil.d(CLASS_NAME+"绑定控件，添加事件监听");
        iv_back = (ImageView) findViewById(R.id.back);
        iv_more = (ImageView) findViewById(R.id.more);
        iv_user_icon = (ImageView) findViewById(R.id.user_icon);
        iv_image = (ImageView) findViewById(R.id.browse_image);
        tv_download_image = (TextView) findViewById(R.id.save_image);
        tv_nick_name = (TextView) findViewById(R.id.nickname);
        tv_concern_count = (TextView) findViewById(R.id.concern_count);
        tv_fans_count = (TextView) findViewById(R.id.fans_count);
        tv_concern_count_label = (TextView) findViewById(R.id.concern_count_label);
        tv_fans_count_label = (TextView) findViewById(R.id.fans_count_label);
        vp_body = (ViewPager) findViewById(R.id.contact_particulars_body);
        pts_tab = (PagerTabStrip) findViewById(R.id.tab);
        rl_header_layout = (RelativeLayout) findViewById(R.id.header_layout);
        fl_browse_image_layout = (FrameLayout) findViewById(R.id.browse_image_layout);
        pts_tab.setTabIndicatorColor(Color.BLUE);
        pts_tab.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        clickListener = new MyOnClickListener();
        iv_user_icon.setOnClickListener(clickListener);
        iv_more.setOnClickListener(clickListener);
        iv_back.setOnClickListener(clickListener);
        iv_image.setOnClickListener(clickListener);
        tv_download_image.setOnClickListener(clickListener);
        tv_concern_count.setOnClickListener(clickListener);
        tv_fans_count.setOnClickListener(clickListener);
        tv_concern_count_label.setOnClickListener(clickListener);
        tv_fans_count_label.setOnClickListener(clickListener);
    }

    private void initFragments() {
        LogUtil.d(CLASS_NAME+"初始化碎片");
        FragmentManager manager = getSupportFragmentManager();
        fragments = new ArrayList<>();
        fragments.add(new FgPersonBrief());
        fragments.add(new FgPersonBlogs());
        fragments.add(new FgPersonAlbum());
        tabs = new ArrayList<>();
        tabs.add("主页");
        tabs.add("微博");
        tabs.add("相册");
        adapter = new ContactParticularsFragmentPagerAdapter(manager,fragments,tabs);
        vp_body.setAdapter(adapter);
    }

    /**
     * 设置用户的基本信息，显示
     * @param contact  用户对象
     */
    private void setBaseData(Contact contact){
        LogUtil.d(CLASS_NAME+"设置显示基本数据");
        tv_nick_name.setText(contact.getNickName());
        tv_concern_count.setText(contact.getConcernCount());
        tv_fans_count.setText(contact.getFansCount());
        ((FgPersonBrief)fragments.get(PERSON_BRIEF)).setAddress(contact.getAddress());
        ((FgPersonBrief)fragments.get(PERSON_BRIEF)).setIntroduction(contact.getIntroduction());
    }

    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.user_icon:
                    //显示全屏头像，先尝试从内存里获取，若无，联网加载并先显示缩略图，若缩略图也没有，显示默认图片
                    Bitmap bitmap = AppCache.getFullScreenSampleImage(UiContactParticulars.this,contact.getIconUrl());
                    if(bitmap != null){
                        iv_image.setImageBitmap(bitmap);
                    }else{
                        bitmap = AppCache.getSampleImage(UiContactParticulars.this,C.task.getSampleContactIcon,contact.getIconUrl());
                        if(bitmap != null){
                            iv_image.setImageBitmap(bitmap);
                        }else{
                            iv_image.setImageResource(R.drawable.blog_picture_default);
                        }
                    }
                    fl_browse_image_layout.setVisibility(View.VISIBLE);
                    break;
                case R.id.back:
                    finish();
                    break;
                case R.id.more:

                    break;
                case R.id.concern_count:
                case R.id.concern_count_label:

                    break;
                case R.id.fans_count:
                case R.id.fans_count_label:

                    break;
                case R.id.browse_image:
                    fl_browse_image_layout.setVisibility(View.GONE);
                    break;
                case R.id.save_image:
                    IOUtil.getBitmapRemote(UiContactParticulars.this,C.task.getOriginalImage,contact.getIconUrl());
                    break;
            }
        }
    }

    //远程信息任务请求成功回调函数
    @Override
    public void onCompleteTask(int taskId, BaseMessage message) {
        LogUtil.d(CLASS_NAME+"远程任务请求成功，编号："+taskId);
        switch (taskId){
            case C.task.getContactInfo:
                try{
                    contact = (Contact) message.getResult(BaseModel.CONTACT);
                    setBaseData(contact);
                    Bitmap bitmap = AppCache.getSampleImage(UiContactParticulars.this,C.task.getSampleContactIcon, contact.getIconUrl());
                    if(bitmap != null){
                        iv_user_icon.setImageBitmap(bitmap);
                    }
                }catch(Exception e){
                    //数据为空时才会抛出异常
                    toast("服务器异常");
                }
                break;
        }
    }

    //远程信息请求任务出错回调函数
    @Override
    public void onNetworkError(int taskId, String errorInfo) {
        LogUtil.d(CLASS_NAME+"远程信息请求任务失败，任务编号："+taskId);
    }

    //远程图片请求任务开始回调函数
    @Override
    public void onLoadPictureStart(int taskId) {
        LogUtil.d(CLASS_NAME+"开始远程图片请求任务，任务编号："+taskId);
    }

    //远程图片请求任务出错回调函数
    @Override
    public void onLoadPictureError(int taskId, String error) {
        LogUtil.d(CLASS_NAME+"远程图片请求任务失败，任务编号："+taskId);
        switch (taskId){
            case C.task.getOriginalImage:
                toast("保存图片失败");
                break;
        }
    }

    //远程图片请求任务完成回调函数
    @Override
    public void onLoadPictureComplete(final int taskId, final String url) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (taskId){
                    case C.task.getSampleContactIcon:
                        Bitmap bitmap = getSampleImage(UiContactParticulars.this,C.task.getSampleContactIcon,url);
                        if(bitmap != null){
                            iv_user_icon.setImageBitmap(bitmap);
                        }
                        break;
                    case C.task.getOriginalImage:
                        LogUtil.d(CLASS_NAME+"成功保存图片");
                        toast("成功保存原图到"+ SDUtil.getDownLoadRealFileName(url));
                        break;
                    case C.task.getSampleFullScreenImage:
                        bitmap = AppCache.getFullScreenSampleImage(UiContactParticulars.this,url);
                        if(bitmap != null){
                            LogUtil.d(CLASS_NAME+"查看全屏图");
                            iv_image.setImageBitmap(bitmap);
                        }
                        break;
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
