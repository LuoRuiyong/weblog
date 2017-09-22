package com.luoruiyong.weblog.model;

import com.luoruiyong.weblog.base.BaseModel;

/**图像类
 * Created by Administrator on 2017/9/7.
 */

public class Picture extends BaseModel{
    public static final String TYPE_PNG = "png";
    public static final String TYPE_JEPG = "jpg";
    public static final int BLOG_IMAGE_WIDTH = 350;
    public static final int BLOG_IMAGE_HEIGHT = 350;
    public static final int CONTACT_ICON_WIDTH = 80;
    public static final int CONTACT_ICON_HEIGHT = 80;

    private String url;

    public Picture() {}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
