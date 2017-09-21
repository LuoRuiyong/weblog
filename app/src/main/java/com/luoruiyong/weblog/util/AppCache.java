package com.luoruiyong.weblog.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.luoruiyong.weblog.base.C;
import com.luoruiyong.weblog.model.Picture;

/**应用缓存，主要针对图片
 * 本类对外功能
 * 1.获取用户头像缩略图（内存->本地->服务器）
 * 2.获取微博图片缩略图（内存->本地->服务器）
 * 3.获取自定义图片缩略图（内存->本地->服务器）
 * 4.获取原图（本地->服务器）
 * 5.清除应用本地缓存
 * Created by Administrator on 2017/9/21.
 */

public class AppCache {
    /**
     * 获取自定义缩略图
     * @param context  活动上下文
     * @param url   图片资源路径
     * @param width  指定图片宽度
     * @param height  指定图片高度
     * @return  缩略图（位图）或null
     */
    public static Bitmap getSampleCustomCacheImage(Context context, String url, int width, int height){
        //尝试从内存缓存区获取
        Bitmap bitmap = MemoryUtil.getBitmap(url);
        if(bitmap == null ){
            //内存缓存区不存在，尝试从本地获取
            bitmap = SDUtil.getSampleCustomImage(url,width,height);
            if(bitmap == null){
                //本地不存在，尝试联网从服务器获取（异步）
                IOUtil.getSampleBitmapRemote(context,C.task.getSampleCustomImage,url,Picture.PATTERN_SAMPLE_CUSTOM_IMAGE,width,height);
            }else{
                MemoryUtil.addBitmap(url,bitmap);
            }
        }
        return null;
    }

    /**
     * 获取微博图片缩略图
     * @param context  活动上下文
     * @param url  图片资源路径
     * @return   微博图片缩略图（位图）或null
     */
    public  static Bitmap getSampleCacheBlogImage(Context context,String url){
        //尝试从内存缓存区获取
        Bitmap bitmap = MemoryUtil.getBitmap(url);
        if(bitmap == null ){
            //内存缓存区不存在，尝试从本地获取
            bitmap = SDUtil.getSampleBlogImage(url);
            if(bitmap == null){
                //本地不存在，尝试联网从服务器获取（异步）
                IOUtil.getBitmapRemote(context, C.task.getSampleBlogImage,url, Picture.PATTERN_SAMPLE_BLOG_IMAGE);
            }else{
                MemoryUtil.addBitmap(url,bitmap);
            }
        }
        return  bitmap;
    }

    /**
     * 获取用户头像缩略图
     * @param context  活动上下文
     * @param url  图片资源路径
     * @return   用户头像缩略图（位图）或null
     */
    public  static Bitmap getSampleCacheContactIcon(Context context,String url){
        //尝试从内存缓存区获取
        Bitmap bitmap = MemoryUtil.getBitmap(url);
        if(bitmap == null ){
            //内存缓存区不存在，尝试从本地获取
            bitmap = SDUtil.getSampleContactIcon(url);
            if(bitmap == null){
                //本地不存在，尝试联网从服务器获取（异步）
                IOUtil.getBitmapRemote(context, C.task.getSampleContactIcon,url, Picture.PATTERN_SAMPLE_CONTACT_ICON);
            }else{
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
        Bitmap bitmap= SDUtil.getOriginalImage(url);
        if(bitmap == null){
            //本地不存在指定原图，尝试联网获取（异步）
            IOUtil.getBitmapRemote(context,C.task.getOriginalImage,url,Picture.PATTERN_ORIGINAL_IMAGE);
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
