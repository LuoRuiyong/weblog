package com.luoruiyong.weblog.model;

import com.luoruiyong.weblog.base.BaseModel;

import java.util.ArrayList;

/**微博类
 * Created by Administrator on 2017/9/7.
 */

public class Blog extends BaseModel{

    private String id;
    private String editorId;
    private String editorNickname;
    private String editorIconUrl;
    private String content;
    private String commentCount;
    private String shareCount;
    private String praiseCount;
    private ArrayList<String> pictureUrl;
    private String createTime;
    private String fanRelationship;

    public Blog() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEditorId() {
        return editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    public String getEditorNickname() {
        return editorNickname;
    }

    public void setEditorNickname(String editorNickname) {
        this.editorNickname = editorNickname;
    }

    public String getEditorIconUrl() {
        return editorIconUrl;
    }

    public void setEditorIconUrl(String editorIconUrl) {
        this.editorIconUrl = editorIconUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getShareCount() {
        return shareCount;
    }

    public void setShareCount(String shareCount) {
        this.shareCount = shareCount;
    }

    public String getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(String praiseCount) {
        this.praiseCount = praiseCount;
    }

    public ArrayList<String> getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(ArrayList<String> pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFanRelationship() {
        return fanRelationship;
    }

    public void setFanRelationship(String fanRelationship) {
        this.fanRelationship = fanRelationship;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id='" + id + '\'' +
                ", editorId='" + editorId + '\'' +
                ", editorNickname='" + editorNickname + '\'' +
                ", editorIconUrl='" + editorIconUrl + '\'' +
                ", content='" + content + '\'' +
                ", commentCount='" + commentCount + '\'' +
                ", shareCount='" + shareCount + '\'' +
                ", praiseCount='" + praiseCount + '\'' +
                ", pictureUrl=" + pictureUrl +
                ", createTime='" + createTime + '\'' +
                ", fanRelationship='" + fanRelationship + '\'' +
                '}';
    }
}
