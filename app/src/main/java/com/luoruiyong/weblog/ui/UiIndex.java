package com.luoruiyong.weblog.ui;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luoruiyong.weblog.R;
import com.luoruiyong.weblog.adapter.MyFragmentPagerAdapter;
import com.luoruiyong.weblog.base.BaseAuth;
import com.luoruiyong.weblog.base.BaseMessage;
import com.luoruiyong.weblog.base.BaseModel;
import com.luoruiyong.weblog.base.BaseUi;
import com.luoruiyong.weblog.base.C;
import com.luoruiyong.weblog.fragment.FgBlogsList;
import com.luoruiyong.weblog.fragment.FgHome;
import com.luoruiyong.weblog.fragment.FgMessage;
import com.luoruiyong.weblog.fragment.FgProfile;
import com.luoruiyong.weblog.model.Blog;
import com.luoruiyong.weblog.model.Picture;
import com.luoruiyong.weblog.util.IOUtil;
import com.luoruiyong.weblog.util.LogUtil;

import java.util.ArrayList;

/**应用首页
 * Created by Administrator on 2017/9/9.
 */

public class UiIndex extends BaseUi implements FgBlogsList.OnItemClickListener, IOUtil.OnLoadPictureTaskListener{
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
    private RelativeLayout rl_progress_layout;

    private ArrayList<Fragment> fragmentList;
    private MyFragmentPagerAdapter adapter;
    private ArrayList<Blog> publicBlogsList;
    private ArrayList<Blog> concernedBlogsList;

    private int selectedPage = 0;
    private final static int PUBLIC_BLOGS_PAGE = 0;
    private final static int CONCERNED_BLOGS_PAGE = 1;

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
        initData();
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


