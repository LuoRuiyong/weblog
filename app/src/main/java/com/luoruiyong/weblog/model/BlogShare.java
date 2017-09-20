package com.luoruiyong.weblog.model;

import com.luoruiyong.weblog.base.BaseModel;

/**分享类
 * Created by Administrator on 2017/9/7.
 */

public class BlogShare extends BaseModel {
    private String id;   //分享id
    private String editorId; //分享微博的用户id
    private String editorNickname;  //分享微博的用户昵称
    private String editorIconUrl;  //分享微博的用户头像资源
    private String content;   //分享附加内容
    private String createtime;  //分享时间

    public BlogShare() {}

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

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "BlogComment{" +
                "id='" + id + '\'' +
                ", editorId='" + editorId + '\'' +
                ", editorNickname='" + editorNickname + '\'' +
                ", editorIconUrl='" + editorIconUrl + '\'' +
                ", content='" + content + '\'' +
                ", createtime='" + createtime + '\'' +
                '}';
    }
}
