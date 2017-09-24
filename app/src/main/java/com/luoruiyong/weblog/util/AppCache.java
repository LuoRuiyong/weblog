package com.luoruiyong.weblog.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.luoruiyong.weblog.base.BaseUi;
import com.luoruiyong.weblog.base.C;

/**应用缓存，主要针对图片
 * 本类对外功能
 * 1.获取缩略图（内存->本地cache目录->服务器）
 * 2.获取全屏缩略图（内存->服务器）
 * 3.获取原图（本地download目录->服务器）
 * 4.清除应用本地缓存
 * Created by Administrator on 2017/9/21.
 */

public class AppCache {
    /**
     * 获取全屏缩略图,一般指联网获取
     * @param context  活动上下文
     * @param url   图片资源路径
     * @return  缩略图（位图）或null
     */
    public static Bitmap getFullScreenSampleImage(Context context,String url){
        //尝试从内存缓存区获取
        Bitmap bitmap = MemoryUtil.getFullScreenBitmap(url);
        if(bitmap == null ){
            //内存中不存在，尝试联网从服务器获取（异步）
            if(context!=null){
                IOUtil.getBitmapRemote(context,C.task.getSampleFullScreenImage,url, BaseUi.WIDTH,BaseUi.HEIGHT);
            }
        }
        return  bitmap;
    }


    /**
     * 获取缩略图
     * @param context  活动上下文
     * @param url  图片资源路径
     * @return   缩略图（位图）或null
     */
    public  static Bitmap getSampleImage(Context context,int taskId,String url){
        //尝试从内存缓存区获取
        Bitmap bitmap = MemoryUtil.getBitmap(url);
        if(bitmap == null ){
            //内存缓存区不存在，尝试从本地获取
            bitmap = SDUtil.getCacheImage(url);
            if(bitmap == null){
                //本地不存在，尝试联网从服务器获取（异步）
                if(context!= null){
                    IOUtil.getBitmapRemote(context,taskId,url);
                }
            }else{
                //本地存在缓存，将资源添加到内存缓存中
                MemoryUtil.addBitmap(url,bitmap);
            }
        }
        return  bitmap;
    }


    /**
     * 获取原图
     * @param context  活动上下文
     * @param url  图片资源路径
     * @return  原图或null
     */
    public  static Bitmap getOriginalImage(Context context,String url){
        //尝试从本地获取原图
        Bitmap bitmap= SDUtil.getDownLoadImage(url);
        if(bitmap == null){
            //本地不存在指定原图，尝试联网获取（异步）
            if(context != null){
                IOUtil.getBitmapRemote(context,C.task.getOriginalImage,url);
            }
        }
        return  bitmap;
    }

    /**
     * 清除本地缓存
     * @return  是否成功
     */
    public  static boolean clearCache(){
        return SDUtil.clearCache();
    }
}
