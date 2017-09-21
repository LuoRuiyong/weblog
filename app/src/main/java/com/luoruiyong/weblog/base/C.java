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
        public static final String cache = base + "cache/";
        public static final String download = base + "download/";
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
        public final static int sign                    = 10004;
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
        public final static int getOriginalContactIcon = 10026;
        public final static int getSampleContactIcon = 10027;
        public final static int getContactIconUrl = 10028;

        public final static int checkAccount            = 10029;
        public final static int checkNickName           = 10030;
        public final static int getOriginalBlogImage = 10031;
        public final static int getSampleBlogImage = 10031;
        public final static int getSampleCustomImage = 10032;
        public final static int getOriginalImage = 10032;

    }

    /**
     * 远程任务请求代码参照表
     */
    public final static class status_code{

    }



    /**
     * 微博后台使用阿里云服务器(CentOS7.3)，
     * 使用Apache+PHP+MySQL,
     * 使用Hush Framework框架
     * 来提供接口
     */
    public final static class api{
        //服务器地址
        private final static String base                    = "http://112.74.13.186:8001/";
        public final static String test                     = "http://112.74.13.186/weblog/";
        //对应服务器中的IndexServer控制器中的*Action方法
        public final static String index                    = base + "index/index";
        //public final static String login                    = base + "index/login";
        public final static String login                    = test + "loginTest.php";
        public final static String logout                   = base + "index/logout";
        //对应服务器中的BlogServer控制器中的*Action方法
        //public final static String publicBlogsList          = base + "blog/publicBlogsList";
        public final static String publicBlogsList          = test + "getPublicBlogsList.php";
        //public final static String concernedBlogsList       = base + "blog/concernedBlogsList";
        public final static String concernedBlogsList       = test + "getConcernedBlogsList.php";
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
        //所有待定接口都有test来暂时替代
        public final static String getContactIconUrl = test + "getUserIconUrlTest.php";   //待定
        public final static String checkAccount             = test;   //待定
        public final static String checkCellNumber          = test;   //待定
        public final static String checkEmail               = test;   //待定
        public final static String checkNickName            = test;   //待定
        public final static String sign                     = test;   //待定

    }

    public final static class err{
        public final static String network = "无法连接网络，请稍后再试";
        public final static String server = "连接不到服务器";
        public final static String jsonFormat = "JSON数据不匹配";
        public final static String noData = "服务器返回结果为空";
        public final static String modelError = "数据与模型不匹配";


    }

    public final static class model{


    }

    public final static class pattern{
        //密码规则：字符可为下划线、数字或字母，字符个数6-20个，如：544322dsf_
        public final static String password = "[a-zA-Z0-9_]{6,20}";
        //账号规则：首字符必须为字母，其余的可以是下划线、数字或字母，字符个数6-20个，如：hsd232432
        public final static String accountDefault = "[a-zA-Z]{1}[a-zA-Z0-9_]{5,20}";
        //手机号码规则：首字符为1，其余为任意数字字符，字符个数11个，如：11234566777
        public final static String cellNumber = "^1[0-9]{10,10}";
        //昵称规则：1-12个任意字符,如：大傻
        public final static String nickName = ".{1,12}";
        //模块1：字母、数字、下划线、中划线字符
        //邮箱规则：首字符为字母或数字，其后可为模块1中的任意字符，中间必须包含@字符，
        //@之后可为模块1中的任意字符，加上.,后可接任意字母，如：ga2323@sae.dfg
        public final static String email = "[a-zA-Z0-9][a-zA-Z0-9_-]*?@[a-zA-Z0-9_-]+?(\\.[a-zA-Z]+?){1,5}";

    }
}
