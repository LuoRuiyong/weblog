package com.luoruiyong.weblog.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**内存帮助类，主要针对图片处理，防止OOM
 * 本类对外功能
 * 1.添加缩略图到内存
 * 2.从内存获取指定缩略图
 * Created by Administrator on 2017/9/21.
 */

public class MemoryUtil {
    private static final String CLASS_NAME = MemoryUtil.class.getSimpleName()+"-->";
    private static LruCache<String,Bitmap> memoryCache;
    private static LruCache<String,Bitmap> fullScreenMemoryCache;

    private MemoryUtil(){}

    //初始化缓存类，仅初始化一次
    private static void initMemoryCache(){
        if(memoryCache == null){
            int maxMemory = (int)Runtime.getRuntime().maxMemory() /1024;
            int cacheSize = maxMemory/8;
            memoryCache = new LruCache<String,Bitmap>(cacheSize){
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount() / 1024;
                }
            };
            LogUtil.d(CLASS_NAME+"初始化memoryCache内存缓存，缓存区大小为："+cacheSize+"KB");
        }
    }

    private static void initFullScreenMemoryCache(){
        if(fullScreenMemoryCache == null){
            int maxMemory = (int)Runtime.getRuntime().maxMemory() /1024;
            int cacheSize = maxMemory/10;
            fullScreenMemoryCache = new LruCache<String,Bitmap>(cacheSize){
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getHeight()*bitmap.getWidth() / 1024;
                }
            };
            LogUtil.d(CLASS_NAME+"初始化fullScreenMemoryCache内存缓存，缓存区大小为："+cacheSize+"KB");
        }
    }

    /**
     * 添加位图到内存缓存区
     * @param url  存储在缓冲区位图的键，这里使用图片的资源路径作为键
     * @param bitmap    存储在缓冲区位图的值
     */
    public static void addBitmap(String url,Bitmap bitmap){
        initMemoryCache();
        if(memoryCache.get(url) == null && bitmap != null){
            memoryCache.put(url,bitmap);
            LogUtil.d(CLASS_NAME+"添加键为："+url+"的位图到缓冲区");
        }
    }

    public static void addFullScreenBitmap(String url,Bitmap bitmap){
        initFullScreenMemoryCache();
        if(fullScreenMemoryCache.get(url) == null && bitmap != null){
            fullScreenMemoryCache.put(url,bitmap);
            LogUtil.d(CLASS_NAME+"添加键为："+url+"的位图到缓冲区");
        }
    }

    /**
     * 从内存缓存区取出指定位图
     * @param url  图片的资源路径
     * @return   相应的位图或null（不存在位图时）
     */
    public static Bitmap getBitmap(String url){
        initMemoryCache();
        Bitmap bitmap= memoryCache.get(url);
        if(bitmap == null){
            LogUtil.d(CLASS_NAME+"缓冲区memoryCache中不存在键为："+url+"的位图");
        }else{
            LogUtil.d(CLASS_NAME+"从缓冲区memoryCache取出键为："+url+"的位图");
        }

        return bitmap;
    }

    public static Bitmap getFullScreenBitmap(String url){
        initFullScreenMemoryCache();
        Bitmap bitmap= fullScreenMemoryCache.get(url);
        if(bitmap == null){
            LogUtil.d(CLASS_NAME+"缓冲区fullScreenMemoryCache中不存在键为："+url+"的位图");
        }else{
            LogUtil.d(CLASS_NAME+"从缓冲区fullScreenMemoryCache取出键为："+url+"的位图");
        }

        return bitmap;
    }

}
