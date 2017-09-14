package com.luoruiyong.weblog.util;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * 缓存管理，主要针对图片处理
 * Created by Administrator on 2017/9/9.
 */

public class AppCache {

    private static  final String CLASS_NAME = AppCache.class.getSimpleName() + "-->";

    /**
     * 从缓存中获取原图
     * @param context  活动上下文
     * @param url    资源路径
     * @param type   资源类型
     * @return    位图或null
     */
    public static Bitmap getCacheImage(Context context,String url,String type){
        String fileName = AppUtil.md5(url);
        //尝试从SD卡中读取
        Bitmap cacheBitmap = SDUtil.getImage(fileName,type);
        if(cacheBitmap == null){
            //本地不存在指定文件，尝试从服务器中下载,无网络时会抛出异常
            try {
                cacheBitmap = IOUtil.getBitmapRemote(context, url);
            }catch (Exception e){
                LogUtil.d(CLASS_NAME+"Exception"+e.getMessage());
            }
            if(cacheBitmap != null){
                //保存到指定的目录下
                SDUtil.saveImage(cacheBitmap,fileName,type);
            }
        }
        return cacheBitmap;
    }

    /**
     * 从缓存中获取指定缩略图
     * @param context  活动上下文，获取网络状态时要使用
     * @param url   资源路径，当缓存中不存在指定图片，则访问服务器
     * @param type  资源类型，指定存在在那个目录下
     * @param width   缩略图的宽度，用于确定缩放比例
     * @param height   缩略图的高度
     * @return   位图或null
     */
    public static Bitmap getCacheSampleImage(Context context,String url,String type,int width,int height){
        String fileName = AppUtil.md5(url);
        Bitmap cacheBitmap = SDUtil.getSampleImage(fileName,type,width,height);
        if(cacheBitmap == null){
            try {
                cacheBitmap = IOUtil.getBitmapRemote(context, url);
            }catch (Exception e){
                LogUtil.d(CLASS_NAME+"Exception"+e.getMessage());
            }
            if(cacheBitmap != null){
                //保存到指定的目录下
                SDUtil.saveImage(cacheBitmap,fileName,type);
                //再次从SD卡中读取缩略图
                cacheBitmap = SDUtil.getSampleImage(fileName,type,width,height);
            }
        }
        return cacheBitmap;
    }
}
