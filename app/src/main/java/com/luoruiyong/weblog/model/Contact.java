package com.luoruiyong.weblog.model;

import com.luoruiyong.weblog.base.BaseModel;

/**用户类，用于存储微博上其他用户的信息，
 * Created by Administrator on 2017/9/7.
 */

public class Contact extends BaseModel {

    private String id;          //用户id
    private String nickName;    //昵称
    private String introduction;    //个性签名
    private String address;     //地址
    private String iconUrl;     //头像资源在服务器中的路径
    private String blogCount;  //微博数量
    private String fansCount;   //粉丝数量
    private String concernCount;//关注数量

    public Contact(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getBlogCount() {
        return blogCount;
    }

    public void setBlogCount(String blogCount) {
        this.blogCount = blogCount;
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

    @Override
    public String toString() {
        return "Contact{" +
                "id='" + id + '\'' +
                ", nickName='" + nickName + '\'' +
                ", introduction='" + introduction + '\'' +
                ", address='" + address + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", blogCount='" + blogCount + '\'' +
                ", fansCount='" + fansCount + '\'' +
                ", concernCount='" + concernCount + '\'' +
                '}';
    }
}
