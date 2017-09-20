package com.luoruiyong.weblog.model;

import com.luoruiyong.weblog.base.BaseModel;

/**评论类
 * Created by Administrator on 2017/9/7.
 */

public class BlogComment extends BaseModel {
    private String id;   //评论id
    private String editorId; //编写评论的用户
    private String editorNickname;  //编写评论的用户昵称
    private String editorIconUrl;  //编写评论的用户头像资源
    private String content;   //评论内容
    private String createtime;  //评论时间

    public BlogComment() {}

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
