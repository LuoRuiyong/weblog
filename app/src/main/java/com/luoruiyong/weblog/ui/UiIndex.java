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
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.luoruiyong.weblog.myview.SlideShowViewGroup;
import com.luoruiyong.weblog.util.AppCache;
import com.luoruiyong.weblog.util.IOUtil;
import com.luoruiyong.weblog.util.LogUtil;
import com.luoruiyong.weblog.util.SDUtil;

import java.util.ArrayList;

/**应用首页
 * Created by Administrator on 2017/9/9.
 */

public class UiIndex extends BaseUi implements FgBlogsList.OnItemClickListener,
        IOUtil.OnLoadPictureTaskListener,SlideShowViewGroup.OnImageChangedListener,
        SlideShowViewGroup.OnClickImageListener{
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
    private TextView tv_current_index;
    private TextView tv_blog_content;
    private ImageView iv_save_image;
    private RelativeLayout rl_progress_layout;
    private FrameLayout fl_browse_image_layout;
    private SlideShowViewGroup vg_image;

    private ArrayList<Fragment> fragmentList;
    private MyFragmentPagerAdapter adapter;
    private ArrayList<Blog> publicBlogsList;
    private ArrayList<Blog> concernedBlogsList;

    private int selectedHomePage = 0;  //当前选中首页中的页面
    private int selectedPage = 0;  //当前选中的主页面
    private final static int PUBLIC_BLOGS_PAGE = 0;    //广场微博页面，selectedPage为HOME_PAGE,才有效
    private final static int CONCERNED_BLOGS_PAGE = 1;  //关注微博页面，selectedPage为HOME_PAGE,才有效

    private final static int HOME_PAGE = 0;    //首页代号
    private final static int MESSAGE_PAGE = 1;  //消息页代号
    private final static int PROFILE_PAGE = 2; //个人信息页代号

    private boolean browseImage = false;  //是否是微博图片浏览模式
    private int clickPosition;    //选中图片在当前list的位置，从0开始标号
    private int index;      //选中图片在微博图片中的序号，从0开始标号

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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(Build.VERSION.SDK_INT >= 21){
            //android5.0以上的系统，隐藏状态栏
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSelect(0);
        selectedPage = HOME_PAGE;
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
        tv_current_index = (TextView) findViewById(R.id.current_index);
        tv_blog_content = (TextView) findViewById(R.id.blog_content);
        iv_save_image = (ImageView) findViewById(R.id.save_image);
        rl_progress_layout = (RelativeLayout) findViewById(R.id.layout_progressbar);
        fl_browse_image_layout = (FrameLayout) findViewById(R.id.browse_image_layout);
        vg_image = (SlideShowViewGroup) findViewById(R.id.image_viewgroup);
        vg_image.setOnImageChangedListener(this);
        vg_image.setOnClickImageListener(this);
        MyOnClickListener listener = new MyOnClickListener();
        iv_home.setOnClickListener(listener);
        iv_message.setOnClickListener(listener);
        iv_write_blog.setOnClickListener(listener);
        iv_profile.setOnClickListener(listener);
        iv_save_image.setOnClickListener(listener);
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
                //成功获取广场微博列表信息，加载到碎片去
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
                //成功获取关注微博列表信息，加载到碎片去
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
        if(taskId == C.task.getOriginalImage){
            toast(error);
        }
    }

    /**
     * 远程图片下载任务完成时回调方法
     * @param taskId
     */
    @Override
    public void onLoadPictureComplete(final int taskId, final String pictureUrl) {
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               LogUtil.d(CLASS_NAME+"远程下载图片任务完成，编号："+taskId);
               switch (taskId){
                   case C.task.getSampleContactIcon:
                   case C.task.getSampleBlogImage:
                       //成功获取微博图片和用户头像
                       if(selectedPage == HOME_PAGE && selectedHomePage == PUBLIC_BLOGS_PAGE){
                           //当前界面为广场微博界面，更新信息
                           LogUtil.d(CLASS_NAME+"更新广场微博列表");
                           ((FgHome)fragmentList.get(0)).notifyPublicDataChange();
                       }else if(selectedPage == HOME_PAGE && selectedHomePage == CONCERNED_BLOGS_PAGE){
                           //当前界面为关注微博界面，更新信息
                           LogUtil.d(CLASS_NAME+"更新关注微博列表");
                           ((FgHome)fragmentList.get(0)).notifyConcernedDataChange();
                       }
                       break;
                   case C.task.getSampleFullScreenImage:
                       //成功获取全屏缩略图，查看微博图片
                       ArrayList<String> imageUrl;
                       if(selectedPage == HOME_PAGE && browseImage){
                           if(selectedHomePage == PUBLIC_BLOGS_PAGE){
                               imageUrl = publicBlogsList.get(clickPosition).getPictureUrl();
                           }else{
                               imageUrl = concernedBlogsList.get(clickPosition).getPictureUrl();
                           }
                           for (int i = 0; i < imageUrl.size(); i++) {
                               //更新图片显示
                               ((ImageView)vg_image.getChildAt(i)).setImageBitmap(
                                       AppCache.getFullScreenSampleImage(UiIndex.this,imageUrl.get(i)));
                           }
                       }
                       break;
                   case C.task.getOriginalImage:
                       String fileNmae = SDUtil.getDownLoadRealFileName(pictureUrl);
                       toast("成功保存图片到"+fileNmae);
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
        clickPosition = position;
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
                index = view.getId();
                //点击第i张微博图片
                LogUtil.d(CLASS_NAME+"点击第"+(view.getId())+"张微博图片");
                browseImage(getPictureUrl(position));

        }
    }

    /**
     * 全屏查看微博图片，带平滑滚动效果
     * @param pictureUrl  所选微博中的所有图片资源列表
     */
    private void browseImage(ArrayList<String> pictureUrl) {
        //动态加载图片到图片浏览容器
        for(String url:pictureUrl){
            ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            image.setLayoutParams(params);
            image.setImageBitmap(AppCache.getFullScreenSampleImage(UiIndex.this,url));
            vg_image.addView(image);
        }
        browseImage = true;
        vg_image.setSelectImage(index);  //设置展示的用户点击选择的图片
        tv_current_index.setText((index+1)+"/"+pictureUrl.size());  //设置页头指示信息
        tv_blog_content.setText(getBlogContent());    //设置微博内容
        fl_browse_image_layout.setVisibility(View.VISIBLE);    //显示
        LogUtil.d(CLASS_NAME+"浏览微博图片");
    }

    private String getBlogContent(){
        if(selectedPage == HOME_PAGE){
            if(selectedHomePage == PUBLIC_BLOGS_PAGE){
                return publicBlogsList.get(clickPosition).getContent();
            }else{
                return concernedBlogsList.get(clickPosition).getContent();
            }
        }
        return null;
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

    /**
     * 设置当前选择的页面，
     * 0为广场微博列表，
     * 1为关注微博列表
     * 提供给FgHome碎片调用，主要是左右滑动选择不同碎片才会调用
     * @param selectedHomePage
     */
    public void setSelectedHomePage(int selectedHomePage){
        LogUtil.d(CLASS_NAME+"当前首页显示内容代号："+ selectedHomePage);
        this.selectedHomePage = selectedHomePage;
        if(selectedPage == HOME_PAGE && selectedHomePage == PUBLIC_BLOGS_PAGE){
            //当前转换为广场微博页面，更新数据
            ((FgHome)fragmentList.get(0)).notifyPublicDataChange();
        }else if(selectedPage == HOME_PAGE && selectedHomePage == CONCERNED_BLOGS_PAGE){
            //当前转换为关注微博页面，更新数据
            ((FgHome)fragmentList.get(0)).notifyConcernedDataChange();
        }
    }

    /**
     * 获取当前子选项的用户ID
     * @param position 指定的位置
     * @return 用户ID
     */
    private int getContactId(int position){
        String contactId = "";
        if(selectedPage == HOME_PAGE && selectedHomePage == PUBLIC_BLOGS_PAGE){
            contactId = publicBlogsList.get(position).getEditorId();
        }else if(selectedPage == HOME_PAGE && selectedHomePage == CONCERNED_BLOGS_PAGE){
            contactId = concernedBlogsList.get(position).getEditorId();
        }
        return Integer.parseInt(contactId);
    }

    private int getBlogId(int position){
        String blogId = "";
        if(selectedPage == HOME_PAGE && selectedHomePage == PUBLIC_BLOGS_PAGE){
            blogId = publicBlogsList.get(position).getId();
        }else if(selectedPage == HOME_PAGE && selectedHomePage == CONCERNED_BLOGS_PAGE){
            blogId = concernedBlogsList.get(position).getId();
        }
        return Integer.parseInt(blogId);
    }

    /**
     * 获取微博列表中指定位置的图片资源
     * @param position  指定位置
     * @return   图片资源列表
     */
    private ArrayList<String> getPictureUrl(int position){
        if(selectedPage == HOME_PAGE && selectedHomePage == PUBLIC_BLOGS_PAGE){
            return publicBlogsList.get(position).getPictureUrl();
        }else if(selectedPage == HOME_PAGE && selectedHomePage == CONCERNED_BLOGS_PAGE){
            return concernedBlogsList.get(position).getPictureUrl();
        }
        return  null;
    }

    /**
     * 提供给自定义图片浏览类SlideShowViewGroup回调方法
     * 用户滑动浏览不同的图片时回调
     * @param index  目标图片在当前微博中的标号
     * @param total  当前微博的总图片数目
     */
    @Override
    public void onImageChanged(final int index, final int total) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //更新指示页头
                int current_index = index + 1;
                tv_current_index.setText(current_index+"/"+total);
            }
        });
    }

    /**
     * 提供给自定义图片浏览类SlideShowViewGroup回调方法
     * 用户点击图片，退出图片浏览模式
     */
    @Override
    public void onClickImage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogUtil.d(CLASS_NAME+"隐藏微博图片容器");
                browseImage = false;
                vg_image.removeAllViews();
                fl_browse_image_layout.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 点击事件监听器
     */
    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.save_image){
                //保存原图
                saveOriginalImage();
                LogUtil.d(CLASS_NAME+"下载原图");
            }else {
                int oldSelectedItem = vp_body.getCurrentItem();
                setNormal(oldSelectedItem);
                switch (view.getId()) {
                    case R.id.iv_home:
                        //首页
                        setSelect(0);
                        break;
                    case R.id.iv_message:
                        //消息页面
                        setSelect(1);
                        break;
                    case R.id.iv_profile:
                        //我的页面
                        setSelect(2);
                        break;
                    case R.id.iv_write_blog:
                        //写微博，跳转到新的Activity
                        setSelect(3);
                        break;
                }
            }
        }
    }

    /**
     * 保存原图操作
     */
    private void saveOriginalImage() {
        String url;
        if(selectedPage == HOME_PAGE && browseImage == true){
            //获取图片资源路径
            if(selectedHomePage == PUBLIC_BLOGS_PAGE){
                url = publicBlogsList.get(clickPosition).getPictureUrl().get(index);
            }else {
                url = concernedBlogsList.get(clickPosition).getPictureUrl().get(index);
            }
            //异步下载图片资源
            IOUtil.getBitmapRemote(UiIndex.this,C.task.getOriginalImage,url);
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
                selectedPage = HOME_PAGE;
                imageResource = R.drawable.home_selected;
                imageView = iv_home;
                textView = tv_home;
                break;
            case 1:
                selectedPage = MESSAGE_PAGE;
                imageResource = R.drawable.message_selected;
                imageView = iv_message;
                textView = tv_message;
                break;
            case 2:
                selectedPage = PUBLIC_BLOGS_PAGE;
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

    @Override
    public void onBackPressed() {
        if(browseImage){
            vg_image.removeAllViews();
            fl_browse_image_layout.setVisibility(View.GONE);
            browseImage = false;
        }else if(selectedPage != HOME_PAGE){
            setNormal(vp_body.getCurrentItem());
            setSelect(0);
            selectedPage = HOME_PAGE;
        }else{
            finish();
        }
    }
}
