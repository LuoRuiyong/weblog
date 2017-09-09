package com.luoruiyong.weblog.model;

import com.luoruiyong.weblog.base.BaseModel;

/**微博类
 * Created by Administrator on 2017/9/7.
 */

public class Blog extends BaseModel{
    public static final String ID = "id";    //微博id
    public static final String AUTHOR = "author";   //撰写者昵称
    public static final String AUTHOR_ID = "authorid";  //撰写者id
    public static final String AUTHOR_FACE_URL = "faceurl";  //撰写者头像
    public static final String CONTENT = "content";   //微博正文
    public static final String COMMENT_COUNT = "commentcount";  //评论数
    public static final String PRAISE_COUNT = "praisecount";   //点赞数
    public static final String TRANSMIT_COUNT = "transmitcount";  //转发数
    public static final String CREATE_TIME = "createtime";   //撰写时间

    private String id;
    private String author;
    private String authorid;
    private String faceurl;
    private String content;
    private String commentcount;
    private String praisecount;  //点赞统计
    private String transmitcount;  //转发统计
    private String createtime;   //发表时间

    public Blog() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(String commentcount) {
        this.commentcount = commentcount;
    }

    public String getPraisecount() {
        return praisecount;
    }

    public void setPraisecount(String praisecount) {
        this.praisecount = praisecount;
    }

    public String getTransmitcount() {
        return transmitcount;
    }

    public void setTransmitcount(String transmitcount) {
        this.transmitcount = transmitcount;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFaceurl() {
        return faceurl;
    }

    public void setFaceurl(String faceurl) {
        this.faceurl = faceurl;
    }

}
