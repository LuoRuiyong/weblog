package com.luoruiyong.weblog.model;

import com.luoruiyong.weblog.base.BaseModel;

/**用户类
 * Created by Administrator on 2017/9/7.
 */

public class Customer extends BaseModel {

    private String id;          //用户id
    private String sid;         //会话id
    private String account;     //账户名
    private String nickName;    //昵称
    private String signText;    //个性签名
    private String cellNumber;  //手机号码
    private String email;       //电子邮箱
    private String address;     //地址
    private String iconUrl;     //头像资源在服务器中的路径
    private String blogsCount;  //微博数量
    private String fansCount;   //粉丝数量
    private String concernCount;//关注数量

    private static boolean login = false;
    private static Customer customer = null;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSignText() {
        return signText;
    }

    public void setSignText(String signText) {
        this.signText = signText;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getBlogsCount() {
        return blogsCount;
    }

    public void setBlogsCount(String blogsCount) {
        this.blogsCount = blogsCount;
    }

    public String getFansCount() {
        return fansCount;
    }

    public void setFansCount(String fansCount) {
        this.fansCount = fansCount;
    }

    public String getConcernCount() {
        return concernCount;
    }

    public void setConcernCount(String concernCount) {
        this.concernCount = concernCount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public static Customer getInstance() {
        if(customer == null){
            customer = new Customer();
        }
        return customer;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", sid='" + sid + '\'' +
                ", account='" + account + '\'' +
                ", nickName='" + nickName + '\'' +
                ", signText='" + signText + '\'' +
                ", cellNumber='" + cellNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", blogsCount='" + blogsCount + '\'' +
                ", fansCount='" + fansCount + '\'' +
                ", concernCount='" + concernCount + '\'' +
                '}';
    }
}
