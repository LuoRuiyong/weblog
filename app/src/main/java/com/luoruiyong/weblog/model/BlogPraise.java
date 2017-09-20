package com.luoruiyong.weblog.model;

import com.luoruiyong.weblog.base.BaseModel;

/**点赞类
 * Created by Administrator on 2017/9/7.
 */

public class BlogPraise extends BaseModel {
    private String id;   //点赞id
    private String editorId; //点赞的用户id
    private String editorNickname;  //点赞的用户昵称
    private String editorIconUrl;  //点赞的用户头像资源
    private String createtime;  //点赞时间

    public BlogPraise() {}

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
                ", createtime='" + createtime + '\'' +
                '}';
    }
}
