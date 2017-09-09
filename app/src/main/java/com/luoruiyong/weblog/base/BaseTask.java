package com.luoruiyong.weblog.base;

import com.luoruiyong.weblog.util.LogUtil;

/**任务类,拥有五个任务状态回调方法
 * 在创建任务时，看需求重写对应的回调逻辑
 * Created by Administrator on 2017/9/8.
 */

public class BaseTask {
    private int id;
    private String name;
    private String Tag = "BaseTask";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void onStart(){
        LogUtil.d(Tag,"onStart()");
    }

    public void onPause(){
        LogUtil.d(Tag,"onPause()");
    }

    public void onStop() throws Exception{
        LogUtil.d(Tag,"onStop()");
    }

    public void onCompleteTask(){
        LogUtil.d(Tag,"onCompleteTask()");
    }

    public void onCompleteTask(String httpResponse){
        LogUtil.d(Tag,"onCompleteTask  "+httpResponse);
    }

    public void onError(String error){
        LogUtil.d(Tag,"onError  "+error);
    }
}
