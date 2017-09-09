package com.luoruiyong.weblog.base;

/**常量类
 * Created by Administrator on 2017/9/8.
 */

public class C {
    /**
     * SD卡缓存目录
     */
    public final static class dir{
        private static final String base = "/sdcard/weblog/";
        public static final String icons = base + "icon/";
        public static final String images = base + "images/";
    }

    /**
     * 添加到主线程消息队列的识别码
     */
    public final static class handler{
        public final static int taskStart               = 0;
        public final static int taskPause               = 1;
        public final static int taskStop                = 2;
        public final static int taskComplete            = 3;
        public final static int taskError               = 4;

    }

    /**
     * 远程任务请求代码参照表
     */
    public final static class task{
        public final static int test                    = 10001;
        public final static int login                   = 10002;
        public final static int logout                  = 10003;
        public final static int signin                  = 10004;
        public final static int publicBlogsList         = 10005;
        public final static int concernedBlogsList      = 10006;
        public final static int myBlogsList             = 10007;
        public final static int blogView                = 10008;
        public final static int addBlog                 = 10009;
        public final static int deleteBlog              = 10010;
        public final static int myRelatedBlogsList      = 10011;
        public final static int myCommentBlogsList      = 10012;
        public final static int myPraiseBlogsList       = 10013;
        public final static int addFan                  = 10014;
        public final static int deleteFan               = 10015;
        public final static int fansList                = 10016;
        public final static int addConcernedUser        = 10017;
        public final static int deleteConcernedUser     = 10018;
        public final static int concernedUserList       = 10019;
        public final static int addComment              = 10020;
        public final static int deleteComment           = 10021;
        public final static int customerView            = 10022;
        public final static int modifyCustomerName      = 10022;
        public final static int modifyCustomerSign      = 10023;
        public final static int modifyCustomerPassword  = 10024;
        public final static int modifyCustomerFace      = 10025;
    }

    /**
     * 微博后台使用阿里云服务器(CentOS7.3)，
     * 使用Apache+PHP+MySQL,
     * 使用Hush Framework框架
     * 来提供接口
     */
    public final static class api{
        //服务器地址
        private final static String base                     = "http://112.74.13.186:8001/";
        //对应服务器中的IndexServer控制器中的*Action方法
        public final static String test                     = base + "index/test";
        public final static String login                    = base + "index/login";
        public final static String logout                   = base + "index/logout";
        public final static String signin                   = base + "index/signin";
        //对应服务器中的BlogServer控制器中的*Action方法
        public final static String publicBlogsList          = base + "blog/publicBlogsList";
        public final static String concernedBlogsList       = base + "blog/concernedBlogsList";
        public final static String myBlogsList              = base + "blog/myBlogsList";
        public final static String blogView                 = base + "blog/blogView";
        public final static String addBlog                  = base + "blog/addBlog";
        public final static String deleteBlog               = base + "blog/deleteBlog";
        public final static String myRelatedBlogsList       = base + "blog/myRelatedBlogsList";
        public final static String myCommentBlogsList       = base + "blog/myCommentBlogsList";
        public final static String myPraiseBlogsList        = base + "blog/myPraiseBlogsList";
        //对应服务器中的FanServer控制器中的*Action方法
        public final static String addFan                   = base + "fan/addFan";
        public final static String deleteFan                = base + "fan/deleteFan";
        public final static String fansList                 = base + "fan/fansList";
        //对应服务器中的ConcernedUserServer控制器中的*Action方法
        public final static String addConcernedUser         = base + "concrenedUser/addConcernedUser";
        public final static String deleteConcernedUser      = base + "concrenedUser/deleteConcernedUser";
        public final static String concernedUserList        = base + "concrenedUser/concernedUserList";
        //对应服务器中的CommentServer控制器中的*Action方法
        public final static String addComment               = base + "comment/addComment";
        public final static String deleteComment            = base + "comment/deleteComment";
        //对应服务器中的CustomerServer控制器中的*Action方法
        public final static String customerView             = base + "customer/customerView";
        public final static String modifyCustomerName       = base + "customer/modifyCustomerName";
        public final static String modifyCustomerSign       = base + "customer/modifyCustomerSign";
        public final static String modifyCustomerPassword   = base + "customer/modifyCustomerPassword";
        public final static String modifyCustomerFace       = base + "customer/modifyCustomerFace";
    }

    public final static class err{
        public final static String network = "网络错误";
        public final static String server = "连接不到服务器";
    }
}
