package com.luoruiyong.weblog.base;

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
    private ExecutorService taskPool;
    private String TAG = "BaseTaskPool";
    public BaseTaskPool() {
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
            LogUtil.d(TAG, "Add a local task to the taskPool");
        }catch (Exception e){
            taskPool.shutdown();
            LogUtil.d(TAG,e.getMessage()+"  taskPool has shutdown");
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
            LogUtil.d(TAG, "Add a net task to the taskPool");
        }catch (Exception e){
            taskPool.shutdown();
            LogUtil.d(TAG,e.getMessage()+"  taskPool has shutdown");
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
            LogUtil.d(TAG, "Add a net task to the taskPool");
        }catch (Exception e){
            taskPool.shutdown();
            LogUtil.d(TAG,e.getMessage()+"  taskPool has shutdown");
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
            LogUtil.d(TAG, "Add a net task to the taskPool");
        }catch (Exception e){
            taskPool.shutdown();
            LogUtil.d(TAG,e.getMessage()+"  taskPool has shutdown");
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
                try{
                    if(this.taskUrl != null){
                        //远程访问任务

                    }else{
                        //本地任务
                    }
                    if(httpRequest != null){
                        //远程任务
                        baseTask.onCompleteTask(httpRequest);
                    }else{
                        //本地任务
                        baseTask.onCompleteTask();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    baseTask.onError(e.getMessage());
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try{
                    baseTask.onStop();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
