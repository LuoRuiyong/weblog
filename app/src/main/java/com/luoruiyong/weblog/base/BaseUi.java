package com.luoruiyong.weblog.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**所有活动的基类，包装了活动中常用的一些方法
 * Created by Administrator on 2017/9/7.
 */

public class BaseUi extends AppCompatActivity {

    private static final String TAG = "BaseUi";
    private BaseHandler handler;
    private BaseTaskPool taskPool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.handler = new BaseHandler(this);
        this.taskPool = new BaseTaskPool();
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
