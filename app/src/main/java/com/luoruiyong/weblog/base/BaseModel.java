package com.luoruiyong.weblog.base;

import org.litepal.crud.DataSupport;

/**模型基础类，model包下的所有类都继承于此类,
 * 而此类的父类是Litepal提供的DataSupport，
 * 封装了SQLite的增删查改等方法
 * Created by Administrator on 2017/9/7.
 */

public class BaseModel extends DataSupport {
    public final static String PACKAGE_NAME = "com.luoruiyong.weblog.model.";
    public final static String CUSTOMER = "Customer";
    public final static String BLOG = "Blog";
    public final static String BLOG_COMMENT = "BlogComment";
    public final static String BLOG_SHARE = "BlogShare";
    public final static String BLOG_PRAISE = "BlogPraise";
    public final static String PICTURE = "Picture";
    public final static String UNIVERSAL_MODEL = "UniversalModel";
}