    //绑定控件，添加事件监听
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
        rl_progress_layout = (RelativeLayout) findViewById(R.id.layout_progressbar);
        MyOnClickListener listener = new MyOnClickListener();
        iv_home.setOnClickListener(listener);
        iv_message.setOnClickListener(listener);
        iv_write_blog.setOnClickListener(listener);
        iv_profile.setOnClickListener(listener);
    }

    //初始化碎片，数据适配器
    private void initFragment() {
        LogUtil.d(CLASS_NAME+"初始化碎片布局");
        FragmentManager manager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        fragmentList.add(new FgHome());
        fragmentList.add(new FgMessage());
        fragmentList.add(new FgProfile());
        adapter = new MyFragmentPagerAdapter(manager,fragmentList);
        vp_body.setAdapter(adapter);
        vp_body.setCurrentItem(0);
    }

    //初始化数据，联网从服务器中获取
    private void initData() {
        rl_progress_layout.setVisibility(View.VISIBLE);
        publicBlogsList = new ArrayList<>();
        concernedBlogsList = new ArrayList<>();
        LogUtil.d(CLASS_NAME+"请求服务器，获取广场微博列表信息");
        doAsyncTask(C.task.publicBlogsList,C.api.publicBlogsList,0);
        LogUtil.d(CLASS_NAME+"请求服务器，获取关注微博列表信息");
        doAsyncTask(C.task.concernedBlogsList,C.api.concernedBlogsList,0);
    }

    /**
     * 联网信息请求完成回调函数
     * @param taskId  远程任务编号号
     * @param message  服务器返回的内容（已初步处理）
     */
    @Override
    public void onCompleteTask(int taskId, BaseMessage message) {
        switch (taskId){
            case C.task.publicBlogsList:
                try {
                    ArrayList<BaseModel> baseBlogs = message.getResultList(BaseModel.BLOG);
                    for (BaseModel model:baseBlogs){
                        publicBlogsList.add((Blog) model);
                    }
                    ((FgHome)fragmentList.get(0)).initPublicBlogData(publicBlogsList);
                    LogUtil.d(CLASS_NAME+"广场微博列表数据加载成功");
                }catch (Exception e){
                    LogUtil.d(CLASS_NAME+"广场微博列表数据加载失败");
                    toast("数据加载失败");
                }
                break;
            case C.task.concernedBlogsList:
                try {
                    ArrayList<BaseModel> baseBlogs = message.getResultList(BaseModel.BLOG);
                    for (BaseModel model:baseBlogs){
                        concernedBlogsList.add((Blog) model);
                    }
                    ((FgHome)fragmentList.get(0)).initConcernBlogData(concernedBlogsList);
                    LogUtil.d(CLASS_NAME+"关注微博列表数据加载成功");
                }catch (Exception e){
                    LogUtil.d(CLASS_NAME+"关注微博列表数据加载失败");
                    toast("数据加载失败");
                }
                break;
        }
        rl_progress_layout.setVisibility(View.GONE);
    }

    /**
     * 联网信息请求发生错误回调函数
     * @param taskId  任务编号
     * @param errorInfo  错误信息
     */
    @Override
    public void onNetworkError(int taskId, String errorInfo) {
        rl_progress_layout.setVisibility(View.GONE);
        LogUtil.d(CLASS_NAME+"远程任务失败，ID:"+taskId+"  原因："+errorInfo);
    }

    /**
     * 远程图片下载任务开始时回调方法
     * @param taskId  任务编号
     */
    @Override
    public void onLoadPictureStart(int taskId) {
        LogUtil.d(CLASS_NAME+"开始远程下载图片任务，编号："+taskId);
    }

    /**
     * 远程图片下载任务发生错误时回调方法
     * @param taskId   任务编号
     * @param error 错误信息
     */
    @Override
    public void onLoadPictureError(int taskId, String error) {
        LogUtil.d(CLASS_NAME+"远程下载图片任务出错，编号："+taskId+",错误信息："+error);
    }

    /**
     * 远程图片下载任务完成时回调方法
     * @param taskId
     */
    @Override
    public void onLoadPictureComplete(final int taskId) {
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               LogUtil.d(CLASS_NAME+"远程下载图片任务完成，编号："+taskId);
               switch (taskId){
                   case C.task.getSampleContactIcon:
                   case C.task.getSampleBlogImage:
                       if(selectedPage == PUBLIC_BLOGS_PAGE){
                           LogUtil.d(CLASS_NAME+"更新广场微博列表");
                           ((FgHome)fragmentList.get(0)).notifyPublicDataChange();
                       }else{
                           LogUtil.d(CLASS_NAME+"更新关注微博列表");
                           ((FgHome)fragmentList.get(0)).notifyConcernedDataChange();
                       }
                       break;
               }
           }
       });

    }

    /**
     * 微博列表子选项上的控件点击回调方法
     * @param view  事件源，微博图片的ID不在R资源库里面，其ID依次为0,1,2...
     * @param position  子选项的位置
     */
    @Override
    public void onItemClick(View view, int position) {
        LogUtil.d(CLASS_NAME + "点击事件成功回传到Activity处理，事件源ID:" + view.getId() + "  位置：" + position);
        int targetId;
        int blogId;
        switch (view.getId()){
            case R.id.user_icon:
            case R.id.nickname:
                //点击头像或昵称，浏览用户基本信息
                targetId = getContactId(position);
                browseContact(targetId);
                LogUtil.d(CLASS_NAME + "点击头像或昵称,目标用户id："+targetId);
                break;
            case R.id.add_concern:
                //点击关注，添加关注
                targetId = getContactId(position);
                int customerId = Integer.parseInt(BaseAuth.getCustomer().getId());
                addConcern(customerId,targetId);
                LogUtil.d(CLASS_NAME+"点击加关注，目标用户id："+targetId+"  我的id："+customerId);
                break;
            case R.id.blog_layout:
            case R.id.comment_layout:
                //点击微博主体或评论
                blogId = getBlogId(position);
                browseBlog(blogId);
                LogUtil.d(CLASS_NAME+"点击微博主体或评论，微博id："+blogId);
                break;
            case R.id.share_layout:
                //点击分享
                blogId = getBlogId(position);
                shareBlog(blogId);
                LogUtil.d(CLASS_NAME+"点击分享，微博id："+blogId);
                break;
            case R.id.praise_layout:
                //点赞
                blogId = getBlogId(position);
                praiseBlog(blogId);
                LogUtil.d(CLASS_NAME+"点赞，微博id："+blogId);
                break;
            default:
                //点击第i+1张微博图片
                Picture picture = getPicture(view.getId(),position);
                browseImage(picture);
                LogUtil.d(CLASS_NAME+"点击第"+(view.getId()+1)+"张微博图片,图片资源路径："+picture.getUrl());
        }
    }

    //浏览图片(放大）
    private void browseImage(Picture picture) {

    }

    //点赞
    private void praiseBlog(int blogId) {

    }

    //分享微博
    private void shareBlog(int blogId) {

    }

    //浏览微博
    private void browseBlog(int blogId) {

    }

    //加关注
    private void addConcern(int id, int targetId) {

    }

    //浏览用户
    private void browseContact(int targetId) {

    }

    //设置当前选择的页面，0为广场微博列表，1为关注微博列表
    public void setSelectedPage(int selectedPage){
        LogUtil.d(CLASS_NAME+"当前首页显示内容代号："+selectedPage);
        this.selectedPage = selectedPage;
        if(selectedPage == PUBLIC_BLOGS_PAGE){
            ((FgHome)fragmentList.get(0)).notifyPublicDataChange();
        }else{
            ((FgHome)fragmentList.get(0)).notifyConcernedDataChange();
        }

    }

    /**
     * 获取当前子选项的用户ID
     * @param position 指定的位置
     * @return 用户ID
     */
    private int getContactId(int position){
        String contactId;
        if(selectedPage == PUBLIC_BLOGS_PAGE){
            contactId = publicBlogsList.get(position).getEditorId();
        }else {
            contactId = concernedBlogsList.get(position).getEditorId();
        }
        return Integer.parseInt(contactId);
    }

    private int getBlogId(int position){
        String blogId;
        if(selectedPage == PUBLIC_BLOGS_PAGE){
            blogId = publicBlogsList.get(position).getId();
        }else {
            blogId = concernedBlogsList.get(position).getId();
        }
        return Integer.parseInt(blogId);
    }

    private Picture getPicture(int order,int position){
        Picture picture = new Picture();
        if(selectedPage == PUBLIC_BLOGS_PAGE){
            picture.setUrl(publicBlogsList.get(position).getPictureUrl().get(order));
        }else {
            picture.setUrl(concernedBlogsList.get(position).getPictureUrl().get(order));
        }
        return picture;
    }

    //底部功能按钮点击事件监听器
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

    //权限申请结果回调函数
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case BaseUi.REQUEST_PERMISSION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LogUtil.d("成功申请内存读写权限");

                }else{
                    LogUtil.d("用户拒绝应用使用内存读写权限");
                    toast("您拒绝了应用使用储存权限，无法保存头像信息");
                }
        }
    }
}
