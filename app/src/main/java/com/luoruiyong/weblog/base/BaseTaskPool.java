package com.luoruiyong.weblog.base;

import android.content.Context;

import com.luoruiyong.weblog.util.AppClient;
import com.luoruiyong.weblog.util.LogUtil;

import org.apache.http.NameValuePair;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**多线程任务池
 * Created by Administrator on 2017/9/8.
 */

public class BaseTaskPool {

    private final static String CLASS_NAME = BaseTaskPool.class.getSimpleName() + "-->";
    private ExecutorService taskPool;
    private Context context;

    public BaseTaskPool(Context context) {
        this.context = context;
        this.taskPool = Executors.newCachedThreadPool();
    }

    /**
     * 本地任务
     * @param taskId    任务id
     * @param baseTask  任务回调方法所在类
     * @param delaytime 延时操作
     */
    public void addTask(int taskId, BaseTask baseTask, long delaytime) {
        baseTask.setId(taskId);
        baseTask.setName("本地任务");
        try{
            taskPool.execute(new TaskThread(null,null,null,baseTask,delaytime));
            LogUtil.d(CLASS_NAME+"添加id为"+baseTask.getId()+"的"+baseTask.getName()+"到线程池");
        }catch (Exception e){
            taskPool.shutdown();
            LogUtil.e(CLASS_NAME+"线程池异常，线程池已关闭");
            LogUtil.d(CLASS_NAME+"异常信息:"+e.getMessage());
        }
    }

    /**
     * 远程任务，无任务参数、无文件传输
     * @param taskId   任务编号
     * @param taskUrl  任务接口资源
     * @param baseTask 任务回调方法所在类
     * @param delaytime  延时操作
     */
    public void addTask(int taskId,String taskUrl, BaseTask baseTask, long delaytime) {
        baseTask.setId(taskId);
        baseTask.setName("远程任务");
        try{
            taskPool.execute(new TaskThread(taskUrl,null,null,baseTask,delaytime));
            LogUtil.d(CLASS_NAME+"添加id为"+baseTask.getId()+"的"+baseTask.getName()+"到线程池");
        }catch (Exception e){
            taskPool.shutdown();
            LogUtil.e(CLASS_NAME+"线程池异常，线程池已关闭");
            LogUtil.d(CLASS_NAME+"异常信息:"+e.getMessage());
        }
    }

    /**
     * 远程任务，有任务参数、无文件传输
     * @param taskId   任务编号
     * @param taskUrl  任务接口资源
     * @param taskParams  任务参数
     * @param baseTask 任务回调方法所在类
     * @param delaytime  延时操作
     */
    public void addTask(int taskId,String taskUrl,HashMap<String,String> taskParams, BaseTask baseTask, long delaytime) {
        baseTask.setId(taskId);
        baseTask.setName("远程任务");
        try{
            taskPool.execute(new TaskThread(taskUrl,taskParams,null,baseTask,delaytime));
            LogUtil.d(CLASS_NAME+"添加id为"+baseTask.getId()+"的"+baseTask.getName()+"到线程池");
        }catch (Exception e){
            taskPool.shutdown();
            LogUtil.e(CLASS_NAME+"线程池异常，线程池已关闭");
            LogUtil.d(CLASS_NAME+"异常信息:"+e.getMessage());
        }
    }

    /**
     * 远程任务，有任务参数、有文件传输
     * @param taskId   任务编号
     * @param taskUrl  任务接口资源
     * @param taskParams  任务参数
     * @param taskFiles  任务传输文件
     * @param baseTask 任务回调方法所在类
     * @param delaytime  延时操作
     */
    public void addTask(int taskId,String taskUrl,HashMap<String,String> taskParams, List<NameValuePair> taskFiles,BaseTask baseTask, long delaytime) {
        baseTask.setId(taskId);
        baseTask.setName("远程任务");
        try{
            taskPool.execute(new TaskThread(taskUrl,taskParams,taskFiles,baseTask,delaytime));
            LogUtil.d(CLASS_NAME+"添加id为"+baseTask.getId()+"的"+baseTask.getName()+"到线程池");
        }catch (Exception e){
            taskPool.shutdown();
            LogUtil.e(CLASS_NAME+"线程池异常，线程池已关闭");
            LogUtil.d(CLASS_NAME+"异常信息:"+e.getMessage());
        }
    }

    /**
     * 任务线程
     */
    private class TaskThread implements Runnable{
        private String taskUrl;
        private HashMap<String,String> params;
        private List<NameValuePair> taskFiles;
        private BaseTask baseTask;
        private long delaytime;

        public TaskThread(String taskUrl, HashMap<String, String> params, List<NameValuePair> taskFiles, BaseTask baseTask, long delaytime) {
            this.taskUrl = taskUrl;
            this.params = params;
            this.taskFiles = taskFiles;
            this.baseTask = baseTask;
            this.delaytime = delaytime;
        }

        @Override
        public void run() {
            try{
                baseTask.onStart();
                String httpRequest = null;  //请求数据结果
                if(delaytime > 0){
                    Thread.sleep(this.delaytime);
                }
                if(this.taskUrl != null) {
                    //远程访问任务
                    AppClient client = new AppClient(context, taskUrl);
                    try {
                        if (params != null || taskFiles != null) {
                            //有请求参数，使用post请求
                            if (taskFiles != null) {
                                //有请求参数，也有文件
                                httpRequest = client.post(params, taskFiles);
                            } else {
                                httpRequest = client.post(params);
                            }
                        } else {
                            //无请求参数，使用get请求
                            httpRequest = client.get();
                        }
                    } catch (Exception e) {
                        LogUtil.d(CLASS_NAME + "异常信息：" + e.getMessage());
                        baseTask.onError(e.getMessage());
                    }
                }else{
                    //本地任务
                    //目前暂时不添加本地任务
                }if(httpRequest != null){
                    //远程任务
                    baseTask.onCompleteTask(httpRequest);
                }else{
                    //本地任务
                    baseTask.onCompleteTask();
                }
            }catch (Exception e){
                LogUtil.d(CLASS_NAME+"异常信息："+e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
