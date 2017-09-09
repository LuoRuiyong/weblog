package com.luoruiyong.weblog.base;

import com.luoruiyong.weblog.util.LogUtil;

/**任务类,拥有五个任务状态回调方法
 * 在创建任务时，看需求重写对应的回调逻辑
 * Created by Administrator on 2017/9/8.
 */

public class BaseTask {
    public static final int COMPLETE = 2;
    public static final int PAUSE = 1;
    public static final int START = 0;
    public static final int STOP = 3;
    public static final int ERROR = 4;

    private int id;
    private String name;
    private String CLASS_NAME = BaseTask.class.getSimpleName() + "-->";

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
        LogUtil.d(CLASS_NAME+"onStart()");
    }

    public void onPause(){
        LogUtil.d(CLASS_NAME+"onPause()");
    }

    public void onStop() throws Exception{
        LogUtil.d(CLASS_NAME+"onStop()");
    }

    public void onCompleteTask(){
        LogUtil.d(CLASS_NAME+"onCompleteTask()");
    }

    public void onCompleteTask(String httpResponse){
        LogUtil.d(CLASS_NAME+"onCompleteTask  "+httpResponse);
    }

    public void onError(String error){
        LogUtil.d(CLASS_NAME+"onError  "+error);
    }
}
