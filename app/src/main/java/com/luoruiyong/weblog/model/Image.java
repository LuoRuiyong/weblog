package com.luoruiyong.weblog.model;

import com.luoruiyong.weblog.base.BaseModel;

/**图像类
 * Created by Administrator on 2017/9/7.
 */

public class Image extends BaseModel {
    public static final String ID = "id";
    public static final String TYPE = "type";
    public static final String URL = "url";

    private String id;
    private String type;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
