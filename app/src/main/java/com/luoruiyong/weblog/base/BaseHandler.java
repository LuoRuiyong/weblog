package com.luoruiyong.weblog.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.luoruiyong.weblog.util.AppUtil;
import com.luoruiyong.weblog.util.LogUtil;

/**消息处理器
 * Created by Administrator on 2017/9/8.
 */

public class BaseHandler extends Handler{

    private final static String CLASS_NAME = BaseHandler.class.getSimpleName() + "-->";
    private BaseUi ui;
    public static final String TASK_ID = "taskId";
    public static final String DATA = "data";

    public BaseHandler(BaseUi ui) {
        this.ui = ui;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case C.handler.taskStart:
                ui.showProgressBar();
                break;
            case C.handler.taskPause:
                ui.showProgressBar();
                break;
            case C.handler.taskStop:
                ui.hideProgressBar();
                break;
            case C.handler.taskComplete:
                taskComplete(msg);
                break;
            case C.handler.taskError:
                taskError(msg);
                break;
        }
    }

    /**
     * 异步任务出错
     * @param msg 消息内容
     */
    private void taskError(Message msg) {
        Bundle bundle = msg.getData();
        int taskId = bundle.getInt(BaseHandler.TASK_ID);
        String errorInfo = bundle.getString(BaseHandler.DATA);
        ui.onNetworkError(taskId,errorInfo);
        ui.hideProgressBar();
    }

    /**
     * 异步任务完成信息处理
     * @param msg  消息队列的参数，包含taskId和httpResult
     */
    private void taskComplete(Message msg) {
        Bundle bundle = msg.getData();
        int taskId = bundle.getInt(BaseHandler.TASK_ID);
        String result = bundle.getString(BaseHandler.DATA);
        if(result != null){
            try {
                //回调处理方法
                ui.onCompleteTask(taskId, AppUtil.getBaseMessageFromJson(result));
            } catch (Exception e) {
                //JSON数据解析失败,或数据与模型不匹配
                LogUtil.d(CLASS_NAME+"错误提示："+e.getMessage());
                ui.toast(e.getMessage());
            }
        }
        ui.hideProgressBar();
    }
}
