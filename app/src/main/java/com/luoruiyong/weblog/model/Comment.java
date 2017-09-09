package com.luoruiyong.weblog.model;

import com.luoruiyong.weblog.base.BaseModel;

/**评论类
 * Created by Administrator on 2017/9/7.
 */

public class Comment extends BaseModel {
    public static final String ID ="id";
    public static final String AUTHOR = "author";
    public static final String AUTHOR_ID = "authorid";
    public static final String AUTHOR_FACE_URL = "faceurl";
    public static final String CONTENT = "content";
    public static final String CREATE_TIME ="createtime";

    private String id;
    private String author;
    private String authorid;
    private String faceurl;
    private String content;
    private String createtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public String getFaceurl() {
        return faceurl;
    }

    public void setFaceurl(String faceurl) {
        this.faceurl = faceurl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
