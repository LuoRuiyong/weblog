package com.luoruiyong.weblog.model;

import com.luoruiyong.weblog.base.BaseModel;

/**简单任务通用类，结果包含操作是否成功
 * Created by Administrator on 2017/9/13.
 */

public class UniversalModel extends BaseModel {

    public static final String SUCCEED = "succeed";
    public static final String FAIL = "fail";


    private String status;   //返回任务请求在服务器上执行的结果，成功或失败

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
