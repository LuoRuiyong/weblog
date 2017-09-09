package com.luoruiyong.weblog.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.luoruiyong.weblog.util.LogUtil;

import org.apache.http.NameValuePair;

import java.util.HashMap;
import java.util.List;

/**所有活动的基类，包装了活动中常用的一些方法
 * Created by Administrator on 2017/9/7.
 */

public class BaseUi extends AppCompatActivity {

    private final static String CLASS_NAME = BaseUi.class.getSimpleName() + "-->";
    protected BaseHandler handler;
    protected BaseTaskPool taskPool;
    protected ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.progressBar = new ProgressBar(this);
        this.handler = new BaseHandler(this);
        this.taskPool = new BaseTaskPool(this);
    }

    /**
     * 本地任务
     * @param taskid   任务id
     * @param delaytime  延迟时间
     */
    public void doAsyncTask(int taskid,long delaytime){
        taskPool.addTask(taskid,new BaseTask(){
            @Override
            public void onCompleteTask() {
                super.onCompleteTask();
                sendMessage(C.handler.taskComplete,getTaskId(),null);
            }
            @Override
            public void onError(String error) {
                super.onError(error);
                sendMessage(C.handler.taskError,getTaskId(),error);
            }
        },delaytime);
    }

    /**
     * 远程任务，请求无参数，无文件
     * @param taskid  任务id
     * @param taskUrl  任务资源路径，即服务器提供的数据接口
     * @param delaytime  延迟
     */
    public void doAsyncTask(int taskid,String taskUrl,long delaytime){
        taskPool.addTask(taskid,taskUrl,new BaseTask(){
            @Override
            public void onCompleteTask(String httpResult) {
                super.onCompleteTask();
                sendMessage(C.handler.taskComplete,getTaskId(),httpResult);
            }
            @Override
            public void onError(String error) {
                super.onError(error);
                sendMessage(C.handler.taskError,getTaskId(),error);
            }
        },delaytime);
    }

    /**
     * 远程任务，请求有参数，无文件
     * @param taskid  任务id
     * @param taskUrl  任务资源路径，即服务器提供的数据接口
     * @param taskParams  请求参数
     * @param delaytime  延迟
     */
    public void doAsyncTask(int taskid, String taskUrl, HashMap<String,String> taskParams,long delaytime){
        taskPool.addTask(taskid,taskUrl,taskParams,new BaseTask(){
            @Override
            public void onCompleteTask(String httpResult) {
                super.onCompleteTask();
                sendMessage(C.handler.taskComplete,getTaskId(),httpResult);
            }
            @Override
            public void onError(String error) {
                super.onError(error);
                sendMessage(C.handler.taskError,getTaskId(),error);
            }
        },delaytime);
    }

    /**
     * 远程任务，请求有参数，有文件
     * @param taskid  任务id
     * @param taskUrl  任务资源路径，即服务器提供的数据接口
     * @param taskParams  请求传输参数
     * @param taskFiles 请求传输文件
     * @param delaytime  延迟
     */
    public void doAsyncTask(int taskid, String taskUrl, HashMap<String,String> taskParams,
                            List<NameValuePair> taskFiles,long delaytime){
        taskPool.addTask(taskid,taskUrl,taskParams,taskFiles,new BaseTask(){
            @Override
            public void onCompleteTask(String httpResult) {
                super.onCompleteTask();
                sendMessage(C.handler.taskComplete,getTaskId(),httpResult);
            }
            @Override
            public void onError(String error) {
                super.onError(error);
                sendMessage(C.handler.taskError,getTaskId(),error);
            }
        },delaytime);
    }

    //任务回调方法调用，添加信息到主线程的消息队列中
    public void sendMessage(int what, int taskId, String result){
        Bundle bundle = new Bundle();
        bundle.putInt(BaseHandler.TASK_ID,taskId);
        bundle.putString(BaseHandler.DATA,result);
        Message message = new Message();
        message.what = what;
        message.setData(bundle);
        handler.sendMessage(message);
    }

    //任务请求完成回调函数，在子类中重新该逻辑
    public void onCompleteTask(int taskId,BaseMessage message){
        LogUtil.d(CLASS_NAME + "onCompleteTask="+message);
    }

    //任务请求发生错误回调函数，在子类中重新该逻辑
    public void onNetworkError(int taskId,String errorInfo){
        LogUtil.d(CLASS_NAME + "onErrorTask="+errorInfo);
    }

    public void showProgressBar(){
        if(!progressBar.isShown()){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressBar(){
        if(progressBar.isShown()){
            progressBar.setVisibility(View.GONE);
        }
    }



    /**
     * 打开指定活动，并关闭之前的所有活动窗口
     * @param classObj 目标活动
     */
    protected void froward(Class<?> classObj){
        Intent intent = new Intent(BaseUi.this,classObj);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    protected void forward(Class<?> classObj,Bundle bundle){
        Intent intent = new Intent(BaseUi.this,classObj);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * 打开目标活动窗口，覆盖当前窗口
     * @param classObj
     */
    protected void overlay(Class<?> classObj){
        Intent intent = new Intent(BaseUi.this,classObj);
        startActivity(intent);
    }

    protected void overlay(Class<?> classObj,Bundle bundle){
        Intent intent = new Intent(BaseUi.this,classObj);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 弹出提示信息
     * @param msg  提示信息
     */
    protected void toast(String msg){
        Toast.makeText(BaseUi.this,msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取动态加载布局服务
     * @return 动态布局加载器
     */
    protected LayoutInflater getLayout(){
        return (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 动态加载布局
     * @param layoutId  布局文件id
     * @return  动态生成的视图
     */
    protected View getLayout(int layoutId){
        return getLayout().inflate(layoutId,null);
    }

    /**
     * 获取动态布局中的子控件
     * @param layoutId  布局文件id
     * @param itemId  子控件id
     * @return
     */
    protected View getLayout(int layoutId,int itemId){
        return getLayout(layoutId).findViewById(itemId);
    }
}
