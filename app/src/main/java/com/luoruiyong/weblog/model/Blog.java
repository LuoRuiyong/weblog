package com.luoruiyong.weblog.model;

import com.luoruiyong.weblog.base.BaseModel;

/**微博类
 * Created by Administrator on 2017/9/7.
 */

public class Blog extends BaseModel{
    public static final String ID = "id";
    public static final String AUTHOR = "author";
    public static final String AUTHOR_ID = "authorid";
    public static final String AUTHOR_FACE_URL = "faceurl";
    public static final String CONTENT = "content";
    public static final String COMMENT_COUNT = "commentcount";
    public static final String PRAISE_COUNT = "praisecount";
    public static final String TRANSMIT_COUNT = "transmitcount";
    public static final String CREATE_TIME = "createtime";

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
