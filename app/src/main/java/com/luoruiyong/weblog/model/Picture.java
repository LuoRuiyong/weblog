package com.luoruiyong.weblog.model;

import com.luoruiyong.weblog.base.BaseModel;

/**图像类
 * Created by Administrator on 2017/9/7.
 */

public class Picture extends BaseModel {
    public static final String TYPE_ICON = "icon";
    public static final String TYPE_IMAGE = "image";


    private String type;
    private String url;

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
