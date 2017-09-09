package com.luoruiyong.weblog.model;

import com.luoruiyong.weblog.base.BaseModel;

/**用户类
 * Created by Administrator on 2017/9/7.
 */

public class Customer extends BaseModel {
    public final static String ID = "id";
    public final static String NICK_NAME = "nickname";
    public final static String CSIGN_TEXT = "signtext";
    public final static String FACE_URL = "faceurl";
    public final static String BLOGS_COUNT = "blogscount";
    public final static String FANS_COUNT = "fanscount";
    public final static String CONCERN_COUNT = "concerncount";

    private String id;
    private String sid; //会话id
    private String nickname;
    private String signtext;
    private String faceurl; //头像资源在服务器中的路径
    private String blogscount;
    private String fanscount;
    private String concerncount;
    private boolean login = false;
    private static Customer customer = null;

    public Customer() {}

    public static Customer getInstance() {
        if(customer == null){
            customer = new Customer();
        }
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSigntext() {
        return signtext;
    }

    public void setSigntext(String signtext) {
        this.signtext = signtext;
    }

    public String getFaceurl() {
        return faceurl;
    }

    public void setFaceurl(String faceurl) {
        this.faceurl = faceurl;
    }

    public String getBlogscount() {
        return blogscount;
    }

    public void setBlogscount(String blogscount) {
        this.blogscount = blogscount;
    }

    public String getFanscount() {
        return fanscount;
    }

    public void setFanscount(String fanscount) {
        this.fanscount = fanscount;
    }

    public String getConcerncount() {
        return concerncount;
    }

    public void setConcerncount(String concerncount) {
        this.concerncount = concerncount;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }
}
